package com.window_color_panel

import com.intellij.openapi.project.Project
import com.window_color_panel.configuration.tool_window.WindowColorPanelToolWindowFactory
import org.junit.jupiter.api.Test
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Arrays
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class CompatibilityVerificationTest {

    @Test
    fun testShouldBeAvailableIsOverridden() {
        val isOverridden = Arrays.stream(WindowColorPanelToolWindowFactory::class.java.declaredMethods)
            .anyMatch { m ->
                m.name == "shouldBeAvailable" &&
                        m.parameterCount == 1 &&
                        m.parameterTypes[0] == Project::class.java
            }

        assertTrue(isOverridden, "WindowColorPanelToolWindowFactory should override shouldBeAvailable(Project)")
    }

    @Test
    fun testDeprecatedMethodsAreNotOverridden() {
        val methods = WindowColorPanelToolWindowFactory::class.java.declaredMethods
        
        val isIsApplicableOverridden = Arrays.stream(methods)
            .anyMatch { m -> m.name == "isApplicable" && !m.isSynthetic && !m.isBridge }

        val isIsDoNotActivateOnStartOverridden = Arrays.stream(methods)
            .anyMatch { m -> m.name == "isDoNotActivateOnStart" && !m.isSynthetic && !m.isBridge }

        val methodDetails = methods.map { "${it.name}(synthetic=${it.isSynthetic}, bridge=${it.isBridge})" }

        assertFalse(isIsApplicableOverridden, "WindowColorPanelToolWindowFactory should not override deprecated isApplicable. Found methods: $methodDetails")
        assertFalse(isIsDoNotActivateOnStartOverridden, "WindowColorPanelToolWindowFactory should not override deprecated isDoNotActivateOnStart. Found methods: $methodDetails")
    }

    @Test
    fun testExperimentalMethodsAreNotOverridden() {
        val methods = WindowColorPanelToolWindowFactory::class.java.declaredMethods

        val isGetAnchorOverridden = Arrays.stream(methods)
            .anyMatch { m -> m.name == "getAnchor" && !m.isSynthetic && !m.isBridge }

        val isGetIconOverridden = Arrays.stream(methods)
            .anyMatch { m -> m.name == "getIcon" && !m.isSynthetic && !m.isBridge }

        val isManageOverridden = Arrays.stream(methods)
            .anyMatch { m -> m.name == "manage" && !m.isSynthetic && !m.isBridge }

        val methodDetails = methods.map { "${it.name}(synthetic=${it.isSynthetic}, bridge=${it.isBridge})" }

        assertFalse(isGetAnchorOverridden, "WindowColorPanelToolWindowFactory should not override experimental getAnchor. Found methods: $methodDetails")
        assertFalse(isGetIconOverridden, "WindowColorPanelToolWindowFactory should not override experimental getIcon. Found methods: $methodDetails")
        assertFalse(isManageOverridden, "WindowColorPanelToolWindowFactory should not override experimental manage. Found methods: $methodDetails")
    }

    @Test
    fun testPluginXmlHasDoNotActivateOnStart() {
        val pluginXmlPath = Paths.get("src", "main", "resources", "META-INF", "plugin.xml")
        val content = String(Files.readAllBytes(pluginXmlPath))

        assertTrue(
            content.contains("doNotActivateOnStart=\"true\""),
            "plugin.xml should contain doNotActivateOnStart=\"true\" in the toolWindow extension"
        )
    }

    @Test
    fun testPluginXmlHasModernConfigurations() {
        val pluginXmlPath = Paths.get("src", "main", "resources", "META-INF", "plugin.xml")
        val content = String(Files.readAllBytes(pluginXmlPath))

        assertTrue(
            content.contains("anchor=\"left\""),
            "plugin.xml should contain anchor=\"left\" in the toolWindow extension"
        )
        assertTrue(
            content.contains("icon=\"/META-INF/simple-plugin-icon.svg\""),
            "plugin.xml should contain icon attribute in the toolWindow extension"
        )
    }

}
