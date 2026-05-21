package com.window_accent.configuration.tool_window

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService
import com.window_accent.configuration.persistence.WindowTitleNumberingStateService
import com.window_accent.feature.window_color.WindowColorApplier
import com.window_accent.feature.window_title.WindowTitleApplier
import java.awt.GridLayout
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JPanel

/**
 * DumbAware is an IntelliJ Platform marker interface used in JetBrains IDE plugins.
 *
 * In IntelliJ terminology:
 *
 * - “Smart mode” = indexes are ready, PSI/model analysis works normally
 * - “Dumb mode” = the IDE is still indexing, so many code-analysis features are temporarily unavailable
 *
 * When your class implements DumbAware, you're telling the IDE:
 *
 * “This component can safely run even while the IDE is indexing.”
 *
 * @see DumbAware
 * */
class WindowAccentToolWindowFactory : ToolWindowFactory, DumbAware {

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