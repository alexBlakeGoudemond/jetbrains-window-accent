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
