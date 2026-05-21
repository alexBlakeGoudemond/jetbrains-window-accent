package com.window_accent.configuration.persistence

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * Project-level persisted state for window title numbering.
 *
 * This service stores whether title numbering is enabled for the project.
 * The title applier uses this state to decide whether to prefix IDE window
 * titles, and the settings UI uses it to reflect and update the current value.
 */
@Service(Service.Level.PROJECT)
@State(
    name = "WindowTitleNumberingStateService",
    storages = [Storage("windowTitleNumberingStateService.xml")]
)
class WindowTitleNumberingStateService : PersistentStateComponent<WindowTitleNumberingStateService.State> {

    data class State(
        var titleNumberingEnabled: Boolean = true
    )

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    fun isTitleNumberingEnabled(): Boolean = state.titleNumberingEnabled

    fun isTitleNumberingDisabled(): Boolean = !isTitleNumberingEnabled()

    fun setTitleNumberingEnabled(enabled: Boolean) {
        state.titleNumberingEnabled = enabled
    }
}