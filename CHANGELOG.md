# Changelog

## [Unreleased]

## [1.2.6]

## [1.2.5]

### Fixed

- Pass 009 of improving Plugin Unloading to avoid unnecessary project restarts
  - **Hypothesis A — Introspector cache**: Call `Introspector.flushFromCaches(clazz)` for all 4 persistence service classes in `performCleanup` to release any hard `Class<?>` keys
  - **Hypothesis B — tool window button listeners**: Track all 6 `ActionListener` lambdas statically and call `removeAllButtonListeners()` in `performCleanup` to drop captured service/singleton references
  - **Hypothesis C — settings configurable fields**: Null all 4 project service fields in `WindowAccentSettings.disposeUIResources()` so a cached configurable instance cannot retain classloader references
  - **Diagnostic improvement**: Changed "cleanup already completed" log from `DEBUG` to `INFO`
  - Removed `flushEdtQueue()` that caused EAP 2026.2 (262.7132.23) compatibility failures

### Diagnostic notes (from log analysis)

- **Disable skips the GC check**: Disabling produces `classloader unload checked=false` — only a marketplace **update** triggers the real GC check (`checked=true`)
- **`"Application Service created"` absent**: `pluginLoaded` only fires on dynamic enable, not on IDE-startup loads — expected absent
- **`"pluginUnloaded"` absent**: Listener is intentionally unregistered in `beforePluginUnload` before this event fires — expected absent
- **EDT flush was always skipped on disable path**: `beforePluginUnload` runs on the EDT, confirming `flushEdtQueue()` was dead code for that path and only activated on the background-thread `dispose()` path

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

[Unreleased]: https://github.com/alexBlakeGoudemond/jetbrains-window-accent/compare/1.2.6...HEAD
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
