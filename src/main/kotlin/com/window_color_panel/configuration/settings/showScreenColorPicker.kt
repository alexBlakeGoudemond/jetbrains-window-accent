package com.window_color_panel.configuration.settings

import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.image.BufferedImage
import javax.swing.*

// TODO BlakeGoudemond 2026/05/07 | ask robot to review if my refactor was any good
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

    val magnifierCanvas = createMagnifierCanvas(screenshot) { displayMousePoint.x to displayMousePoint.y }
    magnifierCanvas.cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)
    magnifierCanvas.isFocusable = true

    val overlayHandler = initialMagnifyingGlassOverlay(owner, screenSize)
    val colorSelectionHandler: (Boolean) -> Unit =
        colorSelectionOverlayHandler(mousePoint, screenshot, windowColorPanelSettings, overlayHandler)
    overlayHandler.contentPane = magnifierCanvas
    overlayHandler.isVisible = true

    // TODO BlakeGoudemond 2026/05/08 | extract outside this function - make standalone
    fun mouseMotionLocationHandler(): MouseMotionAdapter = object : MouseMotionAdapter() {
        override fun mouseMoved(e: MouseEvent) {
            mousePoint.setLocation(e.x, e.y)
            displayMousePoint.x += (mousePoint.x - displayMousePoint.x) * 0.22
            displayMousePoint.y += (mousePoint.y - displayMousePoint.y) * 0.22
            magnifierCanvas.repaint()
        }

        override fun mouseDragged(e: MouseEvent) {
            mouseMoved(e)
        }
    }

    // TODO BlakeGoudemond 2026/05/08 | extract outside this function - make standalone
    fun onMousePressedSetLocation(): MouseAdapter = object : MouseAdapter() {
        override fun mousePressed(e: MouseEvent) {
            mousePoint.setLocation(e.x, e.y)
            colorSelectionHandler(true)
        }
    }

    // TODO BlakeGoudemond 2026/05/08 | extract outside this function - make standalone
    fun magnifyAgainOnClose() {
        overlayHandler.rootPane.registerKeyboardAction(
            {
                colorSelectionHandler(false)
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        )
    }

    magnifierCanvas.addMouseMotionListener(mouseMotionLocationHandler())
    magnifierCanvas.addMouseListener(onMousePressedSetLocation())
    magnifierCanvas.requestFocusInWindow()

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

fun colorSelectionOverlayHandler(
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