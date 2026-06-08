package com.window_accent.configuration.persistence

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * Project-level persisted state for the user's custom window title label.
 *
 * This service stores the custom label string (e.g. "dattebayo") and whether
 * it is currently enabled for this project window. It is consumed by the title
 * applier and the settings / tool-window UI.
 *
 * The label is defined once in Settings and toggled on/off from the Tool Window.
 */
@Service(Service.Level.PROJECT)
@State(
    name = "WindowCustomTitleStateService",
    storages = [Storage("windowCustomTitleStateService.xml")]
)
class WindowCustomTitleStateService : PersistentStateComponent<WindowCustomTitleStateService.State> {

    data class State(
        var customTitle: String = "",
        var customTitleEnabled: Boolean = false
    )

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    fun getCustomTitle(): String = state.customTitle

    fun setCustomTitle(title: String) {
        state.customTitle = title
    }

    fun isCustomTitleEnabled(): Boolean = state.customTitleEnabled

    fun isCustomTitleDisabled(): Boolean = !isCustomTitleEnabled()

    fun setCustomTitleEnabled(enabled: Boolean) {
        state.customTitleEnabled = enabled
    }
}

