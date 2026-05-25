package com.window_accent.configuration.persistence

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * Project-level persisted state for the window panel appearance.
 *
 * This service stores the panel's enabled/disabled state and its preferred
 * placement around the IDE window. It is consumed by the color applier and the
 * settings UI.
 */
@Service(Service.Level.PROJECT)
@State(
    name = "WindowPanelAppearanceStateService",
    storages = [Storage("windowPanelAppearanceStateService.xml")]
)
open class WindowPanelAppearanceStateService : PersistentStateComponent<WindowPanelAppearanceStateService.State> {

    data class State(
        var side: Side = Side.EAST,
        var panelEnabled: Boolean = true
    )

    enum class Side {
        EAST,
        WEST,
        NORTH,
        SOUTH
    }

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    fun getSide(): Side = state.side

    fun setSide(side: Side) {
        state.side = side
    }

    fun panelIsEnabled(): Boolean = state.panelEnabled

    fun panelIsDisabled(): Boolean = !panelIsEnabled()

    fun setPanelEnabled(enabled: Boolean) {
        state.panelEnabled = enabled
    }
}