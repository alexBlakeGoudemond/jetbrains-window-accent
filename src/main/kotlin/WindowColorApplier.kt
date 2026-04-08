package com.demo

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import java.awt.Color
import javax.swing.JPanel
import javax.swing.SwingUtilities

object WindowColorApplier {

    fun apply(project: Project) {
        SwingUtilities.invokeLater {
            val frame = WindowManager.getInstance().getFrame(project) ?: return@invokeLater
            val root = frame.rootPane

            val color = generateColor(project.name)

            val panel = JPanel()
            panel.background = color
            panel.preferredSize = java.awt.Dimension(20, 0) // thin vertical strip

            root.contentPane.add(panel, java.awt.BorderLayout.EAST)
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