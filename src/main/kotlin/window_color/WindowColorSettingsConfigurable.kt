package com.demo.window_color

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBLabel
import java.awt.*
import javax.swing.*
// TODO Consider splitting into separate classes
class WindowColorSettingsConfigurable(
    private val project: Project
) : Configurable {

    private val settings = project.getService(WindowColorSettings::class.java)

    private val panel = JPanel(BorderLayout())
    private val form = JPanel(GridBagLayout())

    private val sideCombo = JComboBox(WindowColorSettings.Side.entries.toTypedArray())
    private val customColorCheckBox = JCheckBox("Use custom color")
    private val colorPreview = JPanel()
    private val chooseColorButton = JButton("Choose color")
    private val previewLabel = JLabel("")

    private var selectedColor: Color? = null

    override fun getDisplayName(): String = "Window Color Panel"

    override fun createComponent(): JComponent {
        val gbcLabel = GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            anchor = GridBagConstraints.WEST
            insets = Insets(4, 4, 4, 8)
        }

        val gbcField = GridBagConstraints().apply {
            gridx = 1
            gridy = 0
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
            anchor = GridBagConstraints.WEST
            insets = Insets(4, 4, 4, 4)
        }

        form.add(JBLabel("Panel side:"), gbcLabel)
        form.add(sideCombo, gbcField)

        gbcLabel.gridy = 1
        gbcField.gridy = 1
        form.add(JBLabel("Custom color:"), gbcLabel)

        val colorRow = JPanel(FlowLayout(FlowLayout.LEFT, 8, 0)).apply {
            add(colorPreview)
            add(chooseColorButton)
        }
        form.add(colorRow, gbcField)

        gbcLabel.gridy = 2
        gbcField.gridy = 2
        form.add(customColorCheckBox, gbcField)

        gbcLabel.gridy = 3
        gbcField.gridy = 3
        form.add(JBLabel("Preview:"), gbcLabel)
        form.add(previewLabel, gbcField)

        colorPreview.preferredSize = Dimension(24, 24)
        colorPreview.border = BorderFactory.createLineBorder(Color.DARK_GRAY)

        panel.add(form, BorderLayout.NORTH)

        customColorCheckBox.addActionListener {
            updateEnabledState()
            updatePreview()
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

        updateFromSettings()
        updateEnabledState()
        updatePreview()

        return panel
    }

    override fun isModified(): Boolean {
        val selectedSide = sideCombo.selectedItem as WindowColorSettings.Side
        return selectedSide != settings.getSide() ||
            customColorCheckBox.isSelected != settings.isUseCustomColor() ||
            selectedColor?.rgb != settings.getCustomColor()?.rgb
    }

    override fun apply() {
        settings.setSide(sideCombo.selectedItem as WindowColorSettings.Side)
        settings.setUseCustomColor(customColorCheckBox.isSelected)
        settings.setCustomColor(if (customColorCheckBox.isSelected) selectedColor else null)
        WindowColorApplier.apply(project)
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
    }

    private fun updateEnabledState() {
        chooseColorButton.isEnabled = customColorCheckBox.isSelected
        colorPreview.isEnabled = customColorCheckBox.isSelected
    }

    private fun updatePreview() {
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
