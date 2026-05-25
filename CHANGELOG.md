# Changelog

## [Unreleased]

## [1.0.7]

### Added

- Add support for custom ai-playbook instructions, for developers

### Fixed

- Address a bug with SOUTH Color Panel overlapping / sharing the frame with the Status Bar
  - now SOUTH panel is a separate color underneath the Status Bar

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
