package com.window_accent.configuration.tool_window

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.content.ContentFactory
import com.window_accent.configuration.persistence.WindowCustomTitleStateService
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService.Side
import com.window_accent.configuration.persistence.WindowTitleNumberingStateService
import com.window_accent.feature.window_color.WindowColorApplier
import com.window_accent.feature.window_title.WindowTitleApplier
import java.awt.Color
import java.awt.Font
import java.awt.GridLayout
import java.awt.event.ActionListener
import java.util.concurrent.ConcurrentHashMap
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.Timer

/**
 * DumbAware is an IntelliJ Platform marker interface used in JetBrains IDE plugins.
 *
 * In IntelliJ terminology:
 *
 * - "Smart mode" = indexes are ready, PSI/model analysis works normally
 * - "Dumb mode" = the IDE is still indexing, so many code-analysis features are temporarily unavailable
 *
 * When your class implements DumbAware, you're telling the IDE:
 *
 * "This component can safely run even while the IDE is indexing."
 *
 * @see DumbAware
 * */
class WindowAccentToolWindowFactory : ToolWindowFactory, DumbAware {

    companion object {
        /**
         * Tracks all (button, listener) pairs added to tool window panels across all projects.
         *
         * During plugin unload we must remove every [ActionListener] from every [JButton]
         * before IntelliJ's classloader GC check runs.  Each listener lambda captures plugin
         * objects (service instances, [WindowColorApplier], [WindowTitleApplier]) via the
         * refreshButtonText closure.  As long as those lambdas are registered with a
         * Swing component held by IntelliJ's [com.intellij.ui.content.ContentManager], the
         * plugin classloader remains reachable and the GC check fails.
         *
         * removeAllButtonListeners is called from [com.window_accent.WindowAccentApplicationService.performCleanup]
         * which runs inside [com.window_accent.PluginLifecycleListener.beforePluginUnload],
         * guaranteeing cleanup before the GC check.
         */
        private val allButtonListeners =
            ConcurrentHashMap<Project, List<Pair<JButton, ActionListener>>>()

        /**
         * Tracks every [Timer] currently running a border-pulse animation.
         *
         * While a [Timer] is running, Swing's [javax.swing.TimerQueue] holds a strong reference
         * to it, which in turn holds a reference to the [ActionListener] lambda, whose class is
         * loaded by the plugin classloader.  That creates the chain:
         *
         * ```
         * TimerQueue (JDK) → Timer → lambda (plugin class) → PluginClassLoader
         * ```
         *
         * stopAllAnimationTimers is called from removeAllButtonListeners during plugin unload
         * so that [javax.swing.TimerQueue] releases every in-flight animation timer before
         * IntelliJ runs its classloader GC check.
         */
        private val runningAnimationTimers = java.util.concurrent.CopyOnWriteArrayList<Timer>()

        /**
         * Tracks the per-project [Disposable] wrapper that is registered as a child of
         * [ToolWindow.disposable] as a belt-and-suspenders cleanup for the normal project-close path.
         *
         * **Why this matters for dynamic unload (update=true):**
         * When a plugin is *disabled* (`isUpdate=false`), IntelliJ closes the tool window before
         * the classloader GC check, which disposes [ToolWindow.disposable] and fires the lambda —
         * removing it from the Disposer tree. When a plugin is *updated* (`isUpdate=true`), the
         * tool window stays open: [ToolWindow.disposable] remains alive with the lambda (a plugin-
         * class object) still registered as a child, creating the chain:
         *
         * ```
         * Disposer tree → toolWindow.disposable (platform) → lambda (plugin class) → PluginClassLoader
         * ```
         *
         * removeAllButtonListeners explicitly [Disposer.dispose]s each tracked entry, which
         * removes it from [ToolWindow.disposable]'s child tree and breaks the reference chain
         * before IntelliJ runs its GC collectibility check.
         */
        private val toolWindowCleanupDisposables = ConcurrentHashMap<Project, Disposable>()

        fun removeAllButtonListeners() {
            // Stop in-flight border animations so TimerQueue releases their lambdas.
            stopAllAnimationTimers()

            // Remove all button ActionListeners.
            val snapshot = HashMap(allButtonListeners)
            allButtonListeners.clear()
            snapshot.values.flatten().forEach { (button, listener) ->
                button.removeActionListener(listener)
            }

            // Explicitly dispose the Disposer entries registered under toolWindow.disposable.
            // During a plugin UPDATE (isUpdate=true), the tool window is NOT closed before the
            // classloader GC check, so toolWindow.disposable remains alive and any lambda
            // registered as its child (a plugin class) would keep the classloader reachable.
            // Calling Disposer.dispose on each tracked entry removes it from
            // toolWindow.disposable's child tree, breaking the external reference chain.
            val disposablesSnapshot = HashMap(toolWindowCleanupDisposables)
            toolWindowCleanupDisposables.clear()
            disposablesSnapshot.values.forEach { disposable ->
                Disposer.dispose(disposable)
            }
        }

        /**
         * Stops every currently-running border-pulse animation timer and clears the tracking list.
         *
         * Calling [Timer.stop] removes the timer from Swing's [javax.swing.TimerQueue], breaking
         * the external reference chain that would otherwise keep the plugin classloader reachable
         * during IntelliJ's unload GC check.
         */
        private fun stopAllAnimationTimers() {
            val snapshot = ArrayList(runningAnimationTimers)
            runningAnimationTimers.clear()
            snapshot.forEach { it.stop() }
        }

        /** Duration of each step in the border pulse animation, in milliseconds. */
        private const val PULSE_STEP_MS = 120

        /** Border thickness used during the flash phase of the pulse animation. */
        private const val FLASH_BORDER_THICKNESS = 3
    }

    /** Cycle order for the panel direction button: N → S → W → E → N */
    private val sidesCycleOrder = listOf(Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST)

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val colorSettings = project.getService(WindowPanelAppearanceStateService::class.java)
        val titleSettings = project.getService(WindowTitleNumberingStateService::class.java)
        val customTitleSettings = project.getService(WindowCustomTitleStateService::class.java)

        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.border = BorderFactory.createEmptyBorder(12, 12, 12, 12)

        val toggleAllColorsButton = JButton()
        val toggleCurrentColorButton = JButton()
        val cyclePanelDirectionButton = JButton()

        val toggleAllTitlesButton = JButton()
        val toggleCurrentTitleButton = JButton()
        val resetTitleNumberingButton = JButton()

        val toggleCurrentCustomTitleButton = JButton()

        styleAsAllButton(toggleAllColorsButton)
        styleAsAllButton(toggleAllTitlesButton)
        styleAsResetButton(resetTitleNumberingButton)
        styleAsCurrentButton(toggleCurrentColorButton)
        styleAsCurrentButton(toggleCurrentTitleButton)
        styleAsCurrentButton(toggleCurrentCustomTitleButton)
        styleAsCycleButton(cyclePanelDirectionButton)

        fun refreshButtonText() {
            val colorsEnabled = colorSettings.panelIsEnabled()
            toggleAllColorsButton.text = wrapTextInHtmlCenter(
                if (colorsEnabled) "Disable colors for <b>all</b> open windows"
                else "Enable colors for <b>all</b> open windows"
            )
            toggleCurrentColorButton.text = wrapTextInHtmlCenter(
                if (colorsEnabled) "Disable color for current window"
                else "Enable color for current window"
            )

            cyclePanelDirectionButton.text = wrapTextInHtmlCenter("Panel direction: ${colorSettings.getSide()}")
            cyclePanelDirectionButton.toolTipText = "Click Me! Cycle the color panel location"

            val titlesEnabled = titleSettings.isTitleNumberingEnabled()
            toggleAllTitlesButton.text = wrapTextInHtmlCenter(
                if (titlesEnabled) "Disable title numbers for <b>all</b> open windows"
                else "Enable title numbers for <b>all</b> open windows"
            )
            toggleCurrentTitleButton.text = wrapTextInHtmlCenter(
                if (titlesEnabled) "Disable title number for current window"
                else "Enable title number for current window"
            )
            resetTitleNumberingButton.text = wrapTextInHtmlCenter("Reset title numbers: current window → 1")
            resetTitleNumberingButton.toolTipText = "Renumber all open windows starting from 1, with this window first"

            toggleCurrentCustomTitleButton.text = wrapTextInHtmlCenter(
                if (customTitleSettings.isCustomTitleEnabled()) "Disable custom title for current window"
                else "Enable custom title for current window"
            )
        }

        // Assign listeners to named variables so they can be tracked and removed on plugin unload.
        // Each listener lambda captures plugin objects (service instances, singletons) via the
        // refreshButtonText closure. If these listeners remain registered on Swing buttons held
        // by IntelliJ's ContentManager after the plugin is unloaded, the plugin classloader stays
        // reachable and IntelliJ's GC check fails, forcing an unnecessary restart.
        val toggleAllColorsListener = ActionListener {
            animateButtonClick(toggleAllColorsButton, JBColor(Color(0x87CEEB), Color(0x79C0FF)))
            val enabled = colorSettings.panelIsDisabled()
            colorSettings.setPanelEnabled(enabled)
            WindowColorApplier.applyToAllOpenProjects(enabled)
            refreshButtonText()
        }
        val toggleCurrentColorListener = ActionListener {
            animateButtonClick(toggleCurrentColorButton, JBColor(Color(0x90EE90), Color(0x56D364)))
            val enabled = colorSettings.panelIsDisabled()
            colorSettings.setPanelEnabled(enabled)
            WindowColorApplier.applyToCurrentOpenProject(project)
            refreshButtonText()
        }
        val cyclePanelDirectionListener = ActionListener {
            animateButtonClick(cyclePanelDirectionButton, JBColor(Color(0xFFD700), Color(0xFFE566)))
            val currentIndex = sidesCycleOrder.indexOf(colorSettings.getSide())
            val nextSide = sidesCycleOrder[(currentIndex + 1) % sidesCycleOrder.size]
            colorSettings.setSide(nextSide)
            WindowColorApplier.applyToCurrentOpenProject(project)
            refreshButtonText()
        }
        val toggleAllTitlesListener = ActionListener {
            animateButtonClick(toggleAllTitlesButton, JBColor(Color(0x87CEEB), Color(0x79C0FF)))
            val enabled = titleSettings.isTitleNumberingDisabled()
            titleSettings.setTitleNumberingEnabled(enabled)
            WindowTitleApplier.applyToAllOpenProjects(enabled)
            refreshButtonText()
        }
        val toggleCurrentTitleListener = ActionListener {
            animateButtonClick(toggleCurrentTitleButton, JBColor(Color(0x90EE90), Color(0x56D364)))
            val enabled = titleSettings.isTitleNumberingDisabled()
            titleSettings.setTitleNumberingEnabled(enabled)
            WindowTitleApplier.applyToCurrentOpenProject(project, enabled)
            refreshButtonText()
        }
        val resetTitleNumberingListener = ActionListener {
            animateButtonClick(resetTitleNumberingButton, JBColor(Color(0xFF80FF), Color(0xFF80FF)))
            WindowTitleApplier.renumberAllOpenWindows(project)
        }
        val toggleCurrentCustomTitleListener = ActionListener {
            animateButtonClick(toggleCurrentCustomTitleButton, JBColor(Color(0x90EE90), Color(0x56D364)))
            val enabled = customTitleSettings.isCustomTitleDisabled()
            customTitleSettings.setCustomTitleEnabled(enabled)
            WindowTitleApplier.applyToCurrentOpenProject(project)
            refreshButtonText()
        }

        toggleAllColorsButton.addActionListener(toggleAllColorsListener)
        toggleCurrentColorButton.addActionListener(toggleCurrentColorListener)
        cyclePanelDirectionButton.addActionListener(cyclePanelDirectionListener)
        toggleAllTitlesButton.addActionListener(toggleAllTitlesListener)
        toggleCurrentTitleButton.addActionListener(toggleCurrentTitleListener)
        resetTitleNumberingButton.addActionListener(resetTitleNumberingListener)
        toggleCurrentCustomTitleButton.addActionListener(toggleCurrentCustomTitleListener)

        refreshButtonText()

        panel.add(buildButtonRow(toggleAllColorsButton, toggleCurrentColorButton, cyclePanelDirectionButton))
        panel.add(Box.createVerticalStrut(8))
        panel.add(buildButtonRow(toggleAllTitlesButton, toggleCurrentTitleButton, resetTitleNumberingButton))
        panel.add(Box.createVerticalStrut(8))
        panel.add(buildButtonRow(toggleCurrentCustomTitleButton))

        val content = ContentFactory.getInstance().createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)

        val listenerPairs = listOf(
            toggleAllColorsButton to toggleAllColorsListener,
            toggleCurrentColorButton to toggleCurrentColorListener,
            cyclePanelDirectionButton to cyclePanelDirectionListener,
            toggleAllTitlesButton to toggleAllTitlesListener,
            toggleCurrentTitleButton to toggleCurrentTitleListener,
            resetTitleNumberingButton to resetTitleNumberingListener,
            toggleCurrentCustomTitleButton to toggleCurrentCustomTitleListener,
        )
        allButtonListeners[project] = listenerPairs

        // Register a belt-and-suspenders cleanup for the normal project-close path.
        //
        // The disposable is tracked in toolWindowCleanupDisposables so that
        // removeAllButtonListeners() can explicitly Disposer.dispose() it during plugin
        // UPDATE (isUpdate=true), where the tool window is NOT closed before the GC check
        // and toolWindow.disposable would otherwise remain alive with this plugin-class
        // lambda still registered as a child.
        val cleanupDisposable = Disposer.newDisposable("WindowAccent-toolwindow-button-cleanup")
        toolWindowCleanupDisposables[project] = cleanupDisposable
        Disposer.register(toolWindow.disposable, cleanupDisposable)
        Disposer.register(cleanupDisposable) {
            allButtonListeners.remove(project)?.forEach { (button, listener) ->
                button.removeActionListener(listener)
            }
            // Self-remove from tracking when naturally disposed (project/tool-window close),
            // so removeAllButtonListeners() does not try to dispose an already-disposed entry.
            toolWindowCleanupDisposables.remove(project)
        }
    }

    /**
     * Wraps [text] in an HTML centre block so that Swing renders it with
     * automatic word-wrapping when the button is narrower than the full label.
     */
    private fun wrapTextInHtmlCenter(text: String) = "<html><center>$text</center></html>"

    /**
     * Styles a button that operates on **all** open windows.
     *
     * Applies a steel-blue border (theme-aware via [JBColor]) and bold font
     * to visually signal wide-impact actions.
     */
    private fun styleAsAllButton(button: JButton) {
        val borderColor = JBColor(Color(0x4682B4), Color(0x58A6FF))
        button.border = BorderFactory.createLineBorder(borderColor, 2, true)
        button.font = button.font.deriveFont(Font.BOLD)
    }

    /**
     * Styles a button that operates on the **current** window only.
     *
     * Applies a muted-green border (theme-aware via [JBColor]) to signal
     * a scoped, lower-impact action.
     */
    private fun styleAsCurrentButton(button: JButton) {
        val borderColor = JBColor(Color(0x5A8A5A), Color(0x3FB950))
        button.border = BorderFactory.createLineBorder(borderColor, 1, true)
    }

    /**
     * Styles the cycle-direction button.
     *
     * Applies a warm-amber border (theme-aware via [JBColor]) to distinguish
     * it as a mode/configuration control rather than an enable/disable toggle.
     */
    private fun styleAsCycleButton(button: JButton) {
        val borderColor = JBColor(Color(0xB8860B), Color(0xD29922))
        button.border = BorderFactory.createLineBorder(borderColor, 1, true)
    }

    /**
     * Styles the reset button.
     *
     * Applies a warm-amber border (theme-aware via [JBColor]) to distinguish
     * it as a mode/configuration control rather than an enable/disable toggle.
     */
    private fun styleAsResetButton(button: JButton) {
        val borderColor = JBColor(Color(0xdc4fe3), Color(0xdc4fe3))
        button.border = BorderFactory.createLineBorder(borderColor, 1, true)
    }

    /**
     * Creates a horizontal row of buttons that each fill an equal share of the available width.
     */
    private fun buildButtonRow(vararg buttons: JButton): JPanel {
        val row = JPanel(GridLayout(1, buttons.size, 8, 0))
        buttons.forEach { row.add(it) }
        return row
    }

    /**
     * Plays a double-pulse border animation on [button] to give immediate visual feedback.
     *
     * The sequence (each step is [PULSE_STEP_MS] ms) is:
     *
     * ```
     * click → [flashBorder] → [originalBorder] → [flashBorder] → [originalBorder]  (stop)
     * ```
     *
     * The flash border uses [flashColor] at a fixed thickness of [FLASH_BORDER_THICKNESS] px,
     * which is always wider than the resting border so the pulse is clearly visible regardless
     * of the button's normal style.
     *
     * **Unload safety:** while a [Timer] is running, Swing's [javax.swing.TimerQueue] holds a
     * strong reference to it.  The timer is therefore tracked in [runningAnimationTimers] so that
     * [stopAllAnimationTimers] (called from [removeAllButtonListeners] during plugin unload) can
     * call [Timer.stop] before IntelliJ's classloader GC check runs.  The timer also removes
     * itself from the list when it self-terminates in normal operation, keeping the list small.
     */
    private fun animateButtonClick(button: JButton, flashColor: Color) {
        val originalBorder = button.border
        val flashBorder = BorderFactory.createLineBorder(flashColor, FLASH_BORDER_THICKNESS, true)

        button.border = flashBorder

        var step = 0
        val timer = Timer(PULSE_STEP_MS, null)
        runningAnimationTimers.add(timer)
        timer.addActionListener {
            step++
            when {
                step >= 3 -> {
                    button.border = originalBorder
                    timer.stop()
                    runningAnimationTimers.remove(timer)
                }
                step % 2 == 0 -> button.border = flashBorder
                else -> button.border = originalBorder
            }
        }
        timer.isRepeats = true
        timer.start()
    }

}