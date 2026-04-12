package com.window_color_panel.feature.window_color

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import com.window_color_panel.configuration.persistence.WindowCustomColorStateService
import com.window_color_panel.configuration.persistence.WindowPanelAppearanceStateService
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.SwingUtilities

/**
 * Applies, updates, and removes the colored window panel for IDE projects.
 *
 * This object delegates color resolution and layout decisions to smaller helpers so the
 * window-management flow stays easy to follow.
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
        SwingUtilities.invokeLater {
            ProjectManager.getInstance().openProjects.forEach { project ->
                project.getService(WindowPanelAppearanceStateService::class.java).setPanelEnabled(enabled)
                applyColorToWindow(project)
            }
        }
    }

    private fun applyColorToWindow(project: Project) {
        val frame = WindowManager.getInstance().getFrame(project) ?: return
        val rootContentPane = frame.rootPane.contentPane
        val panelAppearanceSettings = project.getService(WindowPanelAppearanceStateService::class.java)
        val customColorSettings = project.getService(WindowCustomColorStateService::class.java)
        val existingPanel = findExistingColoredPanel(rootContentPane)

        if (panelAppearanceSettings.panelIsDisabled()) {
            removeColoredPanel(existingPanel, rootContentPane)
            return
        }

        addOrReplaceColoredPanel(existingPanel, rootContentPane, panelAppearanceSettings, customColorSettings, project)
    }

    private fun findExistingColoredPanel(rootContentPane: Container): Component? {
        return rootContentPane.components.firstOrNull {
            (it as? JComponent)?.getClientProperty(PANEL_CLIENT_PROPERTY) == true
        }
    }

    private fun addOrReplaceColoredPanel(
        existingPanel: Component?,
        rootContentPane: Container,
        panelAppearanceSettings: WindowPanelAppearanceStateService,
        customColorSettings: WindowCustomColorStateService,
        project: Project
    ) {
        if (existingPanel != null) {
            rootContentPane.remove(existingPanel)
        }

        val panel = createColoredPanel(panelAppearanceSettings, customColorSettings, project)
        rootContentPane.add(panel, borderLayoutConstraint(panelAppearanceSettings.getSide()))
        rootContentPane.revalidate()
        rootContentPane.repaint()
    }

    private fun createColoredPanel(
        panelAppearanceSettings: WindowPanelAppearanceStateService,
        customColorSettings: WindowCustomColorStateService,
        project: Project
    ): JPanel {
        val panel = JPanel()
        panel.putClientProperty(PANEL_CLIENT_PROPERTY, true)
        panel.background = resolveColor(customColorSettings, project)
        panel.preferredSize = panelDimension(panelAppearanceSettings.getSide())
        return panel
    }

    private fun resolveColor(settings: WindowCustomColorStateService, project: Project): Color {
        return if (settings.isUseCustomColor()) {
            settings.getCustomColor() ?: generateColor(project.name)
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