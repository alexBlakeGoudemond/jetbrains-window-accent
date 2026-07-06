package com.window_accent.feature.window_color

import com.window_accent.diagnostic.windowAccentLogger
import com.intellij.util.ui.UIUtil
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService
import java.awt.*
import javax.swing.JPanel

class ColoredPanel(
    private val side: WindowPanelAppearanceStateService.Side,
    private val panelColor: Color
) : JPanel() {

    private val log = windowAccentLogger<ColoredPanel>()

    init {
        isOpaque = true
        background = UIUtil.getPanelBackground()
        putClientProperty(WindowColorApplier.PANEL_CLIENT_PROPERTY, true)
    }

    override fun paintComponent(g: Graphics) {
        log.debug("ColoredPanel: painting with color=$panelColor")
        super.paintComponent(g)
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        // For now, draw a simple rectangle representing the polygon/panel
        // Add some padding to make it look "inside" the container panel
        val padding = 4
        val rect = when (side) {
            WindowPanelAppearanceStateService.Side.NORTH -> Rectangle(padding, padding, width - 2 * padding, height - padding)
            WindowPanelAppearanceStateService.Side.SOUTH -> Rectangle(padding, 0, width - 2 * padding, height - padding)
            WindowPanelAppearanceStateService.Side.WEST -> Rectangle(padding, padding, width - padding, height - 2 * padding)
            WindowPanelAppearanceStateService.Side.EAST -> Rectangle(0, padding, width - padding, height - 2 * padding)
        }

        // Gradient
        val gradient = when (side) {
            WindowPanelAppearanceStateService.Side.NORTH,
            WindowPanelAppearanceStateService.Side.SOUTH -> GradientPaint(
                0f, 0f, panelColor,
                width.toFloat(), 0f, Color(panelColor.red, panelColor.green, panelColor.blue, 0)
            )
            WindowPanelAppearanceStateService.Side.WEST,
            WindowPanelAppearanceStateService.Side.EAST -> GradientPaint(
                0f, 0f, panelColor,
                0f, height.toFloat(), Color(panelColor.red, panelColor.green, panelColor.blue, 0)
            )
        }
        g2d.paint = gradient
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10)
    }
}
