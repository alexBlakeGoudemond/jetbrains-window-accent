package com.window_color_panel.window_title

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * Project-level persisted settings for title numbering behavior.
 */
@Service(Service.Level.PROJECT)
@State(
    name = "WindowTitleNumberingSettings",
    storages = [Storage("windowTitleNumberingSettings.xml")]
)
class WindowTitleNumberingService : PersistentStateComponent<WindowTitleNumberingService.State> {

    data class State(
        var titleNumberingEnabled: Boolean = false
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