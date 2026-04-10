package com.demo.tool_window

import com.demo.window_color.WindowColorApplier
import com.demo.window_color.WindowColorSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.SwingConstants

class WindowColorToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val settings = project.getService(WindowColorSettings::class.java)

        val panel = JPanel(BorderLayout())
        panel.border = BorderFactory.createEmptyBorder(12, 12, 12, 12)

        val toggleButton = JButton()
        toggleButton.horizontalAlignment = SwingConstants.CENTER

        fun refreshButtonText() {
            toggleButton.text =
                if (settings.isPanelEnabled()) "Disable colored panel" else "Enable colored panel"
        }

        toggleButton.addActionListener {
            WindowColorApplier.toggle(project)
            refreshButtonText()
        }

        refreshButtonText()

        val buttonPanel = JPanel()
        buttonPanel.isOpaque = false
        buttonPanel.add(toggleButton)

        panel.add(buttonPanel, BorderLayout.CENTER)

        val content = ContentFactory.getInstance()
            .createContent(panel, "", false)

        toolWindow.contentManager.addContent(content)
    }
}