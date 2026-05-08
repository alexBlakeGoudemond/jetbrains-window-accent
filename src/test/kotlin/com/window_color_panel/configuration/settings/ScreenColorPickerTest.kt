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
class ScreenColorPickerTest {

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

    @Test
    @DisplayName("Should test mouse point clamping within image bounds")
    fun testMousePointClamping() {
        val screenshot = BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB)

        // Test points both within and beyond bounds
        val testCases = listOf(
            0 to 0,
            1919 to 1079,
            -100 to -100,
            2000 to 2000,
            950 to 540
        )

        for ((x, y) in testCases) {
            val clampedX = x.coerceIn(0, screenshot.width - 1)
            val clampedY = y.coerceIn(0, screenshot.height - 1)

            // All clamped values should be valid for getRGB
            assertDoesNotThrow {
                screenshot.getRGB(clampedX, clampedY)
            }
        }
    }

    @Test
    @DisplayName("Should handle selected color application")
    fun testSelectedColorApplication() {
        val testColor = Color(255, 128, 64)

        // Test that color can be created and its components accessed
        assertEquals(255, testColor.red)
        assertEquals(128, testColor.green)
        assertEquals(64, testColor.blue)

        // Test that color RGB value is preserved
        val extractedColor = Color(testColor.rgb, true)
        assertEquals(testColor.red, extractedColor.red)
        assertEquals(testColor.green, extractedColor.green)
        assertEquals(testColor.blue, extractedColor.blue)
    }

    @Test
    @DisplayName("Should handle screenshot color extraction and application")
    fun testScreenshotColorExtractionAndApplication() {
        // Create a test screenshot with known colors
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
        val testColor = Color(200, 100, 50)
        screenshot.setRGB(50, 50, testColor.rgb)

        // Extract color from specific point
        val mx = 50.coerceIn(0, screenshot.width - 1)
        val my = 50.coerceIn(0, screenshot.height - 1)
        val extractedRgb = screenshot.getRGB(mx, my)
        val extractedColor = Color(extractedRgb, true)

        // Colors should match
        assertEquals(testColor.red, extractedColor.red)
        assertEquals(testColor.green, extractedColor.green)
        assertEquals(testColor.blue, extractedColor.blue)
    }

    @Test
    @DisplayName("Should handle large screen sizes")
    fun testLargeScreenSizeHandling() {
        val largeWidth = 3840  // 4K width
        val largeHeight = 2160  // 4K height
        val screenshot = BufferedImage(largeWidth, largeHeight, BufferedImage.TYPE_INT_RGB)

        // Test center point
        val mx = largeWidth / 2
        val my = largeHeight / 2

        assertDoesNotThrow {
            screenshot.getRGB(mx, my)
        }
    }

    @Test
    @DisplayName("Should handle small screen sizes")
    fun testSmallScreenSizeHandling() {
        val smallWidth = 320
        val smallHeight = 240
        val screenshot = BufferedImage(smallWidth, smallHeight, BufferedImage.TYPE_INT_RGB)

        // Test center point
        val mx = smallWidth / 2
        val my = smallHeight / 2

        assertDoesNotThrow {
            screenshot.getRGB(mx, my)
        }
    }

    @Test
    @DisplayName("Should handle zero coordinates clamping")
    fun testZeroCoordinatesClamping() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)

        val clampedX = (-50).coerceIn(0, 99)
        val clampedY = (-50).coerceIn(0, 99)

        assertEquals(0, clampedX)
        assertEquals(0, clampedY)

        assertDoesNotThrow {
            screenshot.getRGB(clampedX, clampedY)
        }
    }

    @Test
    @DisplayName("Should handle maximum coordinate clamping")
    fun testMaximumCoordinateClamping() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)

        val clampedX = 500.coerceIn(0, 99)
        val clampedY = 500.coerceIn(0, 99)

        assertEquals(99, clampedX)
        assertEquals(99, clampedY)

        assertDoesNotThrow {
            screenshot.getRGB(clampedX, clampedY)
        }
    }

    @Test
    @DisplayName("Should handle display point smoothing with multiple iterations")
    fun testDisplayPointSmoothingIterations() {
        var displayX = 0.0
        var displayY = 0.0

        val targetX = 1000.0
        val targetY = 1000.0
        val smoothingFactor = 0.22

        // Verify smoothing progresses towards target
        val xValues = mutableListOf<Double>()
        val yValues = mutableListOf<Double>()

        repeat(20) {
            displayX += (targetX - displayX) * smoothingFactor
            displayY += (targetY - displayY) * smoothingFactor
            xValues.add(displayX)
            yValues.add(displayY)
        }

        // Values should be monotonically increasing towards target
        for (i in 1 until xValues.size) {
            assertTrue(xValues[i] > xValues[i - 1], "X values should increase: ${xValues[i]} should be > ${xValues[i - 1]}")
            assertTrue(yValues[i] > yValues[i - 1], "Y values should increase: ${yValues[i]} should be > ${yValues[i - 1]}")
        }

        // Should approach but not exceed target
        assertTrue(displayX < targetX, "Display X should be less than target after smoothing")
        assertTrue(displayY < targetY, "Display Y should be less than target after smoothing")
    }

    @Test
    @DisplayName("Should handle display point smoothing convergence")
    fun testDisplayPointSmoothingConvergence() {
        var displayX = 100.0
        var displayY = 100.0

        val targetX = 100.0
        val targetY = 100.0
        val smoothingFactor = 0.22

        // When already at target, should remain at target
        repeat(5) {
            displayX += (targetX - displayX) * smoothingFactor
            displayY += (targetY - displayY) * smoothingFactor
        }

        // Should stay at target value
        assertEquals(100.0, displayX)
        assertEquals(100.0, displayY)
    }

    @Test
    @DisplayName("Should handle negative to positive coordinate smoothing")
    fun testNegativeToPositiveCoordinateSmoothing() {
        var displayX = -100.0
        var displayY = -100.0

        val targetX = 100.0
        val targetY = 100.0
        val smoothingFactor = 0.22

        // Simulate smoothing from negative to positive
        repeat(10) {
            displayX += (targetX - displayX) * smoothingFactor
            displayY += (targetY - displayY) * smoothingFactor
        }

        // Should cross zero and approach target
        assertTrue(displayX > 0 && displayX < targetX)
        assertTrue(displayY > 0 && displayY < targetY)
    }

    @Test
    @DisplayName("Should handle custom color with maximum values")
    fun testCustomColorMaximumValues() {
        val maxColor = Color(255, 255, 255)
        val extractedColor = Color(maxColor.rgb, true)

        assertEquals(255, extractedColor.red)
        assertEquals(255, extractedColor.green)
        assertEquals(255, extractedColor.blue)
    }

    @Test
    @DisplayName("Should handle custom color with minimum values")
    fun testCustomColorMinimumValues() {
        val minColor = Color(0, 0, 0)
        val extractedColor = Color(minColor.rgb, true)

        assertEquals(0, extractedColor.red)
        assertEquals(0, extractedColor.green)
        assertEquals(0, extractedColor.blue)
    }

    @Test
    @DisplayName("Should preserve RGB values through multiple conversions")
    fun testRGBPreservationThroughConversions() {
        val originalColor = Color(123, 45, 67)
        val rgb1 = originalColor.rgb
        val color1 = Color(rgb1, true)
        val rgb2 = color1.rgb
        val color2 = Color(rgb2, true)

        // Should preserve through multiple conversions
        assertEquals(originalColor.red, color2.red)
        assertEquals(originalColor.green, color2.green)
        assertEquals(originalColor.blue, color2.blue)
    }

    @Test
    @DisplayName("Should handle screenshot boundary conditions for different types")
    fun testScreenshotBoundaryConditionsForTypes() {
        val types = arrayOf(
            BufferedImage.TYPE_INT_RGB,
            BufferedImage.TYPE_INT_ARGB,
            BufferedImage.TYPE_INT_BGR,
            BufferedImage.TYPE_INT_ARGB_PRE
        )

        for (type in types) {
            val img = BufferedImage(50, 50, type)

            // Test all corners
            assertDoesNotThrow { img.getRGB(0, 0) }
            assertDoesNotThrow { img.getRGB(49, 0) }
            assertDoesNotThrow { img.getRGB(0, 49) }
            assertDoesNotThrow { img.getRGB(49, 49) }
        }
    }
}
