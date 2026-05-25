package com.window_accent.feature.window_color

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import com.window_accent.PluginLifecycleListener
import com.window_accent.configuration.persistence.WindowCustomColorStateService
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService
import kotlinx.coroutines.*
import java.awt.*
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JRootPane
import javax.swing.RootPaneContainer
import kotlin.time.Duration.Companion.milliseconds

/**
 * Applies and maintains the colored window panel for IDE projects.
 *
 * This object is responsible for the runtime behavior of the feature:
 * it resolves the desired color, creates the panel, places it in the correct
 * position, and removes or reapplies it when settings change.
 */
object WindowColorApplier {

    private val LOG = logger<PluginLifecycleListener>()

    private const val PANEL_CLIENT_PROPERTY = "com.window_accent.windowAccent"
    private const val PANEL_THICKNESS = 20
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun applyToCurrentOpenProject(project: Project) {
        ApplicationManager.getApplication().invokeLater {
            applyColorToWindow(project)
        }
    }

    fun applyToAllOpenProjects(enabled: Boolean) {
        ApplicationManager.getApplication().invokeLater {
            ProjectManager.getInstance().openProjects.forEach { project ->
                project.getService(WindowPanelAppearanceStateService::class.java).setPanelEnabled(enabled)
                applyColorToWindow(project)
            }
        }
    }

    fun removeColorFromAllOpenProjectsSync() {
        LOG.info("[Window Accent] removeColorFromAllOpenProjects triggered")
        ProjectManager.getInstance().openProjects.forEach { project ->
            val frame = getProjectFrame(project)
            if (frame != null) {
                removeAllExistingPanels(frame)
            }
        }
    }

    private fun removeColorFromAllOpenProjectsInternal() {
        ApplicationManager.getApplication().invokeLater {
            removeColorFromAllOpenProjectsSync()
        }
    }

    private fun applyColorToWindow(project: Project) {
        scope.launch {
            repeat(60) {
                val frame = getProjectFrame(project)
                if (frame != null) {
                    val panelSettings = project.getService(WindowPanelAppearanceStateService::class.java)
                    val customColorSettings = project.getService(WindowCustomColorStateService::class.java)

                    removeAllExistingPanels(frame)

                    if (!panelSettings.panelIsDisabled()) {
                        addColoredPanel(frame, panelSettings, customColorSettings, project)
                    }
                    return@launch
                }
                delay(500.milliseconds)
            }
        }
    }

    private fun getProjectFrame(project: Project) =
        WindowManager.getInstance().getFrame(project)

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
        val rootPane = frame.rootPane
        val side = panelSettings.getSide()
        val panel = createColoredPanel(panelSettings, customColorSettings, project)

        if (side == WindowPanelAppearanceStateService.Side.SOUTH) {
            placeSouthPanelAtBottomOfStatusBar(rootPane, panel)
        } else {
            val contentPane = rootPane.contentPane
            contentPane.add(panel, borderLayoutConstraint(side), 0)
        }

        rootPane.revalidate()
        rootPane.repaint()
    }

    /**
     * Move the contentPane into the center of the wrapper, adding the panel to the south
     * */
    private fun placeSouthPanelAtBottomOfStatusBar(rootPane: JRootPane, panel: JPanel) {
        val originalContentPane = rootPane.contentPane
        val wrapper = JPanel(BorderLayout())
        wrapper.putClientProperty(PANEL_CLIENT_PROPERTY, true)
        rootPane.contentPane = wrapper
        wrapper.add(originalContentPane, BorderLayout.CENTER)
        wrapper.add(panel, BorderLayout.SOUTH)
    }

    private fun createColoredPanel(
        panelSettings: WindowPanelAppearanceStateService,
        customColorSettings: WindowCustomColorStateService,
        project: Project
    ): JPanel {
        return JPanel().apply {
            putClientProperty(PANEL_CLIENT_PROPERTY, true)
            background = resolveColor(customColorSettings, project)
            preferredSize = panelDimension(panelSettings.getSide())
        }
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