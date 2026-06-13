package com.window_accent

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.logger
import com.window_accent.configuration.persistence.WindowCustomColorStateService
import com.window_accent.configuration.persistence.WindowCustomTitleStateService
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService
import com.window_accent.configuration.persistence.WindowTitleNumberingStateService
import com.window_accent.configuration.tool_window.WindowAccentToolWindowFactory
import com.window_accent.feature.window_color.WindowColorApplier
import com.window_accent.feature.window_title.WindowTitleApplier
import java.beans.Introspector
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
 * - All pending retry-alarm requests are cancelled via [com.intellij.util.Alarm.cancelAllRequests]
 *   and the alarm itself is disposed via [com.intellij.openapi.util.Disposer.dispose], which nullifies
 *   any EDT-queued task wrappers that held lambda references.
 *
 * Note: an earlier version of this class also called [ApplicationManager.getApplication().invokeAndWait]
 * to drain the EDT queue of already-dispatched alarm task wrappers. This was removed because:
 * - It is a no-op on the normal [PluginLifecycleListener.beforePluginUnload] path (which fires on the EDT)
 * - When [dispose] fires from a background thread (e.g., during application shutdown in EAP automated
 *   review), the [invokeAndWait] blocked the thread while the EDT was busy with workspace/daemon teardown,
 *   exposing threading assertions in 2026.2 EAP (262.7132.23) that produced false-positive failures.
 * - The drain intent is already covered by [com.intellij.util.Alarm.cancelAllRequests] +
 *   [com.intellij.openapi.util.Disposer.dispose] which removes the alarm's internal lambda references.
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

        fun resetCleanupState() {
            cleanupCompleted.set(false)
        }

        fun performCleanup(reason: String) {
            if (cleanupCompleted.compareAndSet(false, true)) {
                LOG.info("[Window Accent] Running final cleanup (reason=$reason)")
                WindowColorApplier.cancelCoroutines()
                WindowTitleApplier.cancelAllPendingOperations()
                WindowColorApplier.removeColorFromAllOpenProjectsSync()
                WindowTitleApplier.removeFromAllOpenProjectsSync()

                flushToolWindowListeners()
                flushIntrospectorCaches()

                LOG.info("[Window Accent] Final cleanup completed successfully")
            } else {
                // Changing from debug to info so this is visible in standard logs.
                // This helps confirm whether dispose() was called after beforePluginUnload already ran.
                LOG.info("[Window Accent] Cleanup already completed, skipping duplicate (reason=$reason)")
            }
        }

        // Remove all button ActionListeners from the tool window panels.
        // These lambdas capture plugin service instances and singletons (WindowColorApplier,
        // WindowTitleApplier). If IntelliJ's ContentManager holds the tool window panel
        // after plugin unload, those listeners would keep the plugin classloader reachable
        // during the GC check.
        private fun flushToolWindowListeners() {
            WindowAccentToolWindowFactory.removeAllButtonListeners()
        }

        /**
         * Removes our persistence-service classes from Java's Introspector BeanInfo cache.
         *
         * IntelliJ's XML serializer may call [java.beans.Introspector.getBeanInfo] for the
         * [com.intellij.openapi.components.PersistentStateComponent] state classes, which caches
         * a [java.beans.BeanInfo] keyed on the [Class] object. This hard Class reference prevents
         * the plugin classloader from being garbage-collected during the dynamic-unload check.
         * Flushing the cache breaks that reference so the classloader becomes collectible.
         */
        private fun flushIntrospectorCaches() {
            val classesToFlush = listOf(
                WindowPanelAppearanceStateService::class.java,
                WindowCustomColorStateService::class.java,
                WindowTitleNumberingStateService::class.java,
                WindowCustomTitleStateService::class.java,
            )
            classesToFlush.forEach { Introspector.flushFromCaches(it) }
            LOG.info("[Window Accent] Flushed Introspector BeanInfo caches for ${classesToFlush.size} service classes")
        }
    }

    override fun dispose() {
        val onEdt = ApplicationManager.getApplication().isDispatchThread
        LOG.info("[Window Accent] dispose() called (onEdt=$onEdt, thread=${Thread.currentThread().name})")
        performCleanup("application-service-dispose")
    }

}
