package com.demo

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

class MyToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {

        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        val label = JLabel("Window Color Panel")
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

        panel.add(label)
        panel.add(toggleButton)

        val content = ContentFactory.getInstance()
            .createContent(panel, "", false)

        toolWindow.contentManager.addContent(content)
    }
}