package com.window_accent.configuration.settings

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.awt.Color
import java.awt.image.BufferedImage
import java.lang.reflect.Method
import javax.swing.JComponent

@DisplayName("ColorMagnifier Tests")
class ColorMagnifierTest {

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

        val zoomRadius = getPrivateFieldAsInt(canvas, "zoomRadius")
        assertEquals(12, zoomRadius)
    }

    @Test
    @DisplayName("createMagnifierCanvas should have correct loupe size")
    fun testMagnifierCanvasLoupeSize() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
        val displayMousePoint = { Pair(50.0, 50.0) }

        val canvas = createMagnifierCanvas(screenshot, displayMousePoint)

        val loupeSize = getPrivateFieldAsInt(canvas, "loupeSize")
        assertEquals(180, loupeSize)
    }

    @Test
    @DisplayName("createMagnifierCanvas should have correct loupe margin")
    fun testMagnifierCanvasLoupeMargin() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
        val displayMousePoint = { Pair(50.0, 50.0) }

        val canvas = createMagnifierCanvas(screenshot, displayMousePoint)

        val loupeMargin = getPrivateFieldAsInt(canvas, "loupeMargin")
        assertEquals(24, loupeMargin)
    }

    @Test
    @DisplayName("Should handle mouse point within screenshot bounds")
    fun testMousePointWithinBounds() {
        val width = 200
        val height = 200
        val screenshot = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val displayMousePoint = { Pair(50.0, 50.0) }

        val canvas = createMagnifierCanvas(screenshot, displayMousePoint)
        canvas.setSize(width, height)

        val getMagnifyingXMethod = getPrivateMethod(canvas, "getMagnifyingX")
        getMagnifyingXMethod.isAccessible = true
        val getMagnifyingYMethod = getPrivateMethod(canvas, "getMagnifyingY")
        getMagnifyingYMethod.isAccessible = true

        val variousMousePositions = getMousePositionRange(width)

        for (mouseX in variousMousePositions) {
            val resultX = getMagnifyingXMethod.invoke(canvas, mouseX) as Int
            assertTrue(resultX >= 0, "Magnifying X should be non-negative for mouseX=$mouseX, got $resultX")
        }
        for (mouseY in variousMousePositions) {
            val resultY = getMagnifyingYMethod.invoke(canvas, mouseY) as Int
            assertTrue(resultY >= 0, "Magnifying Y should be non-negative for mouseY=$mouseY, got $resultY")
        }
    }

    private fun getMousePositionRange(@Suppress("SameParameterValue") width: Int): List<Int> {
        return listOf(
            0,
            (width * 0.25).toInt(),
            (width * 0.5).toInt(),
            (width * 0.75).toInt(),
            width - 1
        )
    }

    private fun getPrivateFieldAsInt(canvas: JComponent, privateField: String): Int {
        val zoomRadiusField = canvas.javaClass.getDeclaredField(privateField)
        zoomRadiusField.isAccessible = true
        val zoomRadius = zoomRadiusField.get(canvas) as Int
        return zoomRadius
    }

    private fun getPrivateMethod(canvas: JComponent, privateMethod: String): Method {
        val getMagnifyingXMethod = canvas.javaClass.getDeclaredMethod(privateMethod, Int::class.java)
        return getMagnifyingXMethod
    }

    @Test
    @DisplayName("Should handle mouse point at screenshot edges")
    fun testMousePointAtEdges() {
        val width = 200
        val height = 200
        val screenshot = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val displayMousePoint = { Pair(0.0, 0.0) }

        val canvas = createMagnifierCanvas(screenshot, displayMousePoint)
        canvas.setSize(width, height)

        val getMagnifyingXMethod = getPrivateMethod(canvas, "getMagnifyingX")
        getMagnifyingXMethod.isAccessible = true
        val getMagnifyingYMethod = getPrivateMethod(canvas, "getMagnifyingY")
        getMagnifyingYMethod.isAccessible = true

        assertDoesNotThrow {
            getMagnifyingXMethod.invoke(canvas, 0)
            getMagnifyingXMethod.invoke(canvas, width - 1)
            getMagnifyingYMethod.invoke(canvas, 0)
            getMagnifyingYMethod.invoke(canvas, height - 1)
        }
    }

    @Test
    @DisplayName("Should handle screenshot smaller than zoom radius")
    fun testScreenshotSmallerThanZoomRadius() {
        val width = 10
        val height = 10
        val screenshot = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        val mouseX = width * 0.5
        val mouseY = height * 0.5
        assertDoesNotThrow {
            createMagnifierCanvas(screenshot) { Pair(mouseX, mouseY) }
        }
    }

    @Test
    @DisplayName("Should handle display mouse point outside screenshot bounds")
    fun testDisplayMousePointOutsideBounds() {
        val width = 100
        val height = 100
        val screenshot = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        val mouseX = width * 1.5
        val mouseY = height * 1.5

        assertDoesNotThrow {
            createMagnifierCanvas(screenshot) { Pair(mouseX, mouseY) }
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
        val width = 100
        val height = 100
        val screenshot = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val displayMousePoint = { Pair(25.0, 25.0) }

        val canvas = createMagnifierCanvas(screenshot, displayMousePoint)

        val getMagnifyingXMethod = getPrivateMethod(canvas, "getMagnifyingX")
        getMagnifyingXMethod.isAccessible = true
        val getMagnifyingYMethod = getPrivateMethod(canvas, "getMagnifyingY")
        getMagnifyingYMethod.isAccessible = true

        val extremeX = width * -10
        val extremeY = height * -10
        val extremeCoords = intArrayOf(Int.MIN_VALUE, extremeX, extremeY, Int.MAX_VALUE)

        for (coordinate in extremeCoords) {
            assertDoesNotThrow {
                getMagnifyingXMethod.invoke(canvas, coordinate)
                getMagnifyingYMethod.invoke(canvas, coordinate)
            }
        }
    }

    @Test
    @DisplayName("toHex should handle alpha channel correctly")
    fun testToHexIgnoresAlpha() {
        val colorWithAlpha = Color(255, 0, 0, 128) // Red with 50% alpha
        val colorWithoutAlpha = Color(255, 0, 0)   // Solid red
        val red = Color.RED

        // toHex should only consider RGB values, not alpha
        assertEquals(toHex(colorWithoutAlpha), toHex(colorWithAlpha))
        assertEquals(toHex(red), toHex(colorWithAlpha))
    }

}
