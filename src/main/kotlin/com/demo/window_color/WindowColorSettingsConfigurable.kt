package com.demo.window_color

import com.demo.window_title.WindowTitleApplier
import com.intellij.icons.AllIcons
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.components.JBLabel
import java.awt.*
import java.awt.event.AWTEventListener
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import javax.swing.*
import kotlin.math.roundToInt

// TODO Consider splitting into separate classes
class WindowColorSettingsConfigurable(
    private val project: Project
) : Configurable {

    private val settings = project.getService(WindowColorSettings::class.java)

    private val panel = JPanel(BorderLayout())
    private val form = JPanel(GridBagLayout())

    private val sideCombo = JComboBox(WindowColorSettings.Side.entries.toTypedArray())
    private val customColorCheckBox = JCheckBox("Use custom color")
    private val titleNumberingCheckBox = JCheckBox("Enable custom title numbering")
    private val colorPreview = JPanel()
    private val chooseColorButton = JButton("Choose color")
    private val dropperButton = JButton(AllIcons.Actions.Colors)
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
            add(dropperButton)
        }
        form.add(colorRow, gbcField)

        gbcLabel.gridy = 2
        gbcField.gridy = 2
        form.add(customColorCheckBox, gbcField)

        gbcLabel.gridy = 3
        gbcField.gridy = 3
        form.add(JBLabel("Title numbering:"), gbcLabel)
        form.add(titleNumberingCheckBox, gbcField)

        gbcLabel.gridy = 4
        gbcField.gridy = 4
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

        dropperButton.toolTipText = "Pick a color from the screen"
        dropperButton.isFocusable = false
        dropperButton.addActionListener {
            pickColorFromScreenAsync()
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
            selectedColor?.rgb != settings.getCustomColor()?.rgb ||
            titleNumberingCheckBox.isSelected != settings.isTitleNumberingEnabled()
    }

    override fun apply() {
        settings.setSide(sideCombo.selectedItem as WindowColorSettings.Side)
        settings.setUseCustomColor(customColorCheckBox.isSelected)
        settings.setCustomColor(if (customColorCheckBox.isSelected) selectedColor else null)
        settings.setTitleNumberingEnabled(titleNumberingCheckBox.isSelected)

        WindowColorApplier.apply(project)

        if (settings.isTitleNumberingEnabled()) {
            WindowTitleApplier.applyToAllOpenProjects()
        } else {
            WindowTitleApplier.removeFromAllOpenProjects()
        }

        ProjectManager.getInstance().openProjects.forEach { openProject ->
            openProject.getService(WindowColorSettings::class.java)
                .setTitleNumberingEnabled(settings.isTitleNumberingEnabled())
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

    private fun updateEnabledState() {
        chooseColorButton.isEnabled = customColorCheckBox.isSelected
        dropperButton.isEnabled = customColorCheckBox.isSelected
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

    private fun pickColorFromScreenAsync() {
        val owner = SwingUtilities.getWindowAncestor(panel) ?: return

        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val robot = Robot()
        val screenshot = robot.createScreenCapture(Rectangle(screenSize))

        val zoomRadius = 12
        val loupeSize = 180
        val loupeMargin = 24

        val mousePoint = Point(screenSize.width / 2, screenSize.height / 2)
        var displayX = mousePoint.x.toDouble()
        var displayY = mousePoint.y.toDouble()
        val displayAlpha = 0.22

        fun toHex(color: Color): String = "#%02X%02X%02X".format(color.red, color.green, color.blue)

        val overlay = JDialog(owner, Dialog.ModalityType.MODELESS).apply {
            isUndecorated = true
            isAlwaysOnTop = true
            background = Color(0, 0, 0, 0)
            defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
            setSize(screenSize)
            setLocation(0, 0)
        }

        val canvas = object : JComponent() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                val g2 = g as Graphics2D
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

                g2.drawImage(screenshot, 0, 0, width, height, null)

                g2.color = Color(0, 0, 0, 60)
                g2.fillRect(0, 0, width, height)

                val mx = displayX.roundToInt().coerceIn(0, screenshot.width - 1)
                val my = displayY.roundToInt().coerceIn(0, screenshot.height - 1)

                val sourceX = (mx - zoomRadius).coerceIn(0, screenshot.width - zoomRadius * 2)
                val sourceY = (my - zoomRadius).coerceIn(0, screenshot.height - zoomRadius * 2)
                val sourceSize = zoomRadius * 2

                val placeRight = mx + loupeSize + loupeMargin <= width
                val placeBelow = my + loupeSize + loupeMargin <= height

                val loupeX = if (placeRight) {
                    (mx + loupeMargin).coerceAtMost(width - loupeSize - 20)
                } else {
                    (mx - loupeSize - loupeMargin).coerceAtLeast(20)
                }

                val loupeY = if (placeBelow) {
                    (my + loupeMargin).coerceAtMost(height - loupeSize - 20)
                } else {
                    (my - loupeSize - loupeMargin).coerceAtLeast(20)
                }

                val hoveredColor = Color(screenshot.getRGB(mx, my), true)

                g2.color = Color(0, 0, 0, 120)
                g2.fillRoundRect(loupeX - 10, loupeY - 10, loupeSize + 20, loupeSize + 52, 22, 22)

                g2.color = Color(255, 255, 255, 25)
                g2.fillRoundRect(loupeX - 8, loupeY - 8, loupeSize + 16, loupeSize + 48, 20, 20)

                g2.drawImage(
                    screenshot,
                    loupeX,
                    loupeY,
                    loupeX + loupeSize,
                    loupeY + loupeSize,
                    sourceX,
                    sourceY,
                    sourceX + sourceSize,
                    sourceY + sourceSize,
                    null
                )

                g2.color = Color(255, 255, 255, 230)
                g2.stroke = BasicStroke(2.5f)
                g2.drawRoundRect(loupeX, loupeY, loupeSize, loupeSize, 16, 16)

                val centerX = loupeX + loupeSize / 2
                val centerY = loupeY + loupeSize / 2
                g2.color = Color(0, 0, 0, 140)
                g2.stroke = BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
                g2.drawLine(centerX - 12, centerY, centerX + 12, centerY)
                g2.drawLine(centerX, centerY - 12, centerX, centerY + 12)

                g2.color = Color.WHITE
                g2.stroke = BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
                g2.drawLine(centerX - 12, centerY, centerX + 12, centerY)
                g2.drawLine(centerX, centerY - 12, centerX, centerY + 12)

                g2.color = hoveredColor
                g2.fillRoundRect(loupeX + 14, loupeY + loupeSize - 44, 44, 24, 8, 8)
                g2.color = Color.WHITE
                g2.drawRoundRect(loupeX + 14, loupeY + loupeSize - 44, 44, 24, 8, 8)

                g2.color = Color(255, 255, 255, 235)
                g2.font = g2.font.deriveFont(Font.BOLD, 12f)
                g2.drawString("HEX: ${toHex(hoveredColor)}", loupeX + 68, loupeY + loupeSize - 28)
                g2.font = g2.font.deriveFont(Font.PLAIN, 12f)
                g2.drawString(
                    "RGB: ${hoveredColor.red}, ${hoveredColor.green}, ${hoveredColor.blue}",
                    loupeX + 68,
                    loupeY + loupeSize - 12
                )
                g2.drawString("Click to select · Esc to cancel", loupeX + 18, loupeY + loupeSize + 20)
            }
        }

        canvas.cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)
        canvas.isFocusable = true

        val closeOverlay: (Boolean) -> Unit = { applySelection ->
            try {
                if (applySelection) {
                    val mx = mousePoint.x.coerceIn(0, screenshot.width - 1)
                    val my = mousePoint.y.coerceIn(0, screenshot.height - 1)
                    val picked = screenshot.getRGB(mx, my)
                    selectedColor = Color(picked, true)
                    customColorCheckBox.isSelected = true
                    updateEnabledState()
                    updatePreview()
                }
            } finally {
                overlay.dispose()
            }
        }

        canvas.addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseMoved(e: MouseEvent) {
                mousePoint.setLocation(e.x, e.y)
                displayX += (mousePoint.x - displayX) * displayAlpha
                displayY += (mousePoint.y - displayY) * displayAlpha
                canvas.repaint()
            }

            override fun mouseDragged(e: MouseEvent) {
                mousePoint.setLocation(e.x, e.y)
                displayX += (mousePoint.x - displayX) * displayAlpha
                displayY += (mousePoint.y - displayY) * displayAlpha
                canvas.repaint()
            }
        })

        canvas.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                mousePoint.setLocation(e.x, e.y)
                closeOverlay(true)
            }
        })

        overlay.rootPane.registerKeyboardAction(
            {
                closeOverlay(false)
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        )

        overlay.contentPane = canvas
        overlay.isVisible = true
        canvas.requestFocusInWindow()
    }

}
