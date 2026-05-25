package com.window_accent

import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.window_accent.feature.window_color.WindowColorApplier
import com.window_accent.feature.window_title.WindowTitleApplier
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.ProjectManager

/**
 * Listener that handles plugin lifecycle events.
 *
 * When the plugin is unloaded (e.g., disabled by the user), we need to ensure
 * that all visual modifications to the IDE windows are reverted.
 */
open class PluginLifecycleListener : DynamicPluginListener {

    private val LOG = logger<PluginLifecycleListener>()

    init {
        LOG.info("[Window Accent] Lifecycle Listener registered")
//        restoreDecorations() // TODO BlakeGoudemond 2026/05/25 | if this is not needed, delete after v1.0.7
    }

    override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
        if (pluginDescriptor.pluginId.idString == "WindowAccent") {
            LOG.info("[Window Accent] Window Accent enabled/loaded: Restoring decorations")
            restoreDecorations()
        }
    }

    override fun beforePluginUnload(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
        val pluginId = pluginDescriptor.pluginId.idString
        if (pluginId == "WindowAccent") {
            LOG.info("[Window Accent] Cleanup triggered due to plugin being disabled")
            performCleanup()
        }
    }

    override fun pluginUnloaded(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
        // This might not show up if the plugin is already fully disposed
        LOG.info("[Window Accent] pluginUnloaded event for: ${pluginDescriptor.pluginId.idString}")
    }

    private fun restoreDecorations() {
        val openProjects = ProjectManager.getInstance().openProjects
        openProjects.forEach { project ->
            WindowColorApplier.applyToCurrentOpenProject(project)
            WindowTitleApplier.applyToCurrentOpenProject(project)
        }
    }

    open fun performCleanup() {
        WindowColorApplier.removeColorFromAllOpenProjectsSync()
        WindowTitleApplier.removeFromAllOpenProjectsSync()
    }
}
