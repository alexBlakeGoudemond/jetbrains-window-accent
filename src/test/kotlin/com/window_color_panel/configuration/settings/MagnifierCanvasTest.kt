package com.window_color_panel.configuration.settings

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.awt.Color
import java.awt.image.BufferedImage

class MagnifierCanvasTest {

    @Test
    fun `test color to hex conversion`() {
        val red = Color(255, 0, 0)
        assertEquals("#FF0000", toHex(red))
        
        val green = Color(0, 255, 0)
        assertEquals("#00FF00", toHex(green))
        
        val blue = Color(0, 0, 255)
        assertEquals("#0000FF", toHex(blue))
    }
}
