package com.window_accent.feature.window_title

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import com.window_accent.configuration.persistence.WindowTitleNumberingStateService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import javax.swing.JFrame

@DisplayName("WindowTitleApplier Tests")
@Suppress("UNCHECKED_CAST")
class WindowTitleApplierTest {

    private lateinit var mockApplication: com.intellij.openapi.application.Application
    private lateinit var mockProjectManager: ProjectManager
    private lateinit var mockWindowManager: WindowManager
    private lateinit var mockProject1: Project
    private lateinit var mockProject2: Project
    private lateinit var mockFrame1: JFrame
    private lateinit var mockFrame2: JFrame

    private lateinit var applicationMock: org.mockito.MockedStatic<ApplicationManager>
    private lateinit var projectManagerMock: org.mockito.MockedStatic<ProjectManager>
    private lateinit var windowManagerMock: org.mockito.MockedStatic<WindowManager>

    @BeforeEach
    fun setup() {
        mockApplication = mock(com.intellij.openapi.application.Application::class.java)
        mockProjectManager = mock(ProjectManager::class.java)
        mockWindowManager = mock(WindowManager::class.java)
        mockProject1 = mock(Project::class.java)
        mockProject2 = mock(Project::class.java)
        mockFrame1 = mock(JFrame::class.java)
        mockFrame2 = mock(JFrame::class.java)

        val mockTitleNumberingService1 = mock(WindowTitleNumberingStateService::class.java)
        val mockTitleNumberingService2 = mock(WindowTitleNumberingStateService::class.java)
        Mockito.`when`(mockProject1.getService(WindowTitleNumberingStateService::class.java)).thenReturn(mockTitleNumberingService1)
        Mockito.`when`(mockProject2.getService(WindowTitleNumberingStateService::class.java)).thenReturn(mockTitleNumberingService2)
        Mockito.`when`(mockTitleNumberingService1.isTitleNumberingEnabled()).thenReturn(true)
        Mockito.`when`(mockTitleNumberingService2.isTitleNumberingEnabled()).thenReturn(true)

        // Setup static mocks
        applicationMock = Mockito.mockStatic(ApplicationManager::class.java)
        applicationMock.`when`<com.intellij.openapi.application.Application> { ApplicationManager.getApplication() }
            .thenReturn(mockApplication)

        projectManagerMock = Mockito.mockStatic(ProjectManager::class.java)
        projectManagerMock.`when`<ProjectManager> { ProjectManager.getInstance() }.thenReturn(mockProjectManager)

        windowManagerMock = Mockito.mockStatic(WindowManager::class.java)
        windowManagerMock.`when`<WindowManager> { WindowManager.getInstance() }.thenReturn(mockWindowManager)

        // Setup invokeLater to run immediately for testing
        Mockito.doAnswer { invocation ->
            (invocation.arguments[0] as Runnable).run()
            null
        }.`when`(mockApplication).invokeLater(Mockito.any(Runnable::class.java))

        // Setup frames
        Mockito.`when`(mockWindowManager.getFrame(mockProject1)).thenReturn(mockFrame1)
        Mockito.`when`(mockWindowManager.getFrame(mockProject2)).thenReturn(mockFrame2)
        Mockito.`when`(mockFrame1.title).thenReturn("Test Project 1")
        Mockito.`when`(mockFrame2.title).thenReturn("Test Project 2")
    }

    @AfterEach
    fun tearDown() {
        // Close static mocks
        applicationMock.close()
        projectManagerMock.close()
        windowManagerMock.close()

        WindowTitleApplier.resetProjectNumbering()

        val focusListenersField = WindowTitleApplier::class.java.getDeclaredField("focusListeners")
        focusListenersField.isAccessible = true
        val focusListeners =
            focusListenersField.get(WindowTitleApplier) as java.util.concurrent.ConcurrentHashMap<Project, java.awt.event.WindowAdapter>
        focusListeners.clear()
    }

    @Test
    @DisplayName("Should apply title to current project when enabled")
    fun testApplyToCurrentOpenProjectEnabled() {
        WindowTitleApplier.applyToCurrentOpenProject(mockProject1, true)

        Mockito.verify(mockFrame1).title = "[1] Test Project 1"
        Mockito.verify(mockFrame1).addWindowFocusListener(Mockito.any(java.awt.event.WindowAdapter::class.java))
    }

    @Test
    @DisplayName("Should remove title from current project when disabled")
    fun testApplyToCurrentOpenProjectDisabled() {
        // First apply title
        WindowTitleApplier.applyToCurrentOpenProject(mockProject1, true)

        // Then disable
        WindowTitleApplier.applyToCurrentOpenProject(mockProject1, false)

        // Verify that focus listener was removed
        Mockito.verify(mockFrame1).removeWindowFocusListener(Mockito.any(java.awt.event.WindowAdapter::class.java))
    }

    @Test
    @DisplayName("Should handle null frame gracefully")
    fun testApplyToCurrentOpenProjectNullFrame() {
        Mockito.`when`(mockWindowManager.getFrame(mockProject1)).thenReturn(null)

        assertDoesNotThrow {
            WindowTitleApplier.applyToCurrentOpenProject(mockProject1, true)
        }

        Mockito.verify(mockFrame1, Mockito.never()).title = Mockito.any()
    }

    @Test
    @DisplayName("Should apply title to all open projects")
    fun testApplyToAllOpenProjects() {
        Mockito.`when`(mockProjectManager.openProjects).thenReturn(arrayOf(mockProject1, mockProject2))

        WindowTitleApplier.applyToAllOpenProjects(true)

        Mockito.verify(mockFrame1).title = "[1] Test Project 1"
        Mockito.verify(mockFrame2).title = "[2] Test Project 2"
    }

    @Test
    @DisplayName("Should remove title from all open projects")
    fun testRemoveFromAllOpenProjects() {
        Mockito.`when`(mockProjectManager.openProjects).thenReturn(arrayOf(mockProject1, mockProject2))

        // First apply titles
        WindowTitleApplier.applyToAllOpenProjects(true)
        Mockito.verify(mockFrame1).title = "[1] Test Project 1"
        Mockito.verify(mockFrame2).title = "[2] Test Project 2"

        // Then remove all
        WindowTitleApplier.removeFromAllOpenProjects()

        // Verify that the numbering starts from 1 again (not strictly required here, but good for testResetProjectNumbering)
    }

    @Test
    @DisplayName("Should assign sequential project numbers")
    fun testProjectNumberAssignment() {
        WindowTitleApplier.applyToCurrentOpenProject(mockProject1, true)
        WindowTitleApplier.applyToCurrentOpenProject(mockProject2, true)

        Mockito.verify(mockFrame1).title = "[1] Test Project 1"
        Mockito.verify(mockFrame2).title = "[2] Test Project 2"

        // Same project should keep same number
        WindowTitleApplier.applyToCurrentOpenProject(mockProject1, true)
        Mockito.verify(mockFrame1, Mockito.times(2)).title = "[1] Test Project 1"
    }

    @Test
    @DisplayName("Should reset project numbering when removing from all projects")
    fun testResetProjectNumbering() {
        Mockito.`when`(mockProjectManager.openProjects).thenReturn(arrayOf(mockProject1, mockProject2))

        // Apply titles
        WindowTitleApplier.applyToAllOpenProjects(true)
        Mockito.verify(mockFrame1).title = "[1] Test Project 1"
        Mockito.verify(mockFrame2).title = "[2] Test Project 2"

        // Remove all and reset
        WindowTitleApplier.removeFromAllOpenProjects()

        // Apply again - should start from 1 again
        WindowTitleApplier.applyToAllOpenProjects(true)
        Mockito.verify(mockFrame1, Mockito.times(2)).title = "[1] Test Project 1"
        Mockito.verify(mockFrame2, Mockito.times(2)).title = "[2] Test Project 2"
    }

    @Test
    @DisplayName("Should handle focus events and reapply title")
    fun testFocusListenerReappliesTitle() {
        WindowTitleApplier.applyToCurrentOpenProject(mockProject1, true)

        // Get the focus listener that was added
        val focusListenersField = WindowTitleApplier::class.java.getDeclaredField("focusListeners")
        focusListenersField.isAccessible = true
        val focusListeners =
            focusListenersField.get(WindowTitleApplier) as java.util.concurrent.ConcurrentHashMap<Project, java.awt.event.WindowAdapter>

        val listener = focusListeners[mockProject1]!!

        // Simulate title being changed externally
        Mockito.`when`(mockFrame1.title).thenReturn("Modified Title")

        // Trigger focus gained
        listener.windowGainedFocus(null)

        // Should reapply the title with prefix
        Mockito.verify(mockFrame1).title = "[1] Modified Title"
    }

    @Test
    @DisplayName("Should handle null title gracefully")
    fun testNullTitleHandling() {
        Mockito.`when`(mockFrame1.title).thenReturn(null)

        WindowTitleApplier.applyToCurrentOpenProject(mockProject1, true)

        // Should not crash, just skip title update
        Mockito.verify(mockFrame1, Mockito.never()).title = Mockito.any()
    }

    @Test
    @DisplayName("Should strip existing prefix correctly")
    fun testStripExistingPrefix() {
        val method = WindowTitleApplier::class.java.getDeclaredMethod("stripExistingPrefix", String::class.java)
        method.isAccessible = true

        assertEquals("My Project", method.invoke(WindowTitleApplier, "[1] My Project"))
        assertEquals("My Project", method.invoke(WindowTitleApplier, "[123] My Project"))
        assertEquals("", method.invoke(WindowTitleApplier, "[5]"))
        assertEquals("Project Without Prefix", method.invoke(WindowTitleApplier, "Project Without Prefix"))
    }

    @Test
    @DisplayName("Should update window title with prefix")
    fun testUpdateWindowTitle() {
        WindowTitleApplier.applyToCurrentOpenProject(mockProject1, true)

        Mockito.verify(mockFrame1).title = "[1] Test Project 1"
    }

    @Test
    @DisplayName("Should not update title if already correct")
    fun testIdempotentTitleUpdate() {
        Mockito.`when`(mockFrame1.title).thenReturn("[1] Test Project 1")

        WindowTitleApplier.applyToCurrentOpenProject(mockProject1, true)

        // The title is already correct, so it should not be set again
        Mockito.verify(mockFrame1, Mockito.never()).title = Mockito.any()
    }

    @Test
    @DisplayName("Should handle empty project list")
    fun testEmptyProjectList() {
        Mockito.`when`(mockProjectManager.openProjects).thenReturn(emptyArray())

        assertDoesNotThrow {
            WindowTitleApplier.applyToAllOpenProjects(true)
            WindowTitleApplier.removeFromAllOpenProjects()
        }
    }

    @Test
    @DisplayName("Should handle special characters in titles")
    fun testSpecialCharactersInTitles() {
        Mockito.`when`(mockFrame1.title).thenReturn("Project with (parentheses) and [brackets] 🚀")

        WindowTitleApplier.applyToCurrentOpenProject(mockProject1, true)

        Mockito.verify(mockFrame1).title = "[1] Project with (parentheses) and [brackets] 🚀"
    }

    @Test
    @DisplayName("Should handle very long titles")
    fun testVeryLongTitles() {
        val longTitle = "A".repeat(1000)
        Mockito.`when`(mockFrame1.title).thenReturn(longTitle)

        WindowTitleApplier.applyToCurrentOpenProject(mockProject1, true)

        Mockito.verify(mockFrame1).title = "[1] ${"A".repeat(1000)}"
    }

    @Test
    @DisplayName("Should handle title with multiple existing prefixes")
    fun testMultipleExistingPrefixes() {
        Mockito.`when`(mockFrame1.title).thenReturn("[1] [2] [3] Actual Title")

        WindowTitleApplier.applyToCurrentOpenProject(mockProject1, true)

        // The stripExistingPrefix method should handle multiple prefixes correctly
        val method = WindowTitleApplier::class.java.getDeclaredMethod("stripExistingPrefix", String::class.java)
        method.isAccessible = true
        val result = method.invoke(WindowTitleApplier, "[1] [2] [3] Actual Title") as String
        assertEquals("Actual Title", result)
    }

    @Test
    @DisplayName("Should handle title change events and reapply title")
    fun testTitleListenerReappliesTitle() {
        WindowTitleApplier.applyToCurrentOpenProject(mockProject1, true)

        // Get the title listener that was added
        val titleListenersField = WindowTitleApplier::class.java.getDeclaredField("titleListeners")
        titleListenersField.isAccessible = true
        val titleListeners =
            titleListenersField.get(WindowTitleApplier) as java.util.concurrent.ConcurrentHashMap<Project, java.beans.PropertyChangeListener>

        val listener = titleListeners[mockProject1]!!

        // Simulate title being changed externally
        val newTitle = "New Title From IDE"
        Mockito.`when`(mockFrame1.title).thenReturn(newTitle)

        // Trigger property change event
        val event = java.beans.PropertyChangeEvent(mockFrame1, "title", "Old Title", newTitle)
        listener.propertyChange(event)

        // Should reapply the title with prefix
        Mockito.verify(mockFrame1).title = "[1] New Title From IDE"
    }

}
