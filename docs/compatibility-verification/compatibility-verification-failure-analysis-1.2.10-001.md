# Compatibility Verification Problem Analysis — v1.2.10 (IU-261.22158.277 / 2026.1)

## Summary

This exception was surfaced during compatibility verification of v1.2.10 against IU-261.22158.277
(IntelliJ IDEA 2026.1). It is **identical in nature** to Exception 002 documented in
[`compatibility-verification-failure-analysis-1.2.5-eap-2026.2.md`](compatibility-verification-failure-analysis-1.2.5-eap-2026.2.md).

No plugin code appears in the stack trace. This is a latent threading bug in the IntelliJ Platform's
`DaemonFusReporter` that is exposed whenever any write action is initiated on a coroutine background
thread while the code-analysis daemon is active.

---

## Failure file

- `.ai-resources/compatibility-verification/compatibility-verification-problem-1.2.10-001.txt`

---

## Exception — Read-action violation in `DaemonFusReporter`

```
com.intellij.openapi.diagnostic.RuntimeExceptionWithAttachments:
Read access is allowed from inside read-action only
  (see Application.runReadAction()); If you access or modify model on EDT
  consider wrapping your code in WriteIntentReadAction or ReadAction
Current thread: Thread[#94,DefaultDispatcher-worker-21,5,main]

  at CodeInsightContextManagerImpl.getPreferredContext
  at EditorContextManagerImpl.getEditorContexts
  at EditorContextManager.getEditorContext
  at TrafficLightRenderer.getContext
  at TrafficLightRenderer.getErrorCounts
  at DaemonFusReporter.daemonStopped
  at DaemonFusReporter.daemonCanceled
  at DaemonProgressIndicator.doCancel
  at DaemonCodeAnalyzerImpl.cancelAllUpdateProgresses
  at DaemonListeners$8.beforeWriteActionStart
  at NestedLocksThreadingSupport.runWriteAction          ← coroutine write action
  at PlatformReadWriteActionSupport.runWriteAction$2$1
  at PlatformReadWriteActionSupport.executeWriteActionWithPossibleRetry
  at CoroutineScheduler$Worker.run
```

**Thread**: `DefaultDispatcher-worker-21` (Kotlin coroutine background thread)  
**Plugin code in stack trace**: none.

---

## Root cause — platform threading bug (same as v1.2.5 Exception 002)

When a write action is started via the coroutine-based write-action API
(`PlatformReadWriteActionSupport.runWriteAction`), the platform broadcasts `beforeWriteActionStart`
to all listeners before acquiring the write lock. `DaemonListeners` responds by cancelling the
code-analysis daemon. During that cancel, `DaemonFusReporter.daemonCanceled` is called, which
internally invokes `TrafficLightRenderer.getErrorCounts` → `EditorContextManagerImpl.getEditorContexts`
→ `CodeInsightContextManagerImpl.getPreferredContext`. That final call requires a read action —
but none is held on the background coroutine thread that is initiating the write action.

This is a bug in the IntelliJ Platform's `DaemonFusReporter`: it accesses model data that
requires a read lock from within a `beforeWriteActionStart` notification, which fires before
any lock is held. This is independent of which plugin (if any) triggers the write action.

### Comparison with v1.2.5

In v1.2.5 the **indirect trigger** was our plugin: `flushEdtQueue()` called `invokeAndWait`
from a background thread during shutdown, which blocked that thread and forced the EDT to process
tasks including write actions, exposing this platform bug. Removing `flushEdtQueue()` eliminated
the trigger.

In v1.2.10, there is no `flushEdtQueue()`. The write action on the coroutine thread is initiated
by **platform or other plugin code** — our plugin is not in the call stack at all. The bug is
present in the platform regardless of our plugin; the compatibility verifier just happens to
exercise it during its test run.

---

## Plugin code involvement

None. The plugin's cleanup path (`beforePluginUnload` → `performCleanup`) runs synchronously on
the EDT and does not schedule coroutine-based write actions. The exception occurs on
`DefaultDispatcher-worker-21`, which is a platform-managed coroutine thread.

---

## Suppression

Because this is a known platform bug unrelated to our plugin, it is suppressed in the
`pluginVerification` Gradle task via `plugin-verifier-ignored-problems.txt` (project root).

```
# Known IntelliJ Platform threading bug in DaemonFusReporter (2026.1 / 2026.2).
# DaemonFusReporter.daemonCanceled accesses model data without a read action from
# within beforeWriteActionStart, which fires before any lock is held on a coroutine
# write-action thread. No plugin code appears in the stack trace.
# Same exception class and call chain as v1.2.5 Exception 002.
# See docs/compatibility-verification/compatibility-verification-failure-analysis-1.2.10-001.md
.*DaemonFusReporter.*daemonCanceled.*
.*CodeInsightContextManagerImpl.*getPreferredContext.*
```

---

## Lessons

| Lesson | Detail |
|--------|--------|
| Same platform exception can appear without a plugin trigger | In v1.2.5 our code was the indirect trigger; in v1.2.10 it is triggered by platform/IDE code entirely |
| Absence of plugin code in stack does not guarantee the plugin is innocent | But presence of a coroutine platform write action not started by our code is strong evidence it is not |
| Document suppressed verifier problems explicitly | Prevents confusion when the verifier report is reviewed and the suppressed item is noticed |

---

## Action taken

- Added `plugin-verifier-ignored-problems.txt` at project root with a pattern matching this
  exception class and call chain.
- Added `freeArgs` to `pluginVerification` in `build.gradle.kts` to pass the ignore file to
  the plugin verifier CLI.
- If a future version of IntelliJ fixes `DaemonFusReporter` to wrap the context lookup in a
  read action, this suppression can be removed.

