package com.window_color_panel.configuration.persistence

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import java.awt.Color

/**
 * Project-level persisted settings for custom window color selection.
 *
 * Stores whether a custom color is in use and the selected color value.
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