package com.window_accent.feature.window_title

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.WindowManager
import com.intellij.util.Alarm
import com.window_accent.configuration.persistence.WindowCustomTitleStateService
import com.window_accent.configuration.persistence.WindowTitleNumberingStateService
import java.awt.Frame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * Applies and maintains a numeric prefix in IDE window titles.
 *
 * The prefix is assigned per open project and is restored when focus changes
 * or when the IDE rewrites the title. This class manages the lifecycle of the
 * title decoration for the current project session.
 */
class WindowTitleApplier {

    companion object {
        private val INSTANCE = WindowTitleApplier()
        fun getInstance(): WindowTitleApplier = INSTANCE

        private const val RETRY_DELAY_MS = 500L
        private const val MAX_RETRIES = 60

        fun applyToCurrentOpenProject(project: Project, enabled: Boolean? = null) = getInstance().applyToCurrentOpenProject(project, enabled)
        fun applyToAllOpenProjects(enabled: Boolean? = null) = getInstance().applyToAllOpenProjects(enabled)
        fun removeFromAllOpenProjects() = getInstance().removeFromAllOpenProjects()
        fun removeFromAllOpenProjectsSync() = getInstance().removeFromAllOpenProjectsSync()
        fun cancelAllPendingOperations() = getInstance().cancelAllPendingOperations()
        fun resetProjectNumbering() = getInstance().resetProjectNumbering()
    }

    private val logger = logger<WindowTitleApplier>()

    /**
     * Set to true at the very start of [cancelAllPendingOperations].
     *
     * Any EDT task that was queued before cleanup began but runs after the flag is set
     * will see the flag and skip re-registering AWT listeners or Disposer holders,
     * preventing new platform→plugin references from being created after cleanup.
     */
    @Volatile private var isShuttingDown = false

    private val counter = AtomicInteger(1)
    private val projectNumbers = ConcurrentHashMap<Project, Int>()

    /**
     * Stores (listener, frame) pairs so that cleanup can always remove each listener
     * from the exact frame it was added to, regardless of what
     * [WindowManager.getInstance().getFrame(project)] returns at cleanup time.
     */
    private val focusListeners = ConcurrentHashMap<Project, Pair<WindowAdapter, Frame>>()
    private val titleListeners = ConcurrentHashMap<Project, Pair<java.beans.PropertyChangeListener, Frame>>()

    /**
     * Alarm used exclusively for the frame-availability retry loop in [doApplyTitle].
     *
     * Using [Alarm] (rather than a coroutine scope) ensures that [cancelAllRequests] fully
     * releases all pending request runnables synchronously — leaving no plugin-classloader
     * references in any platform scheduler when the plugin is unloaded.
     */
    private val retryAlarm = Alarm(Alarm.ThreadToUse.SWING_THREAD)

    /**
     * Stores a dispose-closure per project so that [disposeAllTrackedDisposables] can
     * evict the plugin-code lambda from IntelliJ's Disposer ObjectTree during cleanup.
     */
    private val projectDisposeClosures = ConcurrentHashMap<Project, () -> Unit>()

    fun applyToCurrentOpenProject(project: Project, enabled: Boolean? = null) {
        val numberingEnabled = enabled ?: project.getService(WindowTitleNumberingStateService::class.java).isTitleNumberingEnabled()
        val customTitleEnabled = project.getService(WindowCustomTitleStateService::class.java).isCustomTitleEnabled()
        val shouldApply = numberingEnabled || customTitleEnabled
        runOnEdt {
            if (shouldApply) {
                applyTitleToWindow(project)
            } else {
                removeTitleFromWindow(project)
            }
        }
    }

    fun applyToAllOpenProjects(enabled: Boolean? = null) {
        runOnEdt {
            ProjectManager.getInstance().openProjects.forEach { project ->
                applyToCurrentOpenProject(project, enabled)
            }
        }
    }

    fun removeFromAllOpenProjects() {
        runOnEdt {
            removeFromAllOpenProjectsInternal()
        }
    }

    fun removeFromAllOpenProjectsSync() {
        logger.info("[Window Accent] removeFromAllOpenProjectsSync triggered")
        ProjectManager.getInstance().openProjects.forEach { project ->
            removeTitleFromWindowSync(project)
        }
        resetProjectNumbering()
    }

    fun cancelAllPendingOperations() {
        // Set the flag FIRST so that any EDT task already in the queue — but not yet
        // executed — will see isShuttingDown = true and skip re-registering listeners
        // or Disposer holders after this cleanup completes.
        isShuttingDown = true
        retryAlarm.cancelAllRequests()
        // Dispose the alarm explicitly so the platform's Disposer tree holds no reference
        // to it (and transitively to this plugin class) after cleanup. cancelAllRequests()
        // is called first to clear pending runnables before disposal.
        Disposer.dispose(retryAlarm)
        // Proactively remove all tracked AWT listeners from their stored frames immediately.
        // Using stored frames (not getProjectFrame) guarantees removal from the exact frame
        // each listener was added to, even if the frame has since changed or become null.
        removeAllTrackedListeners()
        disposeAllTrackedDisposables()
    }

    /**
     * Removes all tracked AWT listeners from their stored frames and clears both maps.
     *
     * Called eagerly from [cancelAllPendingOperations] to ensure no AWT Frame holds
     * a reference to plugin-code listeners at the time of IntelliJ's classloader GC check.
     */
    private fun removeAllTrackedListeners() {
        val focusSnapshot = HashMap(focusListeners)
        focusListeners.clear()
        focusSnapshot.forEach { (_, pair) ->
            val (listener, frame) = pair
            frame.removeWindowFocusListener(listener)
        }

        val titleSnapshot = HashMap(titleListeners)
        titleListeners.clear()
        titleSnapshot.forEach { (_, pair) ->
            val (listener, frame) = pair
            frame.removePropertyChangeListener("title", listener)
        }
    }

    private fun disposeAllTrackedDisposables() {
        val snapshot = ArrayList(projectDisposeClosures.values)
        projectDisposeClosures.clear()
        snapshot.forEach { it() }
    }

    private fun removeFromAllOpenProjectsInternal() {
        ProjectManager.getInstance().openProjects.forEach { project ->
            removeTitleFromWindow(project)
        }
        resetProjectNumbering()
    }

    private fun applyTitleToWindow(project: Project) {
        val number = getWindowProjectNumber(project)
        doApplyTitle(project, number, MAX_RETRIES)
    }

    /**
     * Attempts to apply the title prefix, retrying via [retryAlarm] if the frame is not yet available.
     *
     * **No queued invokeLater**: this method is called from [runOnEdt]
     * (from [applyTitleToWindow]) or from the [Alarm.ThreadToUse.SWING_THREAD] retry alarm —
     * both of which already run on the EDT. The previous inner invokeLater was unnecessary and
     * created a race window where the registration could run *after* [cancelAllPendingOperations]
     * had already cleaned up the Disposer tree and AWT listeners.
     *
     * The [isShuttingDown] guard at the top ensures that any task already queued before cleanup
     * but executed after it will skip all external registrations.
     */
    private fun doApplyTitle(project: Project, number: Int, retriesLeft: Int) {
        if (isShuttingDown) return

        val frame = getProjectFrame(project)
        if (frame != null) {
            val numberingService = project.getService(WindowTitleNumberingStateService::class.java)
            val customTitleService = project.getService(WindowCustomTitleStateService::class.java)
            updateWindowTitle(
                frame, number,
                customTitle = customTitleService.getCustomTitle(),
                customTitleEnabled = customTitleService.isCustomTitleEnabled(),
                numberingEnabled = numberingService.isTitleNumberingEnabled()
            )
            reapplyOnFocus(project, frame)
            reapplyOnTitleChange(project, frame)

            // Double-check the flag before registering with the platform Disposer,
            // since reapplyOnFocus/reapplyOnTitleChange already added AWT listeners above.
            if (!isShuttingDown) {
                val holder = Disposer.newDisposable("WindowAccent-title-cleanup")
                projectDisposeClosures.put(project) { Disposer.dispose(holder) }?.invoke()
                Disposer.register(holder) { cleanupListeners(project) }
                Disposer.register(project, holder)
            }
        } else if (retriesLeft > 0 && !isShuttingDown) {
            retryAlarm.addRequest({ doApplyTitle(project, number, retriesLeft - 1) }, RETRY_DELAY_MS)
        }
    }

    private fun removeTitleFromWindowSync(project: Project) {
        // Remove listeners using stored frames regardless of getProjectFrame result
        removeListeners(project)
        val frame = getProjectFrame(project) ?: return
        stripTitlePrefix(frame)
    }

    private fun removeTitleFromWindow(project: Project) {
        runOnEdt {
            removeListeners(project)
            val frame = getProjectFrame(project) ?: return@runOnEdt
            stripTitlePrefix(frame)
        }
    }

    private fun getProjectFrame(project: Project): Frame? =
        WindowManager.getInstance().getFrame(project)

    private inline fun runOnEdt(crossinline action: () -> Unit) {
        val application = ApplicationManager.getApplication()
        if (application.isDispatchThread) {
            action()
        } else {
            application.invokeAndWait { action() }
        }
    }

    internal fun getWindowProjectNumber(project: Project): Int =
        projectNumbers.computeIfAbsent(project) { counter.getAndIncrement() }

    internal fun updateWindowTitle(
        frame: Frame,
        number: Int,
        customTitle: String = "",
        customTitleEnabled: Boolean = false,
        numberingEnabled: Boolean = true
    ) {
        val currentTitle = frame.title ?: return
        val cleanedTitle = stripExistingPrefix(currentTitle)
        val prefix = buildTitlePrefix(number, numberingEnabled, customTitle, customTitleEnabled)
        val updatedTitle = if (prefix.isNotEmpty()) "$prefix $cleanedTitle" else cleanedTitle

        if (frame.title != updatedTitle) {
            frame.title = updatedTitle
        }
    }

    /**
     * Builds the window title prefix based on the current numbering and custom title state.
     *
     * | Numbering | Custom title | Result              |
     * |-----------|--------------|---------------------|
     * | enabled   | enabled      | `[n - customTitle]` |
     * | enabled   | disabled     | `[n]`               |
     * | disabled  | enabled      | `[customTitle]`     |
     * | disabled  | disabled     | `""` (no prefix)    |
     */
    internal fun buildTitlePrefix(
        number: Int,
        numberingEnabled: Boolean,
        customTitle: String,
        customTitleEnabled: Boolean
    ): String {
        val hasCustomTitle = customTitleEnabled && customTitle.isNotBlank()
        return when {
            numberingEnabled && hasCustomTitle -> "[$number - $customTitle]"
            numberingEnabled -> "[$number]"
            hasCustomTitle -> "[$customTitle]"
            else -> ""
        }
    }

    private fun stripTitlePrefix(frame: Frame) {
        val currentTitle = frame.title ?: return
        val cleanedTitle = stripExistingPrefix(currentTitle)

        if (frame.title != cleanedTitle) {
            frame.title = cleanedTitle
        }
    }

    internal fun stripExistingPrefix(title: String): String =
        title.replace(Regex("^(\\[[^\\]]+]\\s*)+"), "")

    private fun reapplyOnFocus(project: Project, frame: Frame) {
        val listener = createFocusListener(project, frame)
        replaceFocusListener(project, frame, listener)
    }

    private fun reapplyOnTitleChange(project: Project, frame: Frame) {
        val listener = createTitleListener(project, frame)
        replaceTitleListener(project, frame, listener)
    }

    private fun createFocusListener(project: Project, frame: Frame): WindowAdapter =
        object : WindowAdapter() {
            override fun windowGainedFocus(e: WindowEvent?) {
                if (isShuttingDown) return
                val number = projectNumbers[project] ?: return
                val numberingService = project.getService(WindowTitleNumberingStateService::class.java)
                val customTitleService = project.getService(WindowCustomTitleStateService::class.java)
                updateWindowTitle(
                    frame, number,
                    customTitle = customTitleService.getCustomTitle(),
                    customTitleEnabled = customTitleService.isCustomTitleEnabled(),
                    numberingEnabled = numberingService.isTitleNumberingEnabled()
                )
            }
        }

    private fun createTitleListener(project: Project, frame: Frame): java.beans.PropertyChangeListener =
        java.beans.PropertyChangeListener { event ->
            if (isShuttingDown) return@PropertyChangeListener
            if ("title" == event.propertyName) {
                val newTitle = event.newValue as? String ?: return@PropertyChangeListener
                val number = projectNumbers[project] ?: return@PropertyChangeListener
                val numberingService = project.getService(WindowTitleNumberingStateService::class.java)
                val customTitleService = project.getService(WindowCustomTitleStateService::class.java)
                val expectedPrefix = buildTitlePrefix(
                    number,
                    numberingService.isTitleNumberingEnabled(),
                    customTitleService.getCustomTitle(),
                    customTitleService.isCustomTitleEnabled()
                )
                val expectedFullPrefix = if (expectedPrefix.isNotEmpty()) "$expectedPrefix " else ""
                if (expectedFullPrefix.isEmpty() || !newTitle.startsWith(expectedFullPrefix)) {
                    updateWindowTitle(
                        frame, number,
                        customTitle = customTitleService.getCustomTitle(),
                        customTitleEnabled = customTitleService.isCustomTitleEnabled(),
                        numberingEnabled = numberingService.isTitleNumberingEnabled()
                    )
                }
            }
        }

    private fun replaceFocusListener(project: Project, frame: Frame, listener: WindowAdapter) {
        // Remove the old listener from its original frame (stored alongside the listener)
        focusListeners.remove(project)?.let { (oldListener, oldFrame) ->
            oldFrame.removeWindowFocusListener(oldListener)
        }
        focusListeners[project] = listener to frame
        frame.addWindowFocusListener(listener)
    }

    private fun replaceTitleListener(project: Project, frame: Frame, listener: java.beans.PropertyChangeListener) {
        // Remove the old listener from its original frame (stored alongside the listener)
        titleListeners.remove(project)?.let { (oldListener, oldFrame) ->
            oldFrame.removePropertyChangeListener("title", oldListener)
        }
        titleListeners[project] = listener to frame
        frame.addPropertyChangeListener("title", listener)
    }

    /**
     * Removes tracked listeners for [project] from their stored frames.
     *
     * Uses the stored frame reference rather than [getProjectFrame] to guarantee
     * removal from the exact frame each listener was added to, even if that frame
     * has since become unreachable via [WindowManager].
     */
    private fun removeListeners(project: Project) {
        focusListeners.remove(project)?.let { (listener, frame) ->
            frame.removeWindowFocusListener(listener)
        }
        titleListeners.remove(project)?.let { (listener, frame) ->
            frame.removePropertyChangeListener("title", listener)
        }
    }

    private fun cleanupListeners(project: Project) {
        removeListeners(project)
    }

    fun resetProjectNumbering() {
        projectNumbers.clear()
        counter.set(1)
    }
}
