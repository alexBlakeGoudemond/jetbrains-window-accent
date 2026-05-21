# Changelog

## [Unreleased]

## [1.0.2]

### Fixed
- Resolved JetBrains Marketplace compatibility warnings:
    - Replaced deprecated `AppExecutorUtil.getAppScheduledExecutorService()` with Kotlin Coroutines in `WindowColorApplier`.
    - Optimized `WindowColorPanelToolWindowFactory` to avoid compiler-generated bridge methods for deprecated/experimental `ToolWindowFactory` members.
    - Added `DumbAware` to `WindowColorPanelToolWindowFactory` for better performance during indexing.
- Verified fixes with improved automated compatibility tests that check both source code and compiled bytecode.
- Verified Deprecated API and Experimental API usage warnings are no longer reported, via command `./gradlew verifyPlugin --rerun-tasks --info`.

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
