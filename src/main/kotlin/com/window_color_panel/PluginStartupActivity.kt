package com.window_color_panel

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.window_color_panel.feature.window_color.WindowColorApplier
import com.window_color_panel.feature.window_title.WindowTitleApplier

/**
 * Applies persisted window decorations when a project starts.
 */
class PluginStartupActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        WindowColorApplier.applyToCurrentOpenProject(project)
        WindowTitleApplier.applyToCurrentOpenProject(project)
    }
}