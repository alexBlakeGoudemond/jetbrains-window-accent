package com.window_color_panel.feature.window_color

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import com.intellij.util.concurrency.AppExecutorUtil
import com.window_color_panel.configuration.persistence.WindowCustomColorStateService
import com.window_color_panel.configuration.persistence.WindowPanelAppearanceStateService
import java.awt.*
import java.util.concurrent.TimeUnit
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Applies and maintains the colored window panel for IDE projects.
 *
 * This object is responsible for the runtime behavior of the feature:
 * it resolves the desired color, creates the panel, places it in the correct
 * position, and removes or reapplies it when settings change.
 */
object WindowColorApplier {

    private const val PANEL_CLIENT_PROPERTY = "com.window_color_panel.windowColorPanel"
    private const val PANEL_THICKNESS = 20

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

    private fun applyColorToWindow(project: Project) {
        fun tryApply(retries: Int) {
            val frame = getProjectFrame(project)
            if (frame != null) {
                ApplicationManager.getApplication().invokeLater {
                    if (frame !is javax.swing.JFrame) return@invokeLater
                    val rootContentPane = frame.rootPane.contentPane
                    val panelSettings = project.getService(WindowPanelAppearanceStateService::class.java)
                    val customColorSettings = project.getService(WindowCustomColorStateService::class.java)
                    val existingPanel = findExistingColoredPanel(rootContentPane)

                    if (panelSettings.panelIsDisabled()) {
                        removeColoredPanel(existingPanel, rootContentPane)
                        return@invokeLater
                    }

                    addOrReplaceColoredPanel(existingPanel, rootContentPane, panelSettings, customColorSettings, project)
                }
            } else if (retries > 0) {
                AppExecutorUtil.getAppScheduledExecutorService().schedule({
                    tryApply(retries - 1)
                }, 500, TimeUnit.MILLISECONDS)
            }
        }

        tryApply(60) // Retry for 30 seconds
    }

    private fun getProjectFrame(project: Project) =
        WindowManager.getInstance().getFrame(project)

    private fun findExistingColoredPanel(rootContentPane: Container): Component? {
        return rootContentPane.components.firstOrNull {
            (it as? JComponent)?.getClientProperty(PANEL_CLIENT_PROPERTY) == true
        }
    }

    private fun addOrReplaceColoredPanel(
        existingPanel: Component?,
        rootContentPane: Container,
        panelSettings: WindowPanelAppearanceStateService,
        customColorSettings: WindowCustomColorStateService,
        project: Project
    ) {
        if (existingPanel != null) {
            rootContentPane.remove(existingPanel)
        }

        val panel = createColoredPanel(panelSettings, customColorSettings, project)
        rootContentPane.add(panel, borderLayoutConstraint(panelSettings.getSide()))
        rootContentPane.revalidate()
        rootContentPane.repaint()
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