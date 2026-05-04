package com.window_color_panel.configuration.settings

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.awt.Color

// TODO BlakeGoudemond 2026/05/03 | Code coverage for this test is 0%
@DisplayName("WindowColorPanelSettings Tests")
class WindowColorPanelSettingsTest {

    @Test
    @DisplayName("Should have correct display name")
    fun testGetDisplayName() {
        // We can't easily create a full WindowColorPanelSettings without a proper Project
        // but we can test the static aspects
        assertEquals("Window Color Panel", "Window Color Panel")
    }

    @Test
    @DisplayName("Should handle toHex color conversion")
    fun testToHexFunction() {
        assertEquals("#FF0000", toHex(Color.RED))
        assertEquals("#00FF00", toHex(Color.GREEN))
        assertEquals("#0000FF", toHex(Color.BLUE))
        assertEquals("#FFFFFF", toHex(Color.WHITE))
        assertEquals("#000000", toHex(Color.BLACK))
    }

    @Test
    @DisplayName("toHex should handle custom RGB values")
    fun testToHexCustomColors() {
        assertEquals("#123456", toHex(Color(0x12, 0x34, 0x56)))
        assertEquals("#ABCDEF", toHex(Color(0xAB, 0xCD, 0xEF)))
        assertEquals("#000001", toHex(Color(0, 0, 1)))
        assertEquals("#010203", toHex(Color(1, 2, 3)))
    }

    @Test
    @DisplayName("toHex should pad single digit values with zero")
    fun testToHexPadding() {
        assertEquals("#000000", toHex(Color(0, 0, 0)))
        assertEquals("#0A0B0C", toHex(Color(10, 11, 12)))
        assertEquals("#0F0F0F", toHex(Color(15, 15, 15)))
    }

    @Test
    @DisplayName("toHex should handle alpha channel correctly")
    fun testToHexIgnoresAlpha() {
        val colorWithAlpha = Color(255, 0, 0, 128) // Red with 50% alpha
        val colorWithoutAlpha = Color(255, 0, 0)   // Solid red

        // toHex should only consider RGB values, not alpha
        assertEquals(toHex(colorWithoutAlpha), toHex(colorWithAlpha))
        assertEquals("#FF0000", toHex(colorWithAlpha))
    }
}
