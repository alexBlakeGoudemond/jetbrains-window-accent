package com.window_color_panel.settings

import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.JComponent
import kotlin.math.roundToInt

fun getPaintCanvas(
    screenshot: BufferedImage,
    displayPoint: () -> Pair<Double, Double>
) = object : JComponent() {

    val zoomRadius = 12
    val loupeSize = 180
    val loupeMargin = 24

    fun toHex(color: Color): String = "#%02X%02X%02X".format(color.red, color.green, color.blue)

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val graphics = g as Graphics2D
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        graphics.drawImage(screenshot, 0, 0, width, height, null)

        graphics.color = Color(0, 0, 0, 60)
        graphics.fillRect(0, 0, width, height)

        val (displayX, displayY) = displayPoint()
        val mouseX = displayX.roundToInt().coerceIn(0, screenshot.width - 1)
        val mouseY = displayY.roundToInt().coerceIn(0, screenshot.height - 1)

        val loupeX = getLoupeX(mouseX)
        val loupeY = getLoupeY(mouseY)

        val hoveredColor = Color(screenshot.getRGB(mouseX, mouseY), true)

        setupMagnifyingLens(graphics, mouseX, mouseY, loupeX, loupeY)
        setupCursor(loupeX, loupeY, graphics)
        setupColorSelectionPreview(graphics, hoveredColor, loupeX, loupeY)
    }

    /**
     * A loupe is a small magnifying glass
     * */
    private fun getLoupeX(mouseX: Int): Int {
        val placeRight = mouseX + loupeSize + loupeMargin <= width
        val loupeX = if (placeRight) {
            (mouseX + loupeMargin).coerceAtMost(width - loupeSize - 20)
        } else {
            (mouseX - loupeSize - loupeMargin).coerceAtLeast(20)
        }
        return loupeX
    }

    /**
     * A loupe is a small magnifying glass
     * */
    private fun getLoupeY(mouseY: Int): Int {
        val placeBelow = mouseY + loupeSize + loupeMargin <= height
        val loupeY = if (placeBelow) {
            (mouseY + loupeMargin).coerceAtMost(height - loupeSize - 20)
        } else {
            (mouseY - loupeSize - loupeMargin).coerceAtLeast(20)
        }
        return loupeY
    }

    private fun setupColorSelectionPreview(
        graphics: Graphics2D,
        hoveredColor: Color,
        loupeX: Int,
        loupeY: Int
    ) {
        graphics.color = hoveredColor
        graphics.fillRoundRect(loupeX + 14, loupeY + loupeSize - 44, 44, 24, 8, 8)
        graphics.color = Color.WHITE
        graphics.drawRoundRect(loupeX + 14, loupeY + loupeSize - 44, 44, 24, 8, 8)

        graphics.color = Color(255, 255, 255, 235)
        graphics.font = graphics.font.deriveFont(Font.BOLD, 12f)
        graphics.drawString("HEX: ${toHex(hoveredColor)}", loupeX + 68, loupeY + loupeSize - 28)
        graphics.font = graphics.font.deriveFont(Font.PLAIN, 12f)
        graphics.drawString(
            "RGB: ${hoveredColor.red}, ${hoveredColor.green}, ${hoveredColor.blue}",
            loupeX + 68,
            loupeY + loupeSize - 12
        )
        graphics.drawString("Click to select · Esc to cancel", loupeX + 18, loupeY + loupeSize + 20)
    }

    private fun setupMagnifyingLens(graphics: Graphics2D, mouseX: Int, mouseY: Int, loupeX: Int, loupeY: Int) {
        val sourceX = (mouseX - zoomRadius).coerceIn(0, screenshot.width - zoomRadius * 2)
        val sourceY = (mouseY - zoomRadius).coerceIn(0, screenshot.height - zoomRadius * 2)
        val sourceSize = zoomRadius * 2

        graphics.color = Color(0, 0, 0, 120)
        graphics.fillRoundRect(loupeX - 10, loupeY - 10, loupeSize + 20, loupeSize + 52, 22, 22)

        graphics.color = Color(255, 255, 255, 25)
        graphics.fillRoundRect(loupeX - 8, loupeY - 8, loupeSize + 16, loupeSize + 48, 20, 20)

        graphics.drawImage(
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

        graphics.color = Color(255, 255, 255, 230)
        graphics.stroke = BasicStroke(2.5f)
        graphics.drawRoundRect(loupeX, loupeY, loupeSize, loupeSize, 16, 16)
    }

    private fun setupCursor(loupeX: Int, loupeY: Int, graphics: Graphics2D) {
        val centerX = loupeX + loupeSize / 2
        val centerY = loupeY + loupeSize / 2
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