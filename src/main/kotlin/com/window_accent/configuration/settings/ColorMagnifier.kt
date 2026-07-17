package com.window_accent.configuration.settings

import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.JComponent
import kotlin.math.roundToInt

fun toHex(color: Color): String = "#%02X%02X%02X".format(color.red, color.green, color.blue)

fun createMagnifierCanvas(
    screenshot: BufferedImage,
    displayMousePoint: () -> Pair<Double, Double>
) = object : JComponent() {

    val zoomRadius = 12
    val loupeSize = 180
    val loupeMargin = 24

    // The lens panel is painted outside the [0, loupeSize] box used for placement math:
    // the background glow starts 10px above/left of the lens (setupMagnifyingLens) and the
    // "Click to select..." caption extends up to 42px below it (setupColorSelectionPreview).
    // These constants describe that real drawn footprint so the placement clamps below
    // can keep the *entire* panel on-screen, not just the lens square itself.
    private val panelLeftTopExtra = 10
    private val panelRightExtra = 10
    private val panelBottomExtra = 42

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (width <= 0 || height <= 0) return // not yet laid out; nothing sane to paint

        val graphics = g as Graphics2D
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.drawImage(screenshot, 0, 0, width, height, null)

        graphics.color = Color(0, 0, 0, 60)
        graphics.fillRect(0, 0, width, height)
        // TODO Blake-Goudemond 20260717 | First pass of magnifying bugfix with second screen RHS - not yet working
        val (displayX, displayY) = displayMousePoint()

        // Coordinates for PLACING the loupe panel must be in this component's own
        // coordinate space (`width`/`height`), since that's the space the panel is
        // actually painted and clipped in.
        val mouseX = displayX.roundToInt().coerceIn(0, width - 1)
        val mouseY = displayY.roundToInt().coerceIn(0, height - 1)

        // Coordinates for SAMPLING the hovered color/zoom source must be in the
        // screenshot's own pixel space. `screenshot` is not guaranteed to be the same
        // pixel size as this component (e.g. a Robot capture taken at a different
        // per-monitor DPI scale than the overlay is rendered at), so mouseX/mouseY are
        // rescaled here rather than reused directly. Reusing the canvas-space mouseX/Y
        // to index into `screenshot` (as before) is exactly what let the panel's placement
        // math see an out-of-range coordinate and place the loupe outside the visible
        // canvas on multi-monitor setups with mismatched scaling.
        val scaleX = if (width > 0) screenshot.width.toDouble() / width else 1.0
        val scaleY = if (height > 0) screenshot.height.toDouble() / height else 1.0
        val sampleX = (mouseX * scaleX).roundToInt().coerceIn(0, screenshot.width - 1)
        val sampleY = (mouseY * scaleY).roundToInt().coerceIn(0, screenshot.height - 1)

        val magnifyingX = getMagnifyingX(mouseX)
        val magnifyingY = getMagnifyingY(mouseY)

        val hoveredColor = Color(screenshot.getRGB(sampleX, sampleY), true)

        setupMagnifyingLens(graphics, sampleX, sampleY, magnifyingX, magnifyingY)
        setupCursor(magnifyingX, magnifyingY, graphics)
        setupColorSelectionPreview(graphics, hoveredColor, magnifyingX, magnifyingY)
    }

    private fun getMagnifyingX(mouseX: Int): Int {
        val placeRight = mouseX + loupeSize + loupeMargin <= width
        val magnifyingX = if (placeRight) {
            (mouseX + loupeMargin).coerceAtMost(width - loupeSize - panelRightExtra)
        } else {
            (mouseX - loupeSize - loupeMargin).coerceAtLeast(panelLeftTopExtra)
        }
        // Final safety net: whatever branch was taken above, never let the panel's actual
        // painted bounds (magnifyingX - panelLeftTopExtra .. magnifyingX + loupeSize + panelRightExtra)
        // fall outside the component, regardless of how mouseX/width were computed.
        return magnifyingX.coerceIn(panelLeftTopExtra, (width - loupeSize - panelRightExtra).coerceAtLeast(panelLeftTopExtra))
    }

    private fun getMagnifyingY(mouseY: Int): Int {
        val placeBelow = mouseY + loupeSize + loupeMargin <= height
        val magnifyingY = if (placeBelow) {
            (mouseY + loupeMargin).coerceAtMost(height - loupeSize - panelBottomExtra)
        } else {
            (mouseY - loupeSize - loupeMargin).coerceAtLeast(panelLeftTopExtra)
        }
        // Same safety net as getMagnifyingX, but reserving panelBottomExtra (42px) below
        // the lens for the color-swatch + caption text, rather than the old, too-small 20px.
        return magnifyingY.coerceIn(panelLeftTopExtra, (height - loupeSize - panelBottomExtra).coerceAtLeast(panelLeftTopExtra))
    }

    private fun setupColorSelectionPreview(
        graphics: Graphics2D,
        hoveredColor: Color,
        magnifyingX: Int,
        magnifyingY: Int
    ) {
        graphics.color = hoveredColor
        graphics.fillRoundRect(magnifyingX + 14, magnifyingY + loupeSize - 44, 44, 24, 8, 8)
        graphics.color = Color.WHITE
        graphics.drawRoundRect(magnifyingX + 14, magnifyingY + loupeSize - 44, 44, 24, 8, 8)

        graphics.color = Color(255, 255, 255, 235)
        graphics.font = graphics.font.deriveFont(Font.BOLD, 12f)
        graphics.drawString("HEX: ${toHex(hoveredColor)}", magnifyingX + 68, magnifyingY + loupeSize - 28)
        graphics.font = graphics.font.deriveFont(Font.PLAIN, 12f)
        graphics.drawString(
            "RGB: ${hoveredColor.red}, ${hoveredColor.green}, ${hoveredColor.blue}",
            magnifyingX + 68,
            magnifyingY + loupeSize - 12
        )
        graphics.drawString("Click to select · Esc to cancel", magnifyingX + 18, magnifyingY + loupeSize + 20)
    }

    private fun setupMagnifyingLens(
        graphics: Graphics2D,
        mouseX: Int,
        mouseY: Int,
        magnifyingX: Int,
        magnifyingY: Int
    ) {
        val sourceX = (mouseX - zoomRadius).coerceIn(0, screenshot.width - zoomRadius * 2)
        val sourceY = (mouseY - zoomRadius).coerceIn(0, screenshot.height - zoomRadius * 2)
        val sourceSize = zoomRadius * 2

        graphics.color = Color(0, 0, 0, 120)
        graphics.fillRoundRect(magnifyingX - 10, magnifyingY - 10, loupeSize + 20, loupeSize + 52, 22, 22)

        graphics.color = Color(255, 255, 255, 25)
        graphics.fillRoundRect(magnifyingX - 8, magnifyingY - 8, loupeSize + 16, loupeSize + 48, 20, 20)

        graphics.drawImage(
            screenshot,
            magnifyingX,
            magnifyingY,
            magnifyingX + loupeSize,
            magnifyingY + loupeSize,
            sourceX,
            sourceY,
            sourceX + sourceSize,
            sourceY + sourceSize,
            null
        )

        graphics.color = Color(255, 255, 255, 230)
        graphics.stroke = BasicStroke(2.5f)
        graphics.drawRoundRect(magnifyingX, magnifyingY, loupeSize, loupeSize, 16, 16)
    }

    private fun setupCursor(magnifyingX: Int, magnifyingY: Int, graphics: Graphics2D) {
        val centerX = magnifyingX + loupeSize / 2
        val centerY = magnifyingY + loupeSize / 2
        graphics.color = Color(0, 0, 0, 140)
        graphics.stroke = BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        graphics.drawLine(centerX - 12, centerY, centerX + 12, centerY)
        graphics.drawLine(centerX, centerY - 12, centerX, centerY + 12)

        graphics.color = Color.WHITE
        graphics.stroke = BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        graphics.drawLine(centerX - 12, centerY, centerX + 12, centerY)
        graphics.drawLine(centerX, centerY - 12, centerX, centerY + 12)
    }

}