package com.demo.settings

import java.awt.Color
import java.awt.Cursor
import java.awt.Dialog
import java.awt.Point
import java.awt.Rectangle
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

fun pickColorFromScreenAsync(windowColorPanelSettingsConfigurable: WindowColorPanelSettingsConfigurable) {
    val owner = SwingUtilities.getWindowAncestor(windowColorPanelSettingsConfigurable.panel) ?: return

    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val robot = Robot()
    val screenshot = robot.createScreenCapture(Rectangle(screenSize))

    val mousePoint = Point(screenSize.width / 2, screenSize.height / 2)
    var displayX = mousePoint.x.toDouble()
    var displayY = mousePoint.y.toDouble()
    val displayAlpha = 0.22
    val paintCanvas = getPaintCanvas(screenshot, displayX, displayY)

    val overlay = JDialog(owner, Dialog.ModalityType.MODELESS).apply {
        isUndecorated = true
        isAlwaysOnTop = true
        background = Color(0, 0, 0, 0)
        defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        size = screenSize
        setLocation(0, 0)
    }

    paintCanvas.cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)
    paintCanvas.isFocusable = true

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

    paintCanvas.addMouseMotionListener(object : MouseMotionAdapter() {
        override fun mouseMoved(e: MouseEvent) {
            mousePoint.setLocation(e.x, e.y)
            displayX += (mousePoint.x - displayX) * displayAlpha
            displayY += (mousePoint.y - displayY) * displayAlpha
            paintCanvas.repaint()
        }

        override fun mouseDragged(e: MouseEvent) {
            mousePoint.setLocation(e.x, e.y)
            displayX += (mousePoint.x - displayX) * displayAlpha
            displayY += (mousePoint.y - displayY) * displayAlpha
            paintCanvas.repaint()
        }
    })

    paintCanvas.addMouseListener(object : MouseAdapter() {
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

    overlay.contentPane = paintCanvas
    overlay.isVisible = true
    paintCanvas.requestFocusInWindow()
}