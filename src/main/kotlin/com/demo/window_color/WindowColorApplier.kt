package com.demo.window_color

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.SwingUtilities

object WindowColorApplier {

    private const val PANEL_CLIENT_PROPERTY = "com.demo.windowColorPanel"

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
        val existingPanel = rootContentPane.components
            .firstOrNull { (it as? JComponent)?.getClientProperty(PANEL_CLIENT_PROPERTY) == true }

        if (settings.panelIsDisabled()) {
            removeColoredPanelFromWindow(existingPanel, rootContentPane)
            return
        }
        addColoredPanelToWindow(existingPanel, rootContentPane, settings, project)
    }

    private fun addColoredPanelToWindow(
        existingPanel: Component?,
        rootContentPane: Container,
        settings: WindowColorSettings,
        project: Project
    ) {
        if (existingPanel != null) {
            rootContentPane.remove(existingPanel)
        }
        val color = getDesiredColor(settings, project)
        val panel = JPanel()
        panel.putClientProperty(PANEL_CLIENT_PROPERTY, true)
        panel.background = color
        val panelSide = settings.getSide()
        panel.preferredSize = getPanelDimension(panelSide)

        rootContentPane.add(panel, borderLayoutConstraint(panelSide))
        rootContentPane.revalidate()
        rootContentPane.repaint()
    }

    private fun getPanelDimension(panelSide: WindowColorSettings.Side): Dimension = when (panelSide) {
        WindowColorSettings.Side.NORTH,
        WindowColorSettings.Side.SOUTH -> Dimension(0, 20)

        else -> Dimension(20, 0)
    }

    private fun getDesiredColor(settings: WindowColorSettings, project: Project): Color {
        val color = if (settings.isUseCustomColor()) {
            settings.getCustomColor() ?: generateColor(project.name)
        } else {
            generateColor(project.name)
        }
        return color
    }

    private fun removeColoredPanelFromWindow(existingPanel: Component?, rootContentPane: Container) {
        if (existingPanel != null) {
            rootContentPane.remove(existingPanel)
            rootContentPane.revalidate()
            rootContentPane.repaint()
        }
    }

    private fun borderLayoutConstraint(side: WindowColorSettings.Side): String {
        return when (side) {
            WindowColorSettings.Side.EAST -> BorderLayout.EAST
            WindowColorSettings.Side.WEST -> BorderLayout.WEST
            WindowColorSettings.Side.NORTH -> BorderLayout.NORTH
            WindowColorSettings.Side.SOUTH -> BorderLayout.SOUTH
        }
    }

    // TODO BlakeGoudemond 2026/04/12 | consider modelling light mode vs dark mode colors
    private fun generateColor(seed: String): Color {
        val hash = seed.hashCode()
        val r = (hash shr 16) and 0xFF
        val g = (hash shr 8) and 0xFF
        val b = hash and 0xFF
        return JBColor(Color(r, g, b, 180), Color(r, g, b, 180))
    }

}