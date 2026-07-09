package com.window_accent.feature.window_color

import com.intellij.openapi.application.ApplicationManager
import com.window_accent.diagnostic.windowAccentLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import com.intellij.util.Alarm
import com.window_accent.configuration.persistence.WindowCustomColorStateService
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService
import java.awt.*
import java.util.concurrent.ConcurrentHashMap
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JRootPane
import javax.swing.RootPaneContainer

/**
 * Applies and maintains the colored window panel for IDE projects.
 *
 * This object is responsible for the runtime behavior of the feature:
 * it resolves the desired color, creates the panel, places it in the correct
 * position, and removes or reapplies it when settings change.
 */
object WindowColorApplier {

    private val logger = windowAccentLogger<WindowColorApplier>()

    internal const val PANEL_CLIENT_PROPERTY = "com.window_accent.windowAccent"
    private const val PANEL_THICKNESS = 20
    private const val RETRY_DELAY_MS = 500L
    private const val MAX_RETRIES = 60

    /**
     * Set to true at the very start of [cancelCoroutines].
     *
     * Any EDT task queued before cleanup began but executed after this flag is set
     * will see isShuttingDown = true and skip all work, preventing a re-added colored
     * panel or failed service lookup from occurring after the plugin has been unloaded.
     */
    @Volatile private var isShuttingDown = false

    /**
     * Alarm used exclusively for the frame-availability retry loop in [applyColorToWindow].
     *
     * Using [Alarm] (rather than a coroutine scope) ensures that [Alarm.cancelAllRequests] fully
     * releases all pending request runnables synchronously — leaving no plugin-classloader
     * references in any platform scheduler when the plugin is unloaded.
     */
    private val retryAlarm = Alarm(Alarm.ThreadToUse.SWING_THREAD)

    /**
     * Tracks added panels per-project for belt-and-suspenders cleanup.
     *
     * If [getProjectFrame] returns null during cleanup (e.g., frame is already disposed),
     * we can still remove panels by directly iterating this map and removing them from
     * their parent containers. This prevents orphaned panels from holding classloader references.
     */
    private val addedPanels = ConcurrentHashMap<Project, MutableList<Component>>()
    private val projectDisposeClosures = ConcurrentHashMap<Project, () -> Unit>()

    /**
     * Performs synchronous shutdown cleanup for pending UI work.
     *
     * Even though no coroutines are used here, this method is kept to match the existing
     * shutdown API. It marks the applier as shutting down and clears any queued retry tasks
     * so no panel updates run after plugin unload begins.
     */
    fun cancelCoroutines() {
        // Set the flag FIRST so any EDT task still in the queue skips re-adding panels
        isShuttingDown = true
        retryAlarm.cancelAllRequests()
        // Dispose the alarm explicitly so the platform's Disposer tree holds no reference
        // to it (and transitively to this plugin class) after cleanup. cancelAllRequests()
        // is called first to clear pending runnables before disposal.
        Disposer.dispose(retryAlarm)
        disposeAllTrackedDisposables()
    }

    fun applyToCurrentOpenProject(project: Project) {
        runOnEdt {
            applyColorToWindow(project)
        }
    }

    fun applyToAllOpenProjects(enabled: Boolean) {
        runOnEdt {
            ProjectManager.getInstance().openProjects.forEach { project ->
                project.getService(WindowPanelAppearanceStateService::class.java).setPanelEnabled(enabled)
                applyColorToWindow(project)
            }
        }
    }

    fun removeColorFromAllOpenProjectsSync() {
        logger.info("removeColorFromAllOpenProjects triggered")

        // Remove all tracked panels first, even if a project frame is already unavailable.
        val trackedProjects = addedPanels.keys.toList()
        trackedProjects.forEach { project ->
            removeTrackedPanels(project)
        }

        ProjectManager.getInstance().openProjects.forEach { project ->
            // Standard removal via frame (in case we missed any via the stored references)
            val frame = getProjectFrame(project)
            if (frame != null) {
                removeAllExistingPanels(frame)
            }
        }
    }

    private fun applyColorToWindow(project: Project, retriesLeft: Int = MAX_RETRIES) {
        if (isShuttingDown){
            return
        }
        val frame = getProjectFrame(project)
        logger.debug("[Window Accent] WindowColorApplier: frame=$frame for project=${project.name}")
        if (frame != null) {
            val panelSettings = project.getService(WindowPanelAppearanceStateService::class.java)
            val customColorSettings = project.getService(WindowCustomColorStateService::class.java)
            removeAllExistingPanels(frame)
            if (!panelSettings.panelIsDisabled()) {
                addColoredPanel(frame, panelSettings, customColorSettings, project)
            }
        } else if (retriesLeft > 0) {
            retryAlarm.addRequest({ applyColorToWindow(project, retriesLeft - 1) }, RETRY_DELAY_MS)
        }
    }

    private fun getProjectFrame(project: Project) =
        WindowManager.getInstance().getFrame(project)

    private inline fun runOnEdt(crossinline action: () -> Unit) {
        val application = ApplicationManager.getApplication()
        if (application.isDispatchThread) {
            action()
        } else {
            application.invokeAndWait { action() }
        }
    }

    private fun findExistingColoredPanel(rootContentPane: Container): Component? {
        return rootContentPane.components.firstOrNull {
            (it as? JComponent)?.getClientProperty(PANEL_CLIENT_PROPERTY) == true
        }
    }

    private fun removeAllExistingPanels(frame: RootPaneContainer) {
        val rootPane = frame.rootPane
        val currentContentPane = rootPane.contentPane

        // If the current contentPane is our wrapper, unwrap it
        if ((currentContentPane as? JComponent)?.getClientProperty(PANEL_CLIENT_PROPERTY) == true) {
            val originalContent = (currentContentPane as Container).components.firstOrNull {
                it != findExistingColoredPanel(currentContentPane)
            }
            if (originalContent is Container) {
                rootPane.contentPane = originalContent
            }
        }

        // Standard removal for NORTH/EAST/WEST
        findExistingColoredPanel(rootPane.contentPane)?.let {
            removeColoredPanel(it, rootPane.contentPane)
        }
    }

    private fun addColoredPanel(
        frame: RootPaneContainer,
        panelSettings: WindowPanelAppearanceStateService,
        customColorSettings: WindowCustomColorStateService,
        project: Project
    ) {
        removeTrackedPanels(project)
        // Dispose the previous Disposer holder for this project BEFORE updating addedPanels
        projectDisposeClosures.remove(project)?.invoke()
        val rootPane = frame.rootPane
        val side = panelSettings.getSide()
        val panel = ColoredPanel(panelSettings.getSide(), resolveColor(customColorSettings, project), panelSettings.isPanelOpaque())
        panel.preferredSize = panelDimension(panelSettings.getSide())
        addedPanels[project] = mutableListOf(panel)

        if (side == WindowPanelAppearanceStateService.Side.SOUTH) {
            placeSouthPanelAtBottomOfStatusBar(rootPane, panel)
        } else {
            val contentPane = rootPane.contentPane
            contentPane.add(panel, borderLayoutConstraint(side), 0)
        }

        rootPane.revalidate()
        rootPane.repaint()

        if (!isShuttingDown) {
            val projectDisposable = Disposer.newDisposable("WindowAccent-color-panel-cleanup")
            // Register a new cleanup holder with the project lifecycle.
            projectDisposeClosures[project] = { Disposer.dispose(projectDisposable) }
            Disposer.register(projectDisposable) { removeTrackedPanels(project) }
            Disposer.register(project, projectDisposable)
        }
    }

    private fun removeTrackedPanels(project: Project) {
        addedPanels.remove(project)?.forEach { panel ->
            val parent = panel.parent as? Container ?: return@forEach
            parent.remove(panel)
            parent.revalidate()
            parent.repaint()
        }
    }

    private fun disposeAllTrackedDisposables() {
        val snapshot = ArrayList(projectDisposeClosures.values)
        projectDisposeClosures.clear()
        snapshot.forEach { it() }
    }

    /**
     * Move the contentPane into the center of the wrapper, adding the panel to the south
     * */
    private fun placeSouthPanelAtBottomOfStatusBar(rootPane: JRootPane, panel: JPanel) {
        val originalContentPane = rootPane.contentPane
        val wrapper = JPanel(BorderLayout())
        wrapper.isOpaque = false
        wrapper.putClientProperty(PANEL_CLIENT_PROPERTY, true)
        rootPane.contentPane = wrapper
        wrapper.add(originalContentPane, BorderLayout.CENTER)
        wrapper.add(panel, BorderLayout.SOUTH)
    }


    private fun resolveColor(
        customColorSettings: WindowCustomColorStateService,
        project: Project
    ): Color {
        return if (customColorSettings.isUseCustomColor()) {
            customColorSettings.getCustomColor() ?: generateColor(project.name)
        } else {
            generateColor(project.name)
        }
    }

    private fun panelDimension(side: WindowPanelAppearanceStateService.Side): Dimension = when (side) {
        WindowPanelAppearanceStateService.Side.NORTH,
        WindowPanelAppearanceStateService.Side.SOUTH -> Dimension(0, PANEL_THICKNESS)

        else -> Dimension(PANEL_THICKNESS, 0)
    }

    private fun removeColoredPanel(existingPanel: Component?, rootContentPane: Container) {
        if (existingPanel == null) return
        rootContentPane.remove(existingPanel)
        rootContentPane.revalidate()
        rootContentPane.repaint()
    }

    private fun borderLayoutConstraint(side: WindowPanelAppearanceStateService.Side): String = when (side) {
        WindowPanelAppearanceStateService.Side.EAST -> BorderLayout.EAST
        WindowPanelAppearanceStateService.Side.WEST -> BorderLayout.WEST
        WindowPanelAppearanceStateService.Side.NORTH -> BorderLayout.NORTH
        WindowPanelAppearanceStateService.Side.SOUTH -> BorderLayout.SOUTH
    }

    private fun generateColor(seed: String): Color {
        val hash = seed.hashCode()
        val r = (hash shr 16) and 0xFF
        val g = (hash shr 8) and 0xFF
        val b = hash and 0xFF
        return JBColor(Color(r, g, b, 180), Color(r, g, b, 180))
    }
}
