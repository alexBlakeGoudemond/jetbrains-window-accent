package com.window_accent.configuration.settings

import com.intellij.icons.AllIcons
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBLabel
import org.jetbrains.annotations.VisibleForTesting
import com.window_accent.configuration.persistence.WindowCustomTitleStateService
import com.window_accent.configuration.persistence.WindowCustomColorStateService
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService
import com.window_accent.configuration.persistence.WindowTitleNumberingStateService
import com.window_accent.feature.window_color.WindowColorApplier
import com.window_accent.feature.window_title.WindowTitleApplier
import java.awt.*
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
}

open class WindowAccentSettings(
    private val project: Project
) : Configurable, IWindowAccentSettings {

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

    private val form = JPanel(GridBagLayout())

    private val sideCombo = JComboBox(WindowPanelAppearanceStateService.Side.entries.toTypedArray())
    @get:VisibleForTesting
    internal val customColorCheckBox = JCheckBox("Use custom color")
    private val titleNumberingCheckBox = JCheckBox("Enable custom title numbering")
    private val customTitleTextField = JTextField()
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
        panel.add(form, BorderLayout.NORTH)

        configureGrid()
        addFields()
        configureColorPreview()
        bindActions()

        syncFromSettings()
        syncEnabledState()
        syncPreview()

        return panel
    }

    private fun configureGrid(): Pair<GridBagConstraints, GridBagConstraints> {
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

    private fun addFields() {
        val (labelConstraints, fieldConstraints) = configureGrid()

        addPanelSideSettings(labelConstraints, fieldConstraints)
        addCustomColorSettings(labelConstraints, fieldConstraints)
        addTitleNumberingSettings(labelConstraints, fieldConstraints)
        addCustomTitleSettings(labelConstraints, fieldConstraints)
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
        form.add(JBLabel("Custom window title:"), gridBagConstraintsLabel)
        form.add(customTitleTextField, gridBagConstraintsField)
        customTitleTextField.toolTipText =
            "Label shown in the window title alongside the number (e.g. \"dattebayo\"). Toggle on/off in the Tool Window."
    }

    override fun isModified(): Boolean {
        val panelSvc = windowPanelAppearanceStateService ?: return false
        val colorSvc = customColorSettings ?: return false
        val titleSvc = titleNumberingSettings ?: return false
        val customTitleSvc = customTitleSettings ?: return false
        val selectedSide = sideCombo.selectedItem as WindowPanelAppearanceStateService.Side
        return selectedSide != panelSvc.getSide() ||
                customColorCheckBox.isSelected != colorSvc.isUseCustomColor() ||
                selectedColor?.rgb != colorSvc.getCustomColor()?.rgb ||
                titleNumberingCheckBox.isSelected != titleSvc.isTitleNumberingEnabled() ||
                customTitleTextField.text.trim() != customTitleSvc.getCustomTitle()
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
        // Null out plugin service references so that if IntelliJ's configurable cache retains
        // this instance after plugin unload, it no longer holds strong references to plugin
        // class instances that would prevent classloader garbage collection.
        windowPanelAppearanceStateService = null
        customColorSettings = null
        titleNumberingSettings = null
        customTitleSettings = null
    }

    private fun updateSettingsFromUi(): Unit? {
        val panelSvc = windowPanelAppearanceStateService ?: return null
        val colorSvc = customColorSettings ?: return null
        val titleSvc = titleNumberingSettings ?: return null
        val customTitleSvc = customTitleSettings ?: return null
        panelSvc.setSide(sideCombo.selectedItem as WindowPanelAppearanceStateService.Side)
        colorSvc.setUseCustomColor(customColorCheckBox.isSelected)
        colorSvc.setCustomColor(if (customColorCheckBox.isSelected) selectedColor else null)
        titleSvc.setTitleNumberingEnabled(titleNumberingCheckBox.isSelected)
        customTitleSvc.setCustomTitle(customTitleTextField.text.trim())
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
}