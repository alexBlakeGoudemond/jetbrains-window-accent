package com.window_accent.configuration.settings

import com.intellij.icons.AllIcons
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBLabel
import org.jetbrains.annotations.VisibleForTesting
import com.window_accent.configuration.persistence.GlobalCustomTitleStateService
import com.window_accent.configuration.persistence.WindowCustomTitleStateService
import com.window_accent.configuration.persistence.WindowCustomColorStateService
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService
import com.window_accent.configuration.persistence.WindowTitleNumberingStateService
import com.window_accent.WindowAccentApplicationService
import com.window_accent.feature.window_color.WindowColorApplier
import com.window_accent.feature.window_title.WindowTitleApplier
import java.awt.*
import java.lang.ref.WeakReference
import java.util.concurrent.CopyOnWriteArrayList
import javax.swing.*
import javax.swing.JTextField

/**
 * IntelliJ settings UI for configuring window color and title-numbering behavior.
 *
 * This component keeps the form state in sync with persisted project settings and
 * applies changes to the current IDE window when the user clicks Apply.
 */
interface IWindowAccentSettings {
    fun getProject(): Project
    fun getPanel(): JPanel
    fun getCustomColorCheckBox(): JCheckBox
    fun getCustomColorPreviewPanel(): JPanel
    fun setSelectedColor(color: Color?)
    fun syncEnabledState()
    fun syncPreview()
    fun configureGrid(): Pair<GridBagConstraints, GridBagConstraints>
}

open class WindowAccentSettings(
    private val project: Project
) : Configurable, IWindowAccentSettings {

    companion object {
        private val LOG = logger<WindowAccentSettings>()

        /**
         * WeakReference tracking so [disposeAllTrackedInstances] can call
         * [disposeUIResources] on every live instance during plugin unload, before
         * IntelliJ runs its classloader GC collectibility check.
         *
         * WeakReferences are used deliberately: if IntelliJ has already released an
         * instance (it was GC'd before our cleanup fires), the ref resolves to null
         * and we skip it — correct behaviour.  Strong references would prevent GC of
         * the very instances we are trying to make collectable.
         *
         * Called from [com.window_accent.WindowAccentApplicationService.performCleanup].
         */
        private val trackedInstances = CopyOnWriteArrayList<WeakReference<WindowAccentSettings>>()

        internal fun disposeAllTrackedInstances() {
            val snapshot = ArrayList(trackedInstances)
            trackedInstances.clear()
            var disposed = 0
            snapshot.forEach { ref ->
                ref.get()?.let {
                    it.disposeUIResources()
                    disposed++
                }
            }
            LOG.info("[Window Accent] disposeAllTrackedInstances completed ($disposed live instance(s) disposed, ${snapshot.size - disposed} already GC'd)")
        }

        /**
         * Returns the set of AWT [Window]s that currently contain any tracked
         * [WindowAccentSettings] panel in their Swing component hierarchy.
         *
         * **Must be called before [disposeAllTrackedInstances]** so that the
         * [panel.removeAll()][JPanel.removeAll] call inside [disposeUIResources] has not yet
         * severed the containment chain — [SwingUtilities.getWindowAncestor] relies on the
         * parent references being intact.
         *
         * Used by [com.window_accent.WindowAccentApplicationService.performCleanup] to find and
         * dispose any open Settings dialog before IntelliJ's classloader GC check. Disposing
         * the window posts a [java.awt.event.WindowEvent.WINDOW_CLOSED] event to the EDT queue;
         * the ~1–2 s observed gap between [beforePluginUnload][com.window_accent.PluginLifecycleListener.beforePluginUnload]
         * and the GC check gives the EDT time to process that event, allowing IntelliJ's
         * `DialogWrapper` close handler to clear its configurable cache and release the
         * [WindowAccentSettings] instance reference before reachability is checked.
         */
        internal fun findContainingWindows(): Set<Window> =
            trackedInstances
                .mapNotNull { ref ->
                    ref.get()?.panel?.let { panel ->
                        SwingUtilities.getWindowAncestor(panel)
                    }
                }
                .toSet()
    }

    init {
        trackedInstances.add(WeakReference(this))

        // Guard against the "created-after-cleanup" race:
        //
        // IntelliJ sometimes releases its strong reference to a WindowAccentSettings instance
        // just before our beforePluginUnload cleanup fires (making the WeakReference return null
        // and causing disposeAllTrackedInstances() to report "0 live instances"), then
        // re-instantiates the configurable shortly afterwards — for search indexing, isModified()
        // polling, or other platform bookkeeping that occurs during the plugin unload sequence.
        //
        // That new instance is not in our tracked list (trackedInstances was already cleared)
        // and its sideCombo was populated with Side[] in the constructor above, forming the chain:
        //   WindowAccentSettings → sideCombo → DefaultComboBoxModel → Side[] → Side.class
        //     → PluginClassLoader
        //
        // Calling disposeUIResources() immediately clears sideCombo and nulls all service
        // references, breaking that chain. The instance itself still exists (its class pointer
        // keeps PluginClassLoader technically reachable), but IntelliJ's own extension-point
        // unload machinery is responsible for releasing the instance reference; at that point
        // the classloader becomes GC-eligible.
        //
        // This check is a no-op for the normal (non-unload) code path because
        // cleanupCompleted is false until beforePluginUnload fires.
        if (WindowAccentApplicationService.isCleanupCompleted()) {
            LOG.warn("[Window Accent] WindowAccentSettings instantiated after cleanup completed (project=${project.name}) — immediately disposing to prevent post-cleanup PluginClassLoader retention")
            disposeUIResources()
        }
    }

    @VisibleForTesting
    internal val panel = JPanel(BorderLayout())

    /**
     * Service references are held as `var` so that [disposeUIResources] can null them,
     * breaking the plugin-classloader reference chain if IntelliJ's configurable cache
     * holds this instance after plugin unload.
     */
    private var windowPanelAppearanceStateService: WindowPanelAppearanceStateService? =
        project.getService(WindowPanelAppearanceStateService::class.java)
    private var customColorSettings: WindowCustomColorStateService? =
        project.getService(WindowCustomColorStateService::class.java)
    private var titleNumberingSettings: WindowTitleNumberingStateService? =
        project.getService(WindowTitleNumberingStateService::class.java)
    private var customTitleSettings: WindowCustomTitleStateService? =
        project.getService(WindowCustomTitleStateService::class.java)
    private var globalCustomTitleSettings: GlobalCustomTitleStateService? =
        ApplicationManager.getApplication()?.getService(GlobalCustomTitleStateService::class.java)

    private val form = JPanel(GridBagLayout())

    private val sideCombo = JComboBox(WindowPanelAppearanceStateService.Side.entries.toTypedArray())
    @get:VisibleForTesting
    internal val customColorCheckBox = JCheckBox("Use custom color")
    private val titleNumberingCheckBox = JCheckBox("Enable custom title numbering")
    private val customTitleTextField = JTextField()
    private val globalCustomTitleTextField = JTextField()
    private val colorPreview = JPanel()
    private val chooseColorButton = JButton("Choose color")
    private val dropperButton = JButton(AllIcons.Actions.Colors)
    private val previewLabel = JLabel("")

    @set:VisibleForTesting
    @get:VisibleForTesting
    internal var selectedColor: Color? = null

    override fun getProject(): Project = project

    override fun getPanel(): JPanel = panel

    override fun getCustomColorCheckBox(): JCheckBox = customColorCheckBox

    override fun getCustomColorPreviewPanel(): JPanel = colorPreview

    override fun getDisplayName(): String = "Window Accent"

    override fun createComponent(): JComponent {
        panel.removeAll()
        
        // Always initialize form fields for backward compatibility with tests and the Tool Window
        configureGrid()
        addFields()
        configureColorPreview()
        bindActions()

        syncFromSettings()
        syncEnabledState()
        syncPreview()
        
        // Create a message panel for the Settings UI
        val messagePanel = JPanel(BorderLayout()).apply {
            border = BorderFactory.createEmptyBorder(24, 24, 24, 24)
        }
        
        val messageText = JBLabel("""
            <html>
            <h3>Window Accent Settings</h3>
            <p>
            All Window Accent customization has been moved to the <b>Window Accent Tool Window</b> for easier access.
            </p>
            <p>
            To customize your window colors, titles, and other settings, please:
            </p>
            <ol>
            <li>Open the <b>Window Accent Tool Window</b> (via View > Tool Windows > Window Accent)</li>
            <li>Click on the <b>Settings</b> tab</li>
            <li>Configure your preferences</li>
            </ol>
            <p>
            Changes are applied instantly as you adjust settings in the Tool Window.
            </p>
            </html>
        """.trimIndent())
        
        messagePanel.add(messageText, BorderLayout.NORTH)
        panel.add(messagePanel, BorderLayout.CENTER)

        return panel
    }

    private fun addFields() {
        val (labelConstraints, fieldConstraints) = configureGrid()

        addPanelSideSettings(labelConstraints, fieldConstraints)
        addCustomColorSettings(labelConstraints, fieldConstraints)
        addTitleNumberingSettings(labelConstraints, fieldConstraints)
        addCustomTitleSettings(labelConstraints, fieldConstraints)
        addGlobalCustomTitleSettings(labelConstraints, fieldConstraints)
    }

    private fun configureColorPreview() {
        colorPreview.preferredSize = Dimension(24, 24)
        colorPreview.border = BorderFactory.createLineBorder(Color.DARK_GRAY)
    }

    private fun bindActions() {
        chooseColorButton.addActionListener { chooseCustomColor() }
        dropperButton.addActionListener { showScreenColorPicker(this) }
        customColorCheckBox.addActionListener {
            syncEnabledState()
            syncPreview()
        }
    }

    private fun chooseCustomColor() {
        val chosen = JColorChooser.showDialog(
            panel,
            "Choose custom color",
            selectedColor ?: Color(0, 0, 255)
        )
        if (chosen != null) {
            selectedColor = chosen
            syncPreview()
        }
    }

    private fun addPanelSideSettings(
        gridBagConstraintsLabel: GridBagConstraints,
        gridBagConstraintsField: GridBagConstraints
    ) {
        form.add(JBLabel("Panel side:"), gridBagConstraintsLabel)
        form.add(sideCombo, gridBagConstraintsField)
    }

    private fun addCustomColorSettings(
        gridBagConstraintsLabel: GridBagConstraints,
        gridBagConstraintsField: GridBagConstraints
    ) {
        val colorRow = JPanel(FlowLayout(FlowLayout.LEFT, 8, 0)).apply {
            add(colorPreview)
            add(chooseColorButton)
            add(dropperButton)
        }

        gridBagConstraintsLabel.gridy = 1
        gridBagConstraintsField.gridy = 1
        form.add(JBLabel("Custom color:"), gridBagConstraintsLabel)
        form.add(colorRow, gridBagConstraintsField)

        dropperButton.toolTipText = "Pick a color from the screen"
        dropperButton.isFocusable = false

        gridBagConstraintsLabel.gridy = 2
        gridBagConstraintsField.gridy = 2
        form.add(customColorCheckBox, gridBagConstraintsField)

        gridBagConstraintsLabel.gridy = 3
        gridBagConstraintsField.gridy = 3
        form.add(JBLabel("Preview:"), gridBagConstraintsLabel)
        form.add(previewLabel, gridBagConstraintsField)
    }

    private fun addTitleNumberingSettings(
        gridBagConstraintsLabel: GridBagConstraints,
        gridBagConstraintsField: GridBagConstraints
    ) {
        gridBagConstraintsLabel.gridy = 4
        gridBagConstraintsField.gridy = 4
        form.add(JBLabel("Title numbering:"), gridBagConstraintsLabel)
        form.add(titleNumberingCheckBox, gridBagConstraintsField)
    }

    private fun addCustomTitleSettings(
        gridBagConstraintsLabel: GridBagConstraints,
        gridBagConstraintsField: GridBagConstraints
    ) {
        gridBagConstraintsLabel.gridy = 5
        gridBagConstraintsField.gridy = 5
        form.add(JBLabel("Custom title (this window):"), gridBagConstraintsLabel)
        form.add(customTitleTextField, gridBagConstraintsField)
        customTitleTextField.toolTipText =
            "Label shown in this window's title alongside the number (e.g. \"dattebayo\"). Toggle on/off in the Tool Window."
    }

    private fun addGlobalCustomTitleSettings(
        gridBagConstraintsLabel: GridBagConstraints,
        gridBagConstraintsField: GridBagConstraints
    ) {
        gridBagConstraintsLabel.gridy = 6
        gridBagConstraintsField.gridy = 6
        form.add(JBLabel("Custom title (all windows):"), gridBagConstraintsLabel)
        form.add(globalCustomTitleTextField, gridBagConstraintsField)
        globalCustomTitleTextField.toolTipText =
            "Label shown in ALL window titles (e.g. \"PERSONAL\" or \"CLIENT\"). Toggle on/off in the Tool Window."
    }

    override fun isModified(): Boolean {
        val panelSvc = windowPanelAppearanceStateService ?: return false
        val colorSvc = customColorSettings ?: return false
        val titleSvc = titleNumberingSettings ?: return false
        val customTitleSvc = customTitleSettings ?: return false
        val selectedSide = sideCombo.selectedItem as WindowPanelAppearanceStateService.Side
        val globalTitleChanged = globalCustomTitleSettings != null &&
                globalCustomTitleTextField.text.trim() != globalCustomTitleSettings!!.getGlobalCustomTitle()
        return selectedSide != panelSvc.getSide() ||
                customColorCheckBox.isSelected != colorSvc.isUseCustomColor() ||
                selectedColor?.rgb != colorSvc.getCustomColor()?.rgb ||
                titleNumberingCheckBox.isSelected != titleSvc.isTitleNumberingEnabled() ||
                customTitleTextField.text.trim() != customTitleSvc.getCustomTitle() ||
                globalTitleChanged
    }

    override fun apply() {
        updateSettingsFromUi() ?: return

        WindowColorApplier.applyToCurrentOpenProject(project)
        applyTitleNumberingChanges()
        applyCustomTitleChanges()
    }

    override fun reset() {
        syncFromSettings()
        syncEnabledState()
        syncPreview()
    }

    override fun disposeUIResources() {
        LOG.info("[Window Accent] WindowAccentSettings#disposeUIResources() called (project=${project.name})")

        // Null out plugin service references so that if IntelliJ's configurable cache retains
        // this instance after plugin unload, it no longer holds strong references to plugin
        // class instances that would prevent classloader garbage collection.
        windowPanelAppearanceStateService = null
        customColorSettings = null
        titleNumberingSettings = null
        customTitleSettings = null
        globalCustomTitleSettings = null

        // Clear Side enum values from the sideCombo model.
        // form.removeAll() removes sideCombo from its container but does NOT clear the
        // DefaultComboBoxModel. The model holds Side[] entries (plugin class instances),
        // creating the chain:
        //   IntelliJ configurable cache → WindowAccentSettings → sideCombo
        //     → DefaultComboBoxModel → Side[] → Side.class → PluginClassLoader
        // Calling removeAllItems() severs that chain.
        sideCombo.removeAllItems()

        // Remove ActionListeners from buttons — each listener is a plugin lambda capturing
        // `this` (a plugin class instance).  If IntelliJ retains this configurable instance
        // after plugin unload, these listeners would keep the classloader reachable via:
        //   IntelliJ → WindowAccentSettings → JButton → ActionListener lambda → PluginClassLoader
        chooseColorButton.actionListeners.toList().forEach { chooseColorButton.removeActionListener(it) }
        dropperButton.actionListeners.toList().forEach { dropperButton.removeActionListener(it) }
        customColorCheckBox.actionListeners.toList().forEach { customColorCheckBox.removeActionListener(it) }

        // Remove all Swing components from the panel hierarchy so no component subtree
        // is reachable from this instance via panel/form fields.
        panel.removeAll()
        form.removeAll()
    }

    private fun updateSettingsFromUi(): Unit? {
        val panelSvc = windowPanelAppearanceStateService ?: return null
        val colorSvc = customColorSettings ?: return null
        val titleSvc = titleNumberingSettings ?: return null
        val customTitleSvc = customTitleSettings ?: return null
        val globalCustomTitleSvc = globalCustomTitleSettings ?: return null
        panelSvc.setSide(sideCombo.selectedItem as WindowPanelAppearanceStateService.Side)
        colorSvc.setUseCustomColor(customColorCheckBox.isSelected)
        colorSvc.setCustomColor(if (customColorCheckBox.isSelected) selectedColor else null)
        titleSvc.setTitleNumberingEnabled(titleNumberingCheckBox.isSelected)
        customTitleSvc.setCustomTitle(customTitleTextField.text.trim())
        globalCustomTitleSvc.setGlobalCustomTitle(globalCustomTitleTextField.text.trim())
        return Unit
    }

    private fun applyTitleNumberingChanges() {
        WindowTitleApplier.applyToCurrentOpenProject(project, titleNumberingSettings?.isTitleNumberingEnabled() ?: return)
    }

    private fun applyCustomTitleChanges() {
        // Re-apply title so the updated custom title string is reflected immediately
        // (guards against no-op are inside WindowTitleApplier)
        WindowTitleApplier.applyToCurrentOpenProject(project)
    }

    private fun syncFromSettings() {
        sideCombo.selectedItem = windowPanelAppearanceStateService?.getSide()
        customColorCheckBox.isSelected = customColorSettings?.isUseCustomColor() ?: false
        selectedColor = customColorSettings?.getCustomColor()
        titleNumberingCheckBox.isSelected = titleNumberingSettings?.isTitleNumberingEnabled() ?: false
        customTitleTextField.text = customTitleSettings?.getCustomTitle() ?: ""
        globalCustomTitleTextField.text = globalCustomTitleSettings?.getGlobalCustomTitle() ?: ""
    }

    override fun setSelectedColor(color: Color?) {
        selectedColor = color
    }

    override fun syncEnabledState() {
        val customColorEnabled = customColorCheckBox.isSelected
        chooseColorButton.isEnabled = customColorEnabled
        dropperButton.isEnabled = customColorEnabled
        colorPreview.isEnabled = customColorEnabled
    }

    override fun syncPreview() {
        val color = if (customColorCheckBox.isSelected) selectedColor else null
        colorPreview.background = color ?: panel.background
        previewLabel.text = if (color == null) {
            "Auto-generated from project name"
        } else {
            "RGB: ${color.red}, ${color.green}, ${color.blue}"
        }
        colorPreview.repaint()
    }

    override fun configureGrid(): Pair<GridBagConstraints, GridBagConstraints> {
        val labelConstraints = GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            anchor = GridBagConstraints.WEST
            insets = Insets(4, 4, 4, 8)
        }

        val fieldConstraints = GridBagConstraints().apply {
            gridx = 1
            gridy = 0
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
            anchor = GridBagConstraints.WEST
            insets = Insets(4, 4, 4, 4)
        }

        return labelConstraints to fieldConstraints
    }

}