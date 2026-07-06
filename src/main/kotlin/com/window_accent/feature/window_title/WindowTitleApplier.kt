package com.window_accent.feature.window_title

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.WindowManager
import com.intellij.util.Alarm
import com.window_accent.configuration.persistence.GlobalCustomTitleStateService
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
        fun removeFromAllOpenProjectsSync() = getInstance().removeFromAllOpenProjectsSync()
        fun cancelAllPendingOperations() = getInstance().cancelAllPendingOperations()
        fun renumberAllOpenWindows(focusedProject: Project) = getInstance().renumberAllOpenWindows(focusedProject)
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
        val globalCustomTitleEnabled = ApplicationManager.getApplication()
            ?.getService(GlobalCustomTitleStateService::class.java)?.isGlobalCustomTitleEnabled() ?: false
        val shouldApply = numberingEnabled || customTitleEnabled || globalCustomTitleEnabled
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
        logger.debug("[Window Accent] WindowTitleApplier: frame=$frame for project=${project.name}")
        if (frame != null) {
            val numberingService = project.getService(WindowTitleNumberingStateService::class.java)
            val customTitleService = project.getService(WindowCustomTitleStateService::class.java)
            val globalCustomTitleService = ApplicationManager.getApplication()
                .getService(GlobalCustomTitleStateService::class.java)
            updateWindowTitle(
                frame, number,
                customTitle = customTitleService.getCustomTitle(),
                customTitleEnabled = customTitleService.isCustomTitleEnabled(),
                numberingEnabled = numberingService.isTitleNumberingEnabled(),
                globalCustomTitle = globalCustomTitleService.getGlobalCustomTitle(),
                globalCustomTitleEnabled = globalCustomTitleService.isGlobalCustomTitleEnabled()
            )

            // Dispose the previous Disposer holder BEFORE registering new listeners.
            //
            // cleanupListeners (triggered by the old holder's disposal) uses the maps to find
            // which listeners to remove from the frame.  By invoking the old closure here —
            // while the maps still contain the OLD listeners — we ensure the OLD listeners are
            // removed, not the new ones we are about to register below.
            //
            // If this is invoked AFTER reapplyOnFocus/reapplyOnTitleChange (as it was before),
            // the maps already hold the new listeners, so cleanupListeners would silently strip
            // them from the frame, leaving the window unguarded against IDE title rewrites.
            projectDisposeClosures.remove(project)?.invoke()

            reapplyOnFocus(project, frame)
            reapplyOnTitleChange(project, frame)

            // Double-check the flag before registering with the platform Disposer,
            // since reapplyOnFocus/reapplyOnTitleChange already added AWT listeners above.
            if (!isShuttingDown) {
                val holder = Disposer.newDisposable("WindowAccent-title-cleanup")
                projectDisposeClosures[project] = { Disposer.dispose(holder) }
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
        numberingEnabled: Boolean = true,
        globalCustomTitle: String = "",
        globalCustomTitleEnabled: Boolean = false
    ) {
        val currentTitle = frame.title ?: return
        val cleanedTitle = stripExistingPrefix(currentTitle)
        val prefix = buildTitlePrefix(number, numberingEnabled, customTitle, customTitleEnabled, globalCustomTitle, globalCustomTitleEnabled)
        val updatedTitle = if (prefix.isNotEmpty()) "$prefix $cleanedTitle" else cleanedTitle

        if (frame.title != updatedTitle) {
            frame.title = updatedTitle
        }
    }

    /**
     * Builds the window title prefix based on the current numbering, per-window custom title,
     * and global custom title state.
     *
     * The global part appears first, followed by the per-window part. The global title text is
     * styled **bold** and the entire per-window bracket content (number, separator, and custom
     * title) is styled *italic* using Unicode Mathematical Alphanumeric Symbols.
     * Digits have no italic Unicode counterpart so they pass through unchanged.
     *
     * | Numbering | Per-window | Global | Result                                |
     * |-----------|------------|--------|---------------------------------------|
     * | enabled   | enabled    | enabled  | `[boldGlobal][italic(n - perTitle)]`  |
     * | enabled   | enabled    | disabled | `[italic(n - perTitle)]`              |
     * | enabled   | disabled   | enabled  | `[boldGlobal][italic(n)]`             |
     * | enabled   | disabled   | disabled | `[italic(n)]`                         |
     * | disabled  | enabled    | enabled  | `[boldGlobal][italic(perTitle)]`      |
     * | disabled  | enabled    | disabled | `[italic(perTitle)]`                  |
     * | disabled  | disabled   | enabled  | `[boldGlobal]`                        |
     * | disabled  | disabled   | disabled | `""` (no prefix)                      |
     */
    internal fun buildTitlePrefix(
        number: Int,
        numberingEnabled: Boolean,
        customTitle: String,
        customTitleEnabled: Boolean,
        globalCustomTitle: String = "",
        globalCustomTitleEnabled: Boolean = false
    ): String {
        val hasCustomTitle = customTitleEnabled && customTitle.isNotBlank()
        val hasGlobalTitle = globalCustomTitleEnabled && globalCustomTitle.isNotBlank()

        // Apply Unicode mathematical styling to the label text inside the brackets only.
        // Global title → bold; per-window content (number + separator + custom title) → italic.
        //
        // The entire per-window bracket content is passed through toItalic as a single string so
        // that numbering and custom title are styled uniformly. Digits have no italic Unicode
        // counterpart (U+1D455 is absent and the italic digit block does not exist), so digits
        // pass through unchanged — the number's visual appearance is unaffected, but the intent
        // is clear and the approach is future-proof should Unicode add italic digits.
        val styledGlobalTitle = TitleTextStyler.toBold(globalCustomTitle)

        val perWindowPart = when {
            numberingEnabled && hasCustomTitle -> "[${TitleTextStyler.toItalic("$number - $customTitle")}]"
            numberingEnabled                   -> "[${TitleTextStyler.toItalic("$number")}]"
            hasCustomTitle                     -> "[${TitleTextStyler.toItalic(customTitle)}]"
            else                               -> ""
        }

        val globalPart = if (hasGlobalTitle) "[$styledGlobalTitle]" else ""

        return when {
            globalPart.isNotEmpty() && perWindowPart.isNotEmpty() -> "$globalPart$perWindowPart"
            globalPart.isNotEmpty() -> globalPart
            perWindowPart.isNotEmpty() -> perWindowPart
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
                val globalCustomTitleService = ApplicationManager.getApplication()
                    ?.getService(GlobalCustomTitleStateService::class.java)
                    ?: GlobalCustomTitleStateService()
                updateWindowTitle(
                    frame, number,
                    customTitle = customTitleService.getCustomTitle(),
                    customTitleEnabled = customTitleService.isCustomTitleEnabled(),
                    numberingEnabled = numberingService.isTitleNumberingEnabled(),
                    globalCustomTitle = globalCustomTitleService.getGlobalCustomTitle(),
                    globalCustomTitleEnabled = globalCustomTitleService.isGlobalCustomTitleEnabled()
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
                val globalCustomTitleService = ApplicationManager.getApplication()
                    ?.getService(GlobalCustomTitleStateService::class.java)
                    ?: GlobalCustomTitleStateService()
                val expectedPrefix = buildTitlePrefix(
                    number,
                    numberingService.isTitleNumberingEnabled(),
                    customTitleService.getCustomTitle(),
                    customTitleService.isCustomTitleEnabled(),
                    globalCustomTitleService.getGlobalCustomTitle(),
                    globalCustomTitleService.isGlobalCustomTitleEnabled()
                )
                val expectedFullPrefix = if (expectedPrefix.isNotEmpty()) "$expectedPrefix " else ""
                if (expectedFullPrefix.isEmpty() || !newTitle.startsWith(expectedFullPrefix)) {
                    updateWindowTitle(
                        frame, number,
                        customTitle = customTitleService.getCustomTitle(),
                        customTitleEnabled = customTitleService.isCustomTitleEnabled(),
                        numberingEnabled = numberingService.isTitleNumberingEnabled(),
                        globalCustomTitle = globalCustomTitleService.getGlobalCustomTitle(),
                        globalCustomTitleEnabled = globalCustomTitleService.isGlobalCustomTitleEnabled()
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

    /**
     * Renumbers all open IDE windows sequentially starting from 1.
     *
     * [focusedProject] is assigned number 1; the remaining open projects receive
     * 2, 3, … in their current iteration order.  The new numbers are written into
     * [projectNumbers] before titles are re-applied, so every title listener
     * immediately picks up the updated values.
     *
     * Because title numbering is a purely in-memory ("alive") feature, this
     * operation has no persistence side-effects.
     */
    fun renumberAllOpenWindows(focusedProject: Project) {
        runOnEdt {
            val openProjects = ProjectManager.getInstance().openProjects.toList()

            projectNumbers.clear()
            counter.set(1)

            // Focused project always gets number 1
            projectNumbers[focusedProject] = counter.getAndIncrement()

            // Remaining open projects get 2, 3, … in iteration order
            openProjects.filter { it !== focusedProject }.forEach { project ->
                projectNumbers[project] = counter.getAndIncrement()
            }

            // Re-apply titles so every window reflects its new number immediately.
            // applyToCurrentOpenProject respects each project's enabled/disabled state.
            openProjects.forEach { project -> applyToCurrentOpenProject(project) }

            // Ensure the focused project title is updated even if it is not yet
            // in the ProjectManager list (e.g. still initialising).
            if (focusedProject !in openProjects) {
                applyToCurrentOpenProject(focusedProject)
            }
        }
    }

}
