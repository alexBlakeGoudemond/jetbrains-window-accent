package com.window_color_panel.configuration.settings

import com.intellij.openapi.project.Project
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import javax.swing.JCheckBox
import javax.swing.JPanel

/**
 * Base class for ScreenColorPicker tests to provide common mocks and setup.
 */
open class BaseScreenColorPickerTest {

    protected lateinit var mockSettings: WindowColorPanelSettings
    protected lateinit var mockProject: Project
    protected lateinit var mockPanel: JPanel
    protected lateinit var mockCheckBox: JCheckBox

    @BeforeEach
    open fun setUp() {
        mockSettings = mock(WindowColorPanelSettings::class.java)
        mockProject = mock(Project::class.java)
        mockPanel = mock(JPanel::class.java)
        mockCheckBox = mock(JCheckBox::class.java)

        `when`(mockSettings.panel).thenReturn(mockPanel)
        `when`(mockSettings.customColorCheckBox).thenReturn(mockCheckBox)
        `when`(mockSettings.getProject()).thenReturn(mockProject)
    }

}
