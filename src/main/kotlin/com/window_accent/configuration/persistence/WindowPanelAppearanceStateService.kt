package com.window_accent.configuration.persistence

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * Project-level persisted state for the window panel appearance.
 *
 * This service stores the panel's enabled/disabled state and its preferred
 * placement around the IDE window. It is consumed by the color applier and the
 * settings UI.
 */
@Service(Service.Level.PROJECT)
@State(
    name = "WindowPanelAppearanceStateService",
    storages = [Storage("windowPanelAppearanceStateService.xml")]
)
class WindowPanelAppearanceStateService : PersistentStateComponent<WindowPanelAppearanceStateService.State> {

    data class State(
        var side: Side = Side.EAST,
        var panelEnabled: Boolean = true,
        var panelOpaque: Boolean = true,
        var panelPadding: Int = 4,
        var gradientAnchor: GradientAnchor = GradientAnchor.START
    )

    enum class Side {
        EAST,
        WEST,
        NORTH,
        SOUTH
    }

    /**
     * Where the solid (fully-opaque) edge of the color gradient sits along the panel's axis.
     *
     * For NORTH/SOUTH panels the axis is horizontal (left→right); for WEST/EAST panels the
     * axis is vertical (top→bottom). [START] and [END] refer to the geometric start/end of
     * that axis, not a fixed screen direction — the tool window UI maps them to axis-appropriate
     * labels (e.g. "LHS"/"RHS" or "UP"/"DOWN").
     *
     * - [START]: solid color at the axis start (left/top), fading out towards the axis end.
     *   This matches the plugin's original, pre-toggle gradient behavior and is the default.
     * - [END]: solid color at the axis end (right/bottom), fading out towards the axis start.
     * - [MIDDLE]: solid color at the center, fading out towards both edges.
     * - [OFF]: no gradient fade — the panel is a single flat color.
     */
    enum class GradientAnchor {
        START,
        END,
        MIDDLE,
        OFF
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

    fun isPanelOpaque(): Boolean = state.panelOpaque

    fun setPanelOpaque(opaque: Boolean) {
        state.panelOpaque = opaque
    }

    fun getPanelPadding(): Int = state.panelPadding

    fun setPanelPadding(padding: Int) {
        state.panelPadding = padding
    }

    fun getGradientAnchor(): GradientAnchor = state.gradientAnchor

    fun setGradientAnchor(anchor: GradientAnchor) {
        state.gradientAnchor = anchor
    }
}