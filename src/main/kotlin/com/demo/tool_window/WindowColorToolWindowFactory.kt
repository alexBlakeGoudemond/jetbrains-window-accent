package com.demo.tool_window

import com.demo.window_color.WindowColorApplier
import com.demo.window_color.WindowColorSettings
import com.demo.window_title.WindowTitleApplier
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import java.awt.GridLayout
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JPanel

class WindowColorToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val settings = project.getService(WindowColorSettings::class.java)

        val panel = JPanel(GridLayout(0, 1, 8, 8))
        panel.border = BorderFactory.createEmptyBorder(12, 12, 12, 12)

        val toggleAllColorsButton = JButton()
        val toggleAllTitlesButton = JButton()
        val toggleCurrentColorButton = JButton()
        val toggleCurrentTitleButton = JButton()

        fun refreshButtonText() {
            toggleAllColorsButton.text =
                if (settings.panelIsEnabled()) "Disable colors for all open windows"
                else "Enable colors for all open windows"

            toggleAllTitlesButton.text =
                if (settings.isTitleNumberingEnabled()) "Disable title numbers for all open windows"
                else "Enable title numbers for all open windows"

            toggleCurrentColorButton.text =
                if (settings.panelIsEnabled()) "Disable color for current window"
                else "Enable color for current window"

            toggleCurrentTitleButton.text =
                if (settings.isTitleNumberingEnabled()) "Disable title number for current window"
                else "Enable title number for current window"
        }

        toggleAllColorsButton.addActionListener {
            val enabled = !settings.panelIsEnabled()
            settings.setPanelEnabled(enabled)
            WindowColorApplier.applyToAllOpenProjects(enabled)
            refreshButtonText()
        }

        toggleAllTitlesButton.addActionListener {
            val enabled = !settings.isTitleNumberingEnabled()
            settings.setTitleNumberingEnabled(enabled)
            WindowTitleApplier.applyToAllOpenProjects(enabled)
            refreshButtonText()
        }

        toggleCurrentColorButton.addActionListener {
            val enabled = !settings.panelIsEnabled()
            settings.setPanelEnabled(enabled)
            WindowColorApplier.applyToCurrentOpenProject(project)
            refreshButtonText()
        }

        toggleCurrentTitleButton.addActionListener {
            val enabled = !settings.isTitleNumberingEnabled()
            settings.setTitleNumberingEnabled(enabled)
            WindowTitleApplier.apply(project, enabled)
            refreshButtonText()
        }

        refreshButtonText()

        panel.add(toggleAllColorsButton)
        panel.add(toggleAllTitlesButton)
        panel.add(toggleCurrentColorButton)
        panel.add(toggleCurrentTitleButton)

        val content = ContentFactory.getInstance().createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)
    }
}