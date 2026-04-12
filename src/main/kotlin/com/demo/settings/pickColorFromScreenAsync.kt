package com.demo.settings

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Cursor
import java.awt.Dialog
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.Robot
import java.awt.Toolkit
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import javax.swing.JComponent
import javax.swing.JDialog
import javax.swing.KeyStroke
import javax.swing.SwingUtilities
import javax.swing.WindowConstants
import kotlin.math.roundToInt

fun pickColorFromScreenAsync(windowColorPanelSettingsConfigurable: WindowColorPanelSettingsConfigurable) {
    val owner = SwingUtilities.getWindowAncestor(windowColorPanelSettingsConfigurable.panel) ?: return

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

    // TODO BlakeGoudemond 2026/04/12 | refactor?
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
                windowColorPanelSettingsConfigurable.selectedColor = Color(picked, true)
                windowColorPanelSettingsConfigurable.customColorCheckBox.isSelected = true
                windowColorPanelSettingsConfigurable.updateEnabledState()
                windowColorPanelSettingsConfigurable.updatePreview()
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