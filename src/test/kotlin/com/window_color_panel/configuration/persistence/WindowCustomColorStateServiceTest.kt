package com.window_color_panel.configuration.persistence

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.awt.Color

@DisplayName("WindowCustomColorStateService Tests")
class WindowCustomColorStateServiceTest {

    private lateinit var service: WindowCustomColorStateService

    @BeforeEach
    fun setup() {
        service = WindowCustomColorStateService()
    }

    @Test
    @DisplayName("Initial state should have custom color disabled and no color set")
    fun testInitialState() {
        assertFalse(service.isUseCustomColor())
        assertNull(service.getCustomColor())
    }

    @Test
    @DisplayName("Should be able to enable custom color")
    fun testSetUseCustomColorTrue() {
        service.setUseCustomColor(true)
        assertTrue(service.isUseCustomColor())
    }

    @Test
    @DisplayName("Should be able to disable custom color")
    fun testSetUseCustomColorFalse() {
        service.setUseCustomColor(true)
        service.setUseCustomColor(false)
        assertFalse(service.isUseCustomColor())
    }

    @Test
    @DisplayName("Should persist custom color value")
    fun testSetAndGetCustomColor() {
        val customColor = Color(255, 128, 64)
        service.setCustomColor(customColor)

        val retrievedColor = service.getCustomColor()
        assertNotNull(retrievedColor)
        assertEquals(customColor.rgb, retrievedColor!!.rgb)
    }

    @Test
    @DisplayName("Should handle null color")
    fun testSetCustomColorNull() {
        service.setCustomColor(Color.RED)
        service.setCustomColor(null)
        assertNull(service.getCustomColor())
    }

    @Test
    @DisplayName("Should preserve RGB values including alpha channel")
    fun testCustomColorPreservesAlpha() {
        val colorWithAlpha = Color(100, 150, 200, 128)
        service.setCustomColor(colorWithAlpha)

        val retrievedColor = service.getCustomColor()
        assertNotNull(retrievedColor)
        assertEquals(colorWithAlpha.rgb, retrievedColor!!.rgb)
    }

    @Test
    @DisplayName("Should correctly load state")
    fun testLoadState() {
        val newState = WindowCustomColorStateService.State(
            useCustomColor = true,
            customColorRgb = Color.BLUE.rgb
        )
        service.loadState(newState)

        assertTrue(service.isUseCustomColor())
        assertEquals(Color.BLUE.rgb, service.getCustomColor()?.rgb)
    }

    @Test
    @DisplayName("Should correctly return current state")
    fun testGetState() {
        val color = Color(50, 100, 150)
        service.setUseCustomColor(true)
        service.setCustomColor(color)

        val state = service.getState()
        assertTrue(state.useCustomColor)
        assertEquals(color.rgb, state.customColorRgb)
    }

    @Test
    @DisplayName("Should handle state transitions correctly")
    fun testStateTransitions() {
        // Start disabled
        assertFalse(service.isUseCustomColor())

        // Enable and set color
        service.setUseCustomColor(true)
        val color1 = Color(100, 100, 100)
        service.setCustomColor(color1)

        assertTrue(service.isUseCustomColor())
        assertEquals(color1.rgb, service.getCustomColor()?.rgb)

        // Change color while enabled
        val color2 = Color(200, 200, 200)
        service.setCustomColor(color2)
        assertEquals(color2.rgb, service.getCustomColor()?.rgb)

        // Disable
        service.setUseCustomColor(false)
        assertFalse(service.isUseCustomColor())
        // Color should still be stored even though disabled
        assertEquals(color2.rgb, service.getCustomColor()?.rgb)
    }

    @Test
    @DisplayName("Should handle multiple color changes")
    fun testMultipleColorChanges() {
        val colors = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)

        for (color in colors) {
            service.setCustomColor(color)
            assertEquals(color.rgb, service.getCustomColor()?.rgb)
        }
    }
}

