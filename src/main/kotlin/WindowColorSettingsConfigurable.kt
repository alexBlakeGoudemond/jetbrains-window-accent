package com.demo

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBComboBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.fields.ColorPanel
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JComponent
import javax.swing.JPanel

class WindowColorSettingsConfigurable(
    private val project: Project
) : Configurable {

    private val settings = project.getService(WindowColorSettings::class.java)

    private val panel = JPanel(BorderLayout())
    private val form = JPanel(GridBagLayout())

    private val sideCombo = JBComboBox(WindowColorSettings.Side.entries.toTypedArray())
    private val customColorCheckBox = JBCheckBox("Use custom color")
    private val colorPanel = ColorPanel()
    private val previewLabel = JBLabel("")

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
        form.add(customColorCheckBox, gbcField)

        gbcLabel.gridy = 2
        gbcField.gridy = 2
        form.add(JBLabel("Custom color:"), gbcLabel)
        form.add(colorPanel, gbcField)

        gbcLabel.gridy = 3
        gbcField.gridy = 3
        form.add(JBLabel("Preview:"), gbcLabel)
        form.add(previewLabel, gbcField)

        panel.add(form, BorderLayout.NORTH)

        customColorCheckBox.addActionListener {
            updateEnabledState()
            updatePreview()
        }

        colorPanel.addActionListener { updatePreview() }

        updateFromSettings()
        updateEnabledState()
        updatePreview()

        return panel
    }

    override fun isModified(): Boolean {
        val selectedSide = sideCombo.selectedItem as WindowColorSettings.Side
        val currentColorRgb = colorPanel.color?.rgb
        return selectedSide != settings.getSide() ||
            customColorCheckBox.isSelected != settings.isUseCustomColor() ||
            currentColorRgb != settings.getCustomColor()?.rgb
    }

    override fun apply() {
        settings.setSide(sideCombo.selectedItem as WindowColorSettings.Side)
        settings.setUseCustomColor(customColorCheckBox.isSelected)
        settings.setCustomColor(if (customColorCheckBox.isSelected) colorPanel.color else null)
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
        colorPanel.color = settings.getCustomColor()
    }

    private fun updateEnabledState() {
        colorPanel.isEnabled = customColorCheckBox.isSelected
    }

    private fun updatePreview() {
        val color = if (customColorCheckBox.isSelected) colorPanel.color else null
        previewLabel.text = when {
            color == null -> "Auto-generated from project name"
            else -> "RGB: ${color.red}, ${color.green}, ${color.blue}"
        }
    }
}
