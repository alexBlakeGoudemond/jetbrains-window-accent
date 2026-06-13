package com.window_accent

import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.ProjectManager
import com.window_accent.feature.window_color.WindowColorApplier
import com.window_accent.feature.window_title.WindowTitleApplier

/**
 * Listener that handles plugin lifecycle events.
 *
 * When the plugin is loaded, we restore decorations to all open projects.
 * When the plugin is unloaded (e.g., disabled or updated by the user), we trigger
 * cleanup immediately and keep [WindowAccentApplicationService.dispose] as a guaranteed
 * fallback. Cleanup is idempotent, so both hooks are safe.
 *
 * This listener handles restoration on load and initiates unload cleanup on
 * beforePluginUnload to reduce timing risk for dynamic unload.
 */
class PluginLifecycleListener : DynamicPluginListener {

    companion object {
        private val LOG = logger<PluginLifecycleListener>()
    }

    init {
        LOG.info("[Window Accent] Lifecycle Listener registered")
    }

    override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
        if (pluginDescriptor.pluginId.idString == "WindowAccent") {
            // Reset the cleanup guard so the next unload cycle can perform cleanup.
            // Without this, cleanupCompleted stays true from the previous unload and
            // performCleanup() is skipped entirely on the next update, leaving listeners
            // and panels alive — preventing the classloader from being GC'd.
            WindowAccentApplicationService.resetCleanupState()
            ensureApplicationServiceHasBeenInstantiated()
            LOG.info("[Window Accent] Window Accent enabled/loaded: Restoring decorations")
            restoreDecorations()
        }
    }

    // Force-instantiate the application service so that IntelliJ will call dispose() on
    // it when the plugin is later unloaded. Application services are lazily instantiated,
    // so dispose() is only called if the service has been created at least once.
    private fun ensureApplicationServiceHasBeenInstantiated() {
        val windowAccentService = ApplicationManager
            .getApplication()
            .getService(WindowAccentApplicationService::class.java)
        LOG.info("[Window Accent] Window Accent Application Service created: ${windowAccentService.javaClass.name}")
    }

    override fun beforePluginUnload(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
        val pluginId = pluginDescriptor.pluginId.idString
        if (pluginId == "WindowAccent") {
            LOG.info("[Window Accent] Plugin unload triggered (update=$isUpdate) — running cleanup")
            WindowAccentApplicationService.performCleanup("before-plugin-unload")
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

}
