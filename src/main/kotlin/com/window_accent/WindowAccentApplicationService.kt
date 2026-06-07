package com.window_accent

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.logger
import com.window_accent.feature.window_color.WindowColorApplier
import com.window_accent.feature.window_title.WindowTitleApplier

/**
 * Application-level service that participates in IntelliJ's plugin lifecycle.
 *
 * When IntelliJ unloads the plugin dynamically (e.g., on update), it disposes all
 * application services registered by the plugin **before** running the classloader
 * GC check. This [dispose] method therefore acts as a guaranteed final cleanup hook,
 * running after [PluginLifecycleListener.beforePluginUnload] and ensuring that no
 * plugin-classloader references remain in platform code by the time IntelliJ checks
 * whether the classloader can be garbage-collected.
 *
 * Specifically, [dispose] ensures:
 * - [WindowTitleApplier.isShuttingDown] and [WindowColorApplier.isShuttingDown] are set,
 *   preventing any late-running EDT task from re-registering external references.
 * - All tracked AWT listeners are removed from their stored frames.
 * - All Disposer holders are evicted from IntelliJ's ObjectTree.
 * - All pending retry-alarm requests are cancelled and their runnables nulled.
 */
@Service(Service.Level.APP)
class WindowAccentApplicationService : Disposable {

    private val logger = logger<WindowAccentApplicationService>()

    override fun dispose() {
        logger.info("[Window Accent] Application service disposing — running final cleanup")
        WindowColorApplier.cancelCoroutines()
        WindowTitleApplier.cancelAllPendingOperations()
        WindowColorApplier.removeColorFromAllOpenProjectsSync()
        WindowTitleApplier.removeFromAllOpenProjectsSync()
    }

}

