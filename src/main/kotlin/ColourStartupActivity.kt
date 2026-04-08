package com.demo

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class ColourStartupActivity : StartupActivity {
    override fun runActivity(project: Project) {
        WindowColorApplier.apply(project)
    }
}