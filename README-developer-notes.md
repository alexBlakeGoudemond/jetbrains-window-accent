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
| **Force-instantiate the app service on load** | Application services are lazily created; if never accessed, `dispose()` is never called and the service cleanup never runs |
| Dispose old Disposer holders before updating tracked-panel state | If the old holder's callback fires after `addedPanels` is updated to the new panel, it removes the new panel. Always: remove → dispose old → track new → register new |
| Run both `beforePluginUnload` and `dispose()` cleanup through a shared idempotent gate | Guarantees exactly-once cleanup regardless of which hook fires first |
| **Track and explicitly dispose any lambda registered with `toolWindow.disposable`** | During `isUpdate=true`, IntelliJ does NOT close the tool window before the classloader GC check. `toolWindow.disposable` remains alive, keeping any registered plugin-class lambda reachable. Disable (`isUpdate=false`) does close the tool window first, which is why this bug is invisible in disable tests. Always use a named `Disposer.newDisposable(...)` child, store it in a `ConcurrentHashMap<Project, Disposable>`, and call `Disposer.dispose(it)` from the plugin-unload cleanup path. |

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

### Diagnosing a future unload failure

When `Plugin WindowAccent is not unload-safe because class loader cannot be unloaded` appears in
the IDE log, check:

1. **Is `WindowAccentApplicationService.dispose()` logged?**  
   If not, the service was never instantiated. Ensure `pluginLoaded` calls
   `ApplicationManager.getApplication().getService(WindowAccentApplicationService::class.java)`
   to force-instantiate it on every load.

2. **Is `beforePluginUnload` cleanup logged?**  
   If not, the `DynamicPluginListener` registration is missing or firing on the wrong plugin ID.

3. **Note which plugin version is being unloaded.**  
   The `beforePluginUnload` callbacks run on the *old* version's code. If the old version had a
   bug in cleanup, the restart will still happen even if the *new* version is correct. A fix only
   takes effect when the *fixed* version is itself unloaded (i.e., on the *next* update).

Three critical fixes to prevent plugin requiring restart on update (v1.2.3):

1. TRACK ADDED PANELS IN WINDOWCOLORAPPLIER
    - Add ConcurrentHashMap<Project, List<Component>> to track panels per-project
    - Enables belt-and-suspenders cleanup when frames become unavailable
    - Register cleanup callbacks with project disposable lifecycle

2. CONSOLIDATE CLEANUP IN WINDOWACCENTAPPLICATIONSERVICE
    - Add AtomicBoolean guard to ensure cleanup runs exactly once
    - Defer cleanup from PluginLifecycleListener to app service (guaranteed to run before GC check)
    - Add error handling and logging for robustness

3. IMPROVE FRAMELESS CLEANUP IN WINDOWCOLORAPPLIER
    - Remove panels via stored references first (fallback if frame is null)
    - Then attempt removal via frame for double-checking
    - Prevents orphaned panels from holding classloader references

ISSUE: WindowColorApplier panels were never registered with Disposer and had no
fallback removal if frames became unavailable during unload, causing panels to
persist in memory and hold plugin classloader references. Cleanup was also
duplicated between PluginLifecycleListener and WindowAccentApplicationService
without synchronization or idempotency guards.

TESTS: Added tests to verify panel tracking works and cleanup is idempotent.
Updated PluginLifecycleListenerTest to reflect new delegation pattern.

This should eliminate the 'Plugin WindowAccent is not unload-safe' message and
allow dynamic updates without restart.

---

### Classloader leak resolution — passes 010–013 (v1.2.6–v1.2.9)

After v1.2.3, the plugin still required a restart on marketplace updates. Four further passes
were needed to fully resolve the classloader leak. The fix was confirmed via Eclipse Memory
Analyser Tool (MAT) heap dump analysis — the WindowAccent classloader was absent from the heap
after v1.2.9, proving successful GC.

#### Pass 010 — v1.2.6: `cleanupCompleted` never reset between load cycles

`cleanupCompleted` (an `AtomicBoolean` in `WindowAccentApplicationService` companion object)
was never reset to `false` after cleanup. On the second update, the new plugin instance's
`performCleanup` saw `cleanupCompleted = true` and skipped all cleanup entirely.

**Fix:** Added `resetCleanupState()` companion method; called from `PluginLifecycleListener.pluginLoaded`
so each new load cycle starts fresh.

#### Pass 011 — v1.2.7: Instance-field logger and Swing component tree

Two external references survived cleanup:

1. `PluginLifecycleListener.LOG` was an instance field. The platform message bus holds the
   listener instance after unload; an instance-field logger holds a reference to the class,
   keeping the classloader reachable. **Fix:** Moved `LOG` to `companion object`.

2. IntelliJ's configurable cache may retain the `WindowAccentSettings` instance. Each Swing
   component field (checkboxes, buttons, combos) is a plugin class instance. **Fix:** Added
   `panel.removeAll()` and `form.removeAll()` to `disposeUIResources()`.

#### Pass 012 — v1.2.8: Alarm parented to Application (reverted in 013)

Attempted fix: parent `Alarm` to `ApplicationManager.getApplication()`. This caused the
platform's Disposer tree to hold `Application → retryAlarm → Alarm internals → plugin class →
PluginClassLoader` — an external reference. **Reverted in v1.2.9.**

#### Pass 013 — v1.2.9: Explicit Alarm disposal (confirmed fix)

**Fix:** Create `Alarm` with no parent (`Alarm(Alarm.ThreadToUse.SWING_THREAD)`). During cleanup,
call `cancelAllRequests()` then `Disposer.dispose(retryAlarm)` explicitly. This ensures no
platform-owned object retains a reference to the alarm after unload.

**Confirmed via heap dump:** After v1.2.9, the WindowAccent classloader was not present in the
heap dump at all — it was successfully garbage collected. The `.hprof` was triggered by other
plugins (Discord, SonarLint, IdeaVim, GitToolBox) failing the GC check, not WindowAccent.

#### How to verify dynamic unloadability locally (sandbox simulation)

1. Temporarily set `pluginVersion=1.2.9-test` in `gradle.properties`, run `.\gradlew buildPlugin`,
   copy the zip, then restore `pluginVersion=1.2.9`.
2. Run `.\gradlew runIde` — the sandbox starts with `1.2.9` pre-installed.
3. Inside the running sandbox: **Settings → Plugins → ⚙️ → Install Plugin from Disk…** →
   select `WindowAccent-1.2.9-test.zip`.
4. Click **"Restart IDE"** when prompted.
5. Check `C:\Users\<you>\` for a `.hprof` file — absence means the classloader was GC'd (fix works).
6. Check the sandbox log at `.intellijPlatform\sandbox\WindowAccent\IU-<version>\log\idea.log`
   and search for `"class loader cannot be unloaded"` — absence confirms success.

> **Note:** ~~Sandbox *disable* does not trigger the GC check (`checked=false` in log). Only a plugin
> *update* (unload old + load new) exercises the classloader GC check.~~
>
> **Updated (2026.1+):** Sandbox *disable* now **does** trigger the classloader GC check.
> Log 004 (v1.2.10 sandbox disable, IU-261.22158.277) confirmed `classloader unload checked=true`
> on a plain disable. Either this behaviour changed in 2026.1 or the original observation was
> environment-specific. In either case, both disable and update paths now exercise the GC check.

#### Heap dump analysis (Eclipse MAT)

If a future `.hprof` is generated, use Eclipse MAT (https://eclipse.dev/mat/downloads.php):

1. **File → Open Heap Dump…** → select the `.hprof`.
2. Run **OQL:** `SELECT * FROM com.window_accent.WindowAccentApplicationService` — any result
   means that class is still alive after unload.
3. Right-click a result → **"Path to GC Roots"** → **"exclude weak/soft references"** — this
   shows the exact external reference chain keeping the classloader alive.
4. The **first non-`window_accent` object** in the chain is the leak source to fix.

---

### Classloader leak resolution — passes 014–016 (v1.2.10 → v1.2.11)

After v1.2.9, the plugin was confirmed clean via heap dump. However, a fresh sandbox environment
running v1.2.10 under IU-261.22158.277 (2026.1) re-introduced a classloader leak. Three further
issues were identified and resolved using the new `ClassLoaderLeakDiagnostics` utility and IntelliJ's
built-in hprof snapshot analysis (`ide.plugins.snapshot.on.unload.fail=true`).

Confirmed fix: `Successfully unloaded plugin WindowAccent (classloader unload checked=true)` — log 004.

#### Pass 014 — v1.2.11: Caffeine icon cache retaining `PluginClassLoader` via `ImageDataByPathLoader`

**hprof reference path (log 001):**
```
ROOT: Global JNI
  └─ PathClassLoader.classes (ArrayList)
       └─ java.lang.Class
            └─ classLoader → Caffeine BoundedLocalLoadingCache   (icon cache)
                 └─ ConcurrentHashMap
                      └─ key: CachedImageIcon
                           └─ loader: ImageDataByPathLoader
                                └─ classLoader: PluginClassLoader  ★
```

When an icon is loaded via `IconLoader`, an `ImageDataByPathLoader` is created that stores the
`PluginClassLoader` so it can later resolve the icon resource path. That loader is kept as a
key in IntelliJ's global Caffeine icon cache, reachable from a Global JNI root.

**Fix:** Added `IconLoader.clearCache()` to `WindowAccentApplicationService.performCleanup()`.
This evicts all `CachedImageIcon` entries — including those whose `ImageDataByPathLoader` holds a
reference to the plugin classloader — before IntelliJ runs its GC collectibility check.

```kotlin
private fun flushIconLoaderCache() {
    IconLoader.clearCache()
    LOG.info("[Window Accent] Flushed IconLoader cache ...")
}
```

#### Pass 015–016 — v1.2.11: `ClassLoaderLeakDiagnostics` self-interference

During investigation a new utility (`ClassLoaderLeakDiagnostics`) was introduced that scheduled
an async background task from `beforePluginUnload` to check whether the `PluginClassLoader` was
GC-eligible. This caused two further self-inflicted leaks that required fixing in turn.

**Pass 015 — lambda captured `classLoader` directly (log 002):**

The lambda dispatched to `executeOnPooledThread` captured the `ClassLoader` parameter:
```kotlin
// BAD: lambda keeps classLoader strongly reachable for its entire execution lifetime
executeOnPooledThread { performLeakCheck(weakRef, classLoader) }
```
IntelliJ's GC check ran while the lambda was sleeping (3 s delay), finding the classloader
strongly reachable from the pooled thread's stack frame.

**Fix:** Only the `WeakReference` crosses the thread boundary. The strong reference is obtained
from `weakRef.get()` only *after* all GC rounds have run:
```kotlin
executeOnPooledThread { performLeakCheck(weakRef) }  // classLoader NOT captured

private fun performLeakCheck(weakRef: WeakReference<ClassLoader>) {
    // ... sleep and GC rounds ...
    val leakedClassLoader = weakRef.get() ?: return  // obtain strong ref only if still alive
```

**Pass 016 — lambda's own class object retained the classloader (log 003):**

Even after pass 015, the hprof showed:
```
ROOT: Java Frame: ClassLoaderLeakDiagnostics$$Lambda.run(Native method)
  └─ ClassLoaderLeakDiagnostics$$Lambda  (the running lambda object)
       └─ <class>: java.lang.Class
            └─ cachedConstructor: PluginClassLoader  ★
```

This is a fundamental constraint: **any class loaded by the `PluginClassLoader` — including the
lambda class itself — keeps the classloader alive via `Class.classLoader`.** As long as any plugin
code executes on any thread during IntelliJ's GC check window, the classloader is reachable. There
is no way to schedule an async plugin lambda during the unload path that does not self-interfere.

**Fix:** Removed the `ClassLoaderLeakDiagnostics.scheduleLeakCheck(...)` call from
`PluginLifecycleListener.beforePluginUnload` entirely. The class is retained in the codebase for
ad-hoc investigation but must not be called from any plugin lifecycle hook.

#### Key rule added

| Rule | Why |
|------|-----|
| Never schedule async plugin code from the unload path | Any plugin lambda executing during IntelliJ's GC check keeps the `PluginClassLoader` alive via `Class.classLoader` — the lambda's own class is loaded by the plugin classloader |
| Call `IconLoader.clearCache()` during cleanup | `ImageDataByPathLoader` entries in IntelliJ's global Caffeine icon cache store the `PluginClassLoader` and are reachable from a Global JNI root |

#### `ClassLoaderLeakDiagnostics` — ad-hoc usage only

`ClassLoaderLeakDiagnostics` was introduced to diagnose leaks and was instrumental in confirming
the icon cache fix. To use it for future investigations:

1. Temporarily re-add the `scheduleLeakCheck` call to `beforePluginUnload`.
2. Run `./gradlew runIde` (`idea.is.internal=true` and `ide.plugins.snapshot.on.unload.fail=true`
   are already set in `build.gradle.kts`).
3. Trigger a plugin disable/update.
4. Read the `LeakDiagnostics`-tagged log entries and any generated `.hprof`.
5. **Remove the call again before committing** — the diagnostic itself will always appear as the
   retaining root and will cause the unload check to fail.

---

### Classloader leak — pass 017 (v1.3.0 → v1.3.1): `toolWindow.disposable` lambda not disposed on `isUpdate=true`

#### Symptom

v1.3.0 required a restart when installed as a marketplace update from v1.2.12.
Preceding disable tests (v1.2.11 → v1.2.12, `isUpdate=false`) had all passed, masking the bug.

#### Root cause

`WindowAccentToolWindowFactory.createToolWindowContent` registered a belt-and-suspenders cleanup
lambda directly with `toolWindow.disposable`:

```kotlin
Disposer.register(toolWindow.disposable) {          // ← anonymous Disposable wrapping a plugin lambda
    allButtonListeners.remove(project)?.forEach { (button, listener) ->
        button.removeActionListener(listener)
    }
}
```

This created the chain:

```
IntelliJ Disposer tree → toolWindow.disposable (platform) → anonymous Disposable (plugin class) → PluginClassLoader
```

**Why disable (`isUpdate=false`) passed:** IntelliJ closes the tool window before the GC check
when a plugin is disabled. Closing the tool window disposes `toolWindow.disposable`, which fires
the lambda and removes it from the Disposer tree — no reference survives.

**Why update (`isUpdate=true`) failed:** IntelliJ does NOT close the tool window during a plugin
update. `toolWindow.disposable` stays alive with the lambda still registered, keeping the
classloader reachable when the GC check runs.

This bug was present since the first tool window implementation but was only exposed by the first
real marketplace update (`isUpdate=true`) in v1.3.0.

#### Fix (v1.3.1)

Replace the anonymous `Disposer.register(toolWindow.disposable) { lambda }` with a named,
tracked intermediate `Disposable`:

```kotlin
val cleanupDisposable = Disposer.newDisposable("WindowAccent-toolwindow-button-cleanup")
toolWindowCleanupDisposables[project] = cleanupDisposable
Disposer.register(toolWindow.disposable, cleanupDisposable)
Disposer.register(cleanupDisposable) {
    allButtonListeners.remove(project)?.forEach { (button, listener) ->
        button.removeActionListener(listener)
    }
    toolWindowCleanupDisposables.remove(project)   // self-remove on natural disposal
}
```

During plugin unload, `removeAllButtonListeners()` explicitly disposes every entry in
`toolWindowCleanupDisposables`, removing the plugin-class lambda from `toolWindow.disposable`'s
Disposer children before the GC check runs:

```kotlin
val disposablesSnapshot = HashMap(toolWindowCleanupDisposables)
toolWindowCleanupDisposables.clear()
disposablesSnapshot.values.forEach { Disposer.dispose(it) }
```

#### Key rule added

Always use a named intermediate `Disposable` when registering a cleanup lambda with a platform
lifecycle object (`toolWindow.disposable`, `project`, etc.) so that the lambda can be explicitly
removed from the Disposer tree during plugin unload. Never rely on the platform object being
disposed first — for `isUpdate=true`, it will not be.

