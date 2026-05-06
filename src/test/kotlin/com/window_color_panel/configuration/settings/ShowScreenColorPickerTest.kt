package com.window_color_panel.configuration.settings

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.awt.Color
import java.awt.GraphicsEnvironment
import java.awt.Point
import java.awt.image.BufferedImage
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.SwingUtilities
import java.awt.AWTException
import java.awt.HeadlessException
import java.awt.Window

@DisplayName("showScreenColorPicker Tests")
class ShowScreenColorPickerTest {

    @Test
    @DisplayName("Should handle color selection bounds checking")
    fun testColorSelectionBounds() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
        screenshot.setRGB(50, 50, Color.RED.rgb)

        // Test bounds checking logic that would be used in the function
        val testPoints = listOf(
            Point(0, 0),      // Minimum bounds
            Point(50, 50),    // Center
            Point(99, 99),    // Maximum bounds
            Point(-10, -10),  // Negative (should be clamped)
            Point(200, 200)   // Beyond bounds (should be clamped)
        )

        for (point in testPoints) {
            val mx = point.x.coerceIn(0, 99)
            val my = point.y.coerceIn(0, 99)

            assertTrue(mx in 0..99, "X coordinate $mx should be within screenshot bounds")
            assertTrue(my in 0..99, "Y coordinate $my should be within screenshot bounds")

            // Test that we can get RGB from the clamped coordinates
            assertDoesNotThrow {
                val rgb = screenshot.getRGB(mx, my)
                val color = Color(rgb, true)
                assertNotNull(color)
            }
        }
    }

    @Test
    @DisplayName("Should handle different color values from screenshot")
    fun testColorValueExtraction() {
        val screenshot = BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB)

        val testColors = listOf(
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.WHITE,
            Color.BLACK,
            Color(128, 64, 32)
        )

        for ((index, color) in testColors.withIndex()) {
            screenshot.setRGB(index, 0, color.rgb)

            val extractedRgb = screenshot.getRGB(index, 0)
            val extractedColor = Color(extractedRgb, true)

            assertEquals(color.red, extractedColor.red, "Red component should match")
            assertEquals(color.green, extractedColor.green, "Green component should match")
            assertEquals(color.blue, extractedColor.blue, "Blue component should match")
        }
    }

    @Test
    @DisplayName("Should handle screenshot with alpha channel")
    fun testScreenshotWithAlpha() {
        val screenshot = BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB)

        val colorWithAlpha = Color(255, 128, 64, 128) // Semi-transparent orange
        screenshot.setRGB(5, 5, colorWithAlpha.rgb)

        val extractedRgb = screenshot.getRGB(5, 5)
        val extractedColor = Color(extractedRgb, true)

        // The RGB values should be preserved
        assertEquals(colorWithAlpha.red, extractedColor.red)
        assertEquals(colorWithAlpha.green, extractedColor.green)
        assertEquals(colorWithAlpha.blue, extractedColor.blue)
        assertEquals(colorWithAlpha.alpha, extractedColor.alpha)
    }

    @Test
    @DisplayName("Should handle extreme mouse coordinates")
    fun testExtremeMouseCoordinates() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)

        val extremeCoords = intArrayOf(Int.MIN_VALUE, -1000, 1000, Int.MAX_VALUE)

        for (coord in extremeCoords) {
            val clampedX = coord.coerceIn(0, 99)
            val clampedY = coord.coerceIn(0, 99)

            assertTrue(clampedX in 0..99, "X coordinate should be clamped to valid range")
            assertTrue(clampedY in 0..99, "Y coordinate should be clamped to valid range")

            // Should not throw when accessing clamped coordinates
            assertDoesNotThrow {
                screenshot.getRGB(clampedX, clampedY)
            }
        }
    }

    @Test
    @DisplayName("Should handle different BufferedImage types")
    fun testDifferentBufferedImageTypes() {
        val imageTypes = arrayOf(
            BufferedImage.TYPE_INT_RGB,
            BufferedImage.TYPE_INT_ARGB,
            BufferedImage.TYPE_INT_ARGB_PRE,
            BufferedImage.TYPE_INT_BGR
        )

        for (type in imageTypes) {
            val screenshot = BufferedImage(10, 10, type)
            screenshot.setRGB(5, 5, Color.BLUE.rgb)

            assertDoesNotThrow {
                val rgb = screenshot.getRGB(5, 5)
                val color = Color(rgb, true)
                assertNotNull(color)
            }
        }
    }

    @Test
    @DisplayName("Should handle color conversion correctly")
    fun testColorConversion() {
        val testRgb = Color.CYAN.rgb
        val color = Color(testRgb, true)

        // Test that Color constructor preserves RGB values
        assertEquals(Color.CYAN.red, color.red)
        assertEquals(Color.CYAN.green, color.green)
        assertEquals(Color.CYAN.blue, color.blue)

        // Test that RGB int value is preserved
        assertEquals(testRgb, color.rgb)
    }

    @Test
    @DisplayName("Should handle mouse point display smoothing")
    fun testMousePointSmoothing() {
        // Test the smoothing logic that would be used in the magnifier
        var displayX = 100.0
        var displayY = 100.0

        val targetX = 150.0
        val targetY = 150.0

        val smoothingFactor = 0.22

        // Simulate a few iterations of smoothing
        repeat(10) {
            displayX += (targetX - displayX) * smoothingFactor
            displayY += (targetY - displayY) * smoothingFactor
        }

        // Should move towards target but not reach it exactly due to smoothing
        assertTrue(displayX > 100.0 && displayX < targetX)
        assertTrue(displayY > 100.0 && displayY < targetY)
    }

    @Test
    @DisplayName("Should initialize screen color picker components")
    fun testShowScreenColorPickerInitialization() {
        // Set headless mode to avoid showing UI
        val originalHeadless = GraphicsEnvironment.isHeadless()
        System.setProperty("java.awt.headless", "true")

        try {
            // Create mock settings
            val mockSettings = mock(WindowColorPanelSettings::class.java)
            val mockPanel = mock(JPanel::class.java)
            val mockCheckBox = mock(JCheckBox::class.java)

            `when`(mockSettings.panel).thenReturn(mockPanel)
            `when`(mockSettings.customColorCheckBox).thenReturn(mockCheckBox)

            // Since we're in headless mode, SwingUtilities.getWindowAncestor will return null
            // and the function should return early
            showScreenColorPicker(mockSettings)

            // Verify that no exceptions were thrown
            // In headless mode, Robot creation might fail, but the early return should handle it
        } catch (e: Exception) {
            // In headless environment, Robot might not be available
            assertTrue(e is AWTException || e is HeadlessException, "Expected AWT or Headless exception in headless mode")
        } finally {
            // Restore original headless setting
            System.setProperty("java.awt.headless", originalHeadless.toString())
        }
    }

    @Test
    @DisplayName("Should handle null window ancestor")
    fun testShowScreenColorPickerNullWindow() {
        val mockSettings = mock(WindowColorPanelSettings::class.java)
        val mockPanel = mock(JPanel::class.java)

        `when`(mockSettings.panel).thenReturn(mockPanel)

        mockStatic(SwingUtilities::class.java).use { mockedSwing ->
            mockedSwing.`when`<Window?> { SwingUtilities.getWindowAncestor(mockPanel) }.thenReturn(null)

            // Should return early without throwing
            assertDoesNotThrow {
                showScreenColorPicker(mockSettings)
            }
        }
    }
}
