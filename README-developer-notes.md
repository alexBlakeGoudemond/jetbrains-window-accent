# README Developer Notes

## Plugin Structure and Documentation

Beyond trusting AI agents Jetbrains Junie, Github Copilot and ChatGPT, this repository draws inspiration
from the following sources:

- [Jetbrains Plugin Configuration File](https://plugins.jetbrains.com/docs/intellij/plugin-configuration-file.html)
- [MavenHelper Plugin](https://github.com/krasa/MavenHelper)

## Threading & UI Updates

### SwingUtilities.invokeLater vs ApplicationManager.getApplication().invokeLater

Both methods schedule code to run on the Event Dispatch Thread (EDT), but they operate at different abstraction levels:

| Aspect               | SwingUtilities.invokeLater | ApplicationManager.getApplication().invokeLater  |
|----------------------|----------------------------|--------------------------------------------------|
| **Source**           | Standard Java Swing        | IntelliJ Platform SDK                            |
| **IDE Awareness**    | ❌ None                     | ✅ Tracks IDE state, indexing, disposal, modality |
| **Modality Control** | ❌ No                       | ✅ Yes                                            |
| **Disposal Safety**  | ❌ No                       | ✅ Yes                                            |
| **Threading Rules**  | Generic                    | IntelliJ-compliant                               |

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

JetBrains plugins run in a controlled **sandbox environment** for security reasons. System automation features require
OS-level permissions that may be:

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
able to update it without restarting the IDE. This only works if the old plugin's `PluginClassLoader`
can be garbage-collected after unloading. If any platform code still holds a strong reference to a
plugin-class object at GC-check time, IntelliJ logs:

```
Plugin WindowAccent is not unload-safe because class loader cannot be unloaded
```

…and falls back to requiring a restart. Fixing this has required **19 passes** over the plugin's
lifetime. Every root cause discovered, the retention chain it formed, and the fix applied is
summarised below.

> **Note on pass numbering:** Passes 001–003 (v1.0.10–v1.0.12) targeted a closely related but
> distinct "disable bug" (accent changes persisting after disable) before the "restart required"
> framing was established. Pass numbering for the restart-required bug formally started at 001
> with v1.0.10 and continues through 019 (v1.4.4). The v1.3.1 toolWindow fix resolved a real
> leak but was not assigned a formal pass number at the time; the developer's count resumed at
> 017 for the next regression in v1.4.1.

---

### Master fix table

> **Reading the retention chains:** `→` means "holds a reference to". The final link is always
> `→ PluginClassLoader`. The fix breaks at least one link in that chain.
>
> Passes 001–003 were exploratory — the precise external-reference chain had not yet been
> identified. Entries describe what was suspected and what was attempted.

| Pass | Version  | What kept `PluginClassLoader` alive                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | Fix                                                                                                                                                                                                                                                                                                                             |
|------|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 001  | v1.0.10  | Root cause not yet identified. **Suspected: Disposer mis-keying.** Plugin changes (colored strip, title) persisted after disable; Disposer cleanup was not reliably removing the correct registered entries.                                                                                                                                                                                                                                                                                                                                                        | Rewrote load/unload to use a unique plugin-specific String key for Disposer registration.                                                                                                                                                                                                                                       |
| 002  | v1.0.11  | Root cause not yet identified. **Suspected: Live coroutine scopes.** Coroutine scopes holding plugin lambdas were not cancelled on unload, potentially leaving live references on the coroutine dispatcher.                                                                                                                                                                                                                                                                                                                                                         | Added `scope.cancel()` to the unload/disable path to cancel coroutine scopes.                                                                                                                                                                                                                                                   |
| 003  | v1.0.12  | Root cause not yet identified. **Suspected: Alarm thread affinity.** Alarm tasks scheduled on background threads could outlive the EDT cleanup window.                                                                                                                                                                                                                                                                                                                                                                                                              | Changed `Alarm` to `Alarm.ThreadToUse.SWING_THREAD` to schedule callbacks synchronously on the EDT.                                                                                                                                                                                                                             |
| 004  | v1.0.13  | **Inner `invokeLater` race.** `doApplyTitle` posted a nested `invokeLater`; IntelliJ's cleanup ran while the inner task was still queued → frame listeners and `Disposer` entries were registered *after* cleanup: `Frame → AWT listener → plugin lambda → PluginClassLoader`                                                                                                                                                                                                                                                                                       | Removed inner `invokeLater`. Registration now runs synchronously within the outer EDT task.                                                                                                                                                                                                                                     |
| 004  | v1.0.13  | **Unreliable listener removal.** `removeListeners` called `getProjectFrame()` at cleanup time; if the frame had changed, the listener stayed on the original frame: `Frame → WindowAdapter → plugin lambda → PluginClassLoader`                                                                                                                                                                                                                                                                                                                                     | Stored each `(listener, frame)` pair so cleanup always removes from the exact frame the listener was added to.                                                                                                                                                                                                                  |
| 004  | v1.0.13  | No `isShuttingDown` flag; no authoritative cleanup hook.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            | Added `@Volatile isShuttingDown` set *first* in cleanup. Added `WindowAccentApplicationService` (`@Service(Level.APP)`) — IntelliJ disposes app services *before* the GC check, giving a guaranteed final sweep.                                                                                                                |
| 005  | v1.2.1   | **Queued top-level `invokeLater` calls** left plugin runnables in the EDT queue after cleanup began. If the GC check ran before the EDT drained, queued runnables kept plugin lambdas reachable.                                                                                                                                                                                                                                                                                                                                                                    | Changed accent apply/remove entry points to run synchronously on-EDT, or via `invokeAndWait` when off-EDT. Eliminated the queue window.                                                                                                                                                                                         |
| 006  | v1.2.2   | **Retry `Alarm` not properly disposed.** `cancelAllRequests()` was called but the `Alarm` itself was not disposed; platform scheduler references kept the alarm — and transitively plugin classes — reachable.                                                                                                                                                                                                                                                                                                                                                      | Added explicit `Alarm` disposal to cleanup. *(Note: the correct final Alarm fix came in pass 013.)*                                                                                                                                                                                                                             |
| 007  | v1.2.3   | **`WindowColorApplier` panels not tracked.** Panels added to the IDE frame had no stored reference; if `getProjectFrame()` returned null at cleanup, panels were orphaned: `IDE frame → panel → plugin class → PluginClassLoader`. Cleanup was also duplicated without idempotency.                                                                                                                                                                                                                                                                                 | Added `addedPanels: ConcurrentHashMap<Project, List<Component>>` for fallback panel removal. Added `AtomicBoolean` idempotency gate for exactly-once cleanup. Consolidated cleanup in `WindowAccentApplicationService`.                                                                                                         |
| 008  | v1.2.4   | **`WindowAccentApplicationService` never instantiated.** Lazy services are only disposed if they have been created; `dispose()` (the final cleanup hook) was silently never called. Also: ordering bug in `addColoredPanel` — the old Disposer holder's callback fired *after* `addedPanels` was updated to the new panel, immediately removing the newly-added panel.                                                                                                                                                                                              | Force-instantiate the service in `pluginLoaded`. Fixed panel cleanup ordering: remove old panels → dispose old holder → track new panel → register new holder.                                                                                                                                                                  |
| 009  | v1.2.5   | Root cause not yet fully identified. **Multiple hypotheses tested:** (A) `Introspector` cache retaining `Class<?>` keys via persistence service classes; (B) tool-window button `ActionListener` lambdas not removed; (C) `WindowAccentSettings` configurable holding project service references after dispose.                                                                                                                                                                                                                                                     | (A) Added `Introspector.flushFromCaches()` for persistence service classes. (B) Tracked and explicitly removed all button `ActionListener` lambdas in `removeAllButtonListeners()`. (C) Nulled project service fields in `disposeUIResources()`. Also removed `flushEdtQueue()` which caused EAP 2026.2 compatibility failures. |
| 010  | v1.2.6   | **`cleanupCompleted` never reset between load cycles.** The `AtomicBoolean` gate in `WindowAccentApplicationService` companion stayed `true` after the first update; every subsequent unload skipped all cleanup entirely.                                                                                                                                                                                                                                                                                                                                          | Added `resetCleanupState()` called from `pluginLoaded` so each new load cycle starts fresh.                                                                                                                                                                                                                                     |
| 011  | v1.2.7   | **Instance-field logger** in `PluginLifecycleListener`. The platform message bus retains the listener instance after unload; an instance-field logger holds `PluginLifecycleListener.class`: `bus → listener → LOG → Class → PluginClassLoader`. Also: `disposeUIResources()` didn't clear the Swing component tree.                                                                                                                                                                                                                                                | Moved `LOG` to `companion object`. Added `panel.removeAll()` / `form.removeAll()` to `disposeUIResources()`.                                                                                                                                                                                                                    |
| 012  | v1.2.8   | *(Attempted)* Parenting `Alarm` to `Application` introduced `Application → Alarm → plugin class → PluginClassLoader`.                                                                                                                                                                                                                                                                                                                                                                                                                                               | **Reverted** in pass 013.                                                                                                                                                                                                                                                                                                       |
| 013  | v1.2.9 ✅ | **`Alarm` retained by platform Disposer tree.** `retryAlarm` was registered with Disposer but never explicitly disposed: `Disposer tree → retryAlarm → plugin class → PluginClassLoader`.                                                                                                                                                                                                                                                                                                                                                                           | Create `Alarm(SWING_THREAD)` with no parent. Cleanup calls `cancelAllRequests()` then `Disposer.dispose(retryAlarm)` explicitly. **Confirmed clean via Eclipse MAT heap dump — classloader absent from heap.**                                                                                                                  |
| 014  | v1.2.10  | **Caffeine icon cache.** `IconLoader` stores `ImageDataByPathLoader` (holds `PluginClassLoader`) as a key, reachable from a Global JNI root: `JNI → Caffeine cache → CachedImageIcon → ImageDataByPathLoader → PluginClassLoader`. Identified via hprof.                                                                                                                                                                                                                                                                                                            | Added `IconLoader.clearCache()` to `performCleanup()`.                                                                                                                                                                                                                                                                          |
| 015  | v1.2.11  | **Diagnostic self-interference (lambda capture).** `ClassLoaderLeakDiagnostics` lambda captured `ClassLoader` directly across a pooled-thread boundary → strong reference on the thread stack during the GC-check window.                                                                                                                                                                                                                                                                                                                                           | Only `WeakReference` crosses the thread boundary; strong ref obtained only after all GC rounds complete.                                                                                                                                                                                                                        |
| 016  | v1.2.11  | **Diagnostic self-interference (lambda class itself).** Even without a captured variable, the lambda's own class (`ClassLoaderLeakDiagnostics$$Lambda`) keeps `PluginClassLoader` alive via `Class.classLoader` while executing on a background thread. Fundamental constraint: **any plugin code running on any thread during the GC check window keeps the classloader reachable.**                                                                                                                                                                               | Removed `scheduleLeakCheck` from all lifecycle hooks entirely. `ClassLoaderLeakDiagnostics` retained for ad-hoc investigation only — **never call it from a lifecycle hook.**                                                                                                                                                   |
| —    | v1.3.1   | **`toolWindow.disposable` anonymous lambda on `isUpdate=true`.** Anonymous plugin lambda registered with `toolWindow.disposable`; on *update* (`isUpdate=true`) IntelliJ does not close the tool window before the GC check: `Disposer tree → toolWindow.disposable → anonymous Disposable → plugin lambda → PluginClassLoader`. Disable tests (`isUpdate=false`) passed because IntelliJ *does* close the tool window on disable, masking the bug. *(Not assigned a formal pass number at the time; developer pass count resumed at 017 for the next regression.)* | Replaced anonymous lambda with a named intermediate `Disposable` tracked in `toolWindowCleanupDisposables`. Explicitly disposed during `removeAllButtonListeners()` on every unload path.                                                                                                                                       |
| 017  | v1.4.1   | **`sideCombo` holding `Side[]` when Settings were open.** IntelliJ's configurable cache retained the `WindowAccentSettings` instance → `sideCombo → DefaultComboBoxModel → Side[] → Side.class → PluginClassLoader`. Root cause confirmed via log comparison: failure occurred only when the user had visited the Settings panel.                                                                                                                                                                                                                                   | Added `sideCombo.removeAllItems()` to `disposeUIResources()`. Added `WeakReference` instance tracking + proactive `disposeAllTrackedInstances()` called from `performCleanup()`. Moved `sidesCycleOrder` to companion to remove a secondary `Side[]` chain via the tool-window factory.                                         |
| 018  | v1.4.2   | **IntelliJ configurable cache holding the `WindowAccentSettings` instance** after its internals were cleared. The instance pointer alone keeps the classloader reachable: `IntelliJ cache → WindowAccentSettings instance → WindowAccentSettings.class → PluginClassLoader`.                                                                                                                                                                                                                                                                                        | Added `findContainingWindows()` to locate the Swing Settings dialog before disposal. Added `window.dispose()` to post a `WINDOW_CLOSED` event, prompting IntelliJ to flush its configurable cache in the ~1–2 s pre-GC window.                                                                                                  |
| 019  | v1.4.4   | **`WindowAccentSettings` re-instantiated *after* cleanup.** IntelliJ momentarily released its reference (clearing our `WeakReference`, causing "0 live instances" in the log) then re-instantiated the configurable for status-bar search indexing / `isModified()` polling. The new instance populated `sideCombo` with `Side[]` in its constructor. Identified via hprof (`rightPanelLayout of IdeStatusBarImpl`; `WindowAccentSettings` Count=1, retained 11.46 kB).                                                                                             | Added a post-cleanup self-disposal guard to `WindowAccentSettings.init {}`: if `isCleanupCompleted()` returns `true`, immediately call `disposeUIResources()` on the new instance. No-op during normal plugin operation.                                                                                                        |

---

### Key rules (consolidated)

These rules apply at all times to keep the plugin dynamically unloadable:

| Rule                                                                               | Rationale                                                                                                                                                                                                                    |
|------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Never nest `invokeLater` inside another `invokeLater` unnecessarily                | Creates a window where the inner task runs after cleanup, re-registering external references                                                                                                                                 |
| Use `invokeAndWait` off-EDT; run synchronously on-EDT                              | Eliminates the EDT-queue window where plugin lambdas are reachable during the GC check                                                                                                                                       |
| Always set `isShuttingDown = true` **first** in cleanup                            | Prevents any deferred EDT task from re-registering references after cleanup begins                                                                                                                                           |
| Store each AWT listener alongside the frame it was added to                        | Ensures removal from the exact frame at cleanup time, regardless of what `getProjectFrame()` returns                                                                                                                         |
| Never parent an `Alarm` to a platform lifecycle object                             | Platform `Disposer` would retain the alarm (and transitively the plugin class) after unload                                                                                                                                  |
| Always explicitly call `cancelAllRequests()` then `Disposer.dispose(alarm)`        | `cancelAllRequests()` alone leaves the alarm in the Disposer tree; disposal removes the reference entirely                                                                                                                   |
| Call `IconLoader.clearCache()` during cleanup                                      | `ImageDataByPathLoader` in the Caffeine icon cache stores `PluginClassLoader` and is reachable from a Global JNI root                                                                                                        |
| Reset `cleanupCompleted` to `false` in `pluginLoaded`                              | The `AtomicBoolean` gate persists across load cycles; without reset, the second update skips all cleanup                                                                                                                     |
| Use a named intermediate `Disposable` for tool-window lambdas; track it explicitly | On `isUpdate=true`, IntelliJ does not close the tool window before the GC check. Anonymous lambdas registered with `toolWindow.disposable` survive unless explicitly disposed from our cleanup path                          |
| Never schedule async plugin code from the unload path                              | Any plugin class executing on any thread during the GC check keeps `PluginClassLoader` alive via `Class.classLoader` — including the lambda class itself                                                                     |
| Call `window.dispose()` on any open Settings dialog during cleanup                 | Even after `disposeUIResources()` clears the internals, IntelliJ's configurable cache may retain the instance pointer; `window.dispose()` triggers the `WINDOW_CLOSED` handler which flushes that cache in the pre-GC window |
| In `WindowAccentSettings.init {}`, self-dispose if `isCleanupCompleted()` is true  | IntelliJ can re-instantiate the configurable after our cleanup runs (for search indexing / `isModified()` polling); the new instance must immediately clear its `sideCombo → Side[]` chain                                   |
| Force-instantiate `WindowAccentApplicationService` on `pluginLoaded`               | Application services are lazily created; if never accessed, `dispose()` is never called and the cleanup hook never runs                                                                                                      |
| Use a shared idempotency gate (`AtomicBoolean`) for all cleanup paths              | Both `beforePluginUnload` and `dispose()` can fire; the gate ensures exactly-once cleanup                                                                                                                                    |

---

### Diagnosing a future unload failure

When `Plugin WindowAccent is not unload-safe because class loader cannot be unloaded` appears:

1. **Check whether `WindowAccentApplicationService.dispose()` was logged.**  
   If not, the service was never instantiated — ensure `pluginLoaded` force-instantiates it.

2. **Check whether `beforePluginUnload` cleanup was logged.**  
   If not, the `DynamicPluginListener` is not firing for our plugin ID.

3. **Check which version was being *unloaded*.**  
   `beforePluginUnload` runs on the *old* version's code. A fix only takes effect the next time the
   *fixed* version is itself unloaded.

4. **Check the `0 live instances disposed` / `N live instances disposed` log line.**  
   `0` means `trackedInstances` was empty (WeakRefs cleared, or instances created after cleanup). This is
   the pattern for the "created-after-cleanup" race (Pass 019).

5. **Capture an hprof snapshot** —
   see [hprof investigation notes](.ai-resources/repository-information/plugin-unload-hprof-investigation.md) for full
   instructions.

---

### hprof snapshot analysis

When a failure is intermittent or the log gives no obvious clue, capture a heap snapshot at the exact
moment of failure. Full instructions (VM options, file locations, analysis steps, known snapshots
catalogue, and log timeline) are in:

> 📄 [
`.ai-resources/repository-information/plugin-unload-hprof-investigation.md`](.ai-resources/repository-information/plugin-unload-hprof-investigation.md)

**Quick reference:**

Add to `Help > Edit Custom VM Options` (shared across all custom IntelliJ configurations):

```
-XX:+UnlockDiagnosticVMOptions
-Dide.plugins.snapshot.on.unload.fail=true
```

Snapshots are written to `C:\Users\<userDirectory>\` as `unload-WindowAccent-DD.MM.YYYY_HH.mm.ss.hprof`.

**What to look for in the snapshot (IntelliJ's built-in viewer):**

1. **Classes tab → filter to `com.window_accent`** — any class with Count > 0 is alive after unload.
   `WindowAccentSettings` Count=1 with a large retained heap is the primary warning sign.
2. **Click the live instance → Shortest Paths to GC Roots** — trace back to the first
   non-`window_accent` object; that is the leak source.
3. **`Side[]` / `Side` entries alive** — indicates `sideCombo` was not cleared; `disposeUIResources()`
   did not run on that instance.

---

### How to verify dynamic unloadability locally (sandbox simulation)

1. Temporarily set a `-test` suffix version in `gradle.properties`, run `.\gradlew buildPlugin`,
   copy the zip, then restore the original version.
2. Run `.\gradlew runIde` — the sandbox starts with the pre-installed version.
3. Inside the sandbox: **Settings → Plugins → ⚙️ → Install Plugin from Disk…** → select the `-test` zip.
4. Click **"Restart IDE"** when prompted (the sandbox update), then check:
    - `C:\Users\<you>\` — absence of a new `.hprof` means the classloader was GC'd ✅
    - Sandbox log at `.intellijPlatform\sandbox\...\log\idea.log` — search for
      `"class loader cannot be unloaded"` (absence = success) and
      `"Successfully unloaded plugin WindowAccent"` (presence = success).

> **Note:** Both sandbox *disable* and *update* now trigger the classloader GC check in IntelliJ
> 2026.1+ (`classloader unload checked=true` in the log). Either path can be used for verification.
