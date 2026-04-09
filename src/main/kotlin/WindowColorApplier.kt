package com.demo

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.JPanel
import javax.swing.SwingUtilities

object WindowColorApplier {

    private const val PANEL_CLIENT_PROPERTY = "com.demo.windowColorPanel"

    fun apply(project: Project) {
        SwingUtilities.invokeLater {
            val frame = WindowManager.getInstance().getFrame(project) ?: return@invokeLater
            val root = frame.rootPane
            val settings = project.getService(WindowColorSettings::class.java)

            val existingPanel = root.contentPane.components
                .firstOrNull { it.getClientProperty(PANEL_CLIENT_PROPERTY) == true }

            if (existingPanel != null) {
                root.contentPane.remove(existingPanel)
            }

            val color = if (settings.isUseCustomColor()) {
                settings.getCustomColor() ?: generateColor(project.name)
            } else {
                generateColor(project.name)
            }

            val panel = JPanel()
            panel.putClientProperty(PANEL_CLIENT_PROPERTY, true)
            panel.background = color

            val side = settings.getSide()
            panel.preferredSize = when (side) {
                WindowColorSettings.Side.NORTH,
                WindowColorSettings.Side.SOUTH -> Dimension(0, 20)
                else -> Dimension(20, 0)
            }

            root.contentPane.add(panel, borderLayoutConstraint(side))
            root.contentPane.revalidate()
            root.contentPane.repaint()
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

    private fun generateColor(seed: String): Color {
        val hash = seed.hashCode()
        val r = (hash shr 16) and 0xFF
        val g = (hash shr 8) and 0xFF
        val b = hash and 0xFF
        return JBColor(Color(r, g, b, 180), Color(r, g, b, 180))
    }
}