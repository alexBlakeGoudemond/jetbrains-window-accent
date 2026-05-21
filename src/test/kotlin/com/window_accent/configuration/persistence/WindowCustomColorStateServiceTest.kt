package com.window_accent.configuration.persistence

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
    fun `initial state has no custom color`() {
        assertFalse(service.isUseCustomColor())
        assertNull(service.getCustomColor())
    }

    @Test
    fun `can use custom color`() {
        service.setUseCustomColor(true)
        assertTrue(service.isUseCustomColor())
    }

    @Test
    fun `can disable custom color`() {
        service.setUseCustomColor(true)
        service.setUseCustomColor(false)
        assertFalse(service.isUseCustomColor())
    }

    @Test
    fun `can save custom color`() {
        val customColor = Color(255, 128, 64)
        service.setCustomColor(customColor)

        val retrievedColor = service.getCustomColor()
        assertNotNull(retrievedColor)
        assertEquals(customColor.rgb, retrievedColor!!.rgb)
    }

    @Test
    fun `supports null colour`() {
        service.setCustomColor(Color.RED)
        service.setCustomColor(null)
        assertNull(service.getCustomColor())
    }

    @Test
    fun `can set custom colour with alpha channel`() {
        val colorWithAlpha = Color(100, 150, 200, 128)
        service.setCustomColor(colorWithAlpha)

        val retrievedColor = service.getCustomColor()
        assertNotNull(retrievedColor)
        assertEquals(colorWithAlpha.rgb, retrievedColor!!.rgb)
    }

    @Test
    fun `can load custom colour`() {
        val newState = WindowCustomColorStateService.State(
            useCustomColor = true,
            customColorRgb = Color.BLUE.rgb
        )
        service.loadState(newState)

        assertTrue(service.isUseCustomColor())
        assertEquals(Color.BLUE.rgb, service.getCustomColor()?.rgb)
    }

    @Test
    fun `can retrieve custom colour`() {
        val color = Color(50, 100, 150)
        service.setUseCustomColor(true)
        service.setCustomColor(color)

        val state = service.getState()
        assertTrue(state.useCustomColor)
        assertEquals(color.rgb, state.customColorRgb)
    }

    @Test
    fun `can transition between colours`() {
        assertFalse(service.isUseCustomColor())
        service.setUseCustomColor(true)

        val gray = Color(100, 100, 100)
        service.setCustomColor(gray)
        assertEquals(gray.rgb, service.getCustomColor()?.rgb)
        assertTrue(service.isUseCustomColor())

        val white = Color(200, 200, 200)
        service.setCustomColor(white)
        assertEquals(white.rgb, service.getCustomColor()?.rgb)
        assertTrue(service.isUseCustomColor())

        service.setUseCustomColor(false)
        assertFalse(service.isUseCustomColor())
        assertEquals(white.rgb, service.getCustomColor()?.rgb)
    }

    @Test
    fun `can set multiple custom colours`() {
        val colors = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)

        for (color in colors) {
            service.setCustomColor(color)
            assertEquals(color.rgb, service.getCustomColor()?.rgb)
        }
    }
}

