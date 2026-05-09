package com.window_color_panel.configuration.settings

import com.intellij.openapi.fileEditor.FileEditorManager
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.image.BufferedImage
import javax.swing.*

internal class ScreenColorPicker(private val settings: WindowColorPanelSettings) {

    internal lateinit var mousePoint: Point
    private var displayX: Double = 0.0
    private var displayY: Double = 0.0
    private lateinit var magnifierCanvas: JComponent
    private lateinit var overlay: JDialog
    private lateinit var colorSelectionHandler: (Boolean) -> Unit

    fun pickColor(owner: Window, screenshot: BufferedImage, captureArea: Dimension) {
        mousePoint = Point(captureArea.width / 2, captureArea.height / 2)
        displayX = mousePoint.x.toDouble()
        displayY = mousePoint.y.toDouble()

        magnifierCanvas = createMagnifierCanvas(screenshot) { displayX to displayY }
        magnifierCanvas.cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)
        magnifierCanvas.isFocusable = true

        overlay = createOverlay(owner, captureArea)
        colorSelectionHandler = createColorSelectionHandler(screenshot, overlay)
        overlay.contentPane = magnifierCanvas
        overlay.isVisible = true

        magnifierCanvas.addMouseMotionListener(createMouseMotionHandler())
        magnifierCanvas.addMouseListener(createMousePressedHandler())
        magnifierCanvas.requestFocusInWindow()

        setupEscapeKeyHandler()
    }

    internal fun createOverlay(owner: Window, captureArea: Dimension): JDialog =
        JDialog(owner, Dialog.ModalityType.MODELESS).apply {
            isUndecorated = true
            isAlwaysOnTop = true
            background = Color(0, 0, 0, 0)
            defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
            size = captureArea
            setLocation(0, 0)
        }

    internal fun createColorSelectionHandler(screenshot: BufferedImage, overlay: JDialog): (Boolean) -> Unit =
        { applySelection ->
            try {
                if (applySelection) {
                    val mx = mousePoint.x.coerceIn(0, screenshot.width - 1)
                    val my = mousePoint.y.coerceIn(0, screenshot.height - 1)
                    val picked = screenshot.getRGB(mx, my)
                    settings.selectedColor = Color(picked, true)
                    settings.customColorCheckBox.isSelected = true
                    settings.syncEnabledState()
                    settings.syncPreview()
                }
            } finally {
                overlay.dispose()
            }
        }

    private fun createMouseMotionHandler(): MouseMotionAdapter =
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

fun showScreenColorPicker(windowColorPanelSettings: WindowColorPanelSettings) {
    val owner = SwingUtilities.getWindowAncestor(windowColorPanelSettings.panel) ?: return

    val screenSize = Toolkit.getDefaultToolkit().screenSize
    if (screenSize.width <= 0 || screenSize.height <= 0) return

    // Prefer captureComponent (sandbox-safe) over Robot-based screenshot
    val editorComponent = FileEditorManager.getInstance(windowColorPanelSettings.getProject())
        .selectedTextEditor
        ?.component
    if (editorComponent != null) {
        showColorChooserViaGraphicsComponent(editorComponent, owner, windowColorPanelSettings)
    } else {
        // Fallback to full screen capture if no editor is available
        try {
            return showColorChooserViaScreenshot(screenSize, owner, windowColorPanelSettings)
        } catch (_: Exception) {
            return
        }
    }
}

fun showColorChooserViaScreenshot(
    screenSize: Dimension,
    owner: Window,
    windowColorPanelSettings: WindowColorPanelSettings
) {
    val screenshot = takeScreenshot(screenSize)
    setupColorPickerUI(owner, screenshot, screenSize, windowColorPanelSettings)
}

fun showColorChooserViaGraphicsComponent(
    editorComponent: JComponent,
    owner: Window,
    windowColorPanelSettings: WindowColorPanelSettings
) {
    val screenshot = captureComponent(editorComponent)
    val componentSize = Dimension(editorComponent.width, editorComponent.height)
    setupColorPickerUI(owner, screenshot, componentSize, windowColorPanelSettings)
}

// Note: This uses Robot
fun takeScreenshot(screenSize: Dimension): BufferedImage {
    // Note: Robot usage may be restricted in JetBrains plugin sandbox. Ensure permissions are granted.
    if (screenSize.width <= 0 || screenSize.height <= 0) {
        throw IllegalArgumentException("Invalid screen size: ${screenSize.width}x${screenSize.height}")
    }
    try {
        val robot = Robot()
        val screenshot = robot.createScreenCapture(Rectangle(screenSize))
        return screenshot
    } catch (e: Exception) {
        throw RuntimeException("Failed to capture screenshot: ${e.message}", e)
    }
}

fun captureComponent(component: JComponent): BufferedImage {
    if (component.width <= 0 || component.height <= 0) {
        throw IllegalArgumentException("Component has invalid dimensions: ${component.width}x${component.height}")
    }

    val image = BufferedImage(
        component.width,
        component.height,
        BufferedImage.TYPE_INT_ARGB
    )

    val graphics = image.createGraphics()
    try {
        component.paint(graphics)
    } finally {
        graphics.dispose()
    }

    return image
}

private fun setupColorPickerUI(
    owner: Window,
    screenshot: BufferedImage,
    captureArea: Dimension,
    windowColorPanelSettings: WindowColorPanelSettings
) {
    val picker = ScreenColorPicker(windowColorPanelSettings)
    picker.pickColor(owner, screenshot, captureArea)
}
