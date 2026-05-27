package com.window_accent.configuration.persistence

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import java.awt.Color

/**
 * Project-level persisted state for the user's custom window color selection.
 *
 * This service stores whether custom coloring is enabled and, when it is, the
 * selected RGB color value. It is read by the settings UI and used by runtime
 * window-color application logic.
 */
@Service(Service.Level.PROJECT)
@State(
    name = "WindowCustomColorStateService",
    storages = [Storage("windowCustomColorStateService.xml")]
)
class WindowCustomColorStateService : PersistentStateComponent<WindowCustomColorStateService.State> {

    data class State(
        var useCustomColor: Boolean = false,
        var customColorRgb: Int? = null
    )

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    fun isUseCustomColor(): Boolean = state.useCustomColor

    fun setUseCustomColor(useCustomColor: Boolean) {
        state.useCustomColor = useCustomColor
    }

    fun setCustomColor(color: Color?) {
        state.customColorRgb = color?.rgb
    }

    fun getCustomColor(): Color? = state.customColorRgb?.let { Color(it, true) }
}