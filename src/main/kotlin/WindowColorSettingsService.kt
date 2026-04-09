package com.demo

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import java.awt.Color

@Service(Service.Level.PROJECT)
@State(
    name = "WindowColorSettings",
    storages = [Storage("windowColorSettings.xml")]
)
class WindowColorSettings : PersistentStateComponent<WindowColorSettings.State> {

    data class State(
        var side: Side = Side.EAST,
        var useCustomColor: Boolean = false,
        var customColorRgb: Int? = null
    )

    enum class Side(val borderLayoutConstraint: String) {
        EAST("East"),
        WEST("West"),
        NORTH("North"),
        SOUTH("South")
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

    fun isUseCustomColor(): Boolean = state.useCustomColor

    fun setUseCustomColor(useCustomColor: Boolean) {
        state.useCustomColor = useCustomColor
    }

    fun setCustomColor(color: Color?) {
        state.customColorRgb = color?.rgb
    }

    fun getCustomColor(): Color? = state.customColorRgb?.let { Color(it, true) }
}
