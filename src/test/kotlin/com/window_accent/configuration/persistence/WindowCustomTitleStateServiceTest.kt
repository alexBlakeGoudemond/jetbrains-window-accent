package com.window_accent.configuration.persistence

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("WindowCustomTitleStateService Tests")
class WindowCustomTitleStateServiceTest {

    private lateinit var service: WindowCustomTitleStateService

    @BeforeEach
    fun setup() {
        service = WindowCustomTitleStateService()
    }

    @Test
    fun `initial custom title is blank`() {
        assertEquals("", service.getCustomTitle())
    }

    @Test
    fun `initial custom title enabled is false`() {
        assertFalse(service.isCustomTitleEnabled())
        assertTrue(service.isCustomTitleDisabled())
    }

    @Test
    fun `can set and retrieve custom title`() {
        service.setCustomTitle("dattebayo")
        assertEquals("dattebayo", service.getCustomTitle())
    }

    @Test
    fun `can overwrite custom title`() {
        service.setCustomTitle("first")
        service.setCustomTitle("second")
        assertEquals("second", service.getCustomTitle())
    }

    @Test
    fun `can set custom title to blank`() {
        service.setCustomTitle("dattebayo")
        service.setCustomTitle("")
        assertEquals("", service.getCustomTitle())
    }

    @Test
    fun `can enable custom title`() {
        service.setCustomTitleEnabled(true)
        assertTrue(service.isCustomTitleEnabled())
        assertFalse(service.isCustomTitleDisabled())
    }

    @Test
    fun `can disable custom title`() {
        service.setCustomTitleEnabled(true)
        service.setCustomTitleEnabled(false)
        assertFalse(service.isCustomTitleEnabled())
        assertTrue(service.isCustomTitleDisabled())
    }

    @Test
    fun `isCustomTitleEnabled opposite of isCustomTitleDisabled`() {
        service.setCustomTitleEnabled(true)
        assertTrue(service.isCustomTitleEnabled())
        assertFalse(service.isCustomTitleDisabled())

        service.setCustomTitleEnabled(false)
        assertFalse(service.isCustomTitleEnabled())
        assertTrue(service.isCustomTitleDisabled())
    }

    @Test
    fun `custom title loaded when enabled`() {
        val newState = WindowCustomTitleStateService.State(customTitle = "naruto", customTitleEnabled = true)
        service.loadState(newState)
        assertEquals("naruto", service.getCustomTitle())
        assertTrue(service.isCustomTitleEnabled())
    }

    @Test
    fun `custom title loaded when disabled`() {
        val newState = WindowCustomTitleStateService.State(customTitle = "naruto", customTitleEnabled = false)
        service.loadState(newState)
        assertEquals("naruto", service.getCustomTitle())
        assertFalse(service.isCustomTitleEnabled())
    }

    @Test
    fun `when custom title is enabled, can get state`() {
        service.setCustomTitle("dattebayo")
        service.setCustomTitleEnabled(true)
        val state = service.getState()
        assertEquals("dattebayo", state.customTitle)
        assertTrue(state.customTitleEnabled)
    }

    @Test
    fun `when custom title is disabled, can get state`() {
        service.setCustomTitle("dattebayo")
        service.setCustomTitleEnabled(false)
        val state = service.getState()
        assertEquals("dattebayo", state.customTitle)
        assertFalse(state.customTitleEnabled)
    }

    @Test
    fun `can transition custom title enabled state`() {
        assertFalse(service.isCustomTitleEnabled())

        service.setCustomTitleEnabled(true)
        assertTrue(service.isCustomTitleEnabled())

        service.setCustomTitleEnabled(false)
        assertFalse(service.isCustomTitleEnabled())

        service.setCustomTitleEnabled(true)
        assertTrue(service.isCustomTitleEnabled())
    }

    @Test
    fun `toggling custom title enabled rapidly works`() {
        repeat(10) { index ->
            val shouldBeEnabled = index % 2 == 0
            service.setCustomTitleEnabled(shouldBeEnabled)
            assertEquals(shouldBeEnabled, service.isCustomTitleEnabled())
        }
    }

    @Test
    fun `custom title and enabled state are persisted independently`() {
        service.setCustomTitle("hello")
        service.setCustomTitleEnabled(true)

        service.setCustomTitle("world")
        assertTrue(service.isCustomTitleEnabled())

        service.setCustomTitleEnabled(false)
        assertEquals("world", service.getCustomTitle())
    }

    @Test
    fun `loading state replaces both fields`() {
        service.setCustomTitle("old")
        service.setCustomTitleEnabled(true)

        val newState = WindowCustomTitleStateService.State(customTitle = "new", customTitleEnabled = false)
        service.loadState(newState)

        assertEquals("new", service.getCustomTitle())
        assertFalse(service.isCustomTitleEnabled())
    }

    @Test
    fun `loading state is same as retrieving state`() {
        val stateToLoad = WindowCustomTitleStateService.State(customTitle = "test", customTitleEnabled = true)
        service.loadState(stateToLoad)
        val retrievedState = service.getState()
        assertEquals(stateToLoad.customTitle, retrievedState.customTitle)
        assertEquals(stateToLoad.customTitleEnabled, retrievedState.customTitleEnabled)
    }

    // -------------------------------------------------------------------------
    // Emoji persistence — supplementary characters survive the XML round-trip
    // -------------------------------------------------------------------------

    @Test
    fun `emoji is accessible in memory after being set`() {
        val rocket = String(Character.toChars(0x1F680))  // 🚀
        service.setCustomTitle(rocket)
        assertEquals(rocket, service.getCustomTitle())
    }

    @Test
    fun `getState encodes emoji as ASCII-safe escape for XML serialisation`() {
        val rocket = String(Character.toChars(0x1F680))  // 🚀
        service.setCustomTitle(rocket)
        assertEquals("\\u{1F680}", service.getState().customTitle)
    }

    @Test
    fun `loadState decodes emoji escape back to the original character`() {
        service.loadState(WindowCustomTitleStateService.State(customTitle = "\\u{1F680}", customTitleEnabled = true))
        assertEquals(String(Character.toChars(0x1F680)), service.getCustomTitle())
    }

    @Test
    fun `emoji persists correctly across xml round-trip`() {
        val rocket = String(Character.toChars(0x1F680))  // 🚀

        // Session 1: user sets the emoji title
        service.setCustomTitle(rocket)

        // Simulate: IntelliJ writes state to XML (encoded form)
        val stateForXml = service.getState()

        // Session 2: IntelliJ reads the state back from XML (encoded form passed to loadState)
        val freshService = WindowCustomTitleStateService()
        freshService.loadState(stateForXml)

        assertEquals(rocket, freshService.getCustomTitle())
    }

    @Test
    fun `mixed text and emoji persists correctly across xml round-trip`() {
        val rocket = String(Character.toChars(0x1F680))  // 🚀
        val original = "prod $rocket"

        service.setCustomTitle(original)
        val stateForXml = service.getState()

        val freshService = WindowCustomTitleStateService()
        freshService.loadState(stateForXml)

        assertEquals(original, freshService.getCustomTitle())
    }

    @Test
    fun `plain ASCII title is unaffected by encode and decode`() {
        service.setCustomTitle("dattebayo")
        val stateForXml = service.getState()
        assertEquals("dattebayo", stateForXml.customTitle)

        val freshService = WindowCustomTitleStateService()
        freshService.loadState(stateForXml)
        assertEquals("dattebayo", freshService.getCustomTitle())
    }
}

