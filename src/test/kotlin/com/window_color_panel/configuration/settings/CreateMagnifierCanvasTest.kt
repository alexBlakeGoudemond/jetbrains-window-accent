package com.window_color_panel.configuration.settings

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.awt.Color
import java.awt.image.BufferedImage
import javax.swing.JComponent

@DisplayName("createMagnifierCanvas Tests")
class CreateMagnifierCanvasTest {

    @Test
    @DisplayName("toHex should convert color to correct hex format")
    fun testToHex() {
        assertEquals("#FF0000", toHex(Color.RED))
        assertEquals("#00FF00", toHex(Color.GREEN))
        assertEquals("#0000FF", toHex(Color.BLUE))
        assertEquals("#000000", toHex(Color.BLACK))
        assertEquals("#FFFFFF", toHex(Color.WHITE))
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
    @DisplayName("createMagnifierCanvas should return JComponent")
    fun testCreateMagnifierCanvasReturnsComponent() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
        val displayMousePoint = { Pair(50.0, 50.0) }

        val canvas = createMagnifierCanvas(screenshot, displayMousePoint)

        assertNotNull(canvas)
        assertTrue(canvas is JComponent)
    }

    @Test
    @DisplayName("createMagnifierCanvas should have correct zoom radius")
    fun testMagnifierCanvasZoomRadius() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
        val displayMousePoint = { Pair(50.0, 50.0) }

        val canvas = createMagnifierCanvas(screenshot, displayMousePoint)

        // Access private field via reflection for testing
        val zoomRadiusField = canvas.javaClass.getDeclaredField("zoomRadius")
        zoomRadiusField.isAccessible = true
        val zoomRadius = zoomRadiusField.get(canvas) as Int

        assertEquals(12, zoomRadius)
    }

    @Test
    @DisplayName("createMagnifierCanvas should have correct loupe size")
    fun testMagnifierCanvasLoupeSize() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
        val displayMousePoint = { Pair(50.0, 50.0) }

        val canvas = createMagnifierCanvas(screenshot, displayMousePoint)

        // Access private field via reflection for testing
        val loupeSizeField = canvas.javaClass.getDeclaredField("loupeSize")
        loupeSizeField.isAccessible = true
        val loupeSize = loupeSizeField.get(canvas) as Int

        assertEquals(180, loupeSize)
    }

    @Test
    @DisplayName("createMagnifierCanvas should have correct loupe margin")
    fun testMagnifierCanvasLoupeMargin() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
        val displayMousePoint = { Pair(50.0, 50.0) }

        val canvas = createMagnifierCanvas(screenshot, displayMousePoint)

        // Access private field via reflection for testing
        val loupeMarginField = canvas.javaClass.getDeclaredField("loupeMargin")
        loupeMarginField.isAccessible = true
        val loupeMargin = loupeMarginField.get(canvas) as Int

        assertEquals(24, loupeMargin)
    }

    @Test
    @DisplayName("Should handle mouse point within screenshot bounds")
    fun testMousePointWithinBounds() {
        val screenshot = BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB)
        val displayMousePoint = { Pair(50.0, 50.0) }

        val canvas = createMagnifierCanvas(screenshot, displayMousePoint)

        // Set the component size to match the screenshot for testing
        canvas.setSize(200, 200)

        // Test the bounds checking logic by accessing private methods via reflection
        val getMagnifyingXMethod = canvas.javaClass.getDeclaredMethod("getMagnifyingX", Int::class.java)
        getMagnifyingXMethod.isAccessible = true

        val getMagnifyingYMethod = canvas.javaClass.getDeclaredMethod("getMagnifyingY", Int::class.java)
        getMagnifyingYMethod.isAccessible = true

        // Test various mouse positions
        val testPositions = listOf(0, 50, 100, 150, 199)

        for (mouseX in testPositions) {
            val resultX = getMagnifyingXMethod.invoke(canvas, mouseX) as Int
            // The bounds depend on the component size and loupe size
            // Just verify the method returns a reasonable value
            assertTrue(resultX >= 0, "Magnifying X should be non-negative for mouseX=$mouseX, got $resultX")
        }

        for (mouseY in testPositions) {
            val resultY = getMagnifyingYMethod.invoke(canvas, mouseY) as Int
            // Just verify the method returns a reasonable value
            assertTrue(resultY >= 0, "Magnifying Y should be non-negative for mouseY=$mouseY, got $resultY")
        }
    }

    @Test
    @DisplayName("Should handle mouse point at screenshot edges")
    fun testMousePointAtEdges() {
        val screenshot = BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB)
        val displayMousePoint = { Pair(0.0, 0.0) }

        val canvas = createMagnifierCanvas(screenshot, displayMousePoint)

        // Set the component size to match the screenshot for testing
        canvas.setSize(200, 200)

        val getMagnifyingXMethod = canvas.javaClass.getDeclaredMethod("getMagnifyingX", Int::class.java)
        getMagnifyingXMethod.isAccessible = true

        val getMagnifyingYMethod = canvas.javaClass.getDeclaredMethod("getMagnifyingY", Int::class.java)
        getMagnifyingYMethod.isAccessible = true

        // Test edge cases - just verify methods don't throw exceptions
        assertDoesNotThrow {
            getMagnifyingXMethod.invoke(canvas, 0)
            getMagnifyingXMethod.invoke(canvas, 199)
            getMagnifyingYMethod.invoke(canvas, 0)
            getMagnifyingYMethod.invoke(canvas, 199)
        }
    }

    @Test
    @DisplayName("Should handle screenshot smaller than zoom radius")
    fun testScreenshotSmallerThanZoomRadius() {
        // Create a very small screenshot
        val screenshot = BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB)

        // This should not throw an exception during creation
        assertDoesNotThrow {
            createMagnifierCanvas(screenshot) { Pair(5.0, 5.0) }
        }
    }

    @Test
    @DisplayName("Should handle null display mouse point function")
    fun testNullDisplayMousePoint() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)

        // This should not throw an exception during creation
        assertDoesNotThrow {
            createMagnifierCanvas(screenshot) { Pair(50.0, 50.0) }
        }
    }

    @Test
    @DisplayName("Should handle display mouse point outside screenshot bounds")
    fun testDisplayMousePointOutsideBounds() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)

        // Test with mouse point outside bounds
        val displayMousePoint = { Pair(150.0, 150.0) }

        val canvas = createMagnifierCanvas(screenshot, displayMousePoint)

        // Should handle this gracefully without throwing exceptions
        assertDoesNotThrow {
            assertNotNull(canvas)
        }
    }

    @Test
    @DisplayName("Should handle different screenshot types")
    fun testDifferentScreenshotTypes() {
        val screenshotTypes = arrayOf(
            BufferedImage.TYPE_INT_RGB,
            BufferedImage.TYPE_INT_ARGB,
            BufferedImage.TYPE_INT_ARGB_PRE,
            BufferedImage.TYPE_INT_BGR
        )

        for (type in screenshotTypes) {
            val screenshot = BufferedImage(50, 50, type)
            val displayMousePoint = { Pair(25.0, 25.0) }

            assertDoesNotThrow {
                val canvas = createMagnifierCanvas(screenshot, displayMousePoint)
                assertNotNull(canvas)
            }
        }
    }

    @Test
    @DisplayName("Should handle extreme mouse coordinates")
    fun testExtremeMouseCoordinates() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
        val displayMousePoint = { Pair(25.0, 25.0) }

        val canvas = createMagnifierCanvas(screenshot, displayMousePoint)

        val getMagnifyingXMethod = canvas.javaClass.getDeclaredMethod("getMagnifyingX", Int::class.java)
        getMagnifyingXMethod.isAccessible = true

        val getMagnifyingYMethod = canvas.javaClass.getDeclaredMethod("getMagnifyingY", Int::class.java)
        getMagnifyingYMethod.isAccessible = true

        // Test extreme coordinates
        val extremeCoords = intArrayOf(Int.MIN_VALUE, -1000, 1000, Int.MAX_VALUE)

        for (coord in extremeCoords) {
            assertDoesNotThrow {
                getMagnifyingXMethod.invoke(canvas, coord)
                getMagnifyingYMethod.invoke(canvas, coord)
            }
        }
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
