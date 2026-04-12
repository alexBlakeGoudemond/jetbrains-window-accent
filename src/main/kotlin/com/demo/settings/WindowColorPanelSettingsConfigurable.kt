package com.demo.settings

import com.demo.window_color.WindowColorApplier
import com.demo.window_color.WindowColorSettings
import com.demo.window_title.WindowTitleApplier
import com.intellij.icons.AllIcons
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.components.JBLabel
import java.awt.*
import javax.swing.*

class WindowColorPanelSettingsConfigurable(
    private val project: Project
) : Configurable {

    val panel = JPanel(BorderLayout())

    private val settings = project.getService(WindowColorSettings::class.java)
    private val form = JPanel(GridBagLayout())

    private val sideCombo = JComboBox(WindowColorSettings.Side.entries.toTypedArray())
    val customColorCheckBox = JCheckBox("Use custom color")
    private val titleNumberingCheckBox = JCheckBox("Enable custom title numbering")
    private val colorPreview = JPanel()
    private val chooseColorButton = JButton("Choose color")
    private val dropperButton = JButton(AllIcons.Actions.Colors)
    private val previewLabel = JLabel("")

    var selectedColor: Color? = null

    override fun getDisplayName(): String = "Window Color Panel"

    override fun createComponent(): JComponent {

        val gridBagConstraintsLabel = GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            anchor = GridBagConstraints.WEST
            insets = Insets(4, 4, 4, 8)
        }

        val gridBagConstraintsField = GridBagConstraints().apply {
            gridx = 1
            gridy = 0
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
            anchor = GridBagConstraints.WEST
            insets = Insets(4, 4, 4, 4)
        }

        setupPanelSideSettings(gridBagConstraintsLabel, gridBagConstraintsField)
        setupCustomColorSettings(gridBagConstraintsLabel, gridBagConstraintsField)
        setupTitleNumberingSettings(gridBagConstraintsLabel, gridBagConstraintsField)

        colorPreview.preferredSize = Dimension(24, 24)
        colorPreview.border = BorderFactory.createLineBorder(Color.DARK_GRAY)

        panel.add(form, BorderLayout.NORTH)

        updateFromSettings()
        updateEnabledState()
        updatePreview()

        return panel
    }

    private fun setupPanelSideSettings(
        gridBagConstraintsLabel: GridBagConstraints,
        gridBagConstraintsField: GridBagConstraints
    ) {
        form.add(JBLabel("Panel side:"), gridBagConstraintsLabel)
        form.add(sideCombo, gridBagConstraintsField)
    }

    private fun setupCustomColorSettings(
        gridBagConstraintsLabel: GridBagConstraints,
        gridBagConstraintsField: GridBagConstraints
    ) {
        val colorRow = JPanel(FlowLayout(FlowLayout.LEFT, 8, 0)).apply {
            add(colorPreview)
            add(chooseColorButton)
            add(dropperButton)
        }
        chooseColorButton.addActionListener {
            val chosen = JColorChooser.showDialog(
                panel,
                "Choose custom color",
                selectedColor ?: Color(0, 0, 255)
            )
            if (chosen != null) {
                selectedColor = chosen
                updatePreview()
            }
        }
        dropperButton.addActionListener {
            pickColorFromScreenAsync(this)
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
        customColorCheckBox.addActionListener {
            updateEnabledState()
            updatePreview()
        }
        gridBagConstraintsLabel.gridy = 3
        gridBagConstraintsField.gridy = 3
        form.add(JBLabel("Preview:"), gridBagConstraintsLabel)
        form.add(previewLabel, gridBagConstraintsField)
    }

    private fun setupTitleNumberingSettings(
        gridBagConstraintsLabel: GridBagConstraints,
        gridBagConstraintsField: GridBagConstraints
    ) {
        gridBagConstraintsLabel.gridy = 4
        gridBagConstraintsField.gridy = 4
        form.add(JBLabel("Title numbering:"), gridBagConstraintsLabel)
        form.add(titleNumberingCheckBox, gridBagConstraintsField)
    }

    override fun isModified(): Boolean {
        val selectedSide = sideCombo.selectedItem as WindowColorSettings.Side
        return selectedSide != settings.getSide() ||
                customColorCheckBox.isSelected != settings.isUseCustomColor() ||
                selectedColor?.rgb != settings.getCustomColor()?.rgb ||
                titleNumberingCheckBox.isSelected != settings.isTitleNumberingEnabled()
    }

    override fun apply() {
        settings.setSide(sideCombo.selectedItem as WindowColorSettings.Side)
        settings.setUseCustomColor(customColorCheckBox.isSelected)
        settings.setCustomColor(if (customColorCheckBox.isSelected) selectedColor else null)
        settings.setTitleNumberingEnabled(titleNumberingCheckBox.isSelected)

        WindowColorApplier.applyToCurrentOpenProject(project)
        if (settings.isTitleNumberingEnabled()) {
            WindowTitleApplier.applyToCurrentOpenProject(project, true)
        } else {
            WindowTitleApplier.removeFromAllOpenProjects()
        }

        ProjectManager.getInstance().openProjects.forEach { openProject ->
            openProject.getService(WindowColorSettings::class.java).apply {
                setSide(settings.getSide())
                setUseCustomColor(settings.isUseCustomColor())
                setCustomColor(settings.getCustomColor())
                setTitleNumberingEnabled(settings.isTitleNumberingEnabled())
                setPanelEnabled(settings.panelIsEnabled())
            }
        }
    }

    override fun reset() {
        updateFromSettings()
        updateEnabledState()
        updatePreview()
    }

    override fun disposeUIResources() {
        // no-op
    }

    private fun updateFromSettings() {
        sideCombo.selectedItem = settings.getSide()
        customColorCheckBox.isSelected = settings.isUseCustomColor()
        selectedColor = settings.getCustomColor()
        titleNumberingCheckBox.isSelected = settings.isTitleNumberingEnabled()
    }

    fun updateEnabledState() {
        chooseColorButton.isEnabled = customColorCheckBox.isSelected
        dropperButton.isEnabled = customColorCheckBox.isSelected
        colorPreview.isEnabled = customColorCheckBox.isSelected
    }

    fun updatePreview() {
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