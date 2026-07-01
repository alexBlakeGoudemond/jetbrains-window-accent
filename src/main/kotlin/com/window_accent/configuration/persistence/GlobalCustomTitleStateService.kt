package com.window_accent.configuration.persistence

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * Application-level persisted state for the global custom window title label.
 *
 * This service stores a single label string (e.g. "prod") that is displayed in the
 * title of **all** open IDE windows when enabled. It complements the per-project
 * [WindowCustomTitleStateService], which applies a label to a single window only.
 *
 * The label is defined in Settings and toggled on/off from the Tool Window.
 */
@Service(Service.Level.APP)
@State(
    name = "GlobalCustomTitleStateService",
    storages = [Storage("globalCustomTitleStateService.xml")]
)
class GlobalCustomTitleStateService : PersistentStateComponent<GlobalCustomTitleStateService.State> {

    data class State(
        var globalCustomTitle: String = "",
        var globalCustomTitleEnabled: Boolean = false
    )

    private var state = State()

    /**
     * Returns a copy of the current state with the global custom title encoded for safe
     * XML attribute serialisation.
     *
     * Supplementary Unicode characters (emoji) are converted to ASCII-safe escape
     * sequences by [UnicodeXmlSanitizer.encode] before reaching JDOM, preventing
     * surrogate-pair corruption in the persisted XML file.
     */
    override fun getState(): State = state.copy(
        globalCustomTitle = UnicodeXmlSanitizer.encode(state.globalCustomTitle)
    )

    /**
     * Restores state from the persisted XML, decoding any emoji escape sequences
     * written by [getState] back into their original Unicode characters.
     */
    override fun loadState(state: State) {
        this.state = state.copy(
            globalCustomTitle = UnicodeXmlSanitizer.decode(state.globalCustomTitle)
        )
    }

    fun getGlobalCustomTitle(): String = state.globalCustomTitle

    fun setGlobalCustomTitle(title: String) {
        state.globalCustomTitle = title
    }

    fun isGlobalCustomTitleEnabled(): Boolean = state.globalCustomTitleEnabled

    fun isGlobalCustomTitleDisabled(): Boolean = !isGlobalCustomTitleEnabled()

    fun setGlobalCustomTitleEnabled(enabled: Boolean) {
        state.globalCustomTitleEnabled = enabled
    }
}
