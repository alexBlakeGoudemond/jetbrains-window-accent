package com.window_accent.configuration.settings

import com.window_accent.diagnostic.windowAccentLogger
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.image.BufferedImage
import javax.swing.*

private val logger = windowAccentLogger<ScreenColorPicker>()

internal class ScreenColorPicker(private val settings: IWindowAccentSettings) {

    internal lateinit var mousePoint: Point
    private var displayX: Double = 0.0
    private var displayY: Double = 0.0
    private lateinit var magnifierCanvas: JComponent
    private lateinit var overlay: JDialog
    private lateinit var colorSelectionHandler: (Boolean) -> Unit


    fun pickColor(owner: Window, screenshot: BufferedImage, captureArea: Rectangle) {
        mousePoint = Point(captureArea.width / 2, captureArea.height / 2)
        displayX = mousePoint.x.toDouble()
        displayY = mousePoint.y.toDouble()

        magnifierCanvas = createMagnifierCanvasWithCaptureArea(screenshot, captureArea, { displayX to displayY }) { mousePoint }
        magnifierCanvas.cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)
        magnifierCanvas.isFocusable = true

        overlay = createOverlay(owner, captureArea)
        colorSelectionHandler = createColorSelectionHandler(screenshot, overlay)
        overlay.contentPane = magnifierCanvas
        overlay.isVisible = true

        magnifierCanvas.addMouseMotionListener(createMouseMotionHandler(captureArea, screenshot))
        magnifierCanvas.addMouseListener(createMousePressedHandler())
        magnifierCanvas.requestFocusInWindow()

        setupEscapeKeyHandler()
    }

    internal fun createOverlay(owner: Window, captureArea: Rectangle): JDialog =
        JDialog(owner, Dialog.ModalityType.MODELESS).apply {
            isUndecorated = true
            isAlwaysOnTop = true
            background = Color(0, 0, 0, 0)
            defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
            bounds = captureArea
        }

    internal fun createColorSelectionHandler(screenshot: BufferedImage, overlay: JDialog): (Boolean) -> Unit =
        { applySelection ->
            try {
                if (applySelection) {
                    val mx = mousePoint.x.coerceIn(0, screenshot.width - 1)
                    val my = mousePoint.y.coerceIn(0, screenshot.height - 1)
                    val picked = screenshot.getRGB(mx, my)
                    settings.setSelectedColor(Color(picked, true))
                    settings.getCustomColorCheckBox().isSelected = true
                    settings.syncEnabledState()
                    settings.syncPreview()
                }
            } finally {
                overlay.dispose()
            }
        }

    private fun createMouseMotionHandler(captureArea: Rectangle, screenshot: BufferedImage): MouseMotionAdapter =
        object : MouseMotionAdapter() {
            override fun mouseMoved(e: MouseEvent) {
                mousePoint.setLocation(e.x, e.y)
                displayX += (mousePoint.x - displayX) * 0.22
                displayY += (mousePoint.y - displayY) * 0.22
                magnifierCanvas.repaint()
            }

            override fun mouseDragged(e: MouseEvent) {
                mouseMoved(e)
            }
        }

    private fun createMousePressedHandler(): MouseAdapter =
        object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                mousePoint.setLocation(e.x, e.y)
                colorSelectionHandler(true)
            }
        }

    private fun setupEscapeKeyHandler() {
        overlay.rootPane.registerKeyboardAction(
            { colorSelectionHandler(false) },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        )
    }
}

fun showScreenColorPicker(windowAccentSettings: IWindowAccentSettings) {
    val owner = SwingUtilities.getWindowAncestor(windowAccentSettings.getPanel()) ?: return

    val captureBounds = calculateScreenBoundsAcrossMultipleScreens()
    if (captureBounds.isEmpty) return

    try {
        showColorChooserViaFullScreenScreenshot(captureBounds, owner, windowAccentSettings)
    } catch (e: Exception) {
        logger.info("unable to capture screenshot: ${e.message}")
    }
}

private fun calculateScreenBoundsAcrossMultipleScreens(): Rectangle {
    val screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices
    return screenDevices
        .map { it.defaultConfiguration.bounds }
        .fold(Rectangle()) { acc, bounds ->
            if (acc.isEmpty) Rectangle(bounds) else acc.union(bounds)
        }
}

fun showColorChooserViaFullScreenScreenshot(
    captureBounds: Rectangle,
    owner: Window,
    windowAccentSettings: IWindowAccentSettings
) {
    val screenshot = takeScreenshot(captureBounds)
    setupColorPickerUI(owner, screenshot, captureBounds, windowAccentSettings)
}

fun takeScreenshot(captureRect: Rectangle): BufferedImage {
    if (captureRect.width <= 0 || captureRect.height <= 0) {
        throw IllegalArgumentException("Invalid capture rectangle: $captureRect")
    }
    try {
        val robot = Robot()
        val screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices
        if (screenDevices.size <= 1) {
            return robot.createScreenCapture(captureRect)
        }

        val boundsByScreen = screenDevices
            .map { it.defaultConfiguration.bounds }
            .filter { it.intersects(captureRect) }
            .sortedBy { it.x }

        val minY = boundsByScreen.minOfOrNull { it.y } ?: captureRect.y
        val totalWidth = boundsByScreen.sumOf { it.width }.coerceAtLeast(captureRect.width)
        val totalHeight = boundsByScreen.maxOfOrNull { it.y - minY + it.height }?.coerceAtLeast(captureRect.height) ?: captureRect.height
        val screenshot = BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB)
        val graphics = screenshot.createGraphics()
        try {
            var currentX = 0
            for (screenBounds in boundsByScreen) {
                val clippedBounds = screenBounds.intersection(captureRect)
                if (clippedBounds.isEmpty) continue

                val screenCapture = robot.createScreenCapture(clippedBounds)
                graphics.drawImage(screenCapture, currentX, clippedBounds.y - minY, null)
                currentX += clippedBounds.width
            }
        } finally {
            graphics.dispose()
        }
        return screenshot
    } catch (e: Exception) {
        throw RuntimeException("Failed to capture screenshot: ${e.message}", e)
    }
}

private fun setupColorPickerUI(
    owner: Window,
    screenshot: BufferedImage,
    captureArea: Rectangle,
    windowAccentSettings: IWindowAccentSettings
) {
    val picker = ScreenColorPicker(windowAccentSettings)
    picker.pickColor(owner, screenshot, captureArea)
}
