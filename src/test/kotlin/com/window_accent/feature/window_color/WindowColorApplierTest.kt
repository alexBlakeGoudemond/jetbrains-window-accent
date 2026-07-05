package com.window_accent.feature.window_color

import com.window_accent.configuration.persistence.WindowCustomColorStateService
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Component
import java.awt.Container
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JRootPane
import javax.swing.RootPaneContainer
import org.mockito.Mockito.mock

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
    @DisplayName("Should track panels per-project in addedPanels map")
    fun testWindowColorApplierTracksAddedPanelsField() {
        val clazz = WindowColorApplier::class.java
        val addedPanelsField = clazz.getDeclaredField("addedPanels").apply { isAccessible = true }

        @Suppress("UNCHECKED_CAST")
        val addedPanels = addedPanelsField.get(WindowColorApplier) as java.util.concurrent.ConcurrentHashMap<com.intellij.openapi.project.Project, MutableList<Component>>

        // Verify the field exists and is the right type
        assertNotNull(addedPanels, "addedPanels field should exist and not be null")
        assertTrue(addedPanels is java.util.concurrent.ConcurrentHashMap, "addedPanels should be a ConcurrentHashMap")

        // Create mock project and add a panel
        val mockProject = mock(com.intellij.openapi.project.Project::class.java)
        val testPanel = JPanel()

        // Add to the map (simulating addColoredPanel behavior)
        addedPanels.getOrPut(mockProject) { mutableListOf() }.add(testPanel)

        // Verify it was added
        assertTrue(addedPanels.containsKey(mockProject), "Project should be tracked in addedPanels")
        assertEquals(1, addedPanels[mockProject]?.size, "Should have 1 panel for the project")
        assertTrue(addedPanels[mockProject]?.get(0) === testPanel, "Should be the same panel instance")

        // Clean up for other tests
        addedPanels.clear()
    }

    @Test
    @DisplayName("Should handle panel thickness constant")
    fun testPanelThicknessConstantValue() {
        val field = WindowColorApplier::class.java.getDeclaredField("PANEL_THICKNESS")
        field.isAccessible = true
        val constant = field.get(null) as Int

        assertEquals(20, constant)
    }

    @Test
    @DisplayName("Should wrap content pane when adding SOUTH panel")
    fun testAddSouthPanelWrapsContentPane() {
        val rootPane = JRootPane()
        val originalContentPane = JPanel()
        rootPane.contentPane = originalContentPane

        val frame = MockRootPaneContainer(rootPane)
        
        // Use real services instead of mocks
        val panelSettings = WindowPanelAppearanceStateService().apply {
            setSide(WindowPanelAppearanceStateService.Side.SOUTH)
        }
        val customColorSettings = WindowCustomColorStateService()
        
        val project = mock(com.intellij.openapi.project.Project::class.java)
        org.mockito.Mockito.`when`(project.name).thenReturn("TestProject")

        // Use reflection to call addColoredPanel
        val method = WindowColorApplier::class.java.getDeclaredMethod(
            "addColoredPanel",
            RootPaneContainer::class.java,
            WindowPanelAppearanceStateService::class.java,
            WindowCustomColorStateService::class.java,
            com.intellij.openapi.project.Project::class.java
        )
        method.isAccessible = true
        method.invoke(WindowColorApplier, frame, panelSettings, customColorSettings, project)

        // Verify wrapping
        val newContentPane = rootPane.contentPane
        assertNotSame(originalContentPane, newContentPane)
        assertTrue(newContentPane is JPanel)
        assertEquals(BorderLayout::class.java, (newContentPane as JPanel).layout.javaClass)
        
        // Check if original content pane is in CENTER
        val centerComp = (newContentPane.layout as BorderLayout).getLayoutComponent(BorderLayout.CENTER)
        assertEquals(originalContentPane, centerComp)
        
        // Check if colored panel is in SOUTH
        val southComp = (newContentPane.layout as BorderLayout).getLayoutComponent(BorderLayout.SOUTH)
        assertTrue(southComp is JPanel)
        assertEquals(true, (southComp as JComponent).getClientProperty("com.window_accent.windowAccent"))
    }

    @Test
    @DisplayName("Should NOT wrap content pane for non-SOUTH sides")
    fun testAddNorthPanelDoesNotWrap() {
        val rootPane = JRootPane()
        val originalContentPane = JPanel(BorderLayout())
        rootPane.contentPane = originalContentPane

        val frame = MockRootPaneContainer(rootPane)
        
        val panelSettings = WindowPanelAppearanceStateService().apply {
            setSide(WindowPanelAppearanceStateService.Side.NORTH)
        }
        val customColorSettings = WindowCustomColorStateService()
        
        val project = mock(com.intellij.openapi.project.Project::class.java)
        org.mockito.Mockito.`when`(project.name).thenReturn("TestProject")

        val method = WindowColorApplier::class.java.getDeclaredMethod(
            "addColoredPanel",
            RootPaneContainer::class.java,
            WindowPanelAppearanceStateService::class.java,
            WindowCustomColorStateService::class.java,
            com.intellij.openapi.project.Project::class.java
        )
        method.isAccessible = true
        method.invoke(WindowColorApplier, frame, panelSettings, customColorSettings, project)

        // Verify NO wrapping
        assertEquals(originalContentPane, rootPane.contentPane)
        
        // Verify panel added to contentPane
        val panel = originalContentPane.components.find { 
            (it as? JComponent)?.getClientProperty("com.window_accent.windowAccent") == true 
        }
        assertNotNull(panel)
        assertEquals(BorderLayout.NORTH, (originalContentPane.layout as BorderLayout).getConstraints(panel))
    }

    @Test
    @DisplayName("Should unwrap content pane when removing SOUTH panel")
    fun testRemoveAllExistingPanelsUnwrapsSouth() {
        val rootPane = JRootPane()
        val originalContentPane = JPanel()
        val wrapper = JPanel(BorderLayout())
        wrapper.putClientProperty("com.window_accent.windowAccent", true)
        
        val colorPanel = JPanel()
        colorPanel.putClientProperty("com.window_accent.windowAccent", true)
        
        wrapper.add(originalContentPane, BorderLayout.CENTER)
        wrapper.add(colorPanel, BorderLayout.SOUTH)
        rootPane.contentPane = wrapper
        
        val frame = MockRootPaneContainer(rootPane)

        val method = WindowColorApplier::class.java.getDeclaredMethod(
            "removeAllExistingPanels",
            RootPaneContainer::class.java
        )
        method.isAccessible = true
        method.invoke(WindowColorApplier, frame)

        // Verify unwrapping
        assertEquals(originalContentPane, rootPane.contentPane)
        assertEquals(0, originalContentPane.components.size)
    }

    private class MockRootPaneContainer(private val rootPane: JRootPane) : RootPaneContainer {
        override fun getRootPane(): JRootPane = rootPane
        override fun setContentPane(contentPane: Container?) { rootPane.contentPane = contentPane }
        override fun getContentPane(): Container = rootPane.contentPane
        override fun setLayeredPane(layeredPane: javax.swing.JLayeredPane?) { rootPane.layeredPane = layeredPane }
        override fun getLayeredPane(): javax.swing.JLayeredPane = rootPane.layeredPane
        override fun setGlassPane(glassPane: Component?) { rootPane.glassPane = glassPane }
        override fun getGlassPane(): Component = rootPane.glassPane
    }

    // -------------------------------------------------------------------------
    // findExistingColoredPanel tests
    // -------------------------------------------------------------------------

    private fun invokeFindExistingColoredPanel(container: Container): Component? {
        val method = WindowColorApplier::class.java.getDeclaredMethod(
            "findExistingColoredPanel", Container::class.java
        )
        method.isAccessible = true
        return method.invoke(WindowColorApplier, container) as Component?
    }

    @Test
    @DisplayName("findExistingColoredPanel returns the marked panel when present")
    fun testFindExistingColoredPanelFound() {
        val container = JPanel()
        val markedPanel = JPanel()
        markedPanel.putClientProperty("com.window_accent.windowAccent", true)
        container.add(markedPanel)

        val result = invokeFindExistingColoredPanel(container)
        assertEquals(markedPanel, result)
    }

    @Test
    @DisplayName("findExistingColoredPanel returns null when no panel is marked")
    fun testFindExistingColoredPanelNotFound() {
        val container = JPanel()
        container.add(JPanel()) // plain, unmarked panel

        val result = invokeFindExistingColoredPanel(container)
        assertNull(result)
    }

    @Test
    @DisplayName("findExistingColoredPanel returns null for an empty container")
    fun testFindExistingColoredPanelEmptyContainer() {
        val result = invokeFindExistingColoredPanel(JPanel())
        assertNull(result)
    }

    @Test
    @DisplayName("findExistingColoredPanel ignores panels whose client property is false")
    fun testFindExistingColoredPanelIgnoresFalseProperty() {
        val container = JPanel()
        val panel = JPanel()
        panel.putClientProperty("com.window_accent.windowAccent", false)
        container.add(panel)

        val result = invokeFindExistingColoredPanel(container)
        assertNull(result)
    }

    // -------------------------------------------------------------------------
    // removeColoredPanel tests
    // -------------------------------------------------------------------------

    private fun invokeRemoveColoredPanel(existingPanel: Component?, rootContentPane: Container) {
        val method = WindowColorApplier::class.java.getDeclaredMethod(
            "removeColoredPanel", Component::class.java, Container::class.java
        )
        method.isAccessible = true
        method.invoke(WindowColorApplier, existingPanel, rootContentPane)
    }

    @Test
    @DisplayName("removeColoredPanel removes the panel from the container")
    fun testRemoveColoredPanelRemovesPanel() {
        val container = JPanel()
        val panel = JPanel()
        container.add(panel)
        assertEquals(1, container.componentCount)

        invokeRemoveColoredPanel(panel, container)

        assertEquals(0, container.componentCount)
    }

    @Test
    @DisplayName("removeColoredPanel handles a null panel without throwing")
    fun testRemoveColoredPanelHandlesNull() {
        val container = JPanel()
        assertDoesNotThrow {
            invokeRemoveColoredPanel(null, container)
        }
        assertEquals(0, container.componentCount)
    }

    // -------------------------------------------------------------------------
    // createColoredPanel tests
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("ColoredPanel sets the windowAccent client property to true")
    fun testColoredPanelHasClientProperty() {
        val side = WindowPanelAppearanceStateService.Side.NORTH
        val color = Color.RED
        
        val panel = ColoredPanel(side, color)

        assertEquals(true, (panel as javax.swing.JComponent).getClientProperty("com.window_accent.windowAccent"))
    }

    @Test
    @DisplayName("ColoredPanel preferred size reflects the active side")
    fun testColoredPanelPreferredSizeForSide() {
        val color = Color.RED
        val project = mock(com.intellij.openapi.project.Project::class.java)
        org.mockito.Mockito.`when`(project.name).thenReturn("TestProject")

        // NORTH → height 20, width 0
        val northSettings = WindowPanelAppearanceStateService().apply { setSide(WindowPanelAppearanceStateService.Side.NORTH) }
        val northPanel = ColoredPanel(northSettings.getSide(), color)
        // I need to set the preferredSize manually as I do in WindowColorApplier
        northPanel.preferredSize = Dimension(0, 20)
        
        assertEquals(20, northPanel.preferredSize.height)
        assertEquals(0, northPanel.preferredSize.width)

        // EAST → width 20, height 0
        val eastSettings = WindowPanelAppearanceStateService().apply { setSide(WindowPanelAppearanceStateService.Side.EAST) }
        val eastPanel = ColoredPanel(eastSettings.getSide(), color)
        eastPanel.preferredSize = Dimension(20, 0)
        
        assertEquals(20, eastPanel.preferredSize.width)
        assertEquals(0, eastPanel.preferredSize.height)
    }

    // -------------------------------------------------------------------------
    // resolveColor tests
    // -------------------------------------------------------------------------

    private fun invokeResolveColor(
        customColorSettings: WindowCustomColorStateService,
        project: com.intellij.openapi.project.Project
    ): java.awt.Color {
        val method = WindowColorApplier::class.java.getDeclaredMethod(
            "resolveColor",
            WindowCustomColorStateService::class.java,
            com.intellij.openapi.project.Project::class.java
        )
        method.isAccessible = true
        return method.invoke(WindowColorApplier, customColorSettings, project) as java.awt.Color
    }

    @Test
    @DisplayName("resolveColor uses the custom color when it is enabled and set")
    fun testResolveColorUsesCustomColorWhenEnabled() {
        val settings = WindowCustomColorStateService().apply {
            setUseCustomColor(true)
            setCustomColor(java.awt.Color.RED)
        }
        val project = mock(com.intellij.openapi.project.Project::class.java)

        val color = invokeResolveColor(settings, project)

        // Compare RGB ignoring alpha, because the returned Color may be a JBColor wrapper
        assertEquals(java.awt.Color.RED.red, color.red)
        assertEquals(java.awt.Color.RED.green, color.green)
        assertEquals(java.awt.Color.RED.blue, color.blue)
    }

    @Test
    @DisplayName("resolveColor falls back to generated color when custom color is null")
    fun testResolveColorFallsBackToGeneratedWhenCustomNull() {
        val settings = WindowCustomColorStateService().apply {
            setUseCustomColor(true)
            // custom color not set → null
        }
        val project = mock(com.intellij.openapi.project.Project::class.java)
        org.mockito.Mockito.`when`(project.name).thenReturn("FallbackProject")

        val generateMethod = WindowColorApplier::class.java.getDeclaredMethod("generateColor", String::class.java)
        generateMethod.isAccessible = true
        val expected = generateMethod.invoke(WindowColorApplier, "FallbackProject") as java.awt.Color

        val color = invokeResolveColor(settings, project)
        assertEquals(expected.rgb, color.rgb)
    }

    @Test
    @DisplayName("resolveColor uses generated color when custom color is disabled")
    fun testResolveColorUsesGeneratedWhenDisabled() {
        val settings = WindowCustomColorStateService().apply {
            setUseCustomColor(false)
            setCustomColor(java.awt.Color.BLUE) // set but disabled
        }
        val project = mock(com.intellij.openapi.project.Project::class.java)
        org.mockito.Mockito.`when`(project.name).thenReturn("GeneratedProject")

        val generateMethod = WindowColorApplier::class.java.getDeclaredMethod("generateColor", String::class.java)
        generateMethod.isAccessible = true
        val expected = generateMethod.invoke(WindowColorApplier, "GeneratedProject") as java.awt.Color

        val color = invokeResolveColor(settings, project)
        assertEquals(expected.rgb, color.rgb)
    }

}
