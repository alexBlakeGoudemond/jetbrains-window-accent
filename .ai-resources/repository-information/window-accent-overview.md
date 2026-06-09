# Window Accent Overview

Window Accent is a JetBrains plugin that helps distinguish multiple IDE windows by:

- Adding a colored panel on one side of each IDE frame (N/S/W/E)
- Prefixing window titles with numbering and an optional custom label

Core flow:

- `PluginStartupActivity` applies color/title to each project window at startup.
- `PluginLifecycleListener` reacts to dynamic plugin load/unload events.
- `WindowAccentApplicationService` provides final cleanup on plugin dispose.

Main runtime components:

- `WindowColorApplier`: paints/removes the color panel and handles placement logic (including SOUTH wrapper behavior).
- `WindowTitleApplier`: assigns stable per-project numbers, updates prefixes, and manages title/focus listeners.

State is project-scoped and persisted via services in `configuration/persistence`:

- panel side + enabled flag
- custom color + enabled flag
- title numbering enabled flag
- custom title text + enabled flag

User controls:

- Tool Window (`WindowAccentToolWindowFactory`) for fast toggles/cycling panel direction.
- Settings (`WindowAccentSettings`) for side, custom color, title numbering, and custom title.

Dynamic-unload/no-restart strategy:

- `plugin.xml` declares `require-restart="false"`.
- Cleanup removes listeners/panels and cancels pending work on unload.
- EDT work for applying/removing accents is executed synchronously to avoid queued plugin runnables that can keep
  classloaders alive during plugin update.

