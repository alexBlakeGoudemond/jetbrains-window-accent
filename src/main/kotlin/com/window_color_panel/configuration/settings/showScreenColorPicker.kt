package com.window_color_panel.configuration.settings

import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.image.BufferedImage
import javax.swing.*

// TODO BlakeGoudemond 2026/05/06 | hard to test with code coverage, consider refactoring to separate the UI logic from the color picking logic
fun showScreenColorPicker(windowColorPanelSettings: WindowColorPanelSettings) {
    val owner = SwingUtilities.getWindowAncestor(windowColorPanelSettings.panel) ?: return

    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val screenshot = takeScreenshot(screenSize)

    val mousePoint = Point(screenSize.width / 2, screenSize.height / 2)
    val displayMousePoint = object {
        var x = mousePoint.x.toDouble()
        var y = mousePoint.y.toDouble()
    }

    val paintCanvas = createMagnifierCanvas(screenshot) { displayMousePoint.x to displayMousePoint.y }
    paintCanvas.cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)
    paintCanvas.isFocusable = true

    val overlay = initialMagnifyingGlassOverlay(owner, screenSize)
    val magnifyingOverlay: (Boolean) -> Unit =
        movingMagnifyingGlassOverlay(mousePoint, screenshot, windowColorPanelSettings, overlay)
    overlay.contentPane = paintCanvas
    overlay.isVisible = true

    fun onMouseMoveOrMouseDragSetLocation(): MouseMotionAdapter = object : MouseMotionAdapter() {
        override fun mouseMoved(e: MouseEvent) {
            mousePoint.setLocation(e.x, e.y)
            displayMousePoint.x += (mousePoint.x - displayMousePoint.x) * 0.22
            displayMousePoint.y += (mousePoint.y - displayMousePoint.y) * 0.22
            paintCanvas.repaint()
        }

        override fun mouseDragged(e: MouseEvent) {
            mouseMoved(e)
        }
    }

    fun onMousePressedSetLocation(): MouseAdapter = object : MouseAdapter() {
        override fun mousePressed(e: MouseEvent) {
            mousePoint.setLocation(e.x, e.y)
            magnifyingOverlay(true)
        }
    }

    fun magnifyAgainOnClose() {
        overlay.rootPane.registerKeyboardAction(
            {
                magnifyingOverlay(false)
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        )
    }

    paintCanvas.addMouseMotionListener(onMouseMoveOrMouseDragSetLocation())
    paintCanvas.addMouseListener(onMousePressedSetLocation())
    paintCanvas.requestFocusInWindow()

    magnifyAgainOnClose()
}

fun initialMagnifyingGlassOverlay(owner: Window, screenSize: Dimension?): JDialog =
    JDialog(owner, Dialog.ModalityType.MODELESS).apply {
        isUndecorated = true
        isAlwaysOnTop = true
        background = Color(0, 0, 0, 0)
        defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        size = screenSize
        setLocation(0, 0)
    }

fun movingMagnifyingGlassOverlay(
    mousePoint: Point,
    screenshot: BufferedImage,
    windowColorPanelSettings: WindowColorPanelSettings,
    overlay: JDialog
): (Boolean) -> Unit {
    val closeOverlay: (Boolean) -> Unit = { applySelection ->
        try {
            if (applySelection) {
                val mx = mousePoint.x.coerceIn(0, screenshot.width - 1)
                val my = mousePoint.y.coerceIn(0, screenshot.height - 1)
                val picked = screenshot.getRGB(mx, my)
                windowColorPanelSettings.selectedColor = Color(picked, true)
                windowColorPanelSettings.customColorCheckBox.isSelected = true
                windowColorPanelSettings.syncEnabledState()
                windowColorPanelSettings.syncPreview()
            }
        } finally {
            overlay.dispose()
        }
    }
    return closeOverlay
}

fun takeScreenshot(screenSize: Dimension): BufferedImage {
    val robot = Robot()
    val screenshot = robot.createScreenCapture(Rectangle(screenSize))
    return screenshot
}