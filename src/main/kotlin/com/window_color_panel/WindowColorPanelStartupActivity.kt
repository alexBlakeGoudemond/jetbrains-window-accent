package com.window_color_panel

import com.window_color_panel.window_color.WindowColorApplier
import com.window_color_panel.window_title.WindowTitleApplier
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

/**
 * This is the entry point of the plugin. We define it as the entry point in the `plugin.xml`
 * */
class WindowColorPanelStartupActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        WindowColorApplier.applyToCurrentOpenProject(project)
        WindowTitleApplier.applyToCurrentOpenProject(project)
    }

}