package com.window_color_panel.configuration.persistence

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * Project-level persisted settings for the window color panel feature.
 *
 * Stores the side placement and whether the colored panel is enabled.
 */
@Service(Service.Level.PROJECT)
@State(
    name = "WindowColorSettings",
    storages = [Storage("windowColorSettings.xml")]
)
class WindowPanelAppearanceStateService : PersistentStateComponent<WindowPanelAppearanceStateService.State> {

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