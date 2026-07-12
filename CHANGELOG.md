# Changelog

## [Unreleased]

## [2.3.0]

## [2.2.3]

- Fix the Update on Restart bug introduced in version 2.2.2
    - The `GlobalPanelBackgroundColorStateService` was not being cleaned up during GC, which caused the plugin to be
      retained and required a restart to update. This has been fixed by adding it to the flush list, allowing the plugin
      to be unloaded and updated without a restart

## [2.2.2]

### Added

- Added code block to try resolving the Plugin Verification Warning relating to `ToolWindowManager.unregisterToolWindow`
  being deprecated
    - Propose an alternative to resolving the unload problem with stripeTitleProvider: set to null via invasive
      reflection

## [2.2.1]

### Fixed

- Ensure that new class `GlobalPanelBackgroundColorStateService` is cleaned up during GC
    - Forgot to add this to the flush list, accidentally re-introducing the update forcing restart bug in 2.2.0
    - Adding it to the flush list should allow unloading and updating the plugin without a restart

## [2.2.0]

### Added

- Color Panel now has a colored background and adjustable padding
    - The user can control the thickness of the padding between the colored panel and the JPanel
    - The user can choose the color of both the color panel and the background
        - This is particularly useful to create a fading effect

## [2.1.2]

### Fixed

- Plugin Tool Window resizes nicely when interacting with other tool windows
    - The Quick Settings tab does not have any scroll bar (vertical or horizontal) – all buttons are always in view
    - The Settings tab has a vertical scroll bar if the window is too small to show all settings

## [2.1.1]

### Fixed

- Updating plugin should not restart
    - Code changes brought in through 2.1.0 prevented the Garbage Collector form unloading the plugin, forcing a restart
    - This version addresses that by adding support to cleanup the color preset radio buttons in the Tool Window, which
      were retaining a reference to the plugin classloader

## [2.1.0]

### Added

- Added color presets to the Tool Window
    - The settings tab has radio buttons to select from preset colors: 🔴🟠🟡🟢🔵🟣
    - Selecting a color preset adds an emoji to the title and sets the colored panel to the selected color

### Fixed

- Improved the UI of the colored panel
    - Layer the colored panel so there is a clear background
    - Round the panel's corners
    - Apply a shifting gradient to the colored panel

## [2.0.0]

### Added

- All settings are now done through the Tool Window
    - Quick Controls Panel: quickly toggle features through toggle buttons
    - Settings Panel: fine-grained control over Window Accent configuration

### Removed

- The configuration settings in the Settings view have been moved to the Tool Window
    - Settings now have a short paragraph informing the user about the Tool Window

## [1.6.3]

### Fixed

- Emojis now work in Custom Title on Restart of IDE windows
    - Bring in UnicodeXmlSanitizer to safely encode and decode Emojis

## [1.6.2]

### Fixed

- Fix `StrokeKt.strokeIconCache` flush still failing on JDK 25 despite `setAccessible(true)`
    - **Root cause** (`log-example-update-plugin-to-1.6.1.txt`, line 136): The fix in 1.6.1
      added `invalidateAll.isAccessible = true`, but `getMethod("invalidateAll")` resolves to
      the **interface default method** on `LocalManualCache`. On JDK 25, `Method.invoke()` uses
      an internal `MethodHandle`-based dispatch path that enforces `INVOKEINTERFACE` module-
      boundary access **independently of the accessible flag** — so `setAccessible(true)` sets
      the flag but `invoke` still throws `IllegalAccessException`. Confirmed: the `:336` line
      number in the 1.6.1 log is exactly `invoke`, proving `setAccessible` did not throw and
      the fix reached `invoke` but was rejected by the JDK 25 dispatch path.
    - **Fix:** replace `getMethod` (which may return an interface default method) with a concrete
      class hierarchy traversal using `getDeclaredMethod`. A concrete class override uses
      `INVOKEVIRTUAL` semantics which ARE properly suppressed by `setAccessible(true)`.
      Fallback: call `asMap()` (same traversal) then `map.clear()` — `asMap()` returns a
      `java.util.concurrent.ConcurrentMap` (JDK type), so `clear()` is always accessible
      regardless of Caffeine's module boundary. Three-strategy fallback chain with distinct
      log messages for each path.

### Known remaining issue

- `FeatureUsageSettingsEvents` channel retains `LogDefaultConfigurationState.aClass → PluginClassLoader`
    - **Observed** (`log-example-update-plugin-to-1.6.1.txt`, lines 591–717): After the
      tool window root was fixed in 1.6.1, the snapshot analyser found the next live reference:
      ```
      Thread: AppDelayQueue$TransferThread → CancellationScheduledFutureTask
        → coroutine context → ProjectImpl → MyProjectStore
        → FeatureUsageSettingsEvents.channel (BufferedChannel)
        → LogDefaultConfigurationState.aClass: Class<SomeWindowAccentStateService>
        → Class.classLoader: PluginClassLoader
      ```
    - **Root cause:** IntelliJ's FUS (Feature Usage Statistics) telemetry records state-component
      usages via `FeatureUsageSettingsEvents`. It queues `LogDefaultConfigurationState(aClass)`
      objects into a `BufferedChannel` for async processing. If the consumer coroutine has not
      drained the queue before the GC check fires (~27s gap in this log), the `Class` objects
      referencing the plugin's state service classes remain live and retain the `PluginClassLoader`.
    - **Pre-existing:** This root was masked in 1.6.0 and earlier by the closer tool window root.
      The tool window fix in 1.6.1 exposed it as the new dominant chain.
    - **Plugin's responsibility:** None directly — the retention is fully inside IntelliJ's async
      telemetry infrastructure. The plugin cannot clear a `BufferedChannel` without invasive
      Kotlin coroutines internals reflection.
    - **Action:** File a JetBrains issue requesting that `FeatureUsageSettingsEvents` be flushed
      or its `Class<?>` references replaced during plugin unload, or that the channel is drained
      synchronously before the GC collectibility check.

## [1.6.1]

### Fixed

- Accept the warning for `WindowAccentApplicationService.flushToolWindowRegistrations()`
    - Confirm that `@Suppress("DEPRECATION")` on `flushToolWindowRegistrations` is the only
      publishable approach after exhaustive investigation of all `ExtensionPoint` alternatives:
        1. `unregisterExtension(T)` — also `@Deprecated` ("Deprecated in Java"), confirmed via
           decompiled `ExtensionPoint.class` (`idea-2026.1-win/lib/util.jar`).
        2. `unregisterExtensions(BiPredicate<String, ExtensionComponentAdapter>, Boolean)` — not
           deprecated, but `ExtensionComponentAdapter` is `@Internal`; Kotlin emits it in the
           generic signature bytecode attribute even when referenced only as `_`, causing a
           plugin-verifier failure ("usage of Internal API").
        3. `unregisterExtension(Class<out T>)` — not deprecated, but removes ALL extensions of
           that class type (unregisters every plugin's tool windows). Categorically wrong.
    - `@Deprecated` does **not** fail the plugin verifier; `@Internal` does. `@Suppress` with
      full documentation is the correct and only viable path for a publishable plugin.
- Attempt to address update causing restart due to `StrokeKt.strokeIconCache`
    - The flush failing with `IllegalAccessException` during plugin update
    - **Root cause** (`log-example-update-plugin-to-1.6.0.txt`, lines 116–196):
      `getMethod("invalidateAll")` resolves `invalidateAll()` as declared on the Caffeine
      `LocalManualCache` interface rather than a concrete override. Invoking an
      interface-declared method reflectively without `setAccessible(true)` causes
      `IllegalAccessException` at invocation time (JVM access check on the interface's
      package/module), leaving `strokeIconCache` un-flushed and retaining `PluginClassLoader`
      references through the `StrokeKt → strokeIconCache → CachedImageIcon → ImageDataByPathLoader`
      chain. This regressed on IntelliJ 2026.1 build 261.22158.277.
    - **Fix:** added `invalidateAll.isAccessible = true` before `invalidateAll.invoke(cache)`.
      A module-level `InaccessibleObjectException` is still caught by the existing `Exception`
      handler with a graceful warning.

## [1.6.0]

### Added

- Window title labels are now styled using Unicode Mathematical Alphanumeric Symbols
    - The **global title** (all windows) is rendered in **bold**: e.g. `[𝗽𝗿𝗼𝗱]`
    - The **per-window custom title** (and number) are rendered in *italic*: e.g. `[𝟏 - 𝑑𝑎𝑡𝑡𝑒𝑏𝑎𝑦𝑜]`
    - Styling is applied to the text inside the brackets only — the brackets themselves are unchanged
    - No new settings or toggles are required; styling is automatic
    - Introduced `TitleTextStyler` utility (`feature/window_title`) with `toBold()` and `toItalic()` functions
    - **Unicode constraint**: italic digits do not exist in Unicode — digit characters in the number prefix
      pass through unchanged. This is a Unicode limitation and not a bug.
    - **No restart risk**: `TitleTextStyler` is a pure stateless utility; it introduces no new platform
      registrations, listeners, or Disposer entries and does not affect the existing unload cleanup path

### Fixed

- Add @Deprecated to method for ToolWindowManager.unregisterToolWindow to try to remove Plugin Verification warning
- Update title numbering state across all open projects in the toggle listener
    - Should address bug where toggling title numbering does not update all open window instances - just the focussed
      one

## [1.5.3]

### Fixed

- Fix tool window stripe title provider retaining `PluginClassLoader` after plugin update
    - **Root cause identified via hprof snapshot and IntelliJ built-in analysis**
      (`unload-WindowAccent-30.06.2026_08.00.56.hprof`, log lines 492–530):
      When IntelliJ registers a plugin tool window, its `ToolwindowKt` infrastructure creates a
      `stripeTitleProvider` lambda that directly captures the plugin's `PluginClassLoader` as
      `arg$1` (for resolving the stripe button title from the plugin resource bundle). In the
      JetBrains Remote Development backend, this entry in `BackendServerToolWindowManager.idToEntry`
      was not cleared before IntelliJ's GC collectibility check ran. The retention chain was:
      `GC Root (Global JNI) → PathClassLoader → BackendServerToolWindowManager.idToEntry
        → ToolWindowEntry → ToolWindowImpl.stripeTitleProvider
        → ToolwindowKt$$Lambda { arg$1 = PluginClassLoader }`
    - **Fix:** added `flushToolWindowRegistrations()` which proactively calls
      `ToolWindowManager.unregisterToolWindow("WindowAccent")` for each open project during
      `beforePluginUnload`. IntelliJ's own EP-removal call is a no-op if the tool window is
      already unregistered.

## [1.5.2]

### Fixed

- Fix icon stroke icon (002) likely causing update on restart bug
    - **Root cause identified via hprof snapshot** (`unload-WindowAccent-30.06.2026_06.25.59.hprof`):
      same underlying mechanism as v1.2.10 (`ImageDataByPathLoader → PluginClassLoader`), but
      through a **different Caffeine cache** — `com.intellij.ui.icons.StrokeKt.strokeIconCache` —
      introduced in IntelliJ 2026.x for stroke/SVG icon variants.
    - The existing `IconLoader.clearCache()` call (added in v1.2.10) does not clear this separate
      cache. The retention chain was:
      `GC Root (Global JNI) → PathClassLoader → StrokeKt.strokeIconCache (Caffeine)
        → CachedImageIcon → ImageDataByPathLoader.classLoader → PluginClassLoader`
    - **Fix:** added `flushStrokeIconCache()` which reflectively calls `invalidateAll()` on
      `StrokeKt.strokeIconCache` during plugin unload. Gracefully skips on older IntelliJ
      versions where the cache does not exist.

## [1.5.1]

### Fixed

- Window Titles should no longer 'forget' custom titles
    - Occasionally, when multiple IDE windows are open, the custom titles would be
      absent from most of the window instances

## [1.5.0]

### Added

- Added option for Global Window Title
    - Can now set a custom title that will show on all windows

## [1.4.4]

### Fixed

- Pass 019 of improving Plugin Unloading to avoid unnecessary project restarts
    - **Root cause identified via hprof snapshot** (`unload-WindowAccent-23.06.2026_05.55.00.hprof`):
      `WindowAccentSettings` (Count=1, retained=11.46 kB) was alive at GC check time with
      `sideCombo` still populated with `Side[]` entries, forming the chain:
      `WindowAccentSettings → sideCombo → DefaultComboBoxModel → Side[] → Side.class → PluginClassLoader`
    - **Retention path in hprof** traced through
      `rightPanelLayout of com.intellij.openapi.wm.impl.status.IdeStatusBarImpl` — IntelliJ's New UI
      status bar machinery (tool window strip / settings-search widget) re-accesses registered
      configurables during plugin unload, recreating the instance after our cleanup ran.
    - **Why `0 live instances disposed` + Count=1 in heap:** IntelliJ released its strong reference to
      the `WindowAccentSettings` instance just before `disposeAllTrackedInstances()` ran (clearing our
      `WeakReference`), then re-instantiated the configurable shortly afterwards for platform
      bookkeeping (settings-search indexing, `isModified()` polling). The new instance went into the
      already-cleared `trackedInstances` and was never disposed.
    - **Fix:** Added a post-cleanup self-disposal guard to `WindowAccentSettings.init {}`. After adding
      itself to `trackedInstances`, the constructor now checks
      `WindowAccentApplicationService.isCleanupCompleted()`. If cleanup has already run — meaning the
      new instance was created inside the unload window — it immediately calls `disposeUIResources()`
      on itself, clearing `sideCombo` and nulling all service references. This breaks the
      `Side[] → PluginClassLoader` chain even when IntelliJ creates a fresh instance after our cleanup.
    - Added `WindowAccentApplicationService.isCleanupCompleted()` companion function to expose the
      existing `cleanupCompleted` flag for use by `WindowAccentSettings`.
    - Added `WindowAccentApplicationService.resetCleanupState()` call to `WindowAccentSettingsTest`
      `@BeforeEach` to prevent cross-test `cleanupCompleted` state pollution.

### Diagnostic notes (from hprof analysis —

- **hprof Classes view (filtered to `com.window_accent`):**
    - `WindowAccentSettings` — Count=1, Retained=11.46 kB → **primary retention root** (alive at GC
      check with unpopulated `disposeUIResources()` — instance created after cleanup ran)
    - `WindowPanelAppearanceStateService$Side` — Count=4 (all 4 enum values alive)
    - `WindowPanelAppearanceStateService$Side[]` — Count=2 (enum `entries` array + combo model array)
    - `WindowColorApplier` — Count=1, 16 B retained (object singleton, expected)
    - `WindowTitleApplier` — Count=1, 48 B retained (instance, expected)
    - `WindowAccentApplicationService` — Count=0 ✅ (correctly disposed)
    - `PluginStartupActivity`, `PluginLifecycleListener` — Count=0 ✅
- **Retention path:** `IdeStatusBarImpl.rightPanelLayout (BoxLayout)` → right panel container →
  status bar widget (tool window strip / search widget in New UI) → configurable reference →
  `WindowAccentSettings` instance → `WindowAccentSettings.class` → `PluginClassLoader`
- **Non-determinism explained:** The race is between IntelliJ's configurable re-instantiation timing
  and the GC check. When IntelliJ releases and re-creates the configurable between our cleanup and
  the GC check, the failure occurs. When it doesn't, the GC check passes. The `init {}` guard makes
  any re-created instance inert regardless of timing.

## [1.4.3]

### Fixed

- No changes present - additional version to try reproduce non-deterministic bug: updating requires restart
    - Added properties to `AppData\Roaming\JetBrains\IntelliJIdea2026.1\idea64.exe.vmoptions` that should produce an
      hprof snapshot on author's disk during unload failure:
        - `-XX:+UnlockDiagnosticVMOptions`
        - `-Dide.plugins.snapshot.on.unload.fail=true`

### Diagnostic notes (from log analysis — update logs 001 and 002, IU-261.22158.277, 2026-06-23)

- **Two logs compared — same source version 1.4.1, same target version 1.4.2, same IDE build:**
    - **Log 001**: `1.4.1 → 1.4.2`, Settings NOT opened, custom title `customTitleNew` previously set →
      `isUpdate=false`, `0 live instances disposed` →
      classloader could **not** be unloaded
      (`Plugin WindowAccent is not unload-safe because class loader cannot be unloaded`) ❌
    - **Log 002**: `1.4.1 → 1.4.2`, Settings NOT opened, custom title `customTitleNew` previously set →
      `isUpdate=false`, `0 live instances disposed` →
      classloader **unloaded successfully**
      (`Successfully unloaded plugin WindowAccent (classloader unload checked=true)`,
      `Plugin WindowAccent loaded without restart in 252 ms`) ✅
- **Identical surface conditions, different outcomes — the failure is non-deterministic.**
  The settings-open retention chain (fixed in 1.4.1) was not involved in either log. A second,
  intermittent retention root exists that does not require the Settings panel to have been opened.
  Likely candidates: GC timing (a `WeakReference` that hasn't been cleared at the moment of IntelliJ's
  GC check), a coroutine scope that hasn't fully cancelled, or an IntelliJ-internal cache that varies
  per session.
- **`isUpdate=false` is confirmed NOT a predictor of failure.** Both log 001 (❌) and log 002 (✅)
  show `update=false`; the flag does not determine whether the classloader is successfully released.
- **Settings-open case appears resolved.** Both logs had `0 live instances disposed`, confirming the
  1.4.1 `disposeAllTrackedInstances()` / `sideCombo.removeAllItems()` fix correctly handles the
  settings-open retention chain. No further regression observed on that path.
- **No hprof generated for log 001's failure.** The `-Dide.plugins.snapshot.on.unload.fail=true` VM
  option was added to `AppData\Roaming\JetBrains\IntelliJIdea2026.1\idea64.exe.vmoptions` after
  log 001 was captured. Note: this file is **shared across all custom JetBrains configurations**
  (custom configs only isolate plugins, settings dir, and system cache — not the JVM). The option
  is now active for all environments; the next failure will write an `unload-WindowAccent-*.hprof`
  to `C:\Users\<userDirectory>\` regardless of which custom config triggers the update.
- **Next step:** Reproduce the intermittent failure (update away from 1.4.2 with settings not open)
  and capture the hprof to identify the second retention root before adding further code fixes.

## [1.4.2]

### Fixed

- Pass 018 of improving Plugin Unloading to avoid unnecessary project restarts
    - Added `WindowAccentSettings.findContainingWindows()` — finds the Swing window that
      contains the settings panel **before** `disposeAllTrackedInstances()` clears the component
      hierarchy (which would sever the ancestor chain). Returns the set of windows to close.
    - Updated `flushSettingsConfigurables()` to call `window.dispose()` on any Settings dialog
      found to contain our panel. `window.dispose()` posts a `WINDOW_CLOSED` event to the EDT
      queue. The observed ~1–2 s gap between `beforePluginUnload` and the classloader GC check
      is sufficient for the EDT to process that event — prompting IntelliJ to clear its
      configurable cache and release its reference to the (now-internals-cleared)
      `WindowAccentSettings` instance before the GC check runs.

### Diagnostic notes (from log analysis — update logs 001 and 002, IU-261.22158.277, 2026-06-18)

- **Two logs compared — same IDE build, same day, same target version 1.4.1:**
    - **Log 001** (13:57): `1.4.0 → 1.4.1`, Settings NOT opened → `isUpdate=true` →
      hot-reload succeeded (`Plugin WindowAccent loaded without restart in 32 ms`) ✅
    - **Log 002** (14:00): `1.3.1 → 1.4.1`, Settings WERE opened (user visited
      `Settings > Appearance > Window Accent` and set a custom title) → `isUpdate=false` →
      classloader could not be unloaded
      (`Plugin WindowAccent is not unload-safe because class loader cannot be unloaded`) ❌
- **Settings being open is confirmed as the direct cause — not a coincidence.** Version 1.3.1
  lacked `sideCombo.removeAllItems()` and proactive `disposeAllTrackedInstances()`. The
  `sideCombo`'s `DefaultComboBoxModel` still held `Side[]` enum values, forming the chain:
  `IntelliJ configurable cache → WindowAccentSettings → sideCombo → DefaultComboBoxModel
    → Side[] → Side.class → PluginClassLoader`
- **`isUpdate=false` in log 002:** Indicates IntelliJ either pre-detected the classloader was
  not GC-able (due to the cached `WindowAccentSettings` configurable in the open Settings
  dialog) and switched to the "requires restart" path, or converted to it after the GC check
  failed. Either way, cleanup still ran and the root cause is the residual configurable reference.
- **Open question carried into 1.4.2:** The 1.4.1 fix clears the `WindowAccentSettings`
  instance's internals via `disposeAllTrackedInstances()`, but IntelliJ's configurable cache
  may still hold the **instance itself**. Since the instance is a plugin class, the JVM type
  pointer alone keeps the classloader reachable:
  `IntelliJ cache → WindowAccentSettings instance → WindowAccentSettings.class → PluginClassLoader`
  The `window.dispose()` call added in 1.4.2 addresses this by prompting IntelliJ to clear its
  configurable cache (via the `WINDOW_CLOSED` handler) before the GC check runs. The `LOG.info`
  added to `disposeUIResources()` in 1.4.1 will confirm whether this resolves the issue in the
  next update log.

## [1.4.1]

### Fixed

- Pass 017 of improving Plugin Unloading to avoid unnecessary project restarts
    - Added `LOG.info` to `WindowAccentSettings#disposeUIResources()` so the next update log
      will reveal whether IntelliJ calls it before or after the classloader GC check
    - `WindowAccentSettings.disposeUIResources()` now also calls `sideCombo.removeAllItems()`
      to clear `Side` enum values from the `DefaultComboBoxModel`, and removes ActionListeners
      from the color/title buttons (plugin lambdas capturing `this`)
    - Added `WeakReference` instance tracking in `WindowAccentSettings.companion` and
      `disposeAllTrackedInstances()`, called from `WindowAccentApplicationService.performCleanup()`
      to ensure `disposeUIResources()` runs deterministically before the GC check regardless of
      IntelliJ's internal configurable-disposal ordering
    - Moved `sidesCycleOrder` from an instance field to the companion object of
      `WindowAccentToolWindowFactory` so the factory instance holds no plugin-class references
      in its own fields (IntelliJ's tool-window registry may hold the factory instance briefly
      after unload)

### Diagnostic notes (from log analysis — update from 1.3.1 to 1.4.0, IU-261.22158.277)

- **Confirmed retention path:** User had visited Settings > Appearance > Window Accent (to set a
  custom title), causing IntelliJ to cache a `WindowAccentSettings` instance per open project (4
  projects open). The instance's `sideCombo` (`JComboBox`) held `Side` enum values in its
  `DefaultComboBoxModel`, creating:
  `IntelliJ configurable cache → WindowAccentSettings → sideCombo → DefaultComboBoxModel
    → Side[] → Side.class → PluginClassLoader`
- **`disposeUIResources()` timing unknown:** The existing log had no entry for this method being
  called, making it impossible to determine whether IntelliJ called it before or after the GC
  check at `10:41:06,591`. The new `LOG.info` statement will resolve this in the next update.
- **Secondary path also addressed:** `WindowAccentToolWindowFactory.sidesCycleOrder` was an
  instance field holding all 4 `Side` values. Moving it to companion eliminates the secondary
  chain: `IntelliJ tool-window registry → factory instance → sidesCycleOrder → Side.class → ClassLoader`

## [1.4.0]

### Added

- Extended tests to improve test coverage

## [1.3.1]

### Fixed

- Fix classloader leak by ensuring toolWindow.disposable lambda is explicitly disposed during plugin updates

## [1.3.0]

### Added

- Added a button to the Tool Window to reset the Title Numbering
    - If several window instances have opened and closed, numbering can become haphazard; drifting from 1, 2, 3, ... to
      3, 7, 9, 13, ...
    - Click a button in the tool window to reset the numbering to start from 1, with the current window being number 1
- Added a border pulse animation to the Tool Window buttons to show when they have been clicked

## [1.2.12]

### Fixed

- Reviewed Changelog to confirm that all entries are present

## [1.2.11]

### Fixed

- Pass 015 of improving Plugin Unloading to avoid unnecessary project restarts
    - Added additional classes to WindowAccentApplicationService.flushIntrospectorCaches()

## [1.2.10]

### Fixed

- Passes 014 of improving Plugin Unloading to avoid unnecessary project restarts
    - Introduced `ClassLoaderLeakDiagnostics` utility (internal/sandbox only) to assist with diagnosing
      future classloader leaks using thread-stack scanning and `LeakHunter` reflection.
    - Confirmed clean unload via log `Successfully unloaded plugin WindowAccent (classloader unload checked=true)` in
      sandbox logs
    - Fixed classloader leak caused by IntelliJ's global Caffeine icon cache retaining a
      `PluginClassLoader` reference via `ImageDataByPathLoader`. Added `IconLoader.clearCache()`
      to `WindowAccentApplicationService.performCleanup()` to evict all cached icon entries before
      IntelliJ runs its GC collectibility check.

### Diagnostic notes (from log analysis)

- **Pass 014 — icon cache leak (log 001):** hprof snapshot revealed the reference path:
  `Global JNI → PathClassLoader.classes → Class → Caffeine BoundedLocalLoadingCache →
  CachedImageIcon → ImageDataByPathLoader → PluginClassLoader`. Cleared by `IconLoader.clearCache()`.
- **Pass 015 — diagnostic lambda captured classloader directly (log 002):** `ClassLoaderLeakDiagnostics`
  dispatched a lambda that captured the `ClassLoader` parameter, keeping it strongly reachable on the
  pooled thread's stack frame during IntelliJ's GC check. Fixed by passing only a `WeakReference`
  across the thread boundary and obtaining the strong reference after GC rounds.
- **Pass 016 — lambda class object retained classloader (log 003):** Even without a captured variable,
  the lambda's own class object (`ClassLoaderLeakDiagnostics$$Lambda`) keeps the `PluginClassLoader`
  alive via `Class.classLoader`. Fundamental constraint: any plugin code executing on any thread
  during the GC check window makes the classloader reachable. Resolved by removing the
  `scheduleLeakCheck` call from `beforePluginUnload` entirely. The method call is preserved as a
  commented-out line for future ad-hoc investigation.
- **GC check behaviour update:** Sandbox plugin *disable* (not just marketplace *update*) now triggers
  the classloader GC check (`classloader unload checked=true`) in IU-261.22158.277 (2026.1).

## [1.2.9]

### Fixed

- Pass 013 of improving Plugin Unloading to avoid unnecessary project restarts
    - Reverted the 1.2.8 approach of parenting `Alarm` to `ApplicationManager.getApplication()`
    - Enabled heap snapshot generation on classloader GC check
      failure, allowing precise diagnosis of any remaining leaks via `.hprof` analysis.

### Diagnostic notes (from log analysis)

- Reverted the 1.2.8 approach of parenting `Alarm` to `ApplicationManager.getApplication()`.
  Parenting to the application caused the platform's Disposer tree to hold a reference chain
  `Application → retryAlarm → Alarm internals → plugin class → PluginClassLoader`, which is an
  **external** reference from platform code into the plugin classloader — exactly what the GC
  check detects. The fix is to create `Alarm` with no parent and call `cancelAllRequests()` then
  `Disposer.dispose(retryAlarm)` explicitly during cleanup, so no platform-owned object retains
  a reference to the alarm after unload.
    - Added `-XX:+UnlockDiagnosticVMOptions` and `ide.plugins.snapshot.on.unload.fail=true` to
      `runIde` in `build.gradle.kts` to enable heap snapshot generation on classloader GC check
      failure, allowing precise diagnosis of any remaining leaks via `.hprof` analysis.

## [1.2.8]

### Fixed

- Pass 012 of improving Plugin Unloading to avoid unnecessary project restarts
    - `WindowColorApplier` and `WindowTitleApplier` `Alarm` instances are now created with the Application Thread.

### Diagnostic notes (from log analysis)

- Fixed classloader leak caused by `Alarm` instances in `WindowColorApplier` and
  `WindowTitleApplier` being created without a parent disposable. When cleanup called
  `Disposer.dispose(retryAlarm)`, the platform posted a final EDT cancellation task that
  held a reference chain (`EDT task → Alarm → singleton class → plugin classloader`).
  The GC check fired before that task ran, causing the "class loader cannot be unloaded" warning.
- Fix: pass `ApplicationManager.getApplication()` as the parent disposable to both
  `Alarm` constructors. The platform now owns the alarm lifecycle; `cancelAllRequests()`
  alone is sufficient to release all pending task references, and no EDT cancellation
  task is posted on unload.
- Removed now-unused `retryAlarmDisposed` `AtomicBoolean` fields from both appliers.

## [1.2.7]

### Fixed

- Pass 011 of improving Plugin Unloading to avoid unnecessary project restarts
    - Improved `WindowAccentSettings.disposeUIResources` to also call `panel.removeAll()` and
      `form.removeAll()`.

### Diagnostic notes (from log analysis)

- Moved `LOG` in `PluginLifecycleListener` from an instance field to a `companion object` field.
  The platform message bus holds the `PluginLifecycleListener` instance after plugin unload;
  an instance-field logger holds a reference to `PluginLifecycleListener.class`, keeping the
  plugin classloader reachable during IntelliJ's GC check. Moving it to the companion makes it
  a static field within the plugin classloader (not held externally via the bus instance).
- Improved `WindowAccentSettings.disposeUIResources` to also call `panel.removeAll()` and
  `form.removeAll()`, clearing the Swing component tree so that if IntelliJ's configurable
  cache retains the instance, it no longer holds plugin class references via component fields.

## [1.2.6]

### Fixed

- Pass 010 of improving Plugin Unloading to avoid unnecessary project restarts
    - Added `resetCleanupState()` to `WindowAccentApplicationService` and call it from
      `PluginLifecycleListener.pluginLoaded` so each new load cycle starts with a clean slate for the next unload

### Diagnostic notes (from log analysis)

- **Suspected Root cause**: `cleanupCompleted` (`AtomicBoolean` in `WindowAccentApplicationService` companion) was
  never reset between plugin load cycles. After the first update, the flag remained `true`, causing `performCleanup`
  to skip all cleanup on every subsequent unload — leaving AWT listeners, panels, and Disposer entries alive and
  preventing the classloader from being GC'd.
- **Fix**: Added `resetCleanupState()` to `WindowAccentApplicationService` and call it from
  `PluginLifecycleListener.pluginLoaded` so each new load cycle starts with a clean slate for the next unload.
- **Test**: Added `cleanupStateIsResetOnPluginReload` to `PluginUnloadingVerificationTest` to verify the
  reset/unload cycle behaves correctly.

## [1.2.5]

### Fixed

- Pass 009 of improving Plugin Unloading to avoid unnecessary project restarts
    - **Hypothesis A — Introspector cache**: Call `Introspector.flushFromCaches(clazz)` for all 4 persistence service
      classes in `performCleanup` to release any hard `Class<?>` keys
    - **Hypothesis B — tool window button listeners**: Track all 6 `ActionListener` lambdas statically and call
      `removeAllButtonListeners()` in `performCleanup` to drop captured service/singleton references
    - **Hypothesis C — settings configurable fields**: Null all 4 project service fields in
      `WindowAccentSettings.disposeUIResources()` so a cached configurable instance cannot retain classloader references
    - **Diagnostic improvement**: Changed "cleanup already completed" log from `DEBUG` to `INFO`
    - Removed `flushEdtQueue()` that caused EAP 2026.2 (262.7132.23) compatibility failures

### Diagnostic notes (from log analysis)

- **Disable skips the GC check**: Disabling produces `classloader unload checked=false` — only a marketplace **update**
  triggers the real GC check (`checked=true`)
- **`"Application Service created"` absent**: `pluginLoaded` only fires on dynamic enable, not on IDE-startup loads —
  expected absent
- **`"pluginUnloaded"` absent**: Listener is intentionally unregistered in `beforePluginUnload` before this event
  fires — expected absent
- **EDT flush was always skipped on disable path**: `beforePluginUnload` runs on the EDT, confirming `flushEdtQueue()`
  was dead code for that path and only activated on the background-thread `dispose()` path

## [1.2.4]

### Fixed

- Pass 008 of improving Plugin Unloading to avoid unnecessary project restarts
    - **Root cause identified from log analysis**: `WindowAccentApplicationService.dispose()` was
      never called during unload because the service was never instantiated (lazy services are only
      disposed if they have been created). Fixed by force-instantiating the service in `pluginLoaded`
      so IntelliJ will call `dispose()` on every subsequent unload.
    - Fixed ordering bug in `addColoredPanel`: the old Disposer holder's cleanup callback
      (`removeTrackedPanels`) was fired after `addedPanels` was updated to the new panel, causing the
      newly-added panel to be immediately removed. Cleanup now runs in correct order:
      remove old panels → dispose old holder → track new panel → register new holder.

## [1.2.3]

### Fixed

- Pass 007 of improving Plugin Unloading to avoid unnecessary project restarts
    - Register WindowColorApplier panels for cleanup

## [1.2.2]

### Fixed

- Disposed retry `Alarm` instances during unload cleanup
    - Prevents platform-held scheduler references from retaining plugin classloader state across updates

### Added

- Brought in additional tests to try prevent regression with plugin updating leading to restart
    - Updating the plugin should NOT require a restart - so attempts are being made to prevent this

## [1.2.1]

### Fixed

- Reduced dynamic-unload restart fallback risk during plugin update
    - Replaced queued EDT `invokeLater` accent application/removal calls with synchronous EDT execution
    - Avoids leaving queued plugin runnables in the event queue during classloader unload checks

### Added

- Added `.ai-resources` to assist with onboarding the AI model when a fresh context window is used

## [1.2.0]

### Fixed

- Improved Plugin description
    - Leverage header tags and emojis to improve readability
    - Review phrasing of description

### Added

- Brought in a button in the Tool Window to cycle Color Panel Location
    - One can now quickly set the location of the Color Panel: NSWE

## [1.1.0]

### Added

- Added ability to set a custom window title, separate from the window numbering
    - Adding a custom title will appear alongside the title number, if enabled: `[1 - customTitle] ...`

## [1.0.13]

### Fixed

- Fourth pass of improving Plugin Unloading to avoid unnecessary project restarts
    - Define WindowAccentApplicationService to manage lifecyle
    - Refine cleanup code where possible

## [1.0.12]

### Fixed

- Third pass of improving Plugin Unloading to avoid unnecessary project restarts
    - Use Alarm.ThreadToUse.SWING_THREAD to try release references to plugin-code synchronously

## [1.0.11]

### Fixed

- Second pass of improving Plugin Unloading to avoid unnecessary project restarts
    - leverage `scope.cancel()` when unloading

## [1.0.10]

### Fixed

- Improve Plugin Unloading logic to avoid unnecessary project restarts
    - The problem was caused by the way the Dispose logic was implemented – now load and unload using a unique String

## [1.0.9]

### Fixed

- Fixed a bug where Choosing Color from the settings menu locks to the primary screen - now can select colour from all
  connected displays (multiple screens)

## [1.0.8]

### Fixed

- Improved support of .ai-playbook for developers by revising AGENTS.md
- Fixed a `ClassCastException` that occurred during plugin re-enabling from coroutine leaks
    - Added `cancelCoroutines()` to `WindowColorApplier`
    - Added `cancelAllPendingOperations()` to `WindowTitleApplier` for retry logic
    - Leverage `cancelCoroutines()` and `cancelAllPendingOperations()` in `PluginLifecycleListener`

## [1.0.7]

### Added

- Add support for custom ai-playbook instructions, for developers

### Fixed

- Address a bug with SOUTH Color Panel overlapping / sharing the frame with the Status Bar
    - now SOUTH panel is a separate color underneath the Status Bar
- Addressed Compatibility Verification issues raised re: final classes:
    - `WindowPanelAppearanceStateService`
    - `WindowTitleNumberingStateService`
    - `WindowCustomColorStateService`

## [1.0.6]

### Fixed

- Attempt 003 to resolve bug where accent changes remain after plugin is disabled
    - Confirmed using IDE logs that cleanup is not being triggered - need to use event `beforePluginUnload` instead

## [1.0.5]

### Added

- Updated platformVersion from 2025.3 to 2026.1; allowing for more testing on the current build

### Fixed

- Attempt 002 to resolve bug where accent changes remain after plugin is disabled
    - Cleanup steps not wrapped in `ApplicationManager.getApplication().invokeLater` - should execute immediately
- Brought in `intellijPlatformPublishingToken` to use Gradle `publishPlugin` command

## [1.0.4]

### Added

- Added references to similar plugins on the Marketplace:
    - Project Color
    - Colorized Project
- Extended Plugin compatibility from build 253 to an earlier build - 251. This should allow the plugin to be used with
  earlier
  versions of the IDE

### Fixed

- Attempt to resolve bug where accent changes remain after plugin is disabled
    - Achieved with a PluginLifecycleListener

## [1.0.3]

### Fixed

- Renamed project from `WindowColorPanel` to `WindowAccent` and updated references

## [1.0.2]

### Fixed

- Resolved JetBrains Marketplace compatibility warnings:
    - Replaced deprecated `AppExecutorUtil.getAppScheduledExecutorService()` with Kotlin Coroutines in
      `WindowColorApplier`
    - Optimized `WindowAccentToolWindowFactory` to avoid compiler-generated bridge methods for deprecated/experimental
      `ToolWindowFactory` members
    - Added `DumbAware` to `WindowAccentToolWindowFactory` for better performance during indexing
- Verified fixes with improved automated compatibility tests that check both source code and compiled bytecode
- Verified Deprecated API and Experimental API usage warnings are no longer reported, via command
  `./gradlew verifyPlugin --rerun-tasks --info`
- Verified Gradle `verifyPlugin` task completes successfully

## [1.0.1]

### Changed

- Fix deprecated API usage warnings:
    - `ToolWindowFactory.shouldBeAvailable(Project)` replaces `ToolWindowFactory.isApplicable(Project)`
    - `plugin.xml#extensions.toolwindow.doNotActivateOnStart` replaces `ToolWindowFactory.isDoNotActivateOnStart`
- Fix experimental API usage warnings:
    - plugin.xml#extensions.toolWindow.anchor ensures declarative configuration (instead of using the default impl)
    - `getIcon()`, `manage()`, `getAnchor()` not overriden in ToolWindowFactory class

### Added

- Bring in `gradle-changelog-plugin` to better manage changes via a changelog

## [1.0.0]

### Added

- Initial version
- Window color management
- Title numbering options

[Unreleased]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/2.3.0...HEAD
[2.3.0]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/2.2.3...2.3.0
[2.2.3]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/2.2.2...2.2.3
[2.2.2]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/2.2.1...2.2.2
[2.2.1]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/2.2.0...2.2.1
[2.2.0]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/2.1.2...2.2.0
[2.1.2]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/2.1.1...2.1.2
[2.1.1]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/2.1.0...2.1.1
[2.1.0]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/2.0.0...2.1.0
[2.0.0]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.6.3...2.0.0
[1.6.3]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.6.2...1.6.3
[1.6.2]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.6.1...1.6.2
[1.6.1]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.6.0...1.6.1
[1.6.0]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.5.3...1.6.0
[1.5.3]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.5.2...1.5.3
[1.5.2]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.5.1...1.5.2
[1.5.1]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.5.0...1.5.1
[1.5.0]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.4.4...1.5.0
[1.4.4]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.4.3...1.4.4
[1.4.3]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.4.2...1.4.3
[1.4.2]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.4.1...1.4.2
[1.4.1]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.4.0...1.4.1
[1.4.0]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.3.1...1.4.0
[1.3.1]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.3.0...1.3.1
[1.3.0]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.2.12...1.3.0
[1.2.12]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.2.11...1.2.12
[1.2.11]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.2.10...1.2.11
[1.2.10]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.2.9...1.2.10
[1.2.9]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.2.8...1.2.9
[1.2.8]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.2.7...1.2.8
[1.2.7]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.2.6...1.2.7
[1.2.6]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.2.5...1.2.6
[1.2.5]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.2.4...1.2.5
[1.2.4]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.2.3...1.2.4
[1.2.3]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.2.2...1.2.3
[1.2.2]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.2.1...1.2.2
[1.2.1]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.2.0...1.2.1
[1.2.0]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.1.0...1.2.0
[1.1.0]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.0.13...1.1.0
[1.0.13]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.0.12...1.0.13
[1.0.12]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.0.11...1.0.12
[1.0.11]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.0.10...1.0.11
[1.0.10]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.0.9...1.0.10
[1.0.9]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.0.8...1.0.9
[1.0.8]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.0.7...1.0.8
[1.0.7]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.0.6...1.0.7
[1.0.6]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.0.5...1.0.6
[1.0.5]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.0.4...1.0.5
[1.0.4]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.0.3...1.0.4
[1.0.3]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.0.2...1.0.3
[1.0.2]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.0.1...1.0.2
[1.0.1]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.0.0...1.0.1
[1.0.0]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/commits/1.0.0
