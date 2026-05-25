package com.window_accent.configuration.settings

import com.intellij.openapi.project.Project
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockito.MockedStatic
import org.mockito.Mockito.*
import javax.swing.*
import javax.swing.plaf.ButtonUI
import javax.swing.plaf.ComboBoxUI
import javax.swing.plaf.ComponentUI
import javax.swing.plaf.LabelUI
import javax.swing.plaf.PanelUI
import javax.swing.plaf.RootPaneUI
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mockStatic
import org.mockito.stubbing.Answer

/**
 * Base class for ScreenColorPicker tests to provide common mocks and setup.
 */
open class BaseScreenColorPickerTest {

    protected lateinit var mockSettings: IWindowAccentSettings
    protected lateinit var mockProject: Project
    protected lateinit var mockPanel: JPanel
    protected lateinit var mockCheckBox: JCheckBox
    private var mockedUIManager: MockedStatic<UIManager>? = null

    @BeforeEach
    open fun setUp() {
        // Suppress "no ComponentUI class" errors by mocking UIManager.getUI
        try {
            mockedUIManager = mockStatic(UIManager::class.java)
            mockedUIManager?.`when`<UIDefaults> { UIManager.getDefaults() }?.thenReturn(UIDefaults())
            
            val uiAnswer = Answer<ComponentUI?> { invocation ->
                val comp = invocation.getArgument<JComponent>(0)
                when {
                    comp is JPanel -> mock(PanelUI::class.java)
                    comp is JRootPane -> mock(RootPaneUI::class.java)
                    comp is JLabel -> mock(LabelUI::class.java)
                    comp is AbstractButton -> mock(ButtonUI::class.java)
                    comp is JComboBox<*> -> mock(ComboBoxUI::class.java)
                    else -> mock(ComponentUI::class.java)
                }
            }
            mockedUIManager?.`when`<ComponentUI> { UIManager.getUI(any()) }?.thenAnswer(uiAnswer)
        } catch (e: Exception) {
            System.err.println("[DEBUG_LOG] UIManager mock failed: ${e.message}")
        }

        try {
            mockSettings = mock(IWindowAccentSettings::class.java)
            mockProject = mock(Project::class.java)
            mockPanel = JPanel() // Use real JPanel
            mockCheckBox = JCheckBox() // Use real JCheckBox

            `when`(mockSettings.getPanel()).thenReturn(mockPanel)
            `when`(mockSettings.getCustomColorCheckBox()).thenReturn(mockCheckBox)
            `when`(mockSettings.getProject()).thenReturn(mockProject)
        } catch (e: Exception) {
             System.err.println("[DEBUG_LOG] BaseScreenColorPickerTest setup failed: ${e.message}")
        }
    }

    @AfterEach
    open fun tearDown() {
        mockedUIManager?.close()
        mockedUIManager = null
    }

}
