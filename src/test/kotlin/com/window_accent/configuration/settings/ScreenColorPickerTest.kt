package com.window_accent.configuration.settings

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

@DisplayName("ScreenColorPicker Tests")
class ScreenColorPickerTest : BaseScreenColorPickerTest() {

    @Test
    @DisplayName("showScreenColorPicker should return early if window ancestor is null")
    fun testShowScreenColorPickerNullWindowAncestor() {
        mockStatic(SwingUtilities::class.java).use { mockedSwing ->
            mockedSwing.`when`<Window?> { SwingUtilities.getWindowAncestor(mockPanel) }.thenReturn(null)

            assertDoesNotThrow {
                showScreenColorPicker(mockSettings)
            }
        }
    }

    @Test
    @DisplayName("showScreenColorPicker should handle missing editor gracefully")
    fun testShowScreenColorPickerMissingEditorHandling() {
        // When no editor is available, the function attempts screenshot fallback
        // In headless/test environment this will catch exception gracefully
        setHeadlessModeToAvoidShowingUI(true)

        mockStatic(SwingUtilities::class.java).use { mockedSwing ->
            mockedSwing.`when`<Window?> { SwingUtilities.getWindowAncestor(mockPanel) }
                .thenReturn(mock(Window::class.java))

            mockStatic(FileEditorManager::class.java).use { mockedEditorManager ->
                val mockFEM = mock(FileEditorManager::class.java)
                mockedEditorManager.`when`<FileEditorManager> { FileEditorManager.getInstance(mockProject) }
                    .thenReturn(mockFEM)
                `when`(mockFEM.selectedTextEditor).thenReturn(null)

                // Should handle exception gracefully (Robot fails in headless)
                assertDoesNotThrow {
                    showScreenColorPicker(mockSettings)
                }
            }
        }
    }

    @Test
    @DisplayName("createOverlay should create undecorated dialog")
    fun testCreateOverlayUndecorated() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")

        val picker = ScreenColorPicker(mockSettings)
        val owner = JFrame()
        try {
            val size = Dimension(1920, 1080)
            val dialog = picker.createOverlay(owner, size)
            try {
                assertTrue(dialog.isUndecorated)
            } finally {
                dialog.dispose()
            }
        } finally {
            owner.dispose()
        }
    }

    @Test
    @DisplayName("createOverlay should set always on top")
    fun testCreateOverlayAlwaysOnTop() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")

        val picker = ScreenColorPicker(mockSettings)
        val owner = JFrame()
        try {
            val size = Dimension(1920, 1080)
            val dialog = picker.createOverlay(owner, size)
            try {
                assertTrue(dialog.isAlwaysOnTop)
            } finally {
                dialog.dispose()
            }
        } finally {
            owner.dispose()
        }
    }

    @Test
    @DisplayName("createOverlay should have transparent background")
    fun testCreateOverlayTransparent() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")

        val picker = ScreenColorPicker(mockSettings)
        val owner = JFrame()
        try {
            val size = Dimension(1920, 1080)
            val dialog = picker.createOverlay(owner, size)
            try {
                assertEquals(Color(0, 0, 0, 0), dialog.background)
            } finally {
                dialog.dispose()
            }
        } finally {
            owner.dispose()
        }
    }

    @Test
    @DisplayName("createOverlay should set correct size")
    fun testCreateOverlaySetsSize() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")

        val picker = ScreenColorPicker(mockSettings)
        val owner = JFrame()
        try {
            val size = Dimension(1920, 1080)
            val dialog = picker.createOverlay(owner, size)
            try {
                assertEquals(size, dialog.size)
            } finally {
                dialog.dispose()
            }
        } finally {
            owner.dispose()
        }
    }

    @Test
    @DisplayName("createOverlay should position at origin")
    fun testCreateOverlayOriginPosition() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")

        val picker = ScreenColorPicker(mockSettings)
        val owner = JFrame()
        try {
            val size = Dimension(1920, 1080)
            val dialog = picker.createOverlay(owner, size)
            try {
                assertEquals(0, dialog.x)
                assertEquals(0, dialog.y)
            } finally {
                dialog.dispose()
            }
        } finally {
            owner.dispose()
        }
    }

    @Test
    @DisplayName("createOverlay should be disposable on close")
    fun testCreateOverlayDisposeOnClose() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping in headless mode")

        val picker = ScreenColorPicker(mockSettings)
        val owner = JFrame()
        try {
            val size = Dimension(1920, 1080)
            val dialog = picker.createOverlay(owner, size)
            try {
                assertEquals(WindowConstants.DISPOSE_ON_CLOSE, dialog.defaultCloseOperation)
            } finally {
                dialog.dispose()
            }
        } finally {
            owner.dispose()
        }
    }

    @Test
    @DisplayName("createColorSelectionHandler should apply color when applySelection is true")
    fun testColorSelectionHandlerAppliesColor() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
        val testColor = Color(200, 100, 50)
        screenshot.setRGB(50, 50, testColor.rgb)

        val picker = ScreenColorPicker(mockSettings)
        picker.mousePoint = Point(50, 50)
        val mockOverlay = mock(JDialog::class.java)

        val handler = picker.createColorSelectionHandler(screenshot, mockOverlay)

        handler(true)

        // Verify color was set
        verify(mockSettings).selectedColor = any()
        verify(mockSettings).customColorCheckBox
        verify(mockSettings).syncEnabledState()
        verify(mockSettings).syncPreview()
    }

    @Test
    @DisplayName("createColorSelectionHandler should always dispose overlay regardless of selection")
    fun testColorSelectionHandlerDisposesOverlayOnApply() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)

        val picker = ScreenColorPicker(mockSettings)
        picker.mousePoint = Point(50, 50)
        val mockOverlay = mock(JDialog::class.java)

        val handler = picker.createColorSelectionHandler(screenshot, mockOverlay)

        handler(true)

        verify(mockOverlay).dispose()
    }

    @Test
    @DisplayName("createColorSelectionHandler should dispose overlay without applying color when false")
    fun testColorSelectionHandlerDisposesOverlayOnCancel() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)

        val picker = ScreenColorPicker(mockSettings)
        picker.mousePoint = Point(50, 50)
        val mockOverlay = mock(JDialog::class.java)

        val handler = picker.createColorSelectionHandler(screenshot, mockOverlay)

        handler(false)

        // Should dispose but not apply color
        verify(mockOverlay).dispose()
        verify(mockSettings, never()).selectedColor = any()
    }

    @Test
    @DisplayName("createColorSelectionHandler should clamp coordinates within screenshot bounds")
    fun testColorSelectionHandlerClampsCoordinates() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
        val testColor = Color.BLUE
        screenshot.setRGB(0, 0, testColor.rgb) // Set color at clamped position

        val picker = ScreenColorPicker(mockSettings)
        picker.mousePoint = Point(-50, -50) // Out of bounds coordinates

        val mockOverlay = mock(JDialog::class.java)

        val handler = picker.createColorSelectionHandler(screenshot, mockOverlay)

        // Should not throw even with out-of-bounds coordinates
        assertDoesNotThrow {
            handler(true)
        }

        verify(mockOverlay).dispose()
    }

    @Test
    @DisplayName("createColorSelectionHandler should handle color at maximum bounds")
    fun testColorSelectionHandlerMaximumBounds() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
        val testColor = Color.RED
        screenshot.setRGB(99, 99, testColor.rgb)

        val picker = ScreenColorPicker(mockSettings)
        picker.mousePoint = Point(99, 99)
        val mockOverlay = mock(JDialog::class.java)

        val handler = picker.createColorSelectionHandler(screenshot, mockOverlay)

        assertDoesNotThrow {
            handler(true)
        }

        verify(mockOverlay).dispose()
    }

    @Test
    @DisplayName("createColorSelectionHandler should handle color with alpha channel")
    fun testColorSelectionHandlerWithAlpha() {
        val screenshot = BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
        val colorWithAlpha = Color(100, 150, 200, 128)
        screenshot.setRGB(50, 50, colorWithAlpha.rgb)

        val picker = ScreenColorPicker(mockSettings)
        picker.mousePoint = Point(50, 50)
        val mockOverlay = mock(JDialog::class.java)

        val handler = picker.createColorSelectionHandler(screenshot, mockOverlay)

        assertDoesNotThrow {
            handler(true)
        }

        verify(mockSettings).selectedColor = any()
    }

    @Test
    @DisplayName("Should initialize screen color picker components")
    fun testShowScreenColorPickerInitialization() {
        // Set headless mode to avoid showing UI
        val originalHeadless = GraphicsEnvironment.isHeadless()
        setHeadlessModeToAvoidShowingUI(true)

        try {
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
            assertTrue(
                e is AWTException || e is HeadlessException,
                "Expected AWT or Headless exception in headless mode"
            )
        } finally {
            // Restore original headless setting
            setHeadlessModeToAvoidShowingUI(originalHeadless)
        }
    }

    private fun setHeadlessModeToAvoidShowingUI(propertyValue: Boolean) {
        System.setProperty("java.awt.headless", propertyValue.toString())
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
    @DisplayName("showScreenColorPicker should use editor component when available")
    fun testShowScreenColorPickerWithEditor() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless())

        val owner = try {
            JFrame()
        } catch (e: Error) {
            Assumptions.abort<JFrame>("Swing initialization failed: ${e.message}")
        }
        try {
            mockStatic(SwingUtilities::class.java).use { mockedSwing ->
                mockedSwing.`when`<Window?> { SwingUtilities.getWindowAncestor(mockPanel) }
                    .thenReturn(owner)

                mockStatic(FileEditorManager::class.java).use { mockedEditorManager ->
                    val mockFEM = mock(FileEditorManager::class.java)
                    val mockEditor = mock(Editor::class.java)
                    val mockEditorComponent = mock(JComponent::class.java)

                    mockedEditorManager.`when`<FileEditorManager> { FileEditorManager.getInstance(mockProject) }
                        .thenReturn(mockFEM)
                    `when`(mockFEM.selectedTextEditor).thenReturn(mockEditor)
                    `when`(mockEditor.component).thenReturn(mockEditorComponent)
                    `when`(mockEditorComponent.width).thenReturn(800)
                    `when`(mockEditorComponent.height).thenReturn(600)

                    // Mock the creation of JDialog to avoid actual UI instantiation issues
                    mockConstruction(JDialog::class.java) { mockDialog, _ ->
                        val mockRootPane = mock(javax.swing.JRootPane::class.java, withSettings().stubOnly())
                        `when`(mockDialog.rootPane).thenReturn(mockRootPane)
                    }.use { _ ->
                        assertDoesNotThrow {
                            showScreenColorPicker(mockSettings)
                        }
                    }
                }
            }
        } finally {
            owner.dispose()
        }
    }

    @Test
    @DisplayName("showColorChooserViaScreenshot should take screenshot and setup UI")
    fun testShowColorChooserViaScreenshot() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless())
        val owner = JFrame()
        try {
            val screenSize = Dimension(800, 600)
            mockConstruction(JDialog::class.java) { mockDialog, _ ->
                val mockRootPane = mock(javax.swing.JRootPane::class.java, withSettings().stubOnly())
                `when`(mockDialog.rootPane).thenReturn(mockRootPane)
            }.use { _ ->
                assertDoesNotThrow {
                    showColorChooserViaScreenshot(screenSize, owner, mockSettings)
                }
            }
        } finally {
            owner.dispose()
        }
    }

}

