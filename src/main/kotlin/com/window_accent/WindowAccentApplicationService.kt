package com.window_accent

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.wm.ToolWindowManager
import com.window_accent.configuration.persistence.GlobalCustomTitleStateService
import com.window_accent.configuration.persistence.WindowCustomColorStateService
import com.window_accent.configuration.persistence.WindowCustomTitleStateService
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService
import com.window_accent.configuration.persistence.WindowTitleNumberingStateService
import com.window_accent.configuration.settings.WindowAccentSettings
import com.window_accent.configuration.tool_window.WindowAccentToolWindowFactory
import com.window_accent.feature.window_color.WindowColorApplier
import com.window_accent.feature.window_title.WindowTitleApplier
import java.awt.Window
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
 * - IntelliJ's global icon cache ([com.intellij.openapi.util.IconLoader]) is cleared via
 *   [IconLoader.clearCache]. Without this, [com.intellij.ui.icons.ImageDataByPathLoader] objects
 *   stored as keys in the Caffeine icon cache retain a direct strong reference to the
 *   `PluginClassLoader` — preventing it from being GC'd during the dynamic-unload check.
 *   This was the confirmed root cause identified via hprof snapshot analysis (v1.2.10).
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

        /**
         * Returns true if [performCleanup] has already run for this plugin load cycle.
         *
         * Used by [com.window_accent.configuration.settings.WindowAccentSettings] to detect
         * the case where IntelliJ re-instantiates the configurable **after** our cleanup has
         * already run (e.g., for settings-search indexing or `isModified()` polling during
         * the plugin unload sequence). In that scenario the new instance must immediately
         * call [com.window_accent.configuration.settings.WindowAccentSettings.disposeUIResources]
         * on itself to clear the `sideCombo → DefaultComboBoxModel → Side[] → PluginClassLoader`
         * retention chain before IntelliJ's GC collectibility check fires.
         */
        fun isCleanupCompleted(): Boolean = cleanupCompleted.get()

        fun performCleanup(reason: String) {
            if (cleanupCompleted.compareAndSet(false, true)) {
                LOG.info("[Window Accent] Running final cleanup (reason=$reason)")
                WindowColorApplier.cancelCoroutines()
                WindowTitleApplier.cancelAllPendingOperations()
                WindowColorApplier.removeColorFromAllOpenProjectsSync()
                WindowTitleApplier.removeFromAllOpenProjectsSync()

                flushToolWindowListeners()
                flushToolWindowRegistrations()
                flushSettingsConfigurables()
                flushIntrospectorCaches()
                flushIconLoaderCache()
                flushStrokeIconCache()

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
         * Proactively unregisters the Window Accent tool window from IntelliJ's
         * [ToolWindowManager] for each open project.
         *
         * When IntelliJ registers a plugin tool window, its `ToolwindowKt` infrastructure
         * creates a `stripeTitleProvider` lambda that directly captures the plugin's
         * [com.intellij.ide.plugins.cl.PluginClassLoader] as `arg$1`, for resolving the
         * stripe button title from the plugin's resource bundle. This lambda is stored inside
         * a `ToolWindowEntry` in the tool window manager's `idToEntry` map, creating the chain:
         *
         * ```
         * ToolWindowManager.idToEntry → ToolWindowEntry → ToolWindowImpl.stripeTitleProvider
         *   → ToolwindowKt$$Lambda { arg$1 = PluginClassLoader }
         * ```
         *
         * IntelliJ's extension-point removal sequence also calls [ToolWindowManager.unregisterToolWindow]
         * during plugin unload, but in some configurations (e.g. JetBrains Remote Development
         * backend with [com.jetbrains.rdserver.toolWindow.BackendServerToolWindowManager]) this
         * happens after IntelliJ's GC collectibility check, so the retention chain is still
         * alive when the check runs.
         *
         * Calling [ToolWindowManager.unregisterToolWindow] here — before the GC check — removes
         * the `ToolWindowEntry` from the map proactively. IntelliJ's subsequent EP-removal call
         * is a no-op if the tool window is already gone.
         *
         * Root cause confirmed in `log-example-update-plugin-to-1.5.2.txt` via IntelliJ's own
         * snapshot analysis output (lines 492–530).
         */
        private fun flushToolWindowRegistrations() {
            val toolWindowId = "WindowAccent"
            ProjectManager.getInstance().openProjects.forEach { project ->
                try {
                    val twm = ToolWindowManager.getInstance(project)
                    if (twm.getToolWindow(toolWindowId) != null) {
                        twm.unregisterToolWindow(toolWindowId)
                        LOG.info("[Window Accent] Unregistered tool window '$toolWindowId' for project '${project.name}' to release stripeTitleProvider → PluginClassLoader reference")
                    }
                } catch (e: Exception) {
                    LOG.warn("[Window Accent] Could not unregister tool window '$toolWindowId' for project '${project.name}'", e)
                }
            }
        }

        /**
         * Calls [WindowAccentSettings.disposeAllTrackedInstances] to ensure all live
         * [WindowAccentSettings] instances have [WindowAccentSettings.disposeUIResources]
         * called **before** IntelliJ's classloader GC collectibility check runs.
         *
         * If the user opened the Settings panel before the plugin update, IntelliJ's
         * configurable cache holds one [WindowAccentSettings] instance per open project.
         * The instance's [sideCombo][javax.swing.JComboBox] stores
         * [WindowPanelAppearanceStateService.Side] enum values (plugin class instances),
         * creating the chain:
         *
         *   IntelliJ configurable cache → WindowAccentSettings → sideCombo
         *     → DefaultComboBoxModel → Side[] → Side.class → PluginClassLoader
         *
         * Calling [WindowAccentSettings.disposeUIResources] clears the combo model,
         * removes button ActionListeners, and nulls service references — breaking all
         * plugin-class reference chains from the configurable instance before the GC check.
         *
         * Additionally, even after [disposeUIResources] clears the instance's internals,
         * IntelliJ's configurable cache may still hold the **instance itself**. Since the
         * instance is a plugin class, the JVM type pointer alone is sufficient to keep the
         * classloader reachable:
         *
         *   IntelliJ cache → WindowAccentSettings instance
         *     → WindowAccentSettings.class → PluginClassLoader
         *
         * To address this, [WindowAccentSettings.findContainingWindows] locates the Swing
         * [Window] that contains our panel (while the component hierarchy is still intact —
         * before [disposeUIResources] calls [JPanel.removeAll]). After [disposeAllTrackedInstances]
         * clears the instance internals, [Window.dispose] is called on any found window.
         * This posts a [java.awt.event.WindowEvent.WINDOW_CLOSED] to the EDT queue; the
         * observed ~1–2 s gap between [beforePluginUnload] and the GC check is sufficient
         * for the EDT to process that event, prompting IntelliJ to clear its configurable
         * cache and release the [WindowAccentSettings] instance reference before GC runs.
         *
         * The timing of IntelliJ's own configurable disposal (relative to the GC check)
         * is not guaranteed; these calls make the disposal deterministic.
         */
        private fun flushSettingsConfigurables() {
            // Step 1: Snapshot window references BEFORE clearing internals.
            // disposeUIResources() calls panel.removeAll(), which severs the component
            // hierarchy and would cause SwingUtilities.getWindowAncestor(panel) to return null.
            val settingsWindows: Set<Window> = WindowAccentSettings.findContainingWindows()
            if (settingsWindows.isNotEmpty()) {
                LOG.info("[Window Accent] Found ${settingsWindows.size} Settings dialog window(s) containing Window Accent panel — will dispose after clearing instances")
            } else {
                LOG.info("[Window Accent] No Settings dialog window found containing Window Accent panel (panel not yet shown, or Settings already closed)")
            }

            // Step 2: Proactively call disposeUIResources() on all tracked instances.
            // Clears Side[] from combo model, removes ActionListeners, and nulls service refs.
            WindowAccentSettings.disposeAllTrackedInstances()

            // Step 3: Dispose the Settings dialog window(s) found in step 1.
            // window.dispose() posts WINDOW_CLOSED to the EDT queue. The ~1–2 s gap between
            // beforePluginUnload and the classloader GC check (observed in log analysis) gives
            // the EDT time to process that event: IntelliJ's DialogWrapper close handler fires,
            // clears the configurable cache, and releases the WindowAccentSettings instance
            // reference — so the type pointer (instance → class → classloader) is gone before
            // IntelliJ runs its GC collectibility check.
            settingsWindows.forEach { window ->
                if (window.isVisible) {
                    LOG.info("[Window Accent] Disposing Settings dialog (${window::class.simpleName}) to release configurable cache reference before GC check")
                    window.dispose()
                } else {
                    LOG.info("[Window Accent] Settings dialog (${window::class.simpleName}) already hidden — skipping dispose")
                }
            }
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
            // Flush both the service classes (PersistentStateComponent implementations) AND
            // their nested State data classes. IntelliJ's BeanBinding calls
            // Introspector.getBeanInfo() on the *state type* (T in PersistentStateComponent<T>)
            // when serializing/deserializing. The resulting BeanInfo is cached in
            // ThreadGroupContext.getBeanInfoCache() (a WeakHashMap<Class, BeanInfo>). The cache
            // value (BeanInfo) holds PropertyDescriptors → Methods → the State class itself,
            // creating a strong back-reference to the WeakHashMap key and preventing GC.
            // Without flushing the State classes, the retention chain is:
            //   ThreadGroup → ThreadGroupContext → BeanInfoCache → BeanInfo → Method
            //     → State.class → PluginClassLoader
            // flushFromCaches() is a no-op for classes not in the cache, so including
            // extra classes here is always safe.
            val classesToFlush = listOf(
                // Service classes (PersistentStateComponent implementations)
                WindowPanelAppearanceStateService::class.java,
                WindowCustomColorStateService::class.java,
                WindowTitleNumberingStateService::class.java,
                WindowCustomTitleStateService::class.java,
                GlobalCustomTitleStateService::class.java,
                // Nested State data classes — the types actually introspected by BeanBinding
                WindowPanelAppearanceStateService.State::class.java,
                WindowCustomColorStateService.State::class.java,
                WindowTitleNumberingStateService.State::class.java,
                WindowCustomTitleStateService.State::class.java,
                GlobalCustomTitleStateService.State::class.java,
                // Nested enum — may be introspected when serializing the Side property
                WindowPanelAppearanceStateService.Side::class.java,
            )
            classesToFlush.forEach { Introspector.flushFromCaches(it) }
            LOG.info("[Window Accent] Flushed Introspector BeanInfo caches for ${classesToFlush.size} classes (services + state types)")
        }

        /**
         * Clears IntelliJ's global icon-loading cache.
         *
         * When an icon is loaded via [IconLoader], a [com.intellij.ui.icons.ImageDataByPathLoader]
         * is created that stores the plugin ClassLoader so it can later resolve the icon resource
         * path. That loader object is kept as a key in a Caffeine [com.github.benmanes.caffeine.cache.BoundedLocalCache],
         * reachable from a Global JNI root, forming a strong reference chain:
         *
         *   Global JNI → PathClassLoader.classes → Class → Caffeine cache
         *     → CachedImageIcon (key) → ImageDataByPathLoader → PluginClassLoader
         *
         * Calling [IconLoader.clearCache] evicts all entries from that cache, releasing
         * the `PluginClassLoader` reference before IntelliJ runs its GC collectibility check.
         *
         * Root cause confirmed via hprof snapshot analysis during v1.2.10 plugin-disable test.
         */
        private fun flushIconLoaderCache() {
            IconLoader.clearCache()
            LOG.info("[Window Accent] Flushed IconLoader cache to release ImageDataByPathLoader → PluginClassLoader references")
        }

        /**
         * Clears IntelliJ's stroke-icon Caffeine cache (`StrokeKt.strokeIconCache`).
         *
         * IntelliJ 2026.x introduced a separate Caffeine cache in [com.intellij.ui.icons.StrokeKt]
         * for stroke/SVG icon variants. Cache values are [com.intellij.ui.icons.CachedImageIcon]
         * objects whose `loader` field is an [com.intellij.ui.icons.ImageDataByPathLoader] that
         * stores the plugin ClassLoader for resource resolution. This creates the chain:
         *
         *   GC Root (Global JNI) → PathClassLoader → StrokeKt.class
         *     → strokeIconCache (Caffeine) → CachedImageIcon → ImageDataByPathLoader
         *     → PluginClassLoader
         *
         * [IconLoader.clearCache] does NOT clear this cache. Calling `invalidateAll()` here
         * evicts all entries, releasing the `PluginClassLoader` reference before IntelliJ's
         * GC collectibility check.
         *
         * Root cause confirmed via hprof snapshot analysis during v1.5.1 plugin-update test
         * (`unload-WindowAccent-30.06.2026_06.25.59.hprof`). Reflective access with graceful
         * fallback handles older IntelliJ versions where this cache does not exist.
         */
        private fun flushStrokeIconCache() {
            try {
                val strokeKtClass = Class.forName("com.intellij.ui.icons.StrokeKt")
                val cacheField = strokeKtClass.getDeclaredField("strokeIconCache")
                cacheField.isAccessible = true
                val cache = cacheField.get(null) ?: run {
                    LOG.info("[Window Accent] StrokeKt.strokeIconCache is null — skipping flush")
                    return
                }
                val invalidateAll = cache.javaClass.getMethod("invalidateAll")
                invalidateAll.invoke(cache)
                LOG.info("[Window Accent] Flushed StrokeKt strokeIconCache to release ImageDataByPathLoader → PluginClassLoader references")
            } catch (e: ClassNotFoundException) {
                LOG.info("[Window Accent] StrokeKt not found — likely an older IntelliJ version, skipping strokeIconCache flush")
            } catch (e: NoSuchFieldException) {
                LOG.info("[Window Accent] StrokeKt.strokeIconCache field not found — cache may have been renamed, skipping flush")
            } catch (e: Exception) {
                LOG.warn("[Window Accent] Could not flush StrokeKt.strokeIconCache", e)
            }
        }
    }

    override fun dispose() {
        val onEdt = ApplicationManager.getApplication().isDispatchThread
        LOG.info("[Window Accent] dispose() called (onEdt=$onEdt, thread=${Thread.currentThread().name})")
        performCleanup("application-service-dispose")
    }

}
