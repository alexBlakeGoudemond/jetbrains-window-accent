package com.window_accent.configuration.persistence

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * Application-level persisted state for the "last focussed window" title label.
 *
 * This service stores a single label string (e.g. "NEW") that [WindowTitleApplier]
 * shows, as the very first prefix segment, on whichever IDE window instance was
 * most recently focused. Unlike [GlobalCustomTitleStateService] (shown on *every*
 * window) or [WindowCustomTitleStateService] (a fixed, per-project label), this
 * label automatically follows the most-recently-focused project: focusing a new
 * window moves it there and removes it from whichever window held it before.
 *
 * There is no separate enabled/disabled flag by design — the feature is "on" for
 * whichever window currently holds the label exactly when the text is non-blank,
 * matching the plain-text-input UX requested for this field. This mirrors the
 * requested `focussedWindowTitle` field defined in Tool Window Settings.
 */
@Service(Service.Level.APP)
@State(
    name = "LastOpenedWindowTitleStateService",
    storages = [Storage("lastOpenedWindowTitleStateService.xml")]
)
class LastOpenedWindowTitleStateService : PersistentStateComponent<LastOpenedWindowTitleStateService.State> {

    data class State(
        var focussedWindowTitle: String = ""
    )

    private var state = State()

    /**
     * Returns a copy of the current state with the label encoded for safe XML
     * attribute serialisation (see [UnicodeXmlSanitizer]).
     */
    override fun getState(): State = state.copy(
        focussedWindowTitle = UnicodeXmlSanitizer.encode(state.focussedWindowTitle)
    )

    /**
     * Restores state from the persisted XML, decoding any emoji escape sequences
     * written by [getState] back into their original Unicode characters.
     */
    override fun loadState(state: State) {
        this.state = state.copy(
            focussedWindowTitle = UnicodeXmlSanitizer.decode(state.focussedWindowTitle)
        )
    }

    fun getFocussedWindowTitle(): String = state.focussedWindowTitle

    fun setFocussedWindowTitle(title: String) {
        state.focussedWindowTitle = title
    }

    fun isFocussedWindowTitleEnabled(): Boolean = state.focussedWindowTitle.isNotBlank()
}