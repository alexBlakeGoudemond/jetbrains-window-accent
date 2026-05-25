package com.window_accent.configuration.settings

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import java.awt.image.BufferedImage
import javax.swing.JComponent
import javax.swing.JPanel

@DisplayName("ScreenColorPicker Capture Component Tests")
class ScreenColorPickerCaptureComponentTest : BaseScreenColorPickerTest() {

    @Test
    @DisplayName("captureComponent should throw when component width is 0")
    fun testCaptureComponentInvalidWidth() {
        val component = JPanel()
        component.setSize(0, 100)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            captureComponent(component)
        }

        assertTrue(exception.message?.contains("invalid dimensions") ?: false)
    }

    @Test
    @DisplayName("captureComponent should throw when component height is 0")
    fun testCaptureComponentInvalidHeight() {
        val component = JPanel()
        component.setSize(100, 0)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            captureComponent(component)
        }

        assertTrue(exception.message?.contains("invalid dimensions") ?: false)
    }

    @Test
    @DisplayName("captureComponent should throw when component dimensions are negative")
    fun testCaptureComponentNegativeDimensions() {
        val component = JPanel()
        component.setSize(-100, -100)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            captureComponent(component)
        }

        assertTrue(exception.message?.contains("invalid dimensions") ?: false)
    }

    @Test
    @DisplayName("captureComponent should create BufferedImage with correct dimensions")
    fun testCaptureComponentCreatesCorrectDimensions() {
        val component = JPanel()
        component.setSize(640, 480)

        val image = captureComponent(component)

        assertEquals(640, image.width)
        assertEquals(480, image.height)
        assertEquals(BufferedImage.TYPE_INT_ARGB, image.type)
    }

    @Test
    @DisplayName("captureComponent should dispose graphics after painting")
    fun testCaptureComponentDisposesGraphics() {
        val component = JPanel()
        component.setSize(100, 100)

        assertDoesNotThrow {
            captureComponent(component)
        }
    }

}

