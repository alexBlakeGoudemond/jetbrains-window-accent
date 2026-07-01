package com.window_accent.configuration.persistence

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("GlobalCustomTitleStateService Tests")
class GlobalCustomTitleStateServiceTest {

    private lateinit var service: GlobalCustomTitleStateService

    @BeforeEach
    fun setup() {
        service = GlobalCustomTitleStateService()
    }

    @Test
    fun `initial global custom title is blank`() {
        assertEquals("", service.getGlobalCustomTitle())
    }

    @Test
    fun `initial global custom title enabled is false`() {
        assertFalse(service.isGlobalCustomTitleEnabled())
        assertTrue(service.isGlobalCustomTitleDisabled())
    }

    @Test
    fun `can set and retrieve global custom title`() {
        service.setGlobalCustomTitle("prod")
        assertEquals("prod", service.getGlobalCustomTitle())
    }

    @Test
    fun `can overwrite global custom title`() {
        service.setGlobalCustomTitle("first")
        service.setGlobalCustomTitle("second")
        assertEquals("second", service.getGlobalCustomTitle())
    }

    @Test
    fun `can set global custom title to blank`() {
        service.setGlobalCustomTitle("prod")
        service.setGlobalCustomTitle("")
        assertEquals("", service.getGlobalCustomTitle())
    }

    @Test
    fun `can enable global custom title`() {
        service.setGlobalCustomTitleEnabled(true)
        assertTrue(service.isGlobalCustomTitleEnabled())
        assertFalse(service.isGlobalCustomTitleDisabled())
    }

    @Test
    fun `can disable global custom title`() {
        service.setGlobalCustomTitleEnabled(true)
        service.setGlobalCustomTitleEnabled(false)
        assertFalse(service.isGlobalCustomTitleEnabled())
        assertTrue(service.isGlobalCustomTitleDisabled())
    }

    @Test
    fun `isGlobalCustomTitleEnabled opposite of isGlobalCustomTitleDisabled`() {
        service.setGlobalCustomTitleEnabled(true)
        assertTrue(service.isGlobalCustomTitleEnabled())
        assertFalse(service.isGlobalCustomTitleDisabled())

        service.setGlobalCustomTitleEnabled(false)
        assertFalse(service.isGlobalCustomTitleEnabled())
        assertTrue(service.isGlobalCustomTitleDisabled())
    }

    @Test
    fun `global custom title loaded when enabled`() {
        val newState = GlobalCustomTitleStateService.State(globalCustomTitle = "prod", globalCustomTitleEnabled = true)
        service.loadState(newState)
        assertEquals("prod", service.getGlobalCustomTitle())
        assertTrue(service.isGlobalCustomTitleEnabled())
    }

    @Test
    fun `global custom title loaded when disabled`() {
        val newState = GlobalCustomTitleStateService.State(globalCustomTitle = "prod", globalCustomTitleEnabled = false)
        service.loadState(newState)
        assertEquals("prod", service.getGlobalCustomTitle())
        assertFalse(service.isGlobalCustomTitleEnabled())
    }

    @Test
    fun `when global custom title is enabled, can get state`() {
        service.setGlobalCustomTitle("prod")
        service.setGlobalCustomTitleEnabled(true)
        val state = service.getState()
        assertEquals("prod", state.globalCustomTitle)
        assertTrue(state.globalCustomTitleEnabled)
    }

    @Test
    fun `when global custom title is disabled, can get state`() {
        service.setGlobalCustomTitle("prod")
        service.setGlobalCustomTitleEnabled(false)
        val state = service.getState()
        assertEquals("prod", state.globalCustomTitle)
        assertFalse(state.globalCustomTitleEnabled)
    }

    @Test
    fun `can transition global custom title enabled state`() {
        assertFalse(service.isGlobalCustomTitleEnabled())

        service.setGlobalCustomTitleEnabled(true)
        assertTrue(service.isGlobalCustomTitleEnabled())

        service.setGlobalCustomTitleEnabled(false)
        assertFalse(service.isGlobalCustomTitleEnabled())

        service.setGlobalCustomTitleEnabled(true)
        assertTrue(service.isGlobalCustomTitleEnabled())
    }

    @Test
    fun `toggling global custom title enabled rapidly works`() {
        repeat(10) { index ->
            val shouldBeEnabled = index % 2 == 0
            service.setGlobalCustomTitleEnabled(shouldBeEnabled)
            assertEquals(shouldBeEnabled, service.isGlobalCustomTitleEnabled())
        }
    }

    @Test
    fun `global custom title and enabled state are persisted independently`() {
        service.setGlobalCustomTitle("hello")
        service.setGlobalCustomTitleEnabled(true)

        service.setGlobalCustomTitle("world")
        assertTrue(service.isGlobalCustomTitleEnabled())

        service.setGlobalCustomTitleEnabled(false)
        assertEquals("world", service.getGlobalCustomTitle())
    }

    @Test
    fun `loading state replaces both fields`() {
        service.setGlobalCustomTitle("old")
        service.setGlobalCustomTitleEnabled(true)

        val newState = GlobalCustomTitleStateService.State(globalCustomTitle = "new", globalCustomTitleEnabled = false)
        service.loadState(newState)

        assertEquals("new", service.getGlobalCustomTitle())
        assertFalse(service.isGlobalCustomTitleEnabled())
    }

    @Test
    fun `loading state is same as retrieving state`() {
        val stateToLoad = GlobalCustomTitleStateService.State(globalCustomTitle = "test", globalCustomTitleEnabled = true)
        service.loadState(stateToLoad)
        val retrievedState = service.getState()
        assertEquals(stateToLoad.globalCustomTitle, retrievedState.globalCustomTitle)
        assertEquals(stateToLoad.globalCustomTitleEnabled, retrievedState.globalCustomTitleEnabled)
    }

    // -------------------------------------------------------------------------
    // Emoji persistence — supplementary characters survive the XML round-trip
    // -------------------------------------------------------------------------

    @Test
    fun `emoji is accessible in memory after being set`() {
        val rocket = String(Character.toChars(0x1F680))  // 🚀
        service.setGlobalCustomTitle(rocket)
        assertEquals(rocket, service.getGlobalCustomTitle())
    }

    @Test
    fun `getState encodes emoji as ASCII-safe escape for XML serialisation`() {
        val rocket = String(Character.toChars(0x1F680))  // 🚀
        service.setGlobalCustomTitle(rocket)
        assertEquals("\\u{1F680}", service.getState().globalCustomTitle)
    }

    @Test
    fun `loadState decodes emoji escape back to the original character`() {
        service.loadState(GlobalCustomTitleStateService.State(globalCustomTitle = "\\u{1F680}", globalCustomTitleEnabled = true))
        assertEquals(String(Character.toChars(0x1F680)), service.getGlobalCustomTitle())
    }

    @Test
    fun `emoji persists correctly across xml round-trip`() {
        val rocket = String(Character.toChars(0x1F680))  // 🚀

        // Session 1: user sets the emoji title
        service.setGlobalCustomTitle(rocket)

        // Simulate: IntelliJ writes state to XML (encoded form)
        val stateForXml = service.getState()

        // Session 2: IntelliJ reads the state back from XML (encoded form passed to loadState)
        val freshService = GlobalCustomTitleStateService()
        freshService.loadState(stateForXml)

        assertEquals(rocket, freshService.getGlobalCustomTitle())
    }

    @Test
    fun `mixed text and emoji persists correctly across xml round-trip`() {
        val rocket = String(Character.toChars(0x1F680))  // 🚀
        val original = "prod $rocket"

        service.setGlobalCustomTitle(original)
        val stateForXml = service.getState()

        val freshService = GlobalCustomTitleStateService()
        freshService.loadState(stateForXml)

        assertEquals(original, freshService.getGlobalCustomTitle())
    }

    @Test
    fun `plain ASCII title is unaffected by encode and decode`() {
        service.setGlobalCustomTitle("prod")
        val stateForXml = service.getState()
        assertEquals("prod", stateForXml.globalCustomTitle)

        val freshService = GlobalCustomTitleStateService()
        freshService.loadState(stateForXml)
        assertEquals("prod", freshService.getGlobalCustomTitle())
    }
}
