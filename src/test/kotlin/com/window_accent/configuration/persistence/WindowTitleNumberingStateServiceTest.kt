package com.window_accent.configuration.persistence

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

    // TODO BlakeGoudemond 2026/05/16 | first window title is off by default
    @Test
    fun `initial title numbering is enabled`() {
        assertTrue(service.isTitleNumberingEnabled())
        assertFalse(service.isTitleNumberingDisabled())
    }

    @Test
    fun `can enable title numbering`() {
        service.setTitleNumberingEnabled(false)
        service.setTitleNumberingEnabled(true)
        assertTrue(service.isTitleNumberingEnabled())
    }

    @Test
    fun `can disable title numbering`() {
        service.setTitleNumberingEnabled(false)
        assertFalse(service.isTitleNumberingEnabled())
    }

    @Test
    fun `isTitleNumberingEnabled opposite of isTitleNumberingDisabled`() {
        service.setTitleNumberingEnabled(true)
        assertTrue(service.isTitleNumberingEnabled())
        assertFalse(service.isTitleNumberingDisabled())

        service.setTitleNumberingEnabled(false)
        assertFalse(service.isTitleNumberingEnabled())
        assertTrue(service.isTitleNumberingDisabled())
    }

    @Test
    fun `title numbering loaded when enabled`() {
        val newState = WindowTitleNumberingStateService.State(titleNumberingEnabled = true)
        service.loadState(newState)
        assertTrue(service.isTitleNumberingEnabled())
    }

    @Test
    fun `title numbering missing when disabled`() {
        val newState = WindowTitleNumberingStateService.State(titleNumberingEnabled = false)
        service.loadState(newState)
        assertFalse(service.isTitleNumberingEnabled())
    }

    @Test
    fun `when title numbering is enabled, can get state`() {
        service.setTitleNumberingEnabled(true)
        val state = service.getState()
        assertTrue(state.titleNumberingEnabled)
    }

    @Test
    fun `when title numbering is disabled, can get state`() {
        service.setTitleNumberingEnabled(false)
        val state = service.getState()
        assertFalse(state.titleNumberingEnabled)
    }

    @Test
    fun `can transition between title numbering`() {
        assertTrue(service.isTitleNumberingEnabled())

        service.setTitleNumberingEnabled(false)
        assertFalse(service.isTitleNumberingEnabled())

        service.setTitleNumberingEnabled(true)
        assertTrue(service.isTitleNumberingEnabled())

        service.setTitleNumberingEnabled(false)
        assertFalse(service.isTitleNumberingEnabled())

        service.setTitleNumberingEnabled(true)
        assertTrue(service.isTitleNumberingEnabled())
    }

    @Test
    fun `toggling title numbering rapidly works`() {
        repeat(10) { index ->
            val shouldBeEnabled = index % 2 == 0
            service.setTitleNumberingEnabled(shouldBeEnabled)
            assertEquals(shouldBeEnabled, service.isTitleNumberingEnabled())
        }
    }

    @Test
    fun `title numbering is preserved after retrieving more than once`() {
        service.setTitleNumberingEnabled(false)
        val state1 = service.getState()
        val state2 = service.getState()

        assertEquals(state1.titleNumberingEnabled, state2.titleNumberingEnabled)
        assertFalse(service.isTitleNumberingEnabled())
    }

    @Test
    fun `loading title numbering same as retrieving`() {
        val stateToLoad = WindowTitleNumberingStateService.State(titleNumberingEnabled = false)
        service.loadState(stateToLoad)

        val retrievedState = service.getState()
        assertEquals(stateToLoad.titleNumberingEnabled, retrievedState.titleNumberingEnabled)
    }

    @Test
    fun `setting title numbering multiple times works`() {
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

