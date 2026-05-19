package com.window_color_panel

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
@DisplayName("PluginStartupActivity Tests")
class PluginStartupActivityTest {

    @Test
    @DisplayName("Should execute without throwing exceptions")
    fun testExecute() {
        val mockProject = mock(Project::class.java)
        val activity = PluginStartupActivity()

        Mockito.mockStatic(ApplicationManager::class.java).use { applicationManagerMock ->
            val mockApplication = mock(com.intellij.openapi.application.Application::class.java)
            applicationManagerMock.`when`<com.intellij.openapi.application.Application> { ApplicationManager.getApplication() }
                .thenReturn(mockApplication)

            Mockito.doNothing().`when`(mockApplication).invokeLater(Mockito.any(Runnable::class.java))

            val mockTitleService = mock(com.window_color_panel.configuration.persistence.WindowTitleNumberingStateService::class.java)
            Mockito.`when`(mockProject.getService(com.window_color_panel.configuration.persistence.WindowTitleNumberingStateService::class.java))
                .thenReturn(mockTitleService)
            Mockito.`when`(mockTitleService.isTitleNumberingEnabled()).thenReturn(true)

            // Since execute is suspend, we need to run it in a coroutine
            assertDoesNotThrow {
                runBlocking {
                    activity.execute(mockProject)
                }
            }
        }
    }
}
