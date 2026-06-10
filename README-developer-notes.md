# README Developer Notes

## Plugin Structure and Documentation

Beyond trusting AI agents Jetbrains Junie, Github Copilot and ChatGPT, this repository draws inspiration
from the following sources:
- [Jetbrains Plugin Configuration File](https://plugins.jetbrains.com/docs/intellij/plugin-configuration-file.html)
- [MavenHelper Plugin](https://github.com/krasa/MavenHelper)

## Threading & UI Updates

### SwingUtilities.invokeLater vs ApplicationManager.getApplication().invokeLater

Both methods schedule code to run on the Event Dispatch Thread (EDT), but they operate at different abstraction levels:

| Aspect | SwingUtilities.invokeLater | ApplicationManager.getApplication().invokeLater |
|--------|---------------------------|------------------------------------------------|
| **Source** | Standard Java Swing | IntelliJ Platform SDK |
| **IDE Awareness** | ❌ None | ✅ Tracks IDE state, indexing, disposal, modality |
| **Modality Control** | ❌ No | ✅ Yes |
| **Disposal Safety** | ❌ No | ✅ Yes |
| **Threading Rules** | Generic | IntelliJ-compliant |

#### ⚠️ Why this matters in plugins

Using `SwingUtilities.invokeLater` can cause subtle, hard-to-debug issues:

- Code might run when the project/IDE is already disposed
- You may violate IntelliJ's threading model (causing warnings or crashes)
- You lose control over modality states (e.g., code running during modal dialogs unintentionally)
- Breaks assumptions that other plugins depend on (indexing, read/write locks, modality correctness)

#### ✅ Best practice for IntelliJ plugins

**Always use:**
    ```kotlin
    ApplicationManager.getApplication().invokeLater {
        // your code
    }
    ```

**When modality matters (more explicit):**
    ```kotlin
    ApplicationManager.getApplication().invokeLater(
        {
            // your code
        },
        ModalityState.NON_MODAL  // or ModalityState.any()
    )
    ```

#### 🆚 When would SwingUtilities be acceptable?

Almost never in plugin code. Only acceptable if:

- You're writing pure Swing code completely outside IntelliJ context
- You're absolutely certain the code runs independent of IDE lifecycle (rare and risky)

**Bottom line:** Always use `ApplicationManager.getApplication().invokeLater {}` in IntelliJ plugins.

---

## System Automation & Permissions

### java.awt.Robot in JetBrains plugins

In a JetBrains plugin, you should generally avoid `java.awt.Robot` if possible.

#### Why it's problematic

`java.awt.Robot` is used to simulate:
- Keyboard input
- Mouse movement and clicks
- Screen capture
- Clipboard interaction

JetBrains plugins run in a controlled **sandbox environment** for security reasons. System automation features require OS-level permissions that may be:
- **Blocked** by the IDE
- **Blocked** by the operating system
- **Denied** at runtime without explicit allowance

#### ⚠️ Known issues

- IntelliJ may block Robot operations for security
- Some platforms (macOS, Linux) have stricter restrictions
- The sandbox environment prevents certain system calls
- Other plugins may not expect direct OS manipulation

#### ✅ Alternatives (when possible)

Instead of `Robot`, consider:

- **UI Selection:** Use IntelliJ's Editor and PsiElement APIs to work with code
- **Clipboard:** Use `CopyPasteManager` for safe clipboard access
- **File Operations:** Use VirtualFile API instead of OS-level file access
- **Events:** Use IntelliJ's event system instead of keyboard simulation

#### When Robot might be necessary

If you absolutely must use `Robot`:
- Document the requirement clearly
- Add prominent warnings in your plugin description
- Test thoroughly in the IDE sandbox
- Consider requesting explicit permissions

---

## Dynamic Plugin Unloading (no-restart updates)

The plugin is declared with `require-restart="false"` in `plugin.xml`, meaning IntelliJ should be
able to update it without restarting the IDE. However, this only works if the plugin's classloader
can be garbage-collected after unloading. If any platform code still holds a strong reference to
plugin-code objects at the time IntelliJ runs its classloader GC check, it logs:

```
Plugin WindowAccent is not unload-safe because class loader cannot be unloaded
```

...and falls back to requiring a restart.

## Previous issues where Restart was required

### Root cause — the double `invokeLater` race

The root cause was an **unnecessary inner `invokeLater`** inside `WindowTitleApplier.doApplyTitle`.

When IntelliJ loads the new plugin version it fires `pluginLoaded`, which triggers
`restoreDecorations()` → `applyToCurrentOpenProject(project)`. That method posts an **outer**
`invokeLater` to the EDT. When the outer task runs it called `doApplyTitle`, which then posted a
second, **inner** `invokeLater` (wrapping the AWT listener and Disposer registration work).

The inner task was placed at the **end** of the EDT queue. IntelliJ's cleanup
(`cancelAllPendingOperations`) ran on a background thread while the EDT continued processing.
The timeline looked like this:

```
background thread:  cancelAllPendingOperations()
                      ├─ isShuttingDown = true         (flag not present before fix)
                      ├─ retryAlarm.cancelAllRequests()
                      ├─ disposeAllTrackedDisposables() ← disposes all Disposer holders ✓
                      └─ removeFromAllOpenProjectsSync() ← removes AWT listeners ✓

EDT (concurrent):   inner invokeLater runs AFTER disposeAllTrackedDisposables()
                      ├─ frame.addWindowFocusListener(WindowAdapter)   ← plugin lambda on Frame!
                      ├─ frame.addPropertyChangeListener(listener)     ← plugin lambda on Frame!
                      └─ Disposer.register(project, holder)            ← plugin lambda in ObjectTree!

result:  ObjectTree (platform) → holder → lambda → WindowTitleApplier.INSTANCE → classloader 🔴
         AWT Frame  (platform) → WindowAdapter   → WindowTitleApplier.INSTANCE → classloader 🔴
```

Both are external (platform → plugin) strong references. The classloader cannot be GC'd.

### The secondary cause — unreliable listener removal

`removeListeners(project, frame)` previously obtained the frame via `getProjectFrame(project)` at
cleanup time. If the frame returned at cleanup was different from the one the listener was added to
(or null), the listener stayed on the original frame — another platform→plugin reference.

### Fix applied (v1.0.13)

Four changes were made across three files:

#### 1. Removed the inner `invokeLater` (`WindowTitleApplier.doApplyTitle`)

`doApplyTitle` is always called from within an outer `invokeLater` (already on the EDT) or from
an `Alarm.ThreadToUse.SWING_THREAD` retry (also on the EDT). The inner `invokeLater` was
redundant and created the race window. All registration work now runs synchronously within
the same EDT task as the outer `invokeLater`.

#### 2. Added `isShuttingDown` flag to both appliers

```kotlin
@Volatile private var isShuttingDown = false
```

Set as the **very first action** in `cancelAllPendingOperations()` (and `cancelCoroutines()`).
Any EDT task queued before cleanup but executed after the flag is set returns early, so no new
external references can be created once cleanup has started.

Listener callbacks (`windowGainedFocus`, `PropertyChangeListener`) also check the flag so they
are no-ops after cleanup, keeping the thread stack free of plugin frames during IntelliJ's GC check.

#### 3. Stored frames alongside listeners

`focusListeners` and `titleListeners` were changed from `ConcurrentHashMap<Project, Listener>` to
`ConcurrentHashMap<Project, Pair<Listener, Frame>>`. This means cleanup always removes each
listener from the **exact frame it was added to**, regardless of what
`WindowManager.getInstance().getFrame(project)` returns at cleanup time.

`cancelAllPendingOperations()` now calls `removeAllTrackedListeners()` eagerly and upfront — it
iterates the stored `(listener, frame)` pairs, clears the maps, and removes each listener from its
frame before any other cleanup step runs.

#### 4. Added `WindowAccentApplicationService` (belt-and-suspenders)

Registered in `plugin.xml` as `<applicationService>`:

```xml
<applicationService serviceImplementation="com.window_accent.WindowAccentApplicationService"/>
```

IntelliJ calls `dispose()` on all application services during plugin unloading, **before** the
classloader GC check. `WindowAccentApplicationService.dispose()` sets both shutdown flags and
re-runs the full cleanup sequence. This provides a guaranteed final sweep even if
`beforePluginUnload` missed anything due to a timing edge case.

### Key rules to maintain dynamic unloadability

| Rule | Why |
|------|-----|
| Never nest `invokeLater` inside another `invokeLater` unnecessarily | Creates a timing window where the inner task can run after cleanup |
| Always set a `isShuttingDown` flag **first** in cleanup | Prevents any deferred task from re-registering external references |
| Store the `Frame` alongside each AWT listener | Ensures removal from the correct frame at cleanup time |
| Register long-lived state as `@Service(Level.APP)` | IntelliJ manages lifecycle and disposes it before the GC check |
| Remove all `Disposer.register(...)` lambdas explicitly | Any `project → holder → plugin lambda` in `ObjectTree` keeps the classloader alive |

### Additional unload-safety hardening (post-v1.2.0)

Even after v1.0.13 improvements, queued EDT tasks from top-level `invokeLater` calls in
`WindowColorApplier` / `WindowTitleApplier` can still retain plugin lambdas in the event queue
during unload timing windows.

To reduce this risk, accent apply/remove entry points now execute synchronously on EDT:

- If already on EDT: run immediately
- Otherwise: use `ApplicationManager.getApplication().invokeAndWait { ... }`

This removes the queueing window for these paths, while retaining `Alarm` only for explicit
frame-availability retries (which are canceled during unload cleanup).

Also note: `Alarm.cancelAllRequests()` alone is not sufficient for dynamic-unload safety.  
The `Alarm` instance itself must be disposed during unload (`Disposer.dispose(retryAlarm)`),
otherwise scheduler-owned references can still keep plugin classes reachable.
