package com.window_accent.feature.window_color

import com.window_accent.diagnostic.windowAccentLogger
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService.GradientAnchor.START
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService.GradientAnchor.END
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService.GradientAnchor.MIDDLE
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService.GradientAnchor.OFF
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService.Side.NORTH
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService.Side.SOUTH
import java.awt.*
import javax.swing.JPanel

class ColoredPanel(
    private val side: WindowPanelAppearanceStateService.Side,
    private val panelColor: Color,
    private val isPanelOpaque: Boolean = true,
    private val panelPadding: Int = 4,
    private val backgroundColor: Color = Color(0x26, 0x28, 0x2C),
    private val gradientAnchor: WindowPanelAppearanceStateService.GradientAnchor =
        START
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

            applyGradientFade(rect, g2d)
        } finally {
            g2d.dispose()
        }
    }

    // Gradient: NORTH/SOUTH fade along the horizontal axis; WEST/EAST fade along the
    // vertical axis. Both ends are fully opaque — no alpha compositing, no Swing
    // black-fill artifact. The accent colour fades into the IntelliJ dark background.
    //
    // [gradientAnchor] controls where the solid edge of the fade sits along that axis:
    // START = axis start (left/top), END = axis end (right/bottom), MIDDLE = centered
    // with a fade on both sides, OFF = no fade — skip the gradient overlay entirely and
    // leave the flat solid-color fill drawn above (if isPanelOpaque) untouched.
    private fun applyGradientFade(rect: Rectangle, g2d: Graphics2D) {
        if (rect.width <= 0 || rect.height <= 0) {
            return
        }
        val solidColor = Color(panelColor.red, panelColor.green, panelColor.blue, 255)
        val transparentColor = backgroundColor
        val (fractions, colors) = when (gradientAnchor) {
            START -> floatArrayOf(0f, 1f) to arrayOf(solidColor, transparentColor)
            END -> floatArrayOf(0f, 1f) to arrayOf(transparentColor, solidColor)
            MIDDLE -> floatArrayOf(0f, 0.5f, 1f) to arrayOf(transparentColor, solidColor, transparentColor)
            OFF -> floatArrayOf(0f, 1f) to arrayOf(solidColor, solidColor)
        }
        val isHorizontal = side == NORTH || side == SOUTH
        val gradient = if (isHorizontal) {
            LinearGradientPaint(
                rect.x.toFloat(), 0f,
                (rect.x + rect.width).toFloat(), 0f,
                fractions, colors
            )
        } else {
            LinearGradientPaint(
                0f, rect.y.toFloat(),
                0f, (rect.y + rect.height).toFloat(),
                fractions, colors
            )
        }
        g2d.paint = gradient
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10)
    }

}