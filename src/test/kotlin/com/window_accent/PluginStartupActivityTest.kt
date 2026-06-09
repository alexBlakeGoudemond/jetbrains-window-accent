package com.window_accent

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.window_accent.configuration.persistence.WindowCustomTitleStateService
import com.window_accent.configuration.persistence.WindowTitleNumberingStateService
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
            Mockito.`when`(mockApplication.isDispatchThread).thenReturn(false)

            val mockTitleService = WindowTitleNumberingStateService()
            Mockito.`when`(mockProject.getService(WindowTitleNumberingStateService::class.java))
                .thenReturn(mockTitleService)

            val mockCustomTitleService = WindowCustomTitleStateService()
            Mockito.`when`(mockProject.getService(WindowCustomTitleStateService::class.java))
                .thenReturn(mockCustomTitleService)

            // Since execute is suspend, we need to run it in a coroutine
            assertDoesNotThrow {
                runBlocking {
                    activity.execute(mockProject)
                }
            }
        }
    }
}
