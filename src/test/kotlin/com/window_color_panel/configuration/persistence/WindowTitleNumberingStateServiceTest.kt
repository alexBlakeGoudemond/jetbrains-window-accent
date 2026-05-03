package com.window_color_panel.configuration.persistence

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("WindowTitleNumberingStateService Tests")
class WindowTitleNumberingStateServiceTest {

    private lateinit var service: WindowTitleNumberingStateService

    @BeforeEach
    fun setup() {
        service = WindowTitleNumberingStateService()
    }

    @Test
    @DisplayName("Initial state should have title numbering enabled")
    fun testInitialState() {
        assertTrue(service.isTitleNumberingEnabled())
        assertFalse(service.isTitleNumberingDisabled())
    }

    @Test
    @DisplayName("Should be able to enable title numbering")
    fun testSetTitleNumberingEnabledTrue() {
        service.setTitleNumberingEnabled(false)
        service.setTitleNumberingEnabled(true)
        assertTrue(service.isTitleNumberingEnabled())
    }

    @Test
    @DisplayName("Should be able to disable title numbering")
    fun testSetTitleNumberingEnabledFalse() {
        service.setTitleNumberingEnabled(false)
        assertFalse(service.isTitleNumberingEnabled())
    }

    @Test
    @DisplayName("isTitleNumberingEnabled should return opposite of isTitleNumberingDisabled")
    fun testTitleNumberingEnabledDisabledInverse() {
        service.setTitleNumberingEnabled(true)
        assertTrue(service.isTitleNumberingEnabled())
        assertFalse(service.isTitleNumberingDisabled())

        service.setTitleNumberingEnabled(false)
        assertFalse(service.isTitleNumberingEnabled())
        assertTrue(service.isTitleNumberingDisabled())
    }

    @Test
    @DisplayName("Should correctly load state with enabled")
    fun testLoadStateEnabled() {
        val newState = WindowTitleNumberingStateService.State(titleNumberingEnabled = true)
        service.loadState(newState)
        assertTrue(service.isTitleNumberingEnabled())
    }

    @Test
    @DisplayName("Should correctly load state with disabled")
    fun testLoadStateDisabled() {
        val newState = WindowTitleNumberingStateService.State(titleNumberingEnabled = false)
        service.loadState(newState)
        assertFalse(service.isTitleNumberingEnabled())
    }

    @Test
    @DisplayName("Should correctly return current state when enabled")
    fun testGetStateEnabled() {
        service.setTitleNumberingEnabled(true)
        val state = service.getState()
        assertTrue(state.titleNumberingEnabled)
    }

    @Test
    @DisplayName("Should correctly return current state when disabled")
    fun testGetStateDisabled() {
        service.setTitleNumberingEnabled(false)
        val state = service.getState()
        assertFalse(state.titleNumberingEnabled)
    }

    @Test
    @DisplayName("Should handle multiple state changes")
    fun testMultipleStateChanges() {
        // Start enabled
        assertTrue(service.isTitleNumberingEnabled())

        // Disable
        service.setTitleNumberingEnabled(false)
        assertFalse(service.isTitleNumberingEnabled())

        // Enable
        service.setTitleNumberingEnabled(true)
        assertTrue(service.isTitleNumberingEnabled())

        // Disable again
        service.setTitleNumberingEnabled(false)
        assertFalse(service.isTitleNumberingEnabled())

        // Enable again
        service.setTitleNumberingEnabled(true)
        assertTrue(service.isTitleNumberingEnabled())
    }

    @Test
    @DisplayName("Should handle rapid state toggles")
    fun testRapidToggles() {
        repeat(10) { index ->
            val shouldBeEnabled = index % 2 == 0
            service.setTitleNumberingEnabled(shouldBeEnabled)
            assertEquals(shouldBeEnabled, service.isTitleNumberingEnabled())
        }
    }

    @Test
    @DisplayName("Should preserve state after getting it")
    fun testStatePreservationAfterGet() {
        service.setTitleNumberingEnabled(false)
        val state1 = service.getState()
        val state2 = service.getState()

        assertEquals(state1.titleNumberingEnabled, state2.titleNumberingEnabled)
        assertFalse(service.isTitleNumberingEnabled())
    }

    @Test
    @DisplayName("Load and get state should be consistent")
    fun testLoadAndGetConsistency() {
        val stateToLoad = WindowTitleNumberingStateService.State(titleNumberingEnabled = false)
        service.loadState(stateToLoad)

        val retrievedState = service.getState()
        assertEquals(stateToLoad.titleNumberingEnabled, retrievedState.titleNumberingEnabled)
    }

    @Test
    @DisplayName("Setting same state multiple times should work correctly")
    fun testSettingSameStateMultipleTimes() {
        service.setTitleNumberingEnabled(true)
        service.setTitleNumberingEnabled(true)
        service.setTitleNumberingEnabled(true)

        assertTrue(service.isTitleNumberingEnabled())
        assertFalse(service.isTitleNumberingDisabled())

        service.setTitleNumberingEnabled(false)
        service.setTitleNumberingEnabled(false)
        service.setTitleNumberingEnabled(false)

        assertFalse(service.isTitleNumberingEnabled())
        assertTrue(service.isTitleNumberingDisabled())
    }
}

