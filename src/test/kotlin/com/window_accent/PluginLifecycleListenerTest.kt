package com.window_accent

import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.PluginId
import com.window_accent.feature.window_color.WindowColorApplier
import com.window_accent.feature.window_title.WindowTitleApplier
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.mock

@DisplayName("PluginLifecycleListener Tests")
class PluginLifecycleListenerTest {

    @Test
    @DisplayName("Should cleanup decorations when plugin is unloaded")
    fun testPluginUnloaded() {
        Mockito.mockStatic(ApplicationManager::class.java).use { applicationManagerMock ->
            val mockApplication = mock(Application::class.java)
            applicationManagerMock.`when`<Application> { ApplicationManager.getApplication() }.thenReturn(mockApplication)

            Mockito.mockStatic(WindowColorApplier::class.java).use { colorApplierMock ->
                Mockito.mockStatic(WindowTitleApplier::class.java).use { titleApplierMock ->

                    val listener = PluginLifecycleListener()
                    val mockDescriptor = mock(IdeaPluginDescriptor::class.java)
                    val mockPluginId = mock(PluginId::class.java)

                    Mockito.`when`(mockDescriptor.pluginId).thenReturn(mockPluginId)
                    Mockito.`when`(mockPluginId.idString).thenReturn("WindowAccent")

                    listener.pluginUnloaded(mockDescriptor, false)

                    colorApplierMock.verify( { WindowColorApplier.removeColorFromAllOpenProjects() })
                    titleApplierMock.verify( { WindowTitleApplier.removeFromAllOpenProjects() })
                }
            }
        }
    }

    @Test
    @DisplayName("Should not cleanup decorations if a different plugin is unloaded")
    fun testPluginUnloadedDifferentPlugin() {
        Mockito.mockStatic(ApplicationManager::class.java).use { applicationManagerMock ->
            val mockApplication = mock(Application::class.java)
            applicationManagerMock.`when`<Application> { ApplicationManager.getApplication() }.thenReturn(mockApplication)

            Mockito.mockStatic(WindowColorApplier::class.java).use { colorApplierMock ->
                Mockito.mockStatic(WindowTitleApplier::class.java).use { titleApplierMock ->

                    val listener = PluginLifecycleListener()
                    val mockDescriptor = mock(IdeaPluginDescriptor::class.java)
                    val mockPluginId = mock(PluginId::class.java)

                    Mockito.`when`(mockDescriptor.pluginId).thenReturn(mockPluginId)
                    Mockito.`when`(mockPluginId.idString).thenReturn("SomeOtherPlugin")

                    listener.pluginUnloaded(mockDescriptor, false)

                    colorApplierMock.verify({ WindowColorApplier.removeColorFromAllOpenProjects() }, Mockito.never())
                    titleApplierMock.verify({ WindowTitleApplier.removeFromAllOpenProjects() }, Mockito.never())
                }
            }
        }
    }
}
