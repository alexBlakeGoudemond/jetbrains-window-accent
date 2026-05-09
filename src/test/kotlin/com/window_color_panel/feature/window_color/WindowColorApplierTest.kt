package com.window_color_panel.feature.window_color

import com.window_color_panel.configuration.persistence.WindowPanelAppearanceStateService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension

@DisplayName("WindowColorApplier Tests")
class WindowColorApplierTest {

    @Test
    @DisplayName("Should generate consistent colors from project names")
    fun testGenerateColorConsistency() {
        // Test the generateColor function via reflection
        val method = WindowColorApplier::class.java.getDeclaredMethod("generateColor", String::class.java)
        method.isAccessible = true

        // Test that same project name always generates same color
        val color1 = method.invoke(WindowColorApplier, "MyProject") as Color
        val color2 = method.invoke(WindowColorApplier, "MyProject") as Color
        val color3 = method.invoke(WindowColorApplier, "MyProject") as Color

        assertEquals(color1.rgb, color2.rgb)
        assertEquals(color2.rgb, color3.rgb)

        // Test that different project names generate different colors
        val differentColor = method.invoke(WindowColorApplier, "DifferentProject") as Color
        assertNotEquals(color1.rgb, differentColor.rgb)
    }

    @Test
    @DisplayName("Should generate valid colors with alpha")
    fun testGenerateColorProperties() {
        val method = WindowColorApplier::class.java.getDeclaredMethod("generateColor", String::class.java)
        method.isAccessible = true

        val colors = listOf("Project1", "Project2", "Project3", "").map { seed ->
            method.invoke(WindowColorApplier, seed) as Color
        }

        colors.forEach { color ->
            // All colors should have the expected alpha (180)
            assertEquals(180, color.alpha, "Color should have alpha 180")

            // RGB values should be valid (0-255)
            assertTrue(color.red in 0..255, "Red component should be 0-255")
            assertTrue(color.green in 0..255, "Green component should be 0-255")
            assertTrue(color.blue in 0..255, "Blue component should be 0-255")
        }
    }

    @Test
    @DisplayName("Should calculate correct panel dimensions")
    fun testPanelDimension() {
        val method = WindowColorApplier::class.java.getDeclaredMethod(
            "panelDimension",
            WindowPanelAppearanceStateService.Side::class.java
        )
        method.isAccessible = true

        val northDim = method.invoke(WindowColorApplier, WindowPanelAppearanceStateService.Side.NORTH) as Dimension
        val southDim = method.invoke(WindowColorApplier, WindowPanelAppearanceStateService.Side.SOUTH) as Dimension

        assertEquals(0, northDim.width)
        assertEquals(20, northDim.height)
        assertEquals(0, southDim.width)
        assertEquals(20, southDim.height)

        val eastDim = method.invoke(WindowColorApplier, WindowPanelAppearanceStateService.Side.EAST) as Dimension
        val westDim = method.invoke(WindowColorApplier, WindowPanelAppearanceStateService.Side.WEST) as Dimension

        assertEquals(20, eastDim.width)
        assertEquals(0, eastDim.height)
        assertEquals(20, westDim.width)
        assertEquals(0, westDim.height)
    }

    @Test
    @DisplayName("Should map sides to correct border layout constraints")
    fun testBorderLayoutConstraint() {
        val method = WindowColorApplier::class.java.getDeclaredMethod(
            "borderLayoutConstraint",
            WindowPanelAppearanceStateService.Side::class.java
        )
        method.isAccessible = true

        assertEquals(BorderLayout.EAST, method.invoke(WindowColorApplier, WindowPanelAppearanceStateService.Side.EAST))
        assertEquals(BorderLayout.WEST, method.invoke(WindowColorApplier, WindowPanelAppearanceStateService.Side.WEST))
        assertEquals(
            BorderLayout.NORTH,
            method.invoke(WindowColorApplier, WindowPanelAppearanceStateService.Side.NORTH)
        )
        assertEquals(
            BorderLayout.SOUTH,
            method.invoke(WindowColorApplier, WindowPanelAppearanceStateService.Side.SOUTH)
        )
    }

    @Test
    @DisplayName("Should handle color resolution logic simulation")
    fun testColorResolutionLogic() {
        val generateColorMethod = WindowColorApplier::class.java.getDeclaredMethod("generateColor", String::class.java)
        generateColorMethod.isAccessible = true

        // Simulate color resolution logic
        fun resolveColor(useCustomColor: Boolean, customColor: Color?, projectName: String): Color {
            return if (useCustomColor) {
                customColor ?: (generateColorMethod.invoke(WindowColorApplier, projectName) as Color)
            } else {
                generateColorMethod.invoke(WindowColorApplier, projectName) as Color
            }
        }

        val generatedColor = generateColorMethod.invoke(WindowColorApplier, "TestProject") as Color

        // Test with custom color enabled and provided
        val result1 = resolveColor(true, Color.RED, "TestProject")
        assertEquals(Color.RED, result1)

        // Test with custom color enabled but null
        val result2 = resolveColor(true, null, "TestProject")
        assertEquals(generatedColor, result2)

        // Test with custom color disabled
        val result3 = resolveColor(false, Color.BLUE, "TestProject")
        assertEquals(generatedColor, result3)
    }

    @Test
    @DisplayName("Should generate different colors for different project names")
    fun testGenerateColorVariety() {
        val method = WindowColorApplier::class.java.getDeclaredMethod("generateColor", String::class.java)
        method.isAccessible = true

        val projectNames = listOf("ProjectA", "ProjectB", "ProjectC", "AnotherProject", "Test123")
        val colors = projectNames.map { method.invoke(WindowColorApplier, it) as Color }

        // Check that we get some variety (at least not all identical)
        val uniqueColors = colors.distinctBy { it.rgb }
        assertTrue(uniqueColors.size > 1, "Should generate different colors for different project names")
    }

    @Test
    @DisplayName("Should handle empty and special project names")
    fun testGenerateColorEdgeCases() {
        val method = WindowColorApplier::class.java.getDeclaredMethod("generateColor", String::class.java)
        method.isAccessible = true

        // Test edge cases
        assertDoesNotThrow { method.invoke(WindowColorApplier, "") }
        assertDoesNotThrow { method.invoke(WindowColorApplier, "Project with spaces") }
        assertDoesNotThrow { method.invoke(WindowColorApplier, "Project-with-dashes") }
        assertDoesNotThrow { method.invoke(WindowColorApplier, "Project_with_underscores") }
        assertDoesNotThrow { method.invoke(WindowColorApplier, "Project123") }
        assertDoesNotThrow { method.invoke(WindowColorApplier, "🚀 Project with emoji") }

        // All should return valid colors
        val edgeCaseColors = listOf("", "A", "1", "Special chars: !@#\$%^&*()").map {
            method.invoke(WindowColorApplier, it) as Color
        }

        edgeCaseColors.forEach { color ->
            assertTrue(color.red in 0..255)
            assertTrue(color.green in 0..255)
            assertTrue(color.blue in 0..255)
            assertEquals(180, color.alpha)
        }
    }

    @Test
    @DisplayName("Should maintain panel thickness constant")
    fun testPanelThicknessConstant() {
        val method = WindowColorApplier::class.java.getDeclaredMethod(
            "panelDimension",
            WindowPanelAppearanceStateService.Side::class.java
        )
        method.isAccessible = true

        val thickness = 20

        // All sides should use the same thickness
        val dimensions = WindowPanelAppearanceStateService.Side.entries.map { side ->
            method.invoke(WindowColorApplier, side) as Dimension
        }

        dimensions.forEach { dim: Dimension ->
            val actualThickness = if (dim.width == 0) dim.height else dim.width
            assertEquals(thickness, actualThickness, "Panel thickness should be constant")
        }
    }

    @Test
    @DisplayName("Should handle all enum sides")
    fun testAllEnumSides() {
        val method = WindowColorApplier::class.java.getDeclaredMethod(
            "borderLayoutConstraint",
            WindowPanelAppearanceStateService.Side::class.java
        )
        method.isAccessible = true

        // Test all enum values are handled
        WindowPanelAppearanceStateService.Side.entries.forEach { side ->
            assertDoesNotThrow {
                method.invoke(WindowColorApplier, side)
            }
        }

        // Verify we get valid BorderLayout constants
        val validConstraints = setOf(BorderLayout.NORTH, BorderLayout.SOUTH, BorderLayout.EAST, BorderLayout.WEST)
        WindowPanelAppearanceStateService.Side.entries.forEach { side ->
            val constraint = method.invoke(WindowColorApplier, side) as String
            assertTrue(validConstraints.contains(constraint), "Should return valid BorderLayout constraint")
        }
    }

    @Test
    @DisplayName("Should generate colors with reasonable distribution")
    fun testColorDistribution() {
        val method = WindowColorApplier::class.java.getDeclaredMethod("generateColor", String::class.java)
        method.isAccessible = true

        // Generate colors for a few different seeds to ensure basic functionality
        val colors = listOf("Project1", "Project2", "Project3", "Project4", "Project5").map {
            method.invoke(WindowColorApplier, it) as Color
        }

        // Check that we get at least some variety (at least not all identical)
        val uniqueColors = colors.distinctBy { it.rgb }
        assertTrue(uniqueColors.size >= 3, "Should generate at least some different colors for different project names")

        // Ensure all colors are valid
        colors.forEach { color ->
            assertTrue(color.red in 0..255)
            assertTrue(color.green in 0..255)
            assertTrue(color.blue in 0..255)
            assertEquals(180, color.alpha)
        }
    }

    @Test
    @DisplayName("Should handle panel client property constant")
    fun testPanelClientPropertyConstant() {
        val field = WindowColorApplier::class.java.getDeclaredField("PANEL_CLIENT_PROPERTY")
        field.isAccessible = true
        val constant = field.get(null) as String

        assertEquals("com.window_color_panel.windowColorPanel", constant)
    }

    @Test
    @DisplayName("Should handle panel thickness constant")
    fun testPanelThicknessConstantValue() {
        val field = WindowColorApplier::class.java.getDeclaredField("PANEL_THICKNESS")
        field.isAccessible = true
        val constant = field.get(null) as Int

        assertEquals(20, constant)
    }
}
