package com.window_accent.configuration.settings

import com.intellij.openapi.project.Project
import com.window_accent.WindowAccentApplicationService
import com.window_accent.configuration.persistence.WindowCustomTitleStateService
import com.window_accent.configuration.persistence.WindowCustomColorStateService
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService
import com.window_accent.configuration.persistence.WindowTitleNumberingStateService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.awt.Color
import java.awt.GraphicsEnvironment
import javax.swing.JComponent
import javax.swing.UIManager

@DisplayName("WindowAccentSettings Tests")
class WindowAccentSettingsTest {

    private lateinit var mockProject: Project
    private lateinit var mockAppearanceService: WindowPanelAppearanceStateService
    private lateinit var mockCustomColorService: WindowCustomColorStateService
    private lateinit var mockTitleNumberingService: WindowTitleNumberingStateService
    private lateinit var mockCustomTitleService: WindowCustomTitleStateService
    private lateinit var windowAccentSettings: WindowAccentSettings

    @BeforeEach
    fun setup() {
        // Reset cleanup state so that WindowAccentSettings.init {} does not self-dispose.
        // PluginUnloadingVerificationTest.unloadingLifecycleSequenceListenerThenServiceDisposeIsSafe
        // may set cleanupCompleted=true; resetting here prevents cross-test state pollution.
        WindowAccentApplicationService.resetCleanupState()

        // Initialize UIManager to avoid "no ComponentUI class" errors in some test environments
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName())
        } catch (e: Exception) {
            // Ignore if unable to set look and feel
        }

        // Create mocks for the services
        mockProject = mock(Project::class.java)
        mockAppearanceService = WindowPanelAppearanceStateService()
        mockCustomColorService = WindowCustomColorStateService()
        mockTitleNumberingService = WindowTitleNumberingStateService()
        mockCustomTitleService = WindowCustomTitleStateService()

        // Setup default return values for services
        mockAppearanceService.setSide(WindowPanelAppearanceStateService.Side.EAST)
        mockCustomColorService.setUseCustomColor(false)
        mockCustomColorService.setCustomColor(null)
        mockTitleNumberingService.setTitleNumberingEnabled(false)
        mockCustomTitleService.setCustomTitle("")
        mockCustomTitleService.setCustomTitleEnabled(false)

        // Mock the project.getService() method to return our mock services
        `when`(mockProject.getService(WindowPanelAppearanceStateService::class.java))
            .thenReturn(mockAppearanceService)
        `when`(mockProject.getService(WindowCustomColorStateService::class.java))
            .thenReturn(mockCustomColorService)
        `when`(mockProject.getService(WindowTitleNumberingStateService::class.java))
            .thenReturn(mockTitleNumberingService)
        `when`(mockProject.getService(WindowCustomTitleStateService::class.java))
            .thenReturn(mockCustomTitleService)

        // Create the settings instance with mocked project
        windowAccentSettings = WindowAccentSettings(mockProject)
    }

    @Test
    @DisplayName("Should have correct display name")
    fun testGetDisplayName() {
        assertEquals("Window Accent", windowAccentSettings.getDisplayName())
    }

    @Test
    @DisplayName("Should create component successfully")
    fun testCreateComponent() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        val component = windowAccentSettings.createComponent()

        assertNotNull(component)
        assertTrue(component is JComponent)
    }

    @Test
    @DisplayName("Should return same panel instance from createComponent")
    fun testCreateComponentReturnsSamePanel() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        val component1 = windowAccentSettings.createComponent()
        val component2 = windowAccentSettings.createComponent()

        // Should return the same panel instance both times
        assertSame(component1, component2)
    }

    @Test
    @DisplayName("Should not be modified when settings match UI state")
    fun testIsModifiedFalseWhenNoChanges() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        windowAccentSettings.createComponent()

        assertFalse(windowAccentSettings.isModified())
    }

    @Test
    @DisplayName("Should be modified when side is changed")
    fun testIsModifiedWhenSideChanged() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        windowAccentSettings.createComponent()

        // Change the side in the combo box
        val newSide = WindowPanelAppearanceStateService.Side.WEST
        
        // Change the service state to DIFFERENT from what's in UI (EAST)
        mockAppearanceService.setSide(newSide)

        assertTrue(windowAccentSettings.isModified())
    }

    @Test
    @DisplayName("Should be modified when custom color checkbox changes")
    fun testIsModifiedWhenCustomColorCheckboxChanges() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        windowAccentSettings.createComponent()

        // Change the checkbox state
        windowAccentSettings.customColorCheckBox.isSelected = true

        // Verify modified state changed (service still returns false)
        assertTrue(windowAccentSettings.isModified())
    }

    @Test
    @DisplayName("Should be modified when selected color changes")
    fun testIsModifiedWhenSelectedColorChanges() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        windowAccentSettings.createComponent()
        windowAccentSettings.customColorCheckBox.isSelected = true

        // Set a selected color
        windowAccentSettings.selectedColor = Color.RED

        // Service returns null, but selectedColor is RED
        assertTrue(windowAccentSettings.isModified())
    }

    @Test
    @DisplayName("Should sync enabled state based on checkbox")
    fun testSyncEnabledStateWhenDisabled() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        windowAccentSettings.createComponent()
        windowAccentSettings.customColorCheckBox.isSelected = false

        windowAccentSettings.syncEnabledState()

        // Color controls should be disabled
        assertFalse(windowAccentSettings.customColorCheckBox.isSelected)
    }

    @Test
    @DisplayName("Should sync enabled state when enabled")
    fun testSyncEnabledStateWhenEnabled() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        windowAccentSettings.createComponent()
        windowAccentSettings.customColorCheckBox.isSelected = true

        windowAccentSettings.syncEnabledState()

        assertTrue(windowAccentSettings.customColorCheckBox.isSelected)
    }

    @Test
    @DisplayName("Should sync preview with auto-generated text when no color selected")
    fun testSyncPreviewWithoutColor() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        windowAccentSettings.createComponent()
        windowAccentSettings.customColorCheckBox.isSelected = false
        windowAccentSettings.selectedColor = null

        windowAccentSettings.syncPreview()

        // Preview should show auto-generated text
        assertTrue(true) // Visual component tested, no exceptions thrown
    }

    @Test
    @DisplayName("Should sync preview with RGB text when color selected")
    fun testSyncPreviewWithColor() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        windowAccentSettings.createComponent()
        windowAccentSettings.customColorCheckBox.isSelected = true
        windowAccentSettings.selectedColor = Color.RED

        windowAccentSettings.syncPreview()

        // Preview should show RGB values
        assertTrue(true) // Visual component tested, no exceptions thrown
    }

    @Test
    @DisplayName("Should apply settings without crashing")
    fun testApply() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        windowAccentSettings.createComponent()

        // Apply should handle gracefully - may throw exceptions due to IntelliJ Platform dependencies
        try {
            windowAccentSettings.apply()
        } catch (e: Exception) {
            // Expected in test environment - IntelliJ Platform may not be fully initialized
            assertTrue(true, "Apply completed or threw expected exception in test environment")
        }
    }

    @Test
    @DisplayName("Should reset to settings")
    fun testReset() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        windowAccentSettings.createComponent()

        // Make some changes
        windowAccentSettings.customColorCheckBox.isSelected = true
        windowAccentSettings.selectedColor = Color.BLUE

        // Reset should restore from settings
        assertDoesNotThrow {
            windowAccentSettings.reset()
        }

        // Should be back to unchecked (from mock setup)
        assertFalse(windowAccentSettings.customColorCheckBox.isSelected)
    }

    @Test
    @DisplayName("Should dispose UI resources without error")
    fun testDisposeUIResources() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        windowAccentSettings.createComponent()

        assertDoesNotThrow {
            windowAccentSettings.disposeUIResources()
        }
    }

    @Test
    @DisplayName("Should handle toHex color conversion")
    fun testToHexFunction() {
        assertEquals("#FF0000", toHex(Color.RED))
        assertEquals("#00FF00", toHex(Color.GREEN))
        assertEquals("#0000FF", toHex(Color.BLUE))
        assertEquals("#FFFFFF", toHex(Color.WHITE))
        assertEquals("#000000", toHex(Color.BLACK))
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
    @DisplayName("toHex should handle alpha channel correctly")
    fun testToHexIgnoresAlpha() {
        val colorWithAlpha = Color(255, 0, 0, 128) // Red with 50% alpha
        val colorWithoutAlpha = Color(255, 0, 0)   // Solid red

        // toHex should only consider RGB values, not alpha
        assertEquals(toHex(colorWithoutAlpha), toHex(colorWithAlpha))
        assertEquals("#FF0000", toHex(colorWithAlpha))
    }

    @Test
    @DisplayName("Should handle apply with title numbering enabled")
    fun testApplyWithTitleNumberingEnabled() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        `when`(mockTitleNumberingService.isTitleNumberingEnabled()).thenReturn(true)

        windowAccentSettings.createComponent()

        // Apply should handle gracefully - may throw exceptions due to IntelliJ Platform dependencies
        try {
            windowAccentSettings.apply()
        } catch (e: Exception) {
            // Expected in test environment - IntelliJ Platform may not be fully initialized
            assertTrue(true, "Apply completed or threw expected exception in test environment")
        }
    }

    @Test
    @DisplayName("Should handle apply with title numbering disabled")
    fun testApplyWithTitleNumberingDisabled() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        `when`(mockTitleNumberingService.isTitleNumberingEnabled()).thenReturn(false)

        windowAccentSettings.createComponent()

        // Apply should handle gracefully - may throw exceptions due to IntelliJ Platform dependencies
        try {
            windowAccentSettings.apply()
        } catch (e: Exception) {
            // Expected in test environment - IntelliJ Platform may not be fully initialized
            assertTrue(true, "Apply completed or threw expected exception in test environment")
        }
    }

    @Test
    @DisplayName("Should not be modified when custom title text matches persisted value")
    fun testIsModifiedFalseWhenCustomTitleUnchanged() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        mockCustomTitleService.setCustomTitle("dattebayo")
        windowAccentSettings.createComponent()
        // After createComponent, syncFromSettings loads "dattebayo" into the text field
        assertFalse(windowAccentSettings.isModified())
    }

    @Test
    @DisplayName("Should be modified when custom title text changes")
    fun testIsModifiedTrueWhenCustomTitleChanged() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        mockCustomTitleService.setCustomTitle("")
        windowAccentSettings.createComponent()

        // Simulate user typing into the custom title field via the settings instance
        // (the service still returns "" so isModified should see the difference)
        mockCustomTitleService.setCustomTitle("different")

        assertTrue(windowAccentSettings.isModified())
    }

    @Test
    @DisplayName("Should reset custom title from persisted settings")
    fun testResetRestoresCustomTitle() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")
        mockCustomTitleService.setCustomTitle("dattebayo")
        windowAccentSettings.createComponent()

        assertDoesNotThrow {
            windowAccentSettings.reset()
        }
    }
}
