package com.window_accent

import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.application.ApplicationManager
import com.window_accent.diagnostic.windowAccentLogger
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
        private val LOG = windowAccentLogger<PluginLifecycleListener>()
    }

    init {
        LOG.info("Lifecycle Listener registered")
    }

    override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
        if (pluginDescriptor.pluginId.idString == "WindowAccent") {
            // Reset the cleanup guard so the next unload cycle can perform cleanup.
            // Without this, cleanupCompleted stays true from the previous unload and
            // performCleanup() is skipped entirely on the next update, leaving listeners
            // and panels alive — preventing the classloader from being GC'd.
            WindowAccentApplicationService.resetCleanupState()
            ensureApplicationServiceHasBeenInstantiated()
            LOG.info("Window Accent enabled/loaded: Restoring decorations")
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
        LOG.info("Window Accent Application Service created: ${windowAccentService.javaClass.name}")
    }

    override fun beforePluginUnload(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
        val pluginId = pluginDescriptor.pluginId.idString
        if (pluginId == "WindowAccent") {
            LOG.info("Plugin unload triggered (update=$isUpdate) — running cleanup")
            WindowAccentApplicationService.performCleanup("before-plugin-unload")
            // NOTE: ClassLoaderLeakDiagnostics.scheduleLeakCheck is intentionally NOT called here.
            // Scheduling any async task during the unload path means a plugin-class lambda will be
            // alive on a pooled thread when IntelliJ runs its GC collectibility check. Any class
            // loaded by the PluginClassLoader keeps it reachable (via Class.classLoader), so the
            // diagnostic always causes the very leak it is trying to detect.
            //
            // To temporarily re-enable for investigation, uncomment the lines below, run
            // ./gradlew runIde, trigger a disable/update, then remove again before committing:
            //
            // val classLoader = pluginDescriptor.pluginClassLoader
            // if (classLoader != null) { ClassLoaderLeakDiagnostics.scheduleLeakCheck(classLoader) }
            //
            // The companion ide.plugins.snapshot.on.unload.fail=true system property (set in
            // build.gradle.kts) will also produce a .hprof with a reference path on any genuine
            // unload failure.
        }
    }

    override fun pluginUnloaded(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
        // This might not show up if the plugin is already fully disposed
        LOG.info("pluginUnloaded event for: ${pluginDescriptor.pluginId.idString}")
    }

    private fun restoreDecorations() {
        val openProjects = ProjectManager.getInstance().openProjects
        openProjects.forEach { project ->
            WindowColorApplier.applyToCurrentOpenProject(project)
            WindowTitleApplier.applyToCurrentOpenProject(project)
        }
    }

}
