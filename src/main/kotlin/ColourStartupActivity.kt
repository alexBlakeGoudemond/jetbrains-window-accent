package com.demo

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.startup.ProjectActivity

class ColourStartupActivity : ProjectActivity {

    private val log = Logger.getInstance(ColourStartupActivity::class.java)

    override suspend fun execute(project: Project) {
        log.warn("🔥 Plugin is running for project: ${project.name}")
        WindowColorApplier.apply(project)
    }
}