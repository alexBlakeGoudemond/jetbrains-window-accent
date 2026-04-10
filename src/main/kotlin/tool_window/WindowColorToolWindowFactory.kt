package com.demo.tool_window

import com.demo.window_color.WindowColorApplier
import com.demo.window_color.WindowColorSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

class WindowColorToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = JPanel(BorderLayout())
        panel.border = BorderFactory.createEmptyBorder(12, 12, 12, 12)

        val innerPanel = JPanel()
        innerPanel.layout = BoxLayout(innerPanel, BoxLayout.Y_AXIS)

        val label = JLabel("Window Color Panel")
        label.alignmentX = JLabel.CENTER_ALIGNMENT

        val toggleButton = JButton()

        val settings = project.getService(WindowColorSettings::class.java)

        fun refreshButtonText() {
            toggleButton.text = if (settings.isPanelEnabled()) "Disable colored panel" else "Enable colored panel"
        }

        toggleButton.addActionListener {
            WindowColorApplier.toggle(project)
            refreshButtonText()
        }

        refreshButtonText()

        val labelRow = JPanel(FlowLayout(FlowLayout.CENTER, 0, 0))
        labelRow.isOpaque = false
        labelRow.add(label)

        val buttonRow = JPanel(FlowLayout(FlowLayout.CENTER, 0, 0))
        buttonRow.isOpaque = false
        buttonRow.add(toggleButton)

        innerPanel.isOpaque = false
        innerPanel.add(labelRow)
        innerPanel.add(buttonRow)

        panel.add(innerPanel, BorderLayout.CENTER)

        val content = ContentFactory.getInstance()
            .createContent(panel, "", false)

        toolWindow.contentManager.addContent(content)
    }
}