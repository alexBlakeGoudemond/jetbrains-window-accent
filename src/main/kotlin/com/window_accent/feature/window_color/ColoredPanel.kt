package com.window_accent.feature.window_color

import com.window_accent.diagnostic.windowAccentLogger
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService
import java.awt.*
import javax.swing.JPanel

class ColoredPanel(
    private val side: WindowPanelAppearanceStateService.Side,
    private val panelColor: Color,
    private val isPanelOpaque: Boolean,
    private val panelPadding: Int = 4,
    private val backgroundColor: Color = Color(0x26, 0x28, 0x2C)
) : JPanel() {

    private val log = windowAccentLogger<ColoredPanel>()

    init {
        // Make the panel opaque with the background colour so any unpainted pixels
        // (e.g. the padding border outside the gradient rect) match the fade target
        // rather than showing whatever dark/black surface lies behind the component.
        isOpaque = true
        background = backgroundColor
        putClientProperty(WindowColorApplier.PANEL_CLIENT_PROPERTY, true)
    }

    override fun paintComponent(g: Graphics) {
        log.debug("ColoredPanel: painting with color=$panelColor")
        // super.paintComponent fills the entire panel with `background` (ideBackground),
        // covering the padding gap with the correct IDE colour before the gradient draws.
        super.paintComponent(g)
        val g2d = g.create() as Graphics2D
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g2d.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER)

            // Inset the gradient rect by the user-configurable panelPadding on all four edges,
            // centering the accent panel within the JPanel regardless of which side it is on.
            val p = panelPadding
            val rect = Rectangle(p, p, width - 2 * p, height - 2 * p)

            // Draw the solid background only when the opaque mode is explicitly enabled
            if (isPanelOpaque) {
                g2d.paint = Color(panelColor.red, panelColor.green, panelColor.blue, 255)
                g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10)
            }

            // Gradient: NORTH/SOUTH fade left→right (RHS); WEST/EAST fade top→bottom.
            // Both ends are fully opaque — no alpha compositing, no Swing black-fill artifact.
            // The accent colour fades into the IntelliJ dark background (#26282C).
            val solidColor = Color(panelColor.red, panelColor.green, panelColor.blue, 255)
            val colorWithAlpha = solidColor
            val transparentColor = backgroundColor

            val gradient = when (side) {
                // NORTH + SOUTH: color on the left, fades to transparent on the right
                WindowPanelAppearanceStateService.Side.NORTH,
                WindowPanelAppearanceStateService.Side.SOUTH -> GradientPaint(
                    rect.x.toFloat(), 0f, colorWithAlpha,
                    (rect.x + rect.width).toFloat(), 0f, transparentColor
                )
                // WEST + EAST: color at the top, fades to transparent at the bottom
                WindowPanelAppearanceStateService.Side.WEST,
                WindowPanelAppearanceStateService.Side.EAST -> GradientPaint(
                    0f, rect.y.toFloat(), colorWithAlpha,
                    0f, (rect.y + rect.height).toFloat(), transparentColor
                )
            }
            g2d.paint = gradient
            g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10)
        } finally {
            g2d.dispose()
        }
    }
}
