package com.window_accent.configuration.persistence

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("WindowPanelAppearanceStateService Tests")
class WindowPanelAppearanceStateServiceTest {

    private lateinit var service: WindowPanelAppearanceStateService

    @BeforeEach
    fun setup() {
        service = WindowPanelAppearanceStateService()
    }

    @Test
    fun `Initial panel is EAST and enabled`() {
        assertEquals(WindowPanelAppearanceStateService.Side.EAST, service.getSide())
        assertTrue(service.panelIsEnabled())
        assertFalse(service.panelIsDisabled())
    }

    @ParameterizedTest
    @MethodSource("cardinalDirections")
    fun `can set color side`(side: WindowPanelAppearanceStateService.Side) {
        service.setSide(side)
        assertEquals(side, service.getSide())
    }

    companion object {
        @JvmStatic
        fun cardinalDirections(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(WindowPanelAppearanceStateService.Side.EAST),
                Arguments.of(WindowPanelAppearanceStateService.Side.WEST),
                Arguments.of(WindowPanelAppearanceStateService.Side.NORTH),
                Arguments.of(WindowPanelAppearanceStateService.Side.SOUTH)
            )
        }
    }

    @Test
    fun `can enable panel`() {
        service.setPanelEnabled(false)
        assertFalse(service.panelIsEnabled())
        assertTrue(service.panelIsDisabled())

        service.setPanelEnabled(true)
        assertTrue(service.panelIsEnabled())
        assertFalse(service.panelIsDisabled())
    }

    @Test
    fun `can disable panel`() {
        service.setPanelEnabled(false)
        assertFalse(service.panelIsEnabled())
        assertTrue(service.panelIsDisabled())
    }

    @Test
    fun `panelIsEnabled opposite of panelIsDisabled`() {
        service.setPanelEnabled(true)
        assertTrue(service.panelIsEnabled())
        assertFalse(service.panelIsDisabled())

        service.setPanelEnabled(false)
        assertFalse(service.panelIsEnabled())
        assertTrue(service.panelIsDisabled())
    }

    @Test
    fun `can load panel direction`() {
        val newState = WindowPanelAppearanceStateService.State(
            side = WindowPanelAppearanceStateService.Side.NORTH,
            panelEnabled = false
        )
        service.loadState(newState)

        assertEquals(WindowPanelAppearanceStateService.Side.NORTH, service.getSide())
        assertFalse(service.panelIsEnabled())
    }

    @Test
    fun `can return panel direction`() {
        service.setSide(WindowPanelAppearanceStateService.Side.SOUTH)
        service.setPanelEnabled(false)

        val state = service.getState()
        assertEquals(WindowPanelAppearanceStateService.Side.SOUTH, state.side)
        assertFalse(state.panelEnabled)
    }

    @Test
    fun `can transition between panel directions`() {
        assertTrue(service.panelIsEnabled())
        assertEquals(WindowPanelAppearanceStateService.Side.EAST, service.getSide())

        service.setSide(WindowPanelAppearanceStateService.Side.WEST)
        assertEquals(WindowPanelAppearanceStateService.Side.WEST, service.getSide())

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
    fun `can set multiple directions`() {
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
    fun `panel is enabled when changing directions`() {
        service.setPanelEnabled(false)

        service.setSide(WindowPanelAppearanceStateService.Side.NORTH)
        assertFalse(service.panelIsEnabled())

        service.setSide(WindowPanelAppearanceStateService.Side.SOUTH)
        assertFalse(service.panelIsEnabled())
    }

    @Test
    fun `direction is preserved when changing panel enabled state`() {
        service.setSide(WindowPanelAppearanceStateService.Side.NORTH)

        service.setPanelEnabled(false)
        assertEquals(WindowPanelAppearanceStateService.Side.NORTH, service.getSide())

        service.setPanelEnabled(true)
        assertEquals(WindowPanelAppearanceStateService.Side.NORTH, service.getSide())
    }

}

