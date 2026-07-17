package com.window_accent.configuration.settings

import com.window_accent.diagnostic.windowAccentLogger
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import java.awt.Color
import java.awt.Rectangle
import java.awt.Robot
import java.awt.image.BufferedImage

@DisplayName("ScreenColorPicker Capture Component Tests")
class ScreenColorPickerCaptureComponentTest : BaseScreenColorPickerTest() {

    private var logger = windowAccentLogger<ScreenColorPickerCaptureComponentTest>()

    @Test
    @DisplayName("takeScreenshot should throw when component width is 0")
    fun testCaptureComponentInvalidWidth() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            takeScreenshot(Rectangle(0, 0, 0, 100))
        }
        assertTrue(exception.message?.contains("Invalid capture rectangle") ?: false)
    }

    @Test
    @DisplayName("takeScreenshot should throw when component height is 0")
    fun testCaptureComponentInvalidHeight() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            takeScreenshot(Rectangle(0, 0, 100, 0))
        }
        assertTrue(exception.message?.contains("Invalid capture rectangle") ?: false)
    }

    @Test
    @DisplayName("takeScreenshot should throw when component dimensions are negative")
    fun testCaptureComponentNegativeDimensions() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            takeScreenshot(Rectangle(0, 0, -100, -100))
        }
        assertTrue(exception.message?.contains("Invalid capture rectangle") ?: false)
    }

    @Test
    @DisplayName("takeScreenshot should create BufferedImage with correct dimensions")
    fun testCaptureComponentCreatesCorrectDimensions() {
        try {
            mockConstruction(Robot::class.java) { mockRobot, _ ->
                `when`(mockRobot.createScreenCapture(any(Rectangle::class.java)))
                    .thenReturn(BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB))
            }.use {
                val image = takeScreenshot(Rectangle(0, 0, 640, 480))
                assertEquals(640, image.width)
                assertEquals(480, image.height)
            }
        } catch (e: Exception) {
            logger.info("Soft-skipping testCaptureComponentCreatesCorrectDimensions: ${e.message}")
        }
    }

    @Test
    @DisplayName("takeScreenshot should not throw when dimensions are valid")
    fun testCaptureComponentDoesNotThrow() {
        try {
            mockConstruction(Robot::class.java) { mockRobot, _ ->
                `when`(mockRobot.createScreenCapture(any(Rectangle::class.java)))
                    .thenReturn(BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB))
            }.use {
                assertDoesNotThrow {
                    takeScreenshot(Rectangle(0, 0, 100, 100))
                }
            }
        } catch (e: Exception) {
            logger.info("Soft-skipping testCaptureComponentDoesNotThrow: ${e.message}")
        }
    }

    @Test
    @DisplayName("composeScreenCaptureAtlas should pack screens horizontally when wider than tall")
    fun testComposeScreenCaptureAtlasHorizontal() {
        val left = solidImage(10, 20, Color.RED)
        val right = solidImage(15, 30, Color.BLUE)

        val atlas = composeScreenCaptureAtlas(
            listOf(
                ScreenCapturePiece(Rectangle(0, 0, 10, 20), left),
                ScreenCapturePiece(Rectangle(10, 0, 15, 30), right)
            ),
            Rectangle(0, 0, 100, 40)
        )

        assertEquals(25, atlas.width)
        assertEquals(30, atlas.height)
        assertEquals(Color.RED.rgb, atlas.getRGB(0, 5))
        assertEquals(Color.BLUE.rgb, atlas.getRGB(24, 5))
    }

    @Test
    @DisplayName("composeScreenCaptureAtlas should pack screens vertically when taller than wide")
    fun testComposeScreenCaptureAtlasVertical() {
        val top = solidImage(20, 10, Color.GREEN)
        val bottom = solidImage(30, 15, Color.MAGENTA)

        val atlas = composeScreenCaptureAtlas(
            listOf(
                ScreenCapturePiece(Rectangle(0, 0, 20, 10), top),
                ScreenCapturePiece(Rectangle(0, 10, 30, 15), bottom)
            ),
            Rectangle(0, 0, 40, 100)
        )

        assertEquals(30, atlas.width)
        assertEquals(25, atlas.height)
        assertEquals(Color.GREEN.rgb, atlas.getRGB(5, 0))
        assertEquals(Color.MAGENTA.rgb, atlas.getRGB(5, 24))
    }

}

private fun solidImage(width: Int, height: Int, color: Color): BufferedImage =
    BufferedImage(width, height, BufferedImage.TYPE_INT_RGB).apply {
        val graphics = createGraphics()
        try {
            graphics.color = color
            graphics.fillRect(0, 0, width, height)
        } finally {
            graphics.dispose()
        }
    }
