package com.window_color_panel.configuration.settings

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import java.awt.image.BufferedImage
import javax.swing.JComponent

@DisplayName("ScreenColorPicker Capture Component Tests")
class ScreenColorPickerCaptureComponentTest : BaseScreenColorPickerTest() {

    @Test
    @DisplayName("captureComponent should throw when component width is 0")
    fun testCaptureComponentInvalidWidth() {
        val mockComponent = mock(JComponent::class.java)
        `when`(mockComponent.width).thenReturn(0)
        `when`(mockComponent.height).thenReturn(100)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            captureComponent(mockComponent)
        }

        assertTrue(exception.message?.contains("invalid dimensions") ?: false)
    }

    @Test
    @DisplayName("captureComponent should throw when component height is 0")
    fun testCaptureComponentInvalidHeight() {
        val mockComponent = mock(JComponent::class.java)
        `when`(mockComponent.width).thenReturn(100)
        `when`(mockComponent.height).thenReturn(0)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            captureComponent(mockComponent)
        }

        assertTrue(exception.message?.contains("invalid dimensions") ?: false)
    }

    @Test
    @DisplayName("captureComponent should throw when component dimensions are negative")
    fun testCaptureComponentNegativeDimensions() {
        val mockComponent = mock(JComponent::class.java)
        `when`(mockComponent.width).thenReturn(-100)
        `when`(mockComponent.height).thenReturn(-100)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            captureComponent(mockComponent)
        }

        assertTrue(exception.message?.contains("invalid dimensions") ?: false)
    }

    @Test
    @DisplayName("captureComponent should create BufferedImage with correct dimensions")
    fun testCaptureComponentCreatesCorrectDimensions() {
        val mockComponent = mock(JComponent::class.java)
        `when`(mockComponent.width).thenReturn(640)
        `when`(mockComponent.height).thenReturn(480)

        val image = captureComponent(mockComponent)

        assertEquals(640, image.width)
        assertEquals(480, image.height)
        assertEquals(BufferedImage.TYPE_INT_ARGB, image.type)
    }

    @Test
    @DisplayName("captureComponent should dispose graphics after painting")
    fun testCaptureComponentDisposesGraphics() {
        val mockComponent = mock(JComponent::class.java)
        `when`(mockComponent.width).thenReturn(100)
        `when`(mockComponent.height).thenReturn(100)

        assertDoesNotThrow {
            captureComponent(mockComponent)
        }

        verify(mockComponent).paint(any())
    }

}

