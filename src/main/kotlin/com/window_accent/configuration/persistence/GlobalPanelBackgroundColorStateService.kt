package com.window_accent.configuration.persistence

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import java.awt.Color

/**
 * Application-level persisted state for the user's custom panel background / gradient-fade color.
 *
 * This color is used as the "fade-to" end of the accent gradient in every IDE window.
 * It is stored globally (application scope) so that changing it updates every window
 * regardless of which project theme they are using.
 *
 * When [useCustomBackgroundColor] is false the default hard-coded IDE dark background
 * (`#26282C`) is used as the fade target.
 */
@Service(Service.Level.APP)
@State(
    name = "GlobalPanelBackgroundColorStateService",
    storages = [Storage("globalPanelBackgroundColorStateService.xml")]
)
class GlobalPanelBackgroundColorStateService :
    PersistentStateComponent<GlobalPanelBackgroundColorStateService.State> {

    data class State(
        var useCustomBackgroundColor: Boolean = false,
        var customBackgroundColorRgb: Int? = null
    )

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    fun isUseCustomBackgroundColor(): Boolean = state.useCustomBackgroundColor

    fun setUseCustomBackgroundColor(use: Boolean) {
        state.useCustomBackgroundColor = use
    }

    fun setCustomBackgroundColor(color: Color?) {
        state.customBackgroundColorRgb = color?.rgb
    }

    fun getCustomBackgroundColor(): Color? = state.customBackgroundColorRgb?.let { Color(it, true) }

    /** Returns the effective background color: custom if enabled, otherwise the default IDE dark background. */
    fun resolveBackgroundColor(): Color =
        if (isUseCustomBackgroundColor()) getCustomBackgroundColor() ?: DEFAULT_BACKGROUND
        else DEFAULT_BACKGROUND

    companion object {
        /** Default IntelliJ dark theme background — used when no custom color is set. */
        val DEFAULT_BACKGROUND: Color = Color(0x26, 0x28, 0x2C)
    }
}
