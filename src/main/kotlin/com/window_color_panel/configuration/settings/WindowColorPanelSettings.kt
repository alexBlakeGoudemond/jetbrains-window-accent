package com.window_color_panel.configuration.settings

import com.intellij.icons.AllIcons
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBLabel
import com.window_color_panel.configuration.persistence.WindowCustomColorStateService
import com.window_color_panel.configuration.persistence.WindowPanelAppearanceStateService
import com.window_color_panel.configuration.persistence.WindowTitleNumberingStateService
import com.window_color_panel.feature.window_color.WindowColorApplier
import com.window_color_panel.feature.window_title.WindowTitleApplier
import java.awt.*
import javax.swing.*

/**
 * IntelliJ settings UI for configuring window color and title-numbering behavior.
 *
 * This component keeps the form state in sync with persisted project settings and
 * applies changes to the current IDE window when the user clicks Apply.
 */
class WindowColorPanelSettings(
    private val project: Project
) : Configurable {

    val panel = JPanel(BorderLayout())

    private val windowPanelAppearanceStateService = project.getService(WindowPanelAppearanceStateService::class.java)
    private val customColorSettings = project.getService(WindowCustomColorStateService::class.java)
    private val titleNumberingSettings = project.getService(WindowTitleNumberingStateService::class.java)

    private val form = JPanel(GridBagLayout())

    private val sideCombo = JComboBox(WindowPanelAppearanceStateService.Side.entries.toTypedArray())
    val customColorCheckBox = JCheckBox("Use custom color")
    private val titleNumberingCheckBox = JCheckBox("Enable custom title numbering")
    private val colorPreview = JPanel()
    private val chooseColorButton = JButton("Choose color")
    private val dropperButton = JButton(AllIcons.Actions.Colors)
    private val previewLabel = JLabel("")

    var selectedColor: Color? = null

    fun getProject(): Project = project

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

    override fun isModified(): Boolean {
        val selectedSide = sideCombo.selectedItem as WindowPanelAppearanceStateService.Side
        return selectedSide != windowPanelAppearanceStateService.getSide() ||
                customColorCheckBox.isSelected != customColorSettings.isUseCustomColor() ||
                selectedColor?.rgb != customColorSettings.getCustomColor()?.rgb ||
                titleNumberingCheckBox.isSelected != titleNumberingSettings.isTitleNumberingEnabled()
    }

    override fun apply() {
        updateSettingsFromUi()

        WindowColorApplier.applyToCurrentOpenProject(project)
        applyTitleNumberingChanges()
    }

    override fun reset() {
        syncFromSettings()
        syncEnabledState()
        syncPreview()
    }

    override fun disposeUIResources() {
        // no-op
    }

    private fun updateSettingsFromUi() {
        windowPanelAppearanceStateService.setSide(sideCombo.selectedItem as WindowPanelAppearanceStateService.Side)
        customColorSettings.setUseCustomColor(customColorCheckBox.isSelected)
        customColorSettings.setCustomColor(if (customColorCheckBox.isSelected) selectedColor else null)
        titleNumberingSettings.setTitleNumberingEnabled(titleNumberingCheckBox.isSelected)
    }

    private fun applyTitleNumberingChanges() {
        if (titleNumberingSettings.isTitleNumberingEnabled()) {
            WindowTitleApplier.applyToCurrentOpenProject(project, true)
        } else {
            WindowTitleApplier.removeFromAllOpenProjects()
        }
    }

    private fun syncFromSettings() {
        sideCombo.selectedItem = windowPanelAppearanceStateService.getSide()
        customColorCheckBox.isSelected = customColorSettings.isUseCustomColor()
        selectedColor = customColorSettings.getCustomColor()
        titleNumberingCheckBox.isSelected = titleNumberingSettings.isTitleNumberingEnabled()
    }

    fun syncEnabledState() {
        val customColorEnabled = customColorCheckBox.isSelected
        chooseColorButton.isEnabled = customColorEnabled
        dropperButton.isEnabled = customColorEnabled
        colorPreview.isEnabled = customColorEnabled
    }

    fun syncPreview() {
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