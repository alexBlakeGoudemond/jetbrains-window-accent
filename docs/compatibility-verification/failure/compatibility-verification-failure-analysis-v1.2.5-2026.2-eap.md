# Compatibility Verification Failure Analysis — v1.2.5 on EAP 2026.2 (262.7132.23)

## Summary

v1.2.5 was rejected by the JetBrains Marketplace automated compatibility review for EAP build
`262.7132.23` with two exceptions. v1.2.4 passed the same build. The root cause was a new cleanup
step introduced in v1.2.5 (`flushEdtQueue`) that called `invokeAndWait` from a background thread
during application shutdown, forcing EDT processing at a timing that exposed threading assertions
new to 2026.2 EAP.

---

## Failure files

- `.ai-resources/compatibility-verification/compatibility-verification-failure-1.2.5-001.txt`
- `.ai-resources/compatibility-verification/compatibility-verification-failure-1.2.5-002.txt`

---

## Exception 001 — `ExternalSystemModuleOptionsEntity`

```
java.lang.IllegalStateException: Cannot find an entity by id ExternalSystemModuleOptionsEntity-:-0
    at GradleTreeStructureProvider.getGradleModuleShortName
    at GradleTreeStructureProvider.getGradleModuleNode
    ...
```

**Thread**: background thread via `AsyncTreeModel`  
**Context**: The Gradle project-view tree provider tried to look up a workspace-model entity
(`ExternalSystemModuleOptionsEntity`) that was in a torn-down or partially-updated state.  
**Plugin code in stack trace**: none.

---

## Exception 002 — Read-action violation in `DaemonFusReporter`

```
com.intellij.openapi.diagnostic.RuntimeExceptionWithAttachments:
Read access is allowed from inside read-action only
    at DaemonFusReporter.daemonCanceled
    at DaemonListeners$8.beforeWriteActionStart
    at PlatformReadWriteActionSupport.executeWriteActionOnBackgroundWithAtomicCheck
    ...
```

**Thread**: `DefaultDispatcher-worker-49` (background)  
**Context**: A write action was beginning (`executeWriteActionOnBackgroundWithAtomicCheck`).
Before acquiring the write lock, `beforeWriteActionStart` was broadcast, which caused the
code-analysis daemon to cancel. `DaemonFusReporter.daemonCanceled` then tried to call
`TrafficLightRenderer.getContext` → `CodeInsightContextManagerImpl.getPreferredContext`,
which requires a read action — but none was active on that thread.  
**Plugin code in stack trace**: none.

---

## Why no plugin code appears in the stack traces

Both exceptions are thrown entirely within JetBrains IDE internals.
The plugin is the **indirect trigger**, not the direct cause.

---

## Root cause: `flushEdtQueue()` introduced in v1.2.5

### What it did

`flushEdtQueue()` was added to `WindowAccentApplicationService.performCleanup` to drain any
`Alarm`-dispatched EDT-queue task wrappers that still held lambda references after
`cancelAllRequests()` was called:

```kotlin
private fun flushEdtQueue() {
    val application = ApplicationManager.getApplication()
    if (!application.isDispatchThread) {
        application.invokeAndWait { /* drain */ }
    }
}
```

### Why it was safe on the normal path

On the `beforePluginUnload` path (normal plugin disable/update), `beforePluginUnload` always fires
on the EDT. Both sandbox disable logs for v1.2.5 confirm this with the log line:

```
[Window Accent] Skipping EDT flush (already on EDT)
```

So `flushEdtQueue()` was a no-op — dead code — for every observed disable/update scenario.

### Why it caused failures in EAP automated review

The JetBrains Marketplace compatibility reviewer runs the IDE in an automated environment.
During that session, `WindowAccentApplicationService.dispose()` is likely called from a
**background thread** (e.g., during application shutdown), before `beforePluginUnload` fires.

In that scenario `cleanupCompleted` is still `false`, so `performCleanup` runs for the first time,
reaching `flushEdtQueue()`. Since this is a non-EDT thread, `invokeAndWait {}` is called.

`invokeAndWait` **blocks the background thread** until the EDT processes a posted no-op task.
While the background thread is blocked, the EDT is busy handling IDE shutdown tasks:

- Writing workspace model state → `GradleTreeStructureProvider` accesses an entity being torn down
  → **Exception 001**
- Initiating a write action → `beforeWriteActionStart` → daemon cancel → `DaemonFusReporter`
  calls context lookup without a read action → **Exception 002**

Both exceptions are latent threading issues in 2026.2 EAP that are exposed only because
`invokeAndWait` forces the background thread to synchronise with the EDT at a sensitive moment.
v1.2.4 had no `flushEdtQueue()` step, so nothing blocked and these code paths were never hit.

---

## Fix applied in v1.2.5 (re-published)

1. **Removed `flushEdtQueue()` and its call** from `performCleanup`.
2. **The drain intent is preserved** by the existing `cancelAllRequests()` +
   `Disposer.dispose(retryAlarm)` calls, which remove the alarm's internal lambda references.
   In practice the retryAlarm has no in-flight requests at cleanup time (the project frame is
   already available long before `beforePluginUnload` fires).
3. **Added a thread-identification log** to `dispose()`:
   ```
   [Window Accent] dispose() called (onEdt=false, thread=DefaultDispatcher-worker-XX)
   ```
   This confirms whether the background-thread path is taken in the EAP environment and provides
   evidence for future diagnosis.

---

## Lessons

| Lesson | Detail |
|--------|--------|
| `invokeAndWait` during plugin unload is unsafe | The IDE may be in workspace teardown; forcing EDT synchronisation can trigger latent threading assertions |
| Dead code that only fires in non-standard environments is hard to test locally | The local sandbox always ran cleanup on EDT; EAP automated review uses a different lifecycle |
| No plugin code in a stack trace does not mean the plugin is innocent | The plugin can indirectly expose IDE bugs through timing changes introduced by new cleanup steps |
| Always compare passing/failing versions when exceptions appear | v1.2.4 → v1.2.5 diff immediately narrowed the root cause to the three new cleanup methods |

