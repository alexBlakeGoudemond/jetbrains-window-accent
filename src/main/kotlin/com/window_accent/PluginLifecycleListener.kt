package com.window_accent

import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.window_accent.feature.window_color.WindowColorApplier
import com.window_accent.feature.window_title.WindowTitleApplier
import com.intellij.openapi.diagnostic.logger

/**
 * Listener that handles plugin lifecycle events.
 *
 * When the plugin is unloaded (e.g., disabled by the user), we need to ensure
 * that all visual modifications to the IDE windows are reverted.
 */
open class PluginLifecycleListener : DynamicPluginListener {

    private val LOG = logger<PluginLifecycleListener>()

    init {
        // This will confirm in the logs that the listener is active
        LOG.info("[Window Accent] Lifecycle Listener registered")
    }

    override fun beforePluginUnload(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
        val pluginId = pluginDescriptor.pluginId.idString
        LOG.info("[Window Accent] PluginLifecycleListener#beforePluginUnloaded event for: $pluginId")
        if (pluginId == "WindowAccent") {
            LOG.info("[Window Accent] Cleanup triggered")
            performCleanup()
        }
    }

    override fun pluginUnloaded(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
        // This might not show up if the plugin is already fully disposed
        LOG.info("pluginUnloaded event for: ${pluginDescriptor.pluginId.idString}")
    }

    open fun performCleanup() {
        WindowColorApplier.removeColorFromAllOpenProjectsSync()
        WindowTitleApplier.removeFromAllOpenProjectsSync()
    }
}
