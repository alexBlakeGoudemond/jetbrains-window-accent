package com.window_accent.configuration.tool_window

import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.window_accent.diagnostic.windowAccentLogger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import com.window_accent.configuration.persistence.GlobalCustomTitleStateService
import com.window_accent.configuration.persistence.LastOpenedWindowTitleStateService
import com.window_accent.configuration.persistence.GlobalPanelBackgroundColorStateService
import com.window_accent.configuration.persistence.WindowCustomColorStateService
import com.window_accent.configuration.persistence.WindowCustomTitleStateService
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService.Side
import com.window_accent.configuration.settings.IWindowAccentSettings
import com.window_accent.configuration.settings.showScreenColorPicker
import com.window_accent.configuration.persistence.WindowTitleNumberingStateService
import com.window_accent.configuration.settings.WindowAccentSettings
import com.window_accent.feature.window_color.WindowColorApplier
import com.window_accent.feature.window_title.WindowTitleApplier
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.GridLayout
import java.awt.event.ActionListener
import java.util.concurrent.ConcurrentHashMap
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.ButtonGroup
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JColorChooser
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.JSeparator
import javax.swing.SwingConstants
import javax.swing.JSlider
import javax.swing.JTabbedPane
import javax.swing.JTextField
import javax.swing.Timer
import javax.swing.event.ChangeListener
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

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

        private val LOG = windowAccentLogger<WindowAccentToolWindowFactory>()

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

        private val allRadioButtonListeners =
            ConcurrentHashMap<Project, List<Pair<JRadioButton, ActionListener>>>()

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
            stopAllAnimationTimers()
            removeAllButtonActionListeners()
            removeDisposables()
        }

        private fun removeDisposables() {
            val disposablesSnapshot = HashMap(toolWindowCleanupDisposables)
            toolWindowCleanupDisposables.clear()
            disposablesSnapshot.values.forEach { disposable ->
                Disposer.dispose(disposable)
            }
        }

        /**
         * Explicitly dispose the Disposer entries registered under toolWindow.disposable.
         * During a plugin UPDATE (isUpdate=true), the tool window is NOT closed before the
         * classloader GC check, so toolWindow.disposable remains alive and any lambda
         * registered as its child (a plugin class) would keep the classloader reachable.
         * Calling Disposer.dispose on each tracked entry removes it from
         * toolWindow.disposable's child tree, breaking the external reference chain.
         */
        private fun removeAllButtonActionListeners() {
            val snapshot = HashMap(allButtonListeners)
            allButtonListeners.clear()
            snapshot.values.flatten().forEach { (button, listener) ->
                button.removeActionListener(listener)
            }
            val radioButtonSnapshot = HashMap(allRadioButtonListeners)
            allRadioButtonListeners.clear()
            radioButtonSnapshot.values.flatten().forEach { (button, listener) ->
                button.removeActionListener(listener)
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

        /**
         * Cycle order for the panel direction button: N → S → W → E → N.
         *
         * Kept in the companion (static) rather than as an instance field so that the
         * [WindowAccentToolWindowFactory] instance — which IntelliJ's tool-window registry
         * may hold after plugin unload — carries no plugin-class references in its own fields.
         * An instance field holding [Side] enum values would create the chain:
         *   IntelliJ tool-window registry → factory instance → sidesCycleOrder
         *     → Side[] → Side.class → PluginClassLoader
         * In the companion the list is only reachable through the plugin ClassLoader's own
         * class graph, not through any externally-held instance reference.
         */
        private val sidesCycleOrder = listOf(Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST)
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val colorSettings = project.getService(WindowPanelAppearanceStateService::class.java)
        val customColorSettings = project.getService(WindowCustomColorStateService::class.java)
        val titleSettings = project.getService(WindowTitleNumberingStateService::class.java)
        val customTitleSettings = project.getService(WindowCustomTitleStateService::class.java)
        val globalCustomTitleSettings = ApplicationManager.getApplication()
            .getService(GlobalCustomTitleStateService::class.java)
        val lastOpenedWindowTitleSettings = ApplicationManager.getApplication()
            .getService(LastOpenedWindowTitleStateService::class.java)
        val bgColorSettings = ApplicationManager.getApplication()
            .getService(GlobalPanelBackgroundColorStateService::class.java)

        val windowAccentSettings = WindowAccentSettings(project)

        val tabbedPane = JTabbedPane()

        val quickControlsPanel = JPanel()
        quickControlsPanel.layout = BoxLayout(quickControlsPanel, BoxLayout.Y_AXIS)
        quickControlsPanel.border = BorderFactory.createEmptyBorder(12, 12, 12, 12)

        val settingsFormPanel = JPanel(BorderLayout())
        settingsFormPanel.border = BorderFactory.createEmptyBorder(12, 12, 12, 12)

        val panel = quickControlsPanel

        val toggleAllColorsButton = JButton()
        val toggleCurrentColorButton = JButton()
        val cyclePanelDirectionButton = JButton()
        val cycleGradientDirectionButton = JButton()

        val toggleAllTitlesButton = JButton()
        val toggleCurrentTitleButton = JButton()
        val resetTitleNumberingButton = JButton()

        val toggleCurrentCustomTitleButton = JButton()
        val toggleGlobalCustomTitleButton = JButton()

        styleAsAllButton(toggleAllColorsButton)
        styleAsAllButton(toggleAllTitlesButton)
        styleAsResetButton(resetTitleNumberingButton)
        styleAsCurrentButton(toggleCurrentColorButton)
        styleAsCurrentButton(toggleCurrentTitleButton)
        styleAsCurrentButton(toggleCurrentCustomTitleButton)
        styleAsAllButton(toggleGlobalCustomTitleButton)
        styleAsCycleButton(cyclePanelDirectionButton)
//        styleAsGradientButton(cycleGradientDirectionButton)

        // Settings form components
        val sideCombo = JComboBox(WindowPanelAppearanceStateService.Side.entries.toTypedArray())
        val customColorCheckBox = JCheckBox("Use custom color")
        val titleNumberingCheckBox = JCheckBox("Enable custom title numbering")
        val customTitleTextField = JTextField()
        val globalCustomTitleTextField = JTextField()
        val focussedWindowTitleTextField = JTextField()
        val colorPreview = JPanel(BorderLayout())
        val colorPreviewLabel = JLabel("Preview", SwingConstants.CENTER)
        colorPreview.add(colorPreviewLabel, BorderLayout.CENTER)
        val chooseColorButton = JButton("Choose color")
        val dropperButton = JButton(AllIcons.Actions.Colors)
        var selectedColor: Color? = null

        // Background color components
        val bgColorPreview = JPanel(BorderLayout())
        val bgColorPreviewLabel = JLabel("Preview", SwingConstants.CENTER)
        bgColorPreview.add(bgColorPreviewLabel, BorderLayout.CENTER)
        val chooseBgColorButton = JButton("Choose color")
        val bgDropperButton = JButton(AllIcons.Actions.Colors)
        val bgColorCheckBox = JCheckBox("Use custom color")
        var selectedBgColor: Color? = null
        val paddingSlider = JSlider(0, 10, colorSettings.getPanelPadding()).apply {
            majorTickSpacing = 5
            minorTickSpacing = 1
            paintTicks = true
            paintLabels = true
            snapToTicks = true
        }

        // Configure color preview
        colorPreview.preferredSize = Dimension(60, 24)
        colorPreview.border = BorderFactory.createLineBorder(Color.DARK_GRAY)
        colorPreview.isOpaque = true

        // Configure background color preview
        bgColorPreview.preferredSize = Dimension(60, 24)
        bgColorPreview.border = BorderFactory.createLineBorder(Color.DARK_GRAY)
        bgColorPreview.isOpaque = true

        fun updateColorPreview() {
            colorPreview.background = selectedColor ?: Color.DARK_GRAY
            colorPreviewLabel.foreground = if ((selectedColor?.let { (it.red + it.green + it.blue) / 3 } ?: 0) > 128)
                Color.DARK_GRAY else Color.LIGHT_GRAY
            colorPreview.repaint()
        }

        fun updateBgColorPreview() {
            bgColorPreview.background = selectedBgColor ?: Color.DARK_GRAY
            bgColorPreviewLabel.foreground =
                if ((selectedBgColor?.let { (it.red + it.green + it.blue) / 3 } ?: 0) > 128)
                    Color.DARK_GRAY else Color.LIGHT_GRAY
            bgColorPreview.repaint()
        }

        fun syncEnabledState() {
            // All color picker controls are always enabled — no checkboxes to gate them
        }


        var isSyncing = false

        fun syncFromSettings() {
            isSyncing = true
            try {
                LOG.debug("syncFromSettings: side=${colorSettings.getSide()}, useCustomColor=${customColorSettings.isUseCustomColor()}, customTitle='${customTitleSettings.getCustomTitle()}', globalTitle='${globalCustomTitleSettings.getGlobalCustomTitle()}'")
                sideCombo.selectedItem = colorSettings.getSide()
                selectedColor = customColorSettings.getCustomColor()
                updateColorPreview()
                selectedBgColor = bgColorSettings.getCustomBackgroundColor()
                updateBgColorPreview()
                titleNumberingCheckBox.isSelected = titleSettings.isTitleNumberingEnabled()
                customTitleTextField.text = customTitleSettings.getCustomTitle()
                globalCustomTitleTextField.text = globalCustomTitleSettings.getGlobalCustomTitle()
                focussedWindowTitleTextField.text = lastOpenedWindowTitleSettings.getFocussedWindowTitle()
                paddingSlider.value = colorSettings.getPanelPadding()
            } finally {
                isSyncing = false
            }
        }

        val colorPresetsGroup = ButtonGroup()

        fun setTitleWithEmojiPreset(emojiBallRegex: Regex, emoji: String) {
            val currentTitle = customTitleSettings.getCustomTitle()
            LOG.debug("Current title: $currentTitle")
            val newTitle = if (currentTitle.isNotEmpty() && emojiBallRegex.containsMatchIn(currentTitle.take(2))) {
                emoji + currentTitle.substring(2)
            } else if (currentTitle.isNotEmpty() && emojiBallRegex.containsMatchIn(currentTitle.take(1))) {
                emoji + currentTitle.substring(1)
            } else {
                "$emoji $currentTitle"
            }
            LOG.debug("New title: $newTitle")
            customTitleSettings.setCustomTitle(newTitle)
            customTitleSettings.setCustomTitleEnabled(true)
            LOG.debug("Custom title set")
        }

        fun setPanelColor(name: String, radioButton: JRadioButton, color: Color?) {
            LOG.debug("Presets clicked: $name")
            radioButton.isSelected = true
            customColorSettings.setUseCustomColor(true)
            customColorSettings.setCustomColor(color)
            LOG.debug("Custom color set: $color")
        }

        fun buildColorPresetsPanel(group: ButtonGroup): JPanel {
            val panel = JPanel(GridLayout(0, 3, 5, 5))

            val presets = listOf(
                Triple("Red", Color.RED, "🔴"),
                Triple("Orange", Color(255, 102, 20), "🟠"),
                Triple("Yellow", Color.YELLOW, "🟡"),
                Triple("Green", Color.GREEN, "🟢"),
                Triple("Blue", Color.BLUE, "🔵"),
                Triple("Purple", Color(128, 0, 128), "🟣")
            )
            val emojiBallRegex = Regex("[🔴🟠🟡🟢🔵🟣]")

            val presetListenerPairs = mutableListOf<Pair<JRadioButton, ActionListener>>()

            for ((name, color, emoji) in presets) {
                val radioButton = JRadioButton(emoji)
                radioButton.toolTipText = name
                val listener = ActionListener {
                    setTitleWithEmojiPreset(emojiBallRegex, emoji)
                    setPanelColor(name, radioButton, color)

                    WindowColorApplier.applyToCurrentOpenProject(project)
                    WindowTitleApplier.getInstance().applyToCurrentOpenProject(project)

                    syncFromSettings()
                    windowAccentSettings.syncPreview()
                    syncEnabledState()
                    LOG.debug("Sync done")
                }
                radioButton.addActionListener(listener)
                presetListenerPairs.add(radioButton to listener)
                group.add(radioButton)
                panel.add(radioButton)
            }
            allRadioButtonListeners[project] = presetListenerPairs
            return panel
        }

        fun getSeparatorConstraints(
            fieldConstraints: GridBagConstraints,
            gridX: Int,
            gridY: Int,
            gridWidth: Int
        ): GridBagConstraints {
            val panelSeparatorConstraints = (fieldConstraints.clone() as GridBagConstraints).apply {
                gridx = gridX
                gridy = gridY
                gridwidth = gridWidth
                fill = GridBagConstraints.HORIZONTAL
                weightx = 1.0
            }
            return panelSeparatorConstraints
        }

        fun buildSettingsForm(): JPanel {
            val formPanel = JPanel(GridBagLayout())
            val (labelConstraints, fieldConstraints) = windowAccentSettings.configureGrid()

            formPanel.add(JBLabel("Panel side:"), labelConstraints)
            formPanel.add(sideCombo, fieldConstraints)

            labelConstraints.gridy = 1
            fieldConstraints.gridy = 1
            formPanel.add(JBLabel("Panel padding:"), labelConstraints)
            formPanel.add(paddingSlider, fieldConstraints)
            paddingSlider.toolTipText = "Control the padding between the accent panel and the IDE window edge"

            labelConstraints.gridy = 2
            fieldConstraints.gridy = 2
            formPanel.add(JBLabel("Gradient direction:"), labelConstraints)
            formPanel.add(cycleGradientDirectionButton, fieldConstraints)

            val panelSeparatorConstraints = getSeparatorConstraints(fieldConstraints, 0, 3, 2)
            formPanel.add(JSeparator(SwingConstants.HORIZONTAL), panelSeparatorConstraints)

            labelConstraints.gridy = 4
            fieldConstraints.gridy = 4
            formPanel.add(JBLabel("Title numbering:"), labelConstraints)
            formPanel.add(titleNumberingCheckBox, fieldConstraints)

            labelConstraints.gridy = 5
            fieldConstraints.gridy = 5
            formPanel.add(JBLabel("Custom title (this window):"), labelConstraints)
            formPanel.add(customTitleTextField, fieldConstraints)
            customTitleTextField.toolTipText =
                "Label shown in this window's title alongside the number (e.g. \"dattebayo\"). Toggle on/off."

            labelConstraints.gridy = 6
            fieldConstraints.gridy = 6
            formPanel.add(JBLabel("Custom title (all windows):"), labelConstraints)
            formPanel.add(globalCustomTitleTextField, fieldConstraints)
            globalCustomTitleTextField.toolTipText =
                "Label shown in ALL window titles (e.g. \"PERSONAL\" or \"CLIENT\"). Toggle on/off."

            labelConstraints.gridy = 7
            fieldConstraints.gridy = 7
            formPanel.add(JBLabel("Last focussed window title:"), labelConstraints)
            formPanel.add(focussedWindowTitleTextField, fieldConstraints)
            focussedWindowTitleTextField.toolTipText =
                "Label shown at the very front of the title of whichever window instance was focused " +
                        "most recently (e.g. \"NEW\"). Automatically moves to the newest focused window " +
                        "as you switch between windows. Leave blank to disable — there's no separate on/off " +
                        "toggle for this one."

            val titleSeparatorConstraints = getSeparatorConstraints(fieldConstraints, 0, 8, 2)
            formPanel.add(JSeparator(SwingConstants.HORIZONTAL), titleSeparatorConstraints)

            labelConstraints.gridy = 9
            fieldConstraints.gridy = 9
            formPanel.add(JBLabel("Color presets:"), labelConstraints)
            formPanel.add(buildColorPresetsPanel(colorPresetsGroup), fieldConstraints)

            val presetSeparatorConstraints = getSeparatorConstraints(fieldConstraints, 1, 10, 1)
            formPanel.add(JSeparator(SwingConstants.HORIZONTAL), presetSeparatorConstraints)

            labelConstraints.gridy = 11
            fieldConstraints.gridy = 11
            formPanel.add(JBLabel("Custom color:"), labelConstraints)
            formPanel.add(colorPreview, fieldConstraints)

            labelConstraints.gridy = 12
            fieldConstraints.gridy = 12
            val colorButtonsRow = JPanel(GridLayout(1, 2, 4, 0))
            dropperButton.toolTipText = "Pick a color from the screen"
            dropperButton.isFocusable = false
            colorButtonsRow.add(chooseColorButton)
            colorButtonsRow.add(dropperButton)
            formPanel.add(JLabel(""), labelConstraints)
            formPanel.add(colorButtonsRow, fieldConstraints)

            val customColorSeparatorConstraints = getSeparatorConstraints(fieldConstraints, 1, 13, 1)
            formPanel.add(JSeparator(SwingConstants.HORIZONTAL), customColorSeparatorConstraints)

            labelConstraints.gridy = 14
            fieldConstraints.gridy = 14
            formPanel.add(JBLabel("Custom Color (Background):"), labelConstraints)
            formPanel.add(bgColorPreview, fieldConstraints)

            labelConstraints.gridy = 15
            fieldConstraints.gridy = 15
            val bgColorButtonsRow = JPanel(GridLayout(1, 2, 4, 0))
            bgDropperButton.toolTipText = "Pick a background color from the screen"
            bgDropperButton.isFocusable = false
            bgColorButtonsRow.add(chooseBgColorButton)
            bgColorButtonsRow.add(bgDropperButton)
            formPanel.add(JLabel(""), labelConstraints)
            formPanel.add(bgColorButtonsRow, fieldConstraints)

            return formPanel
        }

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

            cycleGradientDirectionButton.text = wrapTextInHtmlCenter(
                "Gradient: ${gradientAnchorLabel(colorSettings.getSide(), colorSettings.getGradientAnchor())}"
            )
            cycleGradientDirectionButton.toolTipText = "Click Me! Cycle the color gradient direction"

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

            toggleGlobalCustomTitleButton.text = wrapTextInHtmlCenter(
                if (globalCustomTitleSettings.isGlobalCustomTitleEnabled()) "Disable global custom title for <b>all</b> windows"
                else "Enable global custom title for <b>all</b> windows"
            )
        }

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
            syncFromSettings()
        }
        val cycleGradientDirectionListener = ActionListener {
            animateButtonClick(cycleGradientDirectionButton, JBColor(Color(0x20B2AA), Color(0x39C5CF)))
            val order = gradientAnchorCycleOrder(colorSettings.getSide())
            val currentIndex = order.indexOf(colorSettings.getGradientAnchor()).let { if (it < 0) 0 else it }
            val nextAnchor = order[(currentIndex + 1) % order.size]
            colorSettings.setGradientAnchor(nextAnchor)
            WindowColorApplier.applyToCurrentOpenProject(project)
            refreshButtonText()
        }
        val toggleAllTitlesListener = ActionListener {
            animateButtonClick(toggleAllTitlesButton, JBColor(Color(0x87CEEB), Color(0x79C0FF)))
            val enabled = titleSettings.isTitleNumberingDisabled()
            updateStateForAllOpenProjects(enabled)
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
        val toggleGlobalCustomTitleListener = ActionListener {
            animateButtonClick(toggleGlobalCustomTitleButton, JBColor(Color(0x87CEEB), Color(0x79C0FF)))
            val enabled = globalCustomTitleSettings.isGlobalCustomTitleDisabled()
            globalCustomTitleSettings.setGlobalCustomTitleEnabled(enabled)
            WindowTitleApplier.applyToAllOpenProjects()
            refreshButtonText()
        }

        fun applySettings() {
            if (isSyncing) return
            LOG.debug("applySettings: side=${sideCombo.selectedItem as Side}, useCustomColor=${customColorCheckBox.isSelected}, padding=${paddingSlider.value}, customTitle='${customTitleTextField.text.trim()}', globalTitle='${globalCustomTitleTextField.text.trim()}', lastOpenedTitle='${focussedWindowTitleTextField.text.trim()}'")
            colorSettings.setSide(sideCombo.selectedItem as Side)
            colorSettings.setPanelPadding(paddingSlider.value)
            customColorSettings.setUseCustomColor(selectedColor != null)
            customColorSettings.setCustomColor(selectedColor)
            bgColorSettings.setUseCustomBackgroundColor(selectedBgColor != null)
            bgColorSettings.setCustomBackgroundColor(selectedBgColor)
            titleSettings.setTitleNumberingEnabled(titleNumberingCheckBox.isSelected)
            customTitleSettings.setCustomTitle(customTitleTextField.text.trim())
            globalCustomTitleSettings.setGlobalCustomTitle(globalCustomTitleTextField.text.trim())
            lastOpenedWindowTitleSettings.setFocussedWindowTitle(focussedWindowTitleTextField.text.trim())

            WindowColorApplier.applyToCurrentOpenProject(project)
            WindowTitleApplier.applyToCurrentOpenProject(project)
            // The last-opened-window label can currently be showing on a DIFFERENT open
            // window than the one this Settings tab belongs to (whichever project was opened
            // most recently). applyToCurrentOpenProject above only refreshes `project`'s own
            // title, so re-apply everywhere to make sure an edited label text/blank-out is
            // reflected immediately regardless of which window currently holds it.
            WindowTitleApplier.applyToAllOpenProjects()

            // Keep the Quick Controls tab's "Panel direction" / "Gradient direction" button
            // labels in sync when the panel side (or anything else here) is changed from the
            // Settings tab — e.g. via sideCombo. Without this, those labels go stale until a
            // button on the Quick Controls tab is clicked.
            refreshButtonText()
        }

        val chooseColorListener = ActionListener {
            val chosen = JColorChooser.showDialog(
                settingsFormPanel,
                "Choose custom color",
                selectedColor ?: Color(0, 0, 255)
            )
            if (chosen != null) {
                selectedColor = chosen
                updateColorPreview()
                applySettings()
                windowAccentSettings.syncPreview()
            }
        }

        val dropperListener = ActionListener {
            val settingsWrapper = object : IWindowAccentSettings {
                override fun getProject(): Project = project
                override fun getPanel(): JPanel = settingsFormPanel
                override fun getCustomColorCheckBox(): JCheckBox = customColorCheckBox
                override fun getCustomColorPreviewPanel(): JPanel = colorPreview
                override fun setSelectedColor(color: Color?) {
                    selectedColor = color
                    updateColorPreview()
                    applySettings()
                    windowAccentSettings.syncPreview()
                }

                override fun syncEnabledState() = syncEnabledState()
                override fun syncPreview() {
                    windowAccentSettings.syncPreview()
                }

                override fun configureGrid(): Pair<GridBagConstraints, GridBagConstraints> {
                    return windowAccentSettings.configureGrid()
                }
            }
            showScreenColorPicker(settingsWrapper)
        }

        val chooseBgColorListener = ActionListener {
            val chosen = JColorChooser.showDialog(
                settingsFormPanel,
                "Choose background/gradient color",
                selectedBgColor ?: GlobalPanelBackgroundColorStateService.DEFAULT_BACKGROUND
            )
            if (chosen != null) {
                selectedBgColor = chosen
                updateBgColorPreview()
                applySettings()
            }
        }

        val bgDropperListener = ActionListener {
            val settingsWrapper = object : IWindowAccentSettings {
                override fun getProject(): Project = project
                override fun getPanel(): JPanel = settingsFormPanel
                override fun getCustomColorCheckBox(): JCheckBox = bgColorCheckBox
                override fun getCustomColorPreviewPanel(): JPanel = bgColorPreview
                override fun setSelectedColor(color: Color?) {
                    selectedBgColor = color
                    updateBgColorPreview()
                    applySettings()
                }

                override fun syncEnabledState() = syncEnabledState()
                override fun syncPreview() {}

                override fun configureGrid(): Pair<GridBagConstraints, GridBagConstraints> {
                    return windowAccentSettings.configureGrid()
                }
            }
            showScreenColorPicker(settingsWrapper)
        }

        val sideComboListener = ActionListener {
            applySettings()
        }

        val titleNumberingListener = ActionListener {
            applySettings()
        }

        val customTitleListener = object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) = applySettings()
            override fun removeUpdate(e: DocumentEvent) = applySettings()
            override fun changedUpdate(e: DocumentEvent) = applySettings()
        }

        refreshButtonText()
        syncFromSettings()
        syncEnabledState()
        windowAccentSettings.syncPreview()

        toggleAllColorsButton.addActionListener(toggleAllColorsListener)
        toggleCurrentColorButton.addActionListener(toggleCurrentColorListener)
        cyclePanelDirectionButton.addActionListener(cyclePanelDirectionListener)
        cycleGradientDirectionButton.addActionListener(cycleGradientDirectionListener)
        toggleAllTitlesButton.addActionListener(toggleAllTitlesListener)
        toggleCurrentTitleButton.addActionListener(toggleCurrentTitleListener)
        resetTitleNumberingButton.addActionListener(resetTitleNumberingListener)
        toggleCurrentCustomTitleButton.addActionListener(toggleCurrentCustomTitleListener)
        toggleGlobalCustomTitleButton.addActionListener(toggleGlobalCustomTitleListener)
        chooseColorButton.addActionListener(chooseColorListener)
        dropperButton.addActionListener(dropperListener)
        chooseBgColorButton.addActionListener(chooseBgColorListener)
        bgDropperButton.addActionListener(bgDropperListener)
        val paddingSliderListener = ChangeListener { if (!paddingSlider.valueIsAdjusting) applySettings() }
        paddingSlider.addChangeListener(paddingSliderListener)
        sideCombo.addActionListener(sideComboListener)
        titleNumberingCheckBox.addActionListener(titleNumberingListener)

        customTitleTextField.document.addDocumentListener(customTitleListener)
        globalCustomTitleTextField.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) = applySettings()
            override fun removeUpdate(e: DocumentEvent) = applySettings()
            override fun changedUpdate(e: DocumentEvent) = applySettings()
        })
        focussedWindowTitleTextField.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) = applySettings()
            override fun removeUpdate(e: DocumentEvent) = applySettings()
            override fun changedUpdate(e: DocumentEvent) = applySettings()
        })

        panel.add(buildButtonRow(toggleAllColorsButton, toggleCurrentColorButton, cyclePanelDirectionButton))
        panel.add(Box.createVerticalStrut(8))
        panel.add(buildButtonRow(toggleAllTitlesButton, toggleCurrentTitleButton, resetTitleNumberingButton))
        panel.add(Box.createVerticalStrut(8))
        panel.add(buildButtonRow(toggleGlobalCustomTitleButton, toggleCurrentCustomTitleButton))

        val settingsForm = buildSettingsForm()
        settingsFormPanel.add(settingsForm, BorderLayout.NORTH)

        tabbedPane.addTab("Quick Controls", quickControlsPanel)
        tabbedPane.addTab("Settings", JBScrollPane(settingsFormPanel).apply {
            border = BorderFactory.createEmptyBorder()
        })

        val content = ContentFactory.getInstance().createContent(tabbedPane, "", false)
        toolWindow.contentManager.addContent(content)

        val listenerPairs = listOf(
            toggleAllColorsButton to toggleAllColorsListener,
            toggleCurrentColorButton to toggleCurrentColorListener,
            cyclePanelDirectionButton to cyclePanelDirectionListener,
            cycleGradientDirectionButton to cycleGradientDirectionListener,
            toggleAllTitlesButton to toggleAllTitlesListener,
            toggleCurrentTitleButton to toggleCurrentTitleListener,
            resetTitleNumberingButton to resetTitleNumberingListener,
            toggleCurrentCustomTitleButton to toggleCurrentCustomTitleListener,
            toggleGlobalCustomTitleButton to toggleGlobalCustomTitleListener,
            chooseColorButton to chooseColorListener,
            dropperButton to dropperListener,
            chooseBgColorButton to chooseBgColorListener,
            bgDropperButton to bgDropperListener,
        )
        allButtonListeners[project] = listenerPairs

        registerCleanupForProjectClose(project, toolWindow)
    }

    // Register a belt-and-suspenders cleanup for the normal project-close path.
    //
    // The disposable is tracked in toolWindowCleanupDisposables so that
    // removeAllButtonListeners() can explicitly Disposer.dispose() it during plugin
    // UPDATE (isUpdate=true), where the tool window is NOT closed before the GC check
    // and toolWindow.disposable would otherwise remain alive with this plugin-class
    // lambda still registered as a child.
    private fun registerCleanupForProjectClose(project: Project, toolWindow: ToolWindow) {
        val cleanupDisposable = Disposer.newDisposable("WindowAccent-toolwindow-button-cleanup")
        toolWindowCleanupDisposables[project] = cleanupDisposable
        Disposer.register(toolWindow.disposable, cleanupDisposable)
        Disposer.register(cleanupDisposable) {
            allButtonListeners.remove(project)?.forEach { (button, listener) ->
                button.removeActionListener(listener)
            }
            allRadioButtonListeners.remove(project)?.forEach { (button, listener) ->
                button.removeActionListener(listener)
            }
            // Self-remove from tracking when naturally disposed (project/tool-window close),
            // so removeAllButtonListeners() does not try to dispose an already-disposed entry.
            toolWindowCleanupDisposables.remove(project)
        }
    }

    // Update the persisted state for EVERY open project, not just the current one.
    // Without this, non-focused windows keep their own stale service value (enabled=true),
    // and the focus listener / title listener re-applies the number prefix the next time
    // those windows are visited or the IDE rewrites their title.
    private fun updateStateForAllOpenProjects(enabled: Boolean) {
        ProjectManager.getInstance().openProjects.forEach { openProject ->
            openProject.getService(WindowTitleNumberingStateService::class.java)
                .setTitleNumberingEnabled(enabled)
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
     * Styles the gradient-direction button.
     *
     * Applies a teal border (theme-aware via [JBColor]) to distinguish it from the
     * panel-side cycle button while still signaling a mode/configuration control.
     */
    private fun styleAsGradientButton(button: JButton) {
        val borderColor = JBColor(Color(0x20B2AA), Color(0x39C5CF))
        button.border = BorderFactory.createLineBorder(borderColor, 1, true)
    }

    /**
     * Ordered cycle of [WindowPanelAppearanceStateService.GradientAnchor] values for [side]'s
     * axis, matching the labels shown to the user:
     *
     * - NORTH/SOUTH (horizontal axis): RHS → LHS → Both Ends -> Middle → Off
     * - WEST/EAST (vertical axis): UP → DOWN → Both Ends -> Middle → Off
     */
    private fun gradientAnchorCycleOrder(side: Side): List<WindowPanelAppearanceStateService.GradientAnchor> =
        when (side) {
            Side.NORTH, Side.SOUTH -> listOf(
                WindowPanelAppearanceStateService.GradientAnchor.END,   // RHS
                WindowPanelAppearanceStateService.GradientAnchor.START, // LHS
                WindowPanelAppearanceStateService.GradientAnchor.MIDDLE,
                WindowPanelAppearanceStateService.GradientAnchor.BOTH_ENDS,
                WindowPanelAppearanceStateService.GradientAnchor.OFF
            )
            Side.WEST, Side.EAST -> listOf(
                WindowPanelAppearanceStateService.GradientAnchor.START, // UP
                WindowPanelAppearanceStateService.GradientAnchor.END,   // DOWN
                WindowPanelAppearanceStateService.GradientAnchor.MIDDLE,
                WindowPanelAppearanceStateService.GradientAnchor.BOTH_ENDS,
                WindowPanelAppearanceStateService.GradientAnchor.OFF
            )
        }

    /**
     * Axis-appropriate display label for [anchor] given the panel's current [side].
     */
    private fun gradientAnchorLabel(
        side: Side,
        anchor: WindowPanelAppearanceStateService.GradientAnchor
    ): String = when (anchor) {
        WindowPanelAppearanceStateService.GradientAnchor.MIDDLE -> "Middle"
        WindowPanelAppearanceStateService.GradientAnchor.BOTH_ENDS -> "Both Ends"
        WindowPanelAppearanceStateService.GradientAnchor.OFF -> "Off"
        WindowPanelAppearanceStateService.GradientAnchor.END ->
            if (side == Side.NORTH || side == Side.SOUTH) "RHS" else "DOWN"
        WindowPanelAppearanceStateService.GradientAnchor.START ->
            if (side == Side.NORTH || side == Side.SOUTH) "LHS" else "UP"
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