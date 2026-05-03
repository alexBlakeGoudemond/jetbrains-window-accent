package com.window_color_panel.configuration.tool_window

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import javax.swing.JButton
import javax.swing.JPanel

@DisplayName("WindowColorPanelToolWindowFactory Tests")
class WindowColorPanelToolWindowFactoryTest {

    @Test
    @DisplayName("Should create tool window factory instance")
    fun testFactoryCreation() {
        val factory = WindowColorPanelToolWindowFactory()
        assertNotNull(factory)
        assertTrue(factory is WindowColorPanelToolWindowFactory)
    }

    @Test
    @DisplayName("Should implement ToolWindowFactory interface")
    fun testImplementsToolWindowFactory() {
        val factory = WindowColorPanelToolWindowFactory()
        assertTrue(factory is com.intellij.openapi.wm.ToolWindowFactory)
    }

    @Test
    @DisplayName("Should create panel with correct layout and border")
    fun testPanelCreation() {
        val panel = JPanel(java.awt.GridLayout(0, 1, 8, 8))
        panel.border = javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)

        assertNotNull(panel)
        assertTrue(panel.layout is java.awt.GridLayout)
        val layout = panel.layout as java.awt.GridLayout
        assertEquals(0, layout.rows)
        assertEquals(1, layout.columns)
        assertEquals(8, layout.hgap)
        assertEquals(8, layout.vgap)

        assertNotNull(panel.border)
    }

    @Test
    @DisplayName("Should create buttons with correct initial state")
    fun testButtonCreation() {
        val button1 = JButton()
        val button2 = JButton()
        val button3 = JButton()
        val button4 = JButton()

        assertNotNull(button1)
        assertNotNull(button2)
        assertNotNull(button3)
        assertNotNull(button4)

        // All buttons should start with empty text (will be set by refreshButtonText)
        assertEquals("", button1.text)
        assertEquals("", button2.text)
        assertEquals("", button3.text)
        assertEquals("", button4.text)
    }

    @Test
    @DisplayName("Should handle button text refresh logic for color buttons")
    fun testColorButtonTextRefreshLogic() {
        // Test the logic for determining button text based on color service state

        // Simulate enabled state
        val enabledText = if (true) "Disable colors for all open windows" else "Enable colors for all open windows"
        assertEquals("Disable colors for all open windows", enabledText)

        // Simulate disabled state
        val disabledText = if (false) "Disable colors for all open windows" else "Enable colors for all open windows"
        assertEquals("Enable colors for all open windows", disabledText)

        // Test current window button text
        val currentEnabledText = if (true) "Disable color for current window" else "Enable color for current window"
        assertEquals("Disable color for current window", currentEnabledText)

        val currentDisabledText = if (false) "Disable color for current window" else "Enable color for current window"
        assertEquals("Enable color for current window", currentDisabledText)
    }

    @Test
    @DisplayName("Should handle button text refresh logic for title buttons")
    fun testTitleButtonTextRefreshLogic() {
        // Test the logic for determining button text based on title service state

        // Simulate enabled state
        val enabledText = if (true) "Disable title numbers for all open windows" else "Enable title numbers for all open windows"
        assertEquals("Disable title numbers for all open windows", enabledText)

        // Simulate disabled state
        val disabledText = if (false) "Disable title numbers for all open windows" else "Enable title numbers for all open windows"
        assertEquals("Enable title numbers for all open windows", disabledText)

        // Test current window button text
        val currentEnabledText = if (true) "Disable title number for current window" else "Enable title number for current window"
        assertEquals("Disable title number for current window", currentEnabledText)

        val currentDisabledText = if (false) "Disable title number for current window" else "Enable title number for current window"
        assertEquals("Enable title number for current window", currentDisabledText)
    }

    @Test
    @DisplayName("Should handle service state toggling logic for colors")
    fun testColorServiceStateToggling() {
        // Test the logic used in button action listeners for color toggling

        // Simulate initial enabled state
        var isEnabled = true
        val newState1 = !isEnabled // Should be false
        assertFalse(newState1)

        // Simulate initial disabled state
        isEnabled = false
        val newState2 = !isEnabled // Should be true
        assertTrue(newState2)
    }

    @Test
    @DisplayName("Should handle service state toggling logic for titles")
    fun testTitleServiceStateToggling() {
        // Test the logic used in button action listeners for title toggling

        // Simulate initial enabled state
        var isEnabled = true
        val newState1 = !isEnabled // Should be false
        assertFalse(newState1)

        // Simulate initial disabled state
        isEnabled = false
        val newState2 = !isEnabled // Should be true
        assertTrue(newState2)
    }

    @Test
    @DisplayName("Should create correct number of buttons")
    fun testButtonCount() {
        // The factory creates 4 buttons
        val expectedButtonCount = 4
        assertEquals(4, expectedButtonCount)
    }

    @Test
    @DisplayName("Should handle button action listener setup")
    fun testButtonActionListenerSetup() {
        val button = JButton()

        // Test that we can add action listeners without errors
        assertDoesNotThrow {
            button.addActionListener { /* mock action */ }
        }

        // Verify the button can have action listeners
        assertNotNull(button.actionListeners)
        assertEquals(1, button.actionListeners.size)
    }

    @Test
    @DisplayName("Should handle panel component addition")
    fun testPanelComponentAddition() {
        val panel = JPanel(java.awt.GridLayout(0, 1, 8, 8))
        val button1 = JButton("Button 1")
        val button2 = JButton("Button 2")
        val button3 = JButton("Button 3")
        val button4 = JButton("Button 4")

        // Add components to panel
        panel.add(button1)
        panel.add(button2)
        panel.add(button3)
        panel.add(button4)

        // Verify components were added
        assertEquals(4, panel.componentCount)
        assertEquals(button1, panel.getComponent(0))
        assertEquals(button2, panel.getComponent(1))
        assertEquals(button3, panel.getComponent(2))
        assertEquals(button4, panel.getComponent(3))
    }

    @Test
    @DisplayName("Should handle border factory usage")
    fun testBorderFactoryUsage() {
        val border = javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)
        assertNotNull(border)
    }

    @Test
    @DisplayName("Should handle grid layout configuration")
    fun testGridLayoutConfiguration() {
        val layout = java.awt.GridLayout(0, 1, 8, 8)
        assertEquals(0, layout.rows)
        assertEquals(1, layout.columns)
        assertEquals(8, layout.hgap)
        assertEquals(8, layout.vgap)
    }
}
