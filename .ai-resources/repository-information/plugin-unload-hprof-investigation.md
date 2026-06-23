# Plugin Unload — hprof Investigation Notes

This document captures everything known about heap snapshot (`.hprof`) generation
for diagnosing classloader-retention failures during plugin updates.

---

## Background

When a plugin update fails to hot-reload (requiring a restart instead), IntelliJ has failed to
garbage-collect the old `PluginClassLoader`. The JVM can optionally write a heap snapshot at the
moment of that failure, pinpointing the exact retention chain.

The snapshot is controlled by:

```
-XX:+UnlockDiagnosticVMOptions
-Dide.plugins.snapshot.on.unload.fail=true
```

---

## Where to add the VM options

### For `runIde` (Gradle sandbox — already configured)

Already present in `build.gradle.kts`. Snapshots from sandbox sessions are written to
`C:\Users\<user>\` (JVM working directory at the time of the Gradle run).

### For the real IntelliJ installation

Use **Help > Edit Custom VM Options** from any IntelliJ window. This always opens:

```
C:\Users\<userDirectory>\AppData\Roaming\JetBrains\IntelliJIdea2026.1\idea64.exe.vmoptions
```

> **Important:** This file is **shared across all custom JetBrains tool window configurations**
> (the "work/powerfleet", "personal/alexBlakeGoudemond", etc. profiles). Custom configurations
> only isolate the plugin list, settings directory (`config`), and system cache (`system`).
> They do **not** fork the JVM options file. Adding the options once activates them for every
> custom environment.

The two lines have been added as of 2026-06-23. No further action is needed per environment.

---

## Where hprof files are written

Snapshots are written to the JVM working directory at the time of the failure:

- **Sandbox (`runIde`):** `C:\Users\<userDirectory>\`
- **Real installation:** `C:\Users\<userDirectory>\`

File naming: `unload-WindowAccent-DD.MM.YYYY_HH.mm.ss.hprof`

### Known snapshots (from `runIde` sandbox, June 15 2026)

These predate 1.4.1 and 1.4.2 — they reflect retention chains from 1.4.0-era code.
Some paths shown may already be fixed; they may still reveal the second root cause.

```
unload-WindowAccent-15.06.2026_05.52.08.hprof  (~430 MB)
unload-WindowAccent-15.06.2026_05.59.15.hprof  (~444 MB)
unload-WindowAccent-15.06.2026_06.00.37.hprof  (~422 MB)
unload-WindowAccent-15.06.2026_06.45.25.hprof  (~392 MB)
unload-WindowAccent-15.06.2026_06.50.22.hprof  (~724 MB)
unload-WindowAccent-15.06.2026_06.57.46.hprof  (~520 MB)
unload-WindowAccent-15.06.2026_08.52.37.hprof  (~577 MB)
unload-WindowAccent-15.06.2026_10.34.20.hprof  (~386 MB)
```

---

## How to analyse a snapshot

IntelliJ has a built-in heap dump viewer:

1. **File > Open** → select the `.hprof` file
2. In the **Class** tab, search for `PluginClassLoader`
3. Right-click the `PluginClassLoader` instance → **Show Retention Path** or
   **Find Shortest Path to GC Root**
4. The path from a GC root → ... → `PluginClassLoader` is the retention chain to fix

### What to look for

- Any chain that does **not** involve `WindowAccentSettings` or `sideCombo` is the
  **second (intermittent) root cause** — not yet identified as of 1.4.2.
- Chains involving `WindowAccentSettings.sideCombo.DefaultComboBoxModel.Side[]` were
  fixed in 1.4.1 and should no longer appear in post-1.4.1 snapshots.

---

## Log timeline (real-installation update tests)

| Log | Update | Settings open | `isUpdate` | Live instances | Result |
|-----|--------|---------------|------------|----------------|--------|
| `log-example-update-plugin-to-1.4.1-001` | 1.4.0 → 1.4.1 | No  | `true`  | 0 | ✅ Hot-reload (32 ms) |
| `log-example-update-plugin-to-1.4.1-002` | 1.3.1 → 1.4.1 | Yes | `false` | — | ❌ Restart required |
| `log-example-update-plugin-to-1.4.2-001` | 1.4.1 → 1.4.2 | No  | `false` | 0 | ❌ Restart required (no hprof — VM options not yet added) |
| `log-example-update-plugin-to-1.4.2-002` | 1.4.1 → 1.4.2 | No  | `false` | 0 | ✅ Hot-reload (252 ms) |

### Key findings from the timeline

- **`isUpdate=false` is not a predictor of failure.** Both log 002 entries show `isUpdate=false`;
  one failed, one succeeded.
- **The settings-open case is fixed (1.4.1).** Log 002 of the 1.4.1 series confirmed it; no
  regression seen since.
- **An intermittent second root cause exists.** Log 001 of the 1.4.2 series failed under identical
  surface conditions to log 002 (which succeeded). Likely GC-timing non-determinism or a
  background thread state that varies per session.
- **Next action:** Reproduce the intermittent failure and capture the hprof before adding more code.

