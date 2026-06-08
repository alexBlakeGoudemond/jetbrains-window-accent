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
}

