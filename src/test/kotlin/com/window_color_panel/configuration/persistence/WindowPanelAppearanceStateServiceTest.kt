package com.window_color_panel.configuration.persistence

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("WindowPanelAppearanceStateService Tests")
class WindowPanelAppearanceStateServiceTest {

    private lateinit var service: WindowPanelAppearanceStateService

    @BeforeEach
    fun setup() {
        service = WindowPanelAppearanceStateService()
    }

    @Test
    @DisplayName("Initial state should have panel on EAST side and enabled")
    fun testInitialState() {
        assertEquals(WindowPanelAppearanceStateService.Side.EAST, service.getSide())
        assertTrue(service.panelIsEnabled())
        assertFalse(service.panelIsDisabled())
    }

    // todo - paramaterized test? with 4 Directions below
    @Test
    @DisplayName("Should set side to WEST")
    fun testSetSideWest() {
        service.setSide(WindowPanelAppearanceStateService.Side.WEST)
        assertEquals(WindowPanelAppearanceStateService.Side.WEST, service.getSide())
    }

    @Test
    @DisplayName("Should set side to NORTH")
    fun testSetSideNorth() {
        service.setSide(WindowPanelAppearanceStateService.Side.NORTH)
        assertEquals(WindowPanelAppearanceStateService.Side.NORTH, service.getSide())
    }

    @Test
    @DisplayName("Should set side to SOUTH")
    fun testSetSideSouth() {
        service.setSide(WindowPanelAppearanceStateService.Side.SOUTH)
        assertEquals(WindowPanelAppearanceStateService.Side.SOUTH, service.getSide())
    }

    @Test
    @DisplayName("Should set side to EAST")
    fun testSetSideEast() {
        service.setSide(WindowPanelAppearanceStateService.Side.WEST)
        service.setSide(WindowPanelAppearanceStateService.Side.EAST)
        assertEquals(WindowPanelAppearanceStateService.Side.EAST, service.getSide())
    }

    @Test
    @DisplayName("Should enable panel")
    fun testSetPanelEnabled() {
        service.setPanelEnabled(false)
        assertFalse(service.panelIsEnabled())
        assertTrue(service.panelIsDisabled())

        service.setPanelEnabled(true)
        assertTrue(service.panelIsEnabled())
        assertFalse(service.panelIsDisabled())
    }

    @Test
    @DisplayName("Should disable panel")
    fun testSetPanelDisabled() {
        service.setPanelEnabled(false)
        assertFalse(service.panelIsEnabled())
        assertTrue(service.panelIsDisabled())
    }

    @Test
    @DisplayName("panelIsEnabled should return opposite of panelIsDisabled")
    fun testPanelEnabledDisabledInverse() {
        service.setPanelEnabled(true)
        assertTrue(service.panelIsEnabled())
        assertFalse(service.panelIsDisabled())

        service.setPanelEnabled(false)
        assertFalse(service.panelIsEnabled())
        assertTrue(service.panelIsDisabled())
    }

    @Test
    @DisplayName("Should correctly load state")
    fun testLoadState() {
        val newState = WindowPanelAppearanceStateService.State(
            side = WindowPanelAppearanceStateService.Side.NORTH,
            panelEnabled = false
        )
        service.loadState(newState)

        assertEquals(WindowPanelAppearanceStateService.Side.NORTH, service.getSide())
        assertFalse(service.panelIsEnabled())
    }

    @Test
    @DisplayName("Should correctly return current state")
    fun testGetState() {
        service.setSide(WindowPanelAppearanceStateService.Side.SOUTH)
        service.setPanelEnabled(false)

        val state = service.getState()
        assertEquals(WindowPanelAppearanceStateService.Side.SOUTH, state.side)
        assertFalse(state.panelEnabled)
    }

    @Test
    @DisplayName("Should handle state transitions correctly")
    fun testStateTransitions() {
        // Start at EAST and enabled
        assertEquals(WindowPanelAppearanceStateService.Side.EAST, service.getSide())
        assertTrue(service.panelIsEnabled())

        // Change to WEST
        service.setSide(WindowPanelAppearanceStateService.Side.WEST)
        assertEquals(WindowPanelAppearanceStateService.Side.WEST, service.getSide())

        // Disable panel
        service.setPanelEnabled(false)
        assertFalse(service.panelIsEnabled())
        assertEquals(WindowPanelAppearanceStateService.Side.WEST, service.getSide())

        // Change back to EAST while disabled
        service.setSide(WindowPanelAppearanceStateService.Side.EAST)
        assertEquals(WindowPanelAppearanceStateService.Side.EAST, service.getSide())
        assertFalse(service.panelIsEnabled())

        // Re-enable panel
        service.setPanelEnabled(true)
        assertTrue(service.panelIsEnabled())
        assertEquals(WindowPanelAppearanceStateService.Side.EAST, service.getSide())
    }

    @Test
    @DisplayName("Should cycle through all sides")
    fun testAllSides() {
        val sides = listOf(
            WindowPanelAppearanceStateService.Side.EAST,
            WindowPanelAppearanceStateService.Side.WEST,
            WindowPanelAppearanceStateService.Side.NORTH,
            WindowPanelAppearanceStateService.Side.SOUTH
        )

        for (side in sides) {
            service.setSide(side)
            assertEquals(side, service.getSide())
        }
    }

    @Test
    @DisplayName("Should preserve panel enabled state when changing sides")
    fun testPanelStatePreservedWhenChangingSides() {
        service.setPanelEnabled(false)

        service.setSide(WindowPanelAppearanceStateService.Side.NORTH)
        assertFalse(service.panelIsEnabled())

        service.setSide(WindowPanelAppearanceStateService.Side.SOUTH)
        assertFalse(service.panelIsEnabled())
    }

    @Test
    @DisplayName("Should preserve side when changing panel enabled state")
    fun testSidePreservedWhenChangingPanelState() {
        service.setSide(WindowPanelAppearanceStateService.Side.NORTH)

        service.setPanelEnabled(false)
        assertEquals(WindowPanelAppearanceStateService.Side.NORTH, service.getSide())

        service.setPanelEnabled(true)
        assertEquals(WindowPanelAppearanceStateService.Side.NORTH, service.getSide())
    }
}

