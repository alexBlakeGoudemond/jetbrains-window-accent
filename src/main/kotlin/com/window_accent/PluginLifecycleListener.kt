package com.window_accent

import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.window_accent.feature.window_color.WindowColorApplier
import com.window_accent.feature.window_title.WindowTitleApplier

/**
 * Listener that handles plugin lifecycle events.
 *
 * When the plugin is unloaded (e.g., disabled by the user), we need to ensure
 * that all visual modifications to the IDE windows are reverted.
 */
open class PluginLifecycleListener : DynamicPluginListener {

    override fun pluginUnloaded(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
        if (pluginDescriptor.pluginId.idString == "WindowAccent") {
            performCleanup()
        }
    }

    open fun performCleanup() {
        WindowColorApplier.removeColorFromAllOpenProjectsSync()
        WindowTitleApplier.removeFromAllOpenProjectsSync()
    }
}
