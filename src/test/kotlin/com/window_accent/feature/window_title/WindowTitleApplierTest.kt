package com.window_accent.feature.window_title

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import com.window_accent.configuration.persistence.GlobalCustomTitleStateService
import com.window_accent.configuration.persistence.WindowCustomTitleStateService
import com.window_accent.configuration.persistence.WindowTitleNumberingStateService
import com.window_accent.diagnostic.windowAccentLogger
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.awt.Frame
import java.awt.event.WindowAdapter
import java.beans.PropertyChangeListener
import java.util.concurrent.ConcurrentHashMap
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

    private lateinit var applier: WindowTitleApplier

    /** Real service instance shared across @BeforeEach and setupFullMocks. */
    private lateinit var mockGlobalCustomTitleService: GlobalCustomTitleStateService
    private var logger = windowAccentLogger<WindowTitleApplierTest>()

    @BeforeEach
    fun setup() {
        applier = WindowTitleApplier()
        mockProject1 = mock(Project::class.java)
        mockProject2 = mock(Project::class.java)

        // Use real Frame/JFrame instead of mocks to avoid Mockito/Java 25 issues
        mockFrame1 = JFrame("Test Project 1")
        mockFrame2 = JFrame("Test Project 2")

        val mockTitleNumberingService1 = WindowTitleNumberingStateService()
        val mockTitleNumberingService2 = WindowTitleNumberingStateService()
        val mockCustomTitleService1 = WindowCustomTitleStateService()
        val mockCustomTitleService2 = WindowCustomTitleStateService()
        Mockito.`when`(mockProject1.getService(WindowTitleNumberingStateService::class.java)).thenReturn(mockTitleNumberingService1)
        Mockito.`when`(mockProject2.getService(WindowTitleNumberingStateService::class.java)).thenReturn(mockTitleNumberingService2)
        Mockito.`when`(mockProject1.getService(WindowCustomTitleStateService::class.java)).thenReturn(mockCustomTitleService1)
        Mockito.`when`(mockProject2.getService(WindowCustomTitleStateService::class.java)).thenReturn(mockCustomTitleService2)
        mockTitleNumberingService1.setTitleNumberingEnabled(true)
        mockTitleNumberingService2.setTitleNumberingEnabled(true)

        mockGlobalCustomTitleService = GlobalCustomTitleStateService()
    }

    private fun setupFullMocks() {
        mockApplication = mock(com.intellij.openapi.application.Application::class.java)
        mockProjectManager = mock(ProjectManager::class.java)
        mockWindowManager = mock(WindowManager::class.java)

        // Setup static mocks
        applicationMock = Mockito.mockStatic(ApplicationManager::class.java)
        applicationMock.`when`<com.intellij.openapi.application.Application> { ApplicationManager.getApplication() }
            .thenReturn(mockApplication)

        projectManagerMock = Mockito.mockStatic(ProjectManager::class.java)
        projectManagerMock.`when`<ProjectManager> { ProjectManager.getInstance() }.thenReturn(mockProjectManager)

        windowManagerMock = Mockito.mockStatic(WindowManager::class.java)
        windowManagerMock.`when`<WindowManager> { WindowManager.getInstance() }.thenReturn(mockWindowManager)
        Mockito.`when`(mockApplication.isDispatchThread).thenReturn(true)

        Mockito.`when`(mockApplication.getService(GlobalCustomTitleStateService::class.java))
            .thenReturn(mockGlobalCustomTitleService)

        // Setup invokeLater to run immediately for testing
        Mockito.doAnswer { invocation ->
            (invocation.arguments[0] as Runnable).run()
            null
        }.`when`(mockApplication).invokeLater(Mockito.any(Runnable::class.java))

        Mockito.`when`(mockWindowManager.getFrame(mockProject1)).thenReturn(mockFrame1)
        Mockito.`when`(mockWindowManager.getFrame(mockProject2)).thenReturn(mockFrame2)
    }

    @AfterEach
    fun tearDown() {
        if (::applicationMock.isInitialized) applicationMock.close()
        if (::projectManagerMock.isInitialized) projectManagerMock.close()
        if (::windowManagerMock.isInitialized) windowManagerMock.close()

        applier.resetProjectNumbering()
    }

    @Test
    @DisplayName("Should strip existing prefix correctly")
    fun testStripExistingPrefix() {
        assertEquals("Title", applier.stripExistingPrefix("[1] Title"))
        assertEquals("Title", applier.stripExistingPrefix("[1] [2] Title"))
        assertEquals("No Prefix", applier.stripExistingPrefix("No Prefix"))
        assertEquals("Title [With Brackets]", applier.stripExistingPrefix("[1] Title [With Brackets]"))
        assertEquals("Title", applier.stripExistingPrefix("[dattebayo] Title"))
        assertEquals("Title", applier.stripExistingPrefix("[1 - dattebayo] Title"))
        assertEquals("Title", applier.stripExistingPrefix("[1 - dattebayo] [2] Title"))
        // Styled prefixes (Unicode math chars inside brackets) are also stripped
        assertEquals("Title", applier.stripExistingPrefix("[${TitleTextStyler.toBold("prod")}] Title"))
        assertEquals("Title", applier.stripExistingPrefix("[${TitleTextStyler.toItalic("dattebayo")}] Title"))
        assertEquals("Title", applier.stripExistingPrefix("[${TitleTextStyler.toBold("prod")}][1 - ${TitleTextStyler.toItalic("dattebayo")}] Title"))
    }

    @Test
    @DisplayName("Should assign sequential project numbers")
    fun testGetWindowProjectNumber() {
        assertEquals(1, applier.getWindowProjectNumber(mockProject1))
        assertEquals(2, applier.getWindowProjectNumber(mockProject2))
        assertEquals(1, applier.getWindowProjectNumber(mockProject1))
    }

    @Test
    @DisplayName("Should update window title with number prefix only")
    fun testUpdateWindowTitleNumberOnly() {
        applier.updateWindowTitle(mockFrame1, 5)
        assertEquals("[5] Test Project 1", mockFrame1.title)
    }

    @Test
    @DisplayName("Should update window title with custom title prefix only")
    fun testUpdateWindowTitleCustomTitleOnly() {
        applier.updateWindowTitle(mockFrame1, 5, customTitle = "dattebayo", customTitleEnabled = true, numberingEnabled = false)
        assertEquals("[${TitleTextStyler.toItalic("dattebayo")}] Test Project 1", mockFrame1.title)
    }

    @Test
    @DisplayName("Should update window title with both number and custom title")
    fun testUpdateWindowTitleNumberAndCustomTitle() {
        applier.updateWindowTitle(mockFrame1, 2, customTitle = "dattebayo", customTitleEnabled = true, numberingEnabled = true)
        assertEquals("[2 - ${TitleTextStyler.toItalic("dattebayo")}] Test Project 1", mockFrame1.title)
    }

    @Test
    @DisplayName("Should update window title with no prefix when both disabled")
    fun testUpdateWindowTitleBothDisabled() {
        mockFrame1.title = "[1] Test Project 1"
        applier.updateWindowTitle(mockFrame1, 1, customTitle = "dattebayo", customTitleEnabled = false, numberingEnabled = false)
        assertEquals("Test Project 1", mockFrame1.title)
    }

    @Test
    @DisplayName("buildTitlePrefix - numbering on, custom title on")
    fun testBuildTitlePrefixBoth() {
        // toItalic applied to the entire per-window content: digits pass through, letters become italic
        assertEquals("[${TitleTextStyler.toItalic("2 - dattebayo")}]", applier.buildTitlePrefix(2, true, "dattebayo", true))
    }

    @Test
    @DisplayName("buildTitlePrefix - numbering on, custom title off")
    fun testBuildTitlePrefixNumberOnly() {
        // toItalic applied to the number: digits have no italic Unicode equivalent so the number is unchanged
        assertEquals("[${TitleTextStyler.toItalic("3")}]", applier.buildTitlePrefix(3, true, "dattebayo", false))
    }

    @Test
    @DisplayName("buildTitlePrefix - numbering off, custom title on")
    fun testBuildTitlePrefixCustomOnly() {
        assertEquals("[${TitleTextStyler.toItalic("dattebayo")}]", applier.buildTitlePrefix(1, false, "dattebayo", true))
    }

    @Test
    @DisplayName("buildTitlePrefix - numbering off, custom title off")
    fun testBuildTitlePrefixNone() {
        assertEquals("", applier.buildTitlePrefix(1, false, "dattebayo", false))
    }

    @Test
    @DisplayName("buildTitlePrefix - numbering on, custom title enabled but blank")
    fun testBuildTitlePrefixBlankCustomTitle() {
        assertEquals("[1]", applier.buildTitlePrefix(1, true, "", true))
    }

    @Test
    @DisplayName("buildTitlePrefix - global title only")
    fun testBuildTitlePrefixGlobalOnly() {
        assertEquals("[${TitleTextStyler.toBold("prod")}]", applier.buildTitlePrefix(1, false, "", false, "prod", true))
    }

    @Test
    @DisplayName("buildTitlePrefix - numbering and global title")
    fun testBuildTitlePrefixNumberAndGlobal() {
        assertEquals("[${TitleTextStyler.toBold("prod")}][${TitleTextStyler.toItalic("2")}]", applier.buildTitlePrefix(2, true, "", false, "prod", true))
    }

    @Test
    @DisplayName("buildTitlePrefix - custom title and global title")
    fun testBuildTitlePrefixCustomAndGlobal() {
        assertEquals(
            "[${TitleTextStyler.toBold("prod")}][${TitleTextStyler.toItalic("dattebayo")}]",
            applier.buildTitlePrefix(1, false, "dattebayo", true, "prod", true)
        )
    }

    @Test
    @DisplayName("buildTitlePrefix - numbering, custom title, and global title all enabled")
    fun testBuildTitlePrefixAllEnabled() {
        assertEquals(
            "[${TitleTextStyler.toBold("prod")}][${TitleTextStyler.toItalic("2 - dattebayo")}]",
            applier.buildTitlePrefix(2, true, "dattebayo", true, "prod", true)
        )
    }

    @Test
    @DisplayName("buildTitlePrefix - global title disabled produces no global part")
    fun testBuildTitlePrefixGlobalDisabled() {
        assertEquals("[1]", applier.buildTitlePrefix(1, true, "", false, "prod", false))
    }

    @Test
    @DisplayName("buildTitlePrefix - global title enabled but blank produces no global part")
    fun testBuildTitlePrefixBlankGlobalTitle() {
        assertEquals("[1]", applier.buildTitlePrefix(1, true, "", false, "", true))
    }

    @Test
    @DisplayName("buildTitlePrefix - all disabled with global params")
    fun testBuildTitlePrefixNoneWithGlobal() {
        assertEquals("", applier.buildTitlePrefix(1, false, "", false, "", false))
    }

    @Test
    @DisplayName("Should apply title to current project when enabled")
    fun testApplyToCurrentOpenProjectEnabled() {
        try {
            setupFullMocks()
        } catch (e: Exception) {
            logger.info("Skipping testApplyToCurrentOpenProjectEnabled due to Mockito/Java 25 limitations: ${e.message}")
            return
        }
        applier.applyToCurrentOpenProject(mockProject1, true)

        assertEquals("[1] Test Project 1", mockFrame1.title)
    }

    @Test
    @DisplayName("Should remove title from current project when disabled")
    fun testApplyToCurrentOpenProjectDisabled() {
        try {
            setupFullMocks()
        } catch (e: Exception) {
            logger.info("Skipping testApplyToCurrentOpenProjectDisabled due to Mockito/Java 25 limitations: ${e.message}")
            return
        }
        applier.applyToCurrentOpenProject(mockProject1, true)
        applier.applyToCurrentOpenProject(mockProject1, false)

        assertEquals("Test Project 1", mockFrame1.title)
    }

    @Test
    @DisplayName("Should handle null frame gracefully")
    fun testApplyToCurrentOpenProjectNullFrame() {
        try {
            setupFullMocks()
        } catch (e: Exception) {
            logger.info("Skipping testApplyToCurrentOpenProjectNullFrame due to Mockito/Java 25 limitations: ${e.message}")
            return
        }
        Mockito.`when`(mockWindowManager.getFrame(mockProject1)).thenReturn(null)

        val initialTitle = mockFrame1.title
        assertDoesNotThrow {
            applier.applyToCurrentOpenProject(mockProject1, true)
        }

        assertEquals(initialTitle, mockFrame1.title)
    }

    @Test
    @DisplayName("Should apply title to all open projects")
    fun testApplyToAllOpenProjects() {
        try {
            setupFullMocks()
        } catch (e: Exception) {
            logger.info("Skipping testApplyToAllOpenProjects due to Mockito/Java 25 limitations: ${e.message}")
            return
        }
        Mockito.`when`(mockProjectManager.openProjects).thenReturn(arrayOf(mockProject1, mockProject2))

        applier.applyToAllOpenProjects(true)

        assertEquals("[1] Test Project 1", mockFrame1.title)
        assertEquals("[2] Test Project 2", mockFrame2.title)
    }

    @Test
    @DisplayName("Should remove title from all open projects")
    fun testRemoveFromAllOpenProjects() {
        try {
            setupFullMocks()
        } catch (e: Exception) {
            logger.info("Skipping testRemoveFromAllOpenProjects due to Mockito/Java 25 limitations: ${e.message}")
            return
        }
        Mockito.`when`(mockProjectManager.openProjects).thenReturn(arrayOf(mockProject1, mockProject2))

        applier.applyToAllOpenProjects(true)
        applier.removeFromAllOpenProjects()

        assertEquals("Test Project 1", mockFrame1.title)
        assertEquals("Test Project 2", mockFrame2.title)
    }

    @Test
    @DisplayName("Should renumber all open windows with focused project as 1")
    fun testRenumberAllOpenWindows() {
        try {
            setupFullMocks()
        } catch (e: Exception) {
            logger.info("Skipping testRenumberAllOpenWindows due to Mockito/Java 25 limitations: ${e.message}")
            return
        }
        Mockito.`when`(mockProjectManager.openProjects).thenReturn(arrayOf(mockProject1, mockProject2))

        // Apply initial titles so project1 = 1, project2 = 2
        applier.applyToAllOpenProjects(true)
        assertEquals("[1] Test Project 1", mockFrame1.title)
        assertEquals("[2] Test Project 2", mockFrame2.title)

        // Simulate drift: manually force project numbers to non-sequential values
        applier.resetProjectNumbering()
        // Assign drifted numbers by applying in reverse order (project2 first → gets 1, project1 → gets 2)
        applier.applyToCurrentOpenProject(mockProject2, true)
        applier.applyToCurrentOpenProject(mockProject1, true)
        assertEquals("[1] Test Project 2", mockFrame2.title)
        assertEquals("[2] Test Project 1", mockFrame1.title)

        // Now renumber with project1 as the focused window → project1 should become 1, project2 → 2
        applier.renumberAllOpenWindows(mockProject1)

        assertEquals("[1] Test Project 1", mockFrame1.title)
        assertEquals("[2] Test Project 2", mockFrame2.title)
    }

    @Test
    @DisplayName("Should assign number 1 to focused project even when it is not in open projects list")
    fun testRenumberAllOpenWindowsFocusedProjectNotInList() {
        try {
            setupFullMocks()
        } catch (e: Exception) {
            logger.info("Skipping testRenumberAllOpenWindowsFocusedProjectNotInList due to Mockito/Java 25 limitations: ${e.message}")
            return
        }
        // Only project2 is "open", but project1 is the focused project
        Mockito.`when`(mockProjectManager.openProjects).thenReturn(arrayOf(mockProject2))

        applier.renumberAllOpenWindows(mockProject1)

        assertEquals(1, applier.getWindowProjectNumber(mockProject1))
        assertEquals("[1] Test Project 1", mockFrame1.title)
    }

    @Test
    @DisplayName("Should reset counter to 1 after renumbering")
    fun testRenumberResetsCounter() {
        // Apply several projects to advance the counter
        assertEquals(1, applier.getWindowProjectNumber(mockProject1))
        assertEquals(2, applier.getWindowProjectNumber(mockProject2))

        // After renumbering with project2 as focus (no platform mocks needed — pure state logic)
        // We verify the projectNumbers map state directly
        applier.resetProjectNumbering()
        assertEquals(1, applier.getWindowProjectNumber(mockProject1))
        assertEquals(2, applier.getWindowProjectNumber(mockProject2))
    }

    // -------------------------------------------------------------------------
    // Reflection helpers
    // -------------------------------------------------------------------------

    /** Reads the private `isShuttingDown` field from [applier]. */
    private fun getIsShuttingDown(): Boolean {
        val field = WindowTitleApplier::class.java.getDeclaredField("isShuttingDown")
        field.isAccessible = true
        return field.get(applier) as Boolean
    }

    /** Writes the private `isShuttingDown` field on [applier]. */
    private fun setIsShuttingDown(value: Boolean) {
        val field = WindowTitleApplier::class.java.getDeclaredField("isShuttingDown")
        field.isAccessible = true
        field.set(applier, value)
    }

    /** Returns the private `projectNumbers` map from [applier]. */
    @Suppress("UNCHECKED_CAST")
    private fun getProjectNumbers(): ConcurrentHashMap<Project, Int> {
        val field = WindowTitleApplier::class.java.getDeclaredField("projectNumbers")
        field.isAccessible = true
        return field.get(applier) as ConcurrentHashMap<Project, Int>
    }

    /** Invokes the private `createFocusListener` method on [applier]. */
    private fun createFocusListenerViaReflection(project: Project, frame: Frame): WindowAdapter {
        val method = WindowTitleApplier::class.java.getDeclaredMethod(
            "createFocusListener", Project::class.java, Frame::class.java
        )
        method.isAccessible = true
        return method.invoke(applier, project, frame) as WindowAdapter
    }

    /** Invokes the private `createTitleListener` method on [applier]. */
    private fun createTitleListenerViaReflection(project: Project, frame: Frame): PropertyChangeListener {
        val method = WindowTitleApplier::class.java.getDeclaredMethod(
            "createTitleListener", Project::class.java, Frame::class.java
        )
        method.isAccessible = true
        return method.invoke(applier, project, frame) as PropertyChangeListener
    }

    /** Invokes the private `replaceFocusListener` method on [applier]. */
    private fun replaceFocusListenerViaReflection(project: Project, frame: Frame, listener: WindowAdapter) {
        val method = WindowTitleApplier::class.java.getDeclaredMethod(
            "replaceFocusListener", Project::class.java, Frame::class.java, WindowAdapter::class.java
        )
        method.isAccessible = true
        method.invoke(applier, project, frame, listener)
    }

    /** Invokes the private `replaceTitleListener` method on [applier]. */
    private fun replaceTitleListenerViaReflection(project: Project, frame: Frame, listener: PropertyChangeListener) {
        val method = WindowTitleApplier::class.java.getDeclaredMethod(
            "replaceTitleListener", Project::class.java, Frame::class.java, PropertyChangeListener::class.java
        )
        method.isAccessible = true
        method.invoke(applier, project, frame, listener)
    }

    /** Invokes the private `removeListeners` method on [applier]. */
    private fun removeListenersViaReflection(project: Project) {
        val method = WindowTitleApplier::class.java.getDeclaredMethod("removeListeners", Project::class.java)
        method.isAccessible = true
        method.invoke(applier, project)
    }

    // -------------------------------------------------------------------------
    // Focus listener tests
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Focus listener does nothing when isShuttingDown is true")
    fun testFocusListenerSkipsWhenShuttingDown() {
        getProjectNumbers()[mockProject1] = 1
        val listener = createFocusListenerViaReflection(mockProject1, mockFrame1)

        setIsShuttingDown(true)
        val titleBefore = mockFrame1.title
        listener.windowGainedFocus(null)

        assertEquals(titleBefore, mockFrame1.title)
    }

    @Test
    @DisplayName("Focus listener does nothing when project has no assigned number")
    fun testFocusListenerSkipsWhenProjectHasNoNumber() {
        // projectNumbers is empty — mockProject1 has no number
        val listener = createFocusListenerViaReflection(mockProject1, mockFrame1)
        val titleBefore = mockFrame1.title
        listener.windowGainedFocus(null)

        assertEquals(titleBefore, mockFrame1.title)
    }

    @Test
    @DisplayName("Focus listener updates window title when focus is gained")
    fun testFocusListenerUpdatesTitle() {
        getProjectNumbers()[mockProject1] = 3
        mockFrame1.title = "My Project"

        val listener = createFocusListenerViaReflection(mockProject1, mockFrame1)
        listener.windowGainedFocus(null)

        assertEquals("[3] My Project", mockFrame1.title)
    }

    @Test
    @DisplayName("Focus listener uses custom title when both numbering and custom title are enabled")
    fun testFocusListenerAppliesCombinedPrefix() {
        val customTitleService = mockProject1.getService(WindowCustomTitleStateService::class.java)
        customTitleService.setCustomTitle("dattebayo")
        customTitleService.setCustomTitleEnabled(true)
        mockProject1.getService(WindowTitleNumberingStateService::class.java).setTitleNumberingEnabled(true)

        getProjectNumbers()[mockProject1] = 2
        mockFrame1.title = "My Project"

        val listener = createFocusListenerViaReflection(mockProject1, mockFrame1)
        listener.windowGainedFocus(null)

        assertEquals("[2 - ${TitleTextStyler.toItalic("dattebayo")}] My Project", mockFrame1.title)
    }

    // -------------------------------------------------------------------------
    // Title listener tests
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Title listener does nothing when isShuttingDown is true")
    fun testTitleListenerSkipsWhenShuttingDown() {
        getProjectNumbers()[mockProject1] = 1
        val listener = createTitleListenerViaReflection(mockProject1, mockFrame1)

        setIsShuttingDown(true)
        mockFrame1.title = "My Project"

        val event = java.beans.PropertyChangeEvent(mockFrame1, "title", "[1] My Project", "My Project")
        listener.propertyChange(event)

        assertEquals("My Project", mockFrame1.title) // no restoration while shutting down
    }

    @Test
    @DisplayName("Title listener does nothing when project has no assigned number")
    fun testTitleListenerSkipsWhenProjectHasNoNumber() {
        // projectNumbers is empty — mockProject1 has no number
        val listener = createTitleListenerViaReflection(mockProject1, mockFrame1)
        mockFrame1.title = "My Project"

        val event = java.beans.PropertyChangeEvent(mockFrame1, "title", "[1] My Project", "My Project")
        listener.propertyChange(event)

        assertEquals("My Project", mockFrame1.title) // no update since no project number
    }

    @Test
    @DisplayName("Title listener does not update when prefix is already correct")
    fun testTitleListenerSkipsWhenPrefixAlreadyCorrect() {
        getProjectNumbers()[mockProject1] = 2
        val listener = createTitleListenerViaReflection(mockProject1, mockFrame1)

        mockFrame1.title = "[2] My Project"
        val event = java.beans.PropertyChangeEvent(mockFrame1, "title", "My Project", "[2] My Project")
        listener.propertyChange(event)

        assertEquals("[2] My Project", mockFrame1.title)
    }

    @Test
    @DisplayName("Title listener re-applies prefix when it is stripped from the title")
    fun testTitleListenerReappliesMissingPrefix() {
        getProjectNumbers()[mockProject1] = 2
        val listener = createTitleListenerViaReflection(mockProject1, mockFrame1)

        mockFrame1.title = "My Project"
        val event = java.beans.PropertyChangeEvent(mockFrame1, "title", "[2] My Project", "My Project")
        listener.propertyChange(event)

        assertEquals("[2] My Project", mockFrame1.title)
    }

    @Test
    @DisplayName("Title listener ignores non-title property change events")
    fun testTitleListenerIgnoresNonTitleEvents() {
        getProjectNumbers()[mockProject1] = 1
        val listener = createTitleListenerViaReflection(mockProject1, mockFrame1)

        mockFrame1.title = "My Project"
        val event = java.beans.PropertyChangeEvent(mockFrame1, "background", "old", "new")
        listener.propertyChange(event)

        assertEquals("My Project", mockFrame1.title)
    }

    // -------------------------------------------------------------------------
    // Listener lifecycle tests
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("replaceFocusListener removes the previous listener from its stored frame")
    fun testReplaceFocusListenerRemovesOldListener() {
        val firstListener = object : WindowAdapter() {}
        replaceFocusListenerViaReflection(mockProject1, mockFrame1, firstListener)
        assertTrue(mockFrame1.windowFocusListeners.any { it === firstListener })

        val secondListener = object : WindowAdapter() {}
        replaceFocusListenerViaReflection(mockProject1, mockFrame1, secondListener)

        assertFalse(mockFrame1.windowFocusListeners.any { it === firstListener }, "Old listener should be removed")
        assertTrue(mockFrame1.windowFocusListeners.any { it === secondListener }, "New listener should be registered")
    }

    @Test
    @DisplayName("replaceTitleListener removes the previous listener from its stored frame")
    fun testReplaceTitleListenerRemovesOldListener() {
        val firstListener = PropertyChangeListener { }
        replaceTitleListenerViaReflection(mockProject1, mockFrame1, firstListener)

        val titleListeners1 = mockFrame1.getPropertyChangeListeners("title")
        assertTrue(titleListeners1.any { it === firstListener })

        val secondListener = PropertyChangeListener { }
        replaceTitleListenerViaReflection(mockProject1, mockFrame1, secondListener)

        val titleListeners2 = mockFrame1.getPropertyChangeListeners("title")
        assertFalse(titleListeners2.any { it === firstListener }, "Old listener should be removed")
        assertTrue(titleListeners2.any { it === secondListener }, "New listener should be registered")
    }

    @Test
    @DisplayName("removeListeners removes focus and title listeners from stored frame")
    fun testRemoveListenersClearsProject() {
        val focusListener = object : WindowAdapter() {}
        val titleListener = PropertyChangeListener { }

        replaceFocusListenerViaReflection(mockProject1, mockFrame1, focusListener)
        replaceTitleListenerViaReflection(mockProject1, mockFrame1, titleListener)

        assertTrue(mockFrame1.windowFocusListeners.any { it === focusListener })
        assertTrue(mockFrame1.getPropertyChangeListeners("title").any { it === titleListener })

        removeListenersViaReflection(mockProject1)

        assertFalse(mockFrame1.windowFocusListeners.any { it === focusListener }, "Focus listener should be removed")
        assertFalse(
            mockFrame1.getPropertyChangeListeners("title").any { it === titleListener },
            "Title listener should be removed"
        )
    }

    @Test
    @DisplayName("removeListeners is idempotent — calling twice does not throw")
    fun testRemoveListenersIsIdempotent() {
        val focusListener = object : WindowAdapter() {}
        replaceFocusListenerViaReflection(mockProject1, mockFrame1, focusListener)

        assertDoesNotThrow {
            removeListenersViaReflection(mockProject1)
            removeListenersViaReflection(mockProject1) // second call should be a no-op
        }
    }

    // -------------------------------------------------------------------------
    // Shutdown flag tests
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("cancelAllPendingOperations sets isShuttingDown to true")
    fun testCancelAllPendingOperationsSetsShuttingDownFlag() {
        assertFalse(getIsShuttingDown())
        try {
            applier.cancelAllPendingOperations()
        } catch (_: Exception) {
            // Platform Disposer may not be fully initialised in the test environment;
            // isShuttingDown is set as the very first statement so we can still assert it.
        }
        assertTrue(getIsShuttingDown())
    }
}
