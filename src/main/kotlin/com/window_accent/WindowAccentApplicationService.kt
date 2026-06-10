package com.window_accent

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.logger
import com.window_accent.feature.window_color.WindowColorApplier
import com.window_accent.feature.window_title.WindowTitleApplier
import java.util.concurrent.atomic.AtomicBoolean

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
 *
 * Cleanup is performed exactly once via [cleanupCompleted] AtomicBoolean to guard against
 * double-cleanup if [PluginLifecycleListener] or other mechanisms trigger cleanup multiple times.
 */
@Service(Service.Level.APP)
class WindowAccentApplicationService : Disposable {

    companion object {
        private val LOG = logger<WindowAccentApplicationService>()

        /**
         * Guards against double-cleanup. IntelliJ may call dispose multiple times,
         * and [PluginLifecycleListener.beforePluginUnload] might also trigger cleanup.
         * This flag ensures cleanup runs exactly once.
         */
        private val cleanupCompleted = AtomicBoolean(false)

        fun performCleanup(reason: String) {
            if (cleanupCompleted.compareAndSet(false, true)) {
                LOG.info("[Window Accent] Running final cleanup (reason=$reason)")
                WindowColorApplier.cancelCoroutines()
                WindowTitleApplier.cancelAllPendingOperations()
                WindowColorApplier.removeColorFromAllOpenProjectsSync()
                WindowTitleApplier.removeFromAllOpenProjectsSync()
                LOG.info("[Window Accent] Final cleanup completed successfully")
            } else {
                LOG.debug("[Window Accent] Cleanup already completed, skipping duplicate cleanup (reason=$reason)")
            }
        }
    }

    override fun dispose() {
        performCleanup("application-service-dispose")
    }

}

