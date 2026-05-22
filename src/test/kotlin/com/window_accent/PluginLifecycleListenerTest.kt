package com.window_accent

import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.extensions.PluginId
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse

@DisplayName("PluginLifecycleListener Tests")
class PluginLifecycleListenerTest {

    @Test
    @DisplayName("Should cleanup decorations when plugin is unloaded")
    fun testPluginUnloaded() {
        var colorCleanupCalled = false
        var titleCleanupCalled = false

        val listener = object : PluginLifecycleListener() {
            override fun performCleanup() {
                colorCleanupCalled = true
                titleCleanupCalled = true
            }
        }

        val mockDescriptor = mock(IdeaPluginDescriptor::class.java)
        val testPluginId = PluginId.getId("WindowAccent")

        `when`(mockDescriptor.pluginId).thenReturn(testPluginId)

        listener.pluginUnloaded(mockDescriptor, false)

        assertTrue(colorCleanupCalled, "Color cleanup should have been called")
        assertTrue(titleCleanupCalled, "Title cleanup should have been called")
    }

    @Test
    @DisplayName("Should not cleanup decorations if a different plugin is unloaded")
    fun testPluginUnloadedDifferentPlugin() {
        var colorCleanupCalled = false
        var titleCleanupCalled = false

        val listener = object : PluginLifecycleListener() {
            override fun performCleanup() {
                colorCleanupCalled = true
                titleCleanupCalled = true
            }
        }

        val mockDescriptor = mock(IdeaPluginDescriptor::class.java)
        val testPluginId = PluginId.getId("SomeOtherPlugin")

        `when`(mockDescriptor.pluginId).thenReturn(testPluginId)

        listener.pluginUnloaded(mockDescriptor, false)

        assertFalse(colorCleanupCalled, "Color cleanup should NOT have been called")
        assertFalse(titleCleanupCalled, "Title cleanup should NOT have been called")
    }

}
