package com.demo

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class ColourStartupActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        WindowColorApplier.apply(project)
    }

}