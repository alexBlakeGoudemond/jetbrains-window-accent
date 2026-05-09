package com.window_color_panel.configuration.settings

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mockConstruction
import org.mockito.Mockito.`when`
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.Robot

@DisplayName("ScreenColorPicker Take Screenshot Tests")
class ScreenColorPickerTakeScreenshotTest : BaseScreenColorPickerTest() {

    @Test
    @DisplayName("takeScreenshot should throw when screen width is 0")
    fun testTakeScreenshotInvalidWidth() {
        val invalidSize = Dimension(0, 1080)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            takeScreenshot(invalidSize)
        }

        assertTrue(exception.message?.contains("Invalid screen size") ?: false)
    }

    @Test
    @DisplayName("takeScreenshot should throw when screen height is 0")
    fun testTakeScreenshotInvalidHeight() {
        val invalidSize = Dimension(1920, 0)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            takeScreenshot(invalidSize)
        }

        assertTrue(exception.message?.contains("Invalid screen size") ?: false)
    }

    @Test
    @DisplayName("takeScreenshot should throw RuntimeException when Robot fails")
    fun testTakeScreenshotRobotFailure() {
        val validSize = Dimension(1920, 1080)

        mockConstruction(Robot::class.java) { mockRobot, _ ->
            `when`(mockRobot.createScreenCapture(any(Rectangle::class.java)))
                .thenThrow(RuntimeException("Robot error"))
        }.use {
            val exception = assertThrows(RuntimeException::class.java) {
                takeScreenshot(validSize)
            }
            assertTrue(exception.message?.contains("Failed to capture screenshot") ?: false)
        }
    }

}

