# README Developer Notes

### SwingUtilities.invokeLater vs ApplicationManager.getApplication().invokeLater

Both methods schedule code to run later on the UI thread, but they operate at different abstraction levels:

1. SwingUtilities.invokeLater { ... }
   - Part of standard Java Swing
   - Pushes your task onto the Event Dispatch Thread (EDT)
   - Has no awareness of IntelliJ’s application lifecycle
   - Just says: “run this on the UI thread when possible”
2. ApplicationManager.getApplication().invokeLater { ... }
   - Part of the IntelliJ Platform SDK
   - Also runs on the EDT, BUT:
     - Is aware of IDE state (e.g. indexing, disposal, modality)
     - Can respect modality states (dialogs, etc.)
     - Prevents running code when the project/app is disposed
     - Integrates with IntelliJ’s threading model

⚠️ Why this matters

In a plugin, using SwingUtilities.invokeLater can cause subtle bugs:

Code might run when the project is already closed/disposed
You might violate IntelliJ threading rules (leading to warnings or crashes)
You lose control over modality (e.g. running during a modal dialog unintentionally)

✅ Best practice (important)

For IntelliJ plugins, you should prefer this:

```
ApplicationManager.getApplication().invokeLater {
// your code
}
```

Or even better (more explicit):

```
ApplicationManager.getApplication().invokeLater(
{
// your code
},
ModalityState.NON_MODAL
)
```

🆚 When would SwingUtilities be okay?

Almost never in plugin code. It’s only acceptable if:

You’re writing pure Swing code outside IntelliJ context, or
You really don’t care about IDE lifecycle (rare, risky)

🔍 Errors in Sandbox with additional plugins

Plugins like Kubernetes integrations tend to:

React to project state changes
Hook into background tasks / indexing
Use IntelliJ’s read/write locks
Depend on modality state correctness

Using SwingUtilities.invokeLater can break assumptions those plugins rely on.
