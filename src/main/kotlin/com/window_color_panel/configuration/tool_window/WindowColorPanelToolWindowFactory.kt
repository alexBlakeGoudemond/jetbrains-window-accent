package com.window_color_panel.configuration.tool_window

import com.window_color_panel.configuration.persistence.WindowPanelAppearanceStateService
import com.window_color_panel.feature.window_color.WindowColorApplier
import com.window_color_panel.feature.window_title.WindowTitleApplier
import com.window_color_panel.window_title.WindowTitleNumberingStateService
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import java.awt.GridLayout
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JPanel

class WindowColorPanelToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val colorSettings = project.getService(WindowPanelAppearanceStateService::class.java)
        val titleSettings = project.getService(WindowTitleNumberingStateService::class.java)

        val panel = JPanel(GridLayout(0, 1, 8, 8))
        panel.border = BorderFactory.createEmptyBorder(12, 12, 12, 12)

        val toggleAllColorsButton = JButton()
        val toggleAllTitlesButton = JButton()
        val toggleCurrentColorButton = JButton()
        val toggleCurrentTitleButton = JButton()

        fun refreshButtonText() {
            toggleAllColorsButton.text =
                if (colorSettings.panelIsEnabled()) "Disable colors for all open windows"
                else "Enable colors for all open windows"

            toggleAllTitlesButton.text =
                if (titleSettings.isTitleNumberingEnabled()) "Disable title numbers for all open windows"
                else "Enable title numbers for all open windows"

            toggleCurrentColorButton.text =
                if (colorSettings.panelIsEnabled()) "Disable color for current window"
                else "Enable color for current window"

            toggleCurrentTitleButton.text =
                if (titleSettings.isTitleNumberingEnabled()) "Disable title number for current window"
                else "Enable title number for current window"
        }

        toggleAllColorsButton.addActionListener {
            val enabled = colorSettings.panelIsDisabled()
            colorSettings.setPanelEnabled(enabled)
            WindowColorApplier.applyToAllOpenProjects(enabled)
            refreshButtonText()
        }

        toggleAllTitlesButton.addActionListener {
            val enabled = titleSettings.isTitleNumberingDisabled()
            titleSettings.setTitleNumberingEnabled(enabled)
            WindowTitleApplier.applyToAllOpenProjects(enabled)
            refreshButtonText()
        }

        toggleCurrentColorButton.addActionListener {
            val enabled = colorSettings.panelIsDisabled()
            colorSettings.setPanelEnabled(enabled)
            WindowColorApplier.applyToCurrentOpenProject(project)
            refreshButtonText()
        }

        toggleCurrentTitleButton.addActionListener {
            val enabled = titleSettings.isTitleNumberingDisabled()
            titleSettings.setTitleNumberingEnabled(enabled)
            WindowTitleApplier.applyToCurrentOpenProject(project, enabled)
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