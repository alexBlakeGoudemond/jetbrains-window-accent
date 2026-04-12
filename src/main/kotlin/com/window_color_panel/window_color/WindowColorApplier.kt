package com.window_color_panel.window_color

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import com.window_color_panel.settings.state.WindowCustomColorSettings
import com.window_color_panel.settings.state.WindowPanelSettings
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
                project.getService(WindowColorSettings::class.java).setPanelEnabled(enabled)
                applyColorToWindow(project)
            }
        }
    }

    private fun applyColorToWindow(project: Project) {
        val frame = WindowManager.getInstance().getFrame(project) ?: return
        val rootContentPane = frame.rootPane.contentPane
        val settings = project.getService(WindowColorSettings::class.java)
        val panelState = findExistingColoredPanel(rootContentPane)

        if (settings.panelIsDisabled()) {
            removeColoredPanel(panelState, rootContentPane)
            return
        }

        addOrReplaceColoredPanel(panelState, rootContentPane, settings, project)
    }

    private fun findExistingColoredPanel(rootContentPane: Container): Component? {
        return rootContentPane.components.firstOrNull {
            (it as? JComponent)?.getClientProperty(PANEL_CLIENT_PROPERTY) == true
        }
    }

    private fun addOrReplaceColoredPanel(
        existingPanel: Component?,
        rootContentPane: Container,
        settings: WindowColorSettings,
        project: Project
    ) {
        if (existingPanel != null) {
            rootContentPane.remove(existingPanel)
        }

        val panel = createColoredPanel(settings, project)
        rootContentPane.add(panel, borderLayoutConstraint(settings.getSide()))
        rootContentPane.revalidate()
        rootContentPane.repaint()
    }

    private fun createColoredPanel(
        settings: WindowColorSettings,
        project: Project
    ): JPanel {
        val panel = JPanel()
        panel.putClientProperty(PANEL_CLIENT_PROPERTY, true)
        panel.background = resolveColor(settings, project)
        panel.preferredSize = panelDimension(settings.getSide())
        return panel
    }

    private fun resolveColor(settings: WindowCustomColorSettings, project: Project): Color {
        return if (settings.panelIsEnabled()) {
            project.getService(WindowCustomColorSettings::class.java).takeIf { it.isUseCustomColor() }
                ?.getCustomColor()
                ?: generateColor(project.name)
        } else {
            generateColor(project.name)
        }
    }

    private fun panelDimension(side: WindowPanelSettings.Side): Dimension = when (side) {
        WindowPanelSettings.Side.NORTH,
        WindowPanelSettings.Side.SOUTH -> Dimension(0, PANEL_THICKNESS)

        else -> Dimension(PANEL_THICKNESS, 0)
    }

    private fun removeColoredPanel(existingPanel: Component?, rootContentPane: Container) {
        if (existingPanel == null) return
        rootContentPane.remove(existingPanel)
        rootContentPane.revalidate()
        rootContentPane.repaint()
    }

    private fun borderLayoutConstraint(side: WindowColorSettings.Side): String = when (side) {
        WindowPanelSettings.Side.EAST -> BorderLayout.EAST
        WindowPanelSettings.Side.WEST -> BorderLayout.WEST
        WindowPanelSettings.Side.NORTH -> BorderLayout.NORTH
        WindowPanelSettings.Side.SOUTH -> BorderLayout.SOUTH
    }

    private fun generateColor(seed: String): Color {
        val hash = seed.hashCode()
        val r = (hash shr 16) and 0xFF
        val g = (hash shr 8) and 0xFF
        val b = hash and 0xFF
        return JBColor(Color(r, g, b, 180), Color(r, g, b, 180))
    }
}