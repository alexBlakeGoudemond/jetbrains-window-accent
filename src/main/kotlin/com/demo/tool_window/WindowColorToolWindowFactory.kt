package com.demo.tool_window

import com.demo.window_color.WindowColorApplier
import com.demo.window_color.WindowColorSettings
import com.demo.window_title.WindowTitleApplier
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

        val togglePanelButton = JButton()
        togglePanelButton.horizontalAlignment = SwingConstants.CENTER

        val toggleTitleButton = JButton()
        toggleTitleButton.horizontalAlignment = SwingConstants.CENTER

        fun refreshButtonText() {
            togglePanelButton.text =
                if (settings.isPanelEnabled()) "Disable colored panel" else "Enable colored panel"

            toggleTitleButton.text =
                if (settings.isTitleNumberingEnabled()) "Disable title numbering" else "Enable title numbering"
        }

        togglePanelButton.addActionListener {
            WindowColorApplier.toggle(project)
            refreshButtonText()
        }

        toggleTitleButton.addActionListener {
            val enabled = settings.toggleTitleNumberingEnabled()
            if (enabled) {
                WindowTitleApplier.apply(project)
            } else {
                WindowTitleApplier.remove(project)
            }
            refreshButtonText()
        }

        refreshButtonText()

        val buttonPanel = JPanel()
        buttonPanel.isOpaque = false
        buttonPanel.add(togglePanelButton)
        buttonPanel.add(toggleTitleButton)

        panel.add(buttonPanel, BorderLayout.CENTER)

        val content = ContentFactory.getInstance()
            .createContent(panel, "", false)

        toolWindow.contentManager.addContent(content)
    }
}