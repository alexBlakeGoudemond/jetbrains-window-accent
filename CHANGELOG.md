# Changelog

## [Unreleased]

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
