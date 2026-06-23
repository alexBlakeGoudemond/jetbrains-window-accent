# Changelog

## [Unreleased]

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

## [1.4.3]

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
  - If several window instances have opened and closed, numbering can become haphazard; drifting from 1, 2, 3, ... to 3, 7, 9, 13, ...
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
    - Confirmed clean unload via log `Successfully unloaded plugin WindowAccent (classloader unload checked=true)` in sandbox logs
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

[Unreleased]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.4.3...HEAD
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
