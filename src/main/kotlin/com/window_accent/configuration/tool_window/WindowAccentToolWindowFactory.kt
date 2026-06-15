package com.window_accent.configuration.tool_window

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.content.ContentFactory
import com.window_accent.configuration.persistence.WindowCustomTitleStateService
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService
import com.window_accent.configuration.persistence.WindowPanelAppearanceStateService.Side
import com.window_accent.configuration.persistence.WindowTitleNumberingStateService
import com.window_accent.feature.window_color.WindowColorApplier
import com.window_accent.feature.window_title.WindowTitleApplier
import java.awt.Color
import java.awt.Font
import java.awt.GridLayout
import java.awt.event.ActionListener
import java.util.concurrent.ConcurrentHashMap
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel

/**
 * DumbAware is an IntelliJ Platform marker interface used in JetBrains IDE plugins.
 *
 * In IntelliJ terminology:
 *
 * - "Smart mode" = indexes are ready, PSI/model analysis works normally
 * - "Dumb mode" = the IDE is still indexing, so many code-analysis features are temporarily unavailable
 *
 * When your class implements DumbAware, you're telling the IDE:
 *
 * "This component can safely run even while the IDE is indexing."
 *
 * @see DumbAware
 * */
class WindowAccentToolWindowFactory : ToolWindowFactory, DumbAware {

    companion object {
        /**
         * Tracks all (button, listener) pairs added to tool window panels across all projects.
         *
         * During plugin unload we must remove every [ActionListener] from every [JButton]
         * before IntelliJ's classloader GC check runs.  Each listener lambda captures plugin
         * objects (service instances, [WindowColorApplier], [WindowTitleApplier]) via the
         * [refreshButtonText] closure.  As long as those lambdas are registered with a
         * Swing component held by IntelliJ's [com.intellij.ui.content.ContentManager], the
         * plugin classloader remains reachable and the GC check fails.
         *
         * [removeAllButtonListeners] is called from [com.window_accent.WindowAccentApplicationService.performCleanup]
         * which runs inside [com.window_accent.PluginLifecycleListener.beforePluginUnload],
         * guaranteeing cleanup before the GC check.
         */
        private val allButtonListeners =
            ConcurrentHashMap<Project, List<Pair<JButton, ActionListener>>>()

        fun removeAllButtonListeners() {
            val snapshot = HashMap(allButtonListeners)
            allButtonListeners.clear()
            snapshot.values.flatten().forEach { (button, listener) ->
                button.removeActionListener(listener)
            }
        }
    }

    /** Cycle order for the panel direction button: N → S → W → E → N */
    private val sidesCycleOrder = listOf(Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST)

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val colorSettings = project.getService(WindowPanelAppearanceStateService::class.java)
        val titleSettings = project.getService(WindowTitleNumberingStateService::class.java)
        val customTitleSettings = project.getService(WindowCustomTitleStateService::class.java)

        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.border = BorderFactory.createEmptyBorder(12, 12, 12, 12)

        val toggleAllColorsButton = JButton()
        val toggleCurrentColorButton = JButton()
        val cyclePanelDirectionButton = JButton()

        val toggleAllTitlesButton = JButton()
        val toggleCurrentTitleButton = JButton()
        val resetTitleNumberingButton = JButton()

        val toggleCurrentCustomTitleButton = JButton()

        styleAsAllButton(toggleAllColorsButton)
        styleAsAllButton(toggleAllTitlesButton)
        styleAsResetButton(resetTitleNumberingButton)
        styleAsCurrentButton(toggleCurrentColorButton)
        styleAsCurrentButton(toggleCurrentTitleButton)
        styleAsCurrentButton(toggleCurrentCustomTitleButton)
        styleAsCycleButton(cyclePanelDirectionButton)

        fun refreshButtonText() {
            val colorsEnabled = colorSettings.panelIsEnabled()
            toggleAllColorsButton.text = wrapTextInHtmlCenter(
                if (colorsEnabled) "Disable colors for <b>all</b> open windows"
                else "Enable colors for <b>all</b> open windows"
            )
            toggleCurrentColorButton.text = wrapTextInHtmlCenter(
                if (colorsEnabled) "Disable color for current window"
                else "Enable color for current window"
            )

            cyclePanelDirectionButton.text = wrapTextInHtmlCenter("Panel direction: ${colorSettings.getSide()}")
            cyclePanelDirectionButton.toolTipText = "Click Me! Cycle the color panel location"

            val titlesEnabled = titleSettings.isTitleNumberingEnabled()
            toggleAllTitlesButton.text = wrapTextInHtmlCenter(
                if (titlesEnabled) "Disable title numbers for <b>all</b> open windows"
                else "Enable title numbers for <b>all</b> open windows"
            )
            toggleCurrentTitleButton.text = wrapTextInHtmlCenter(
                if (titlesEnabled) "Disable title number for current window"
                else "Enable title number for current window"
            )
            resetTitleNumberingButton.text = wrapTextInHtmlCenter("Reset title numbers: current window → 1")
            resetTitleNumberingButton.toolTipText = "Renumber all open windows starting from 1, with this window first"

            toggleCurrentCustomTitleButton.text = wrapTextInHtmlCenter(
                if (customTitleSettings.isCustomTitleEnabled()) "Disable custom title for current window"
                else "Enable custom title for current window"
            )
        }

        // Assign listeners to named variables so they can be tracked and removed on plugin unload.
        // Each listener lambda captures plugin objects (service instances, singletons) via the
        // refreshButtonText closure. If these listeners remain registered on Swing buttons held
        // by IntelliJ's ContentManager after the plugin is unloaded, the plugin classloader stays
        // reachable and IntelliJ's GC check fails, forcing an unnecessary restart.
        val toggleAllColorsListener = ActionListener {
            val enabled = colorSettings.panelIsDisabled()
            colorSettings.setPanelEnabled(enabled)
            WindowColorApplier.applyToAllOpenProjects(enabled)
            refreshButtonText()
        }
        val toggleCurrentColorListener = ActionListener {
            val enabled = colorSettings.panelIsDisabled()
            colorSettings.setPanelEnabled(enabled)
            WindowColorApplier.applyToCurrentOpenProject(project)
            refreshButtonText()
        }
        val cyclePanelDirectionListener = ActionListener {
            val currentIndex = sidesCycleOrder.indexOf(colorSettings.getSide())
            val nextSide = sidesCycleOrder[(currentIndex + 1) % sidesCycleOrder.size]
            colorSettings.setSide(nextSide)
            WindowColorApplier.applyToCurrentOpenProject(project)
            refreshButtonText()
        }
        val toggleAllTitlesListener = ActionListener {
            val enabled = titleSettings.isTitleNumberingDisabled()
            titleSettings.setTitleNumberingEnabled(enabled)
            WindowTitleApplier.applyToAllOpenProjects(enabled)
            refreshButtonText()
        }
        val toggleCurrentTitleListener = ActionListener {
            val enabled = titleSettings.isTitleNumberingDisabled()
            titleSettings.setTitleNumberingEnabled(enabled)
            WindowTitleApplier.applyToCurrentOpenProject(project, enabled)
            refreshButtonText()
        }
        val resetTitleNumberingListener = ActionListener {
            WindowTitleApplier.renumberAllOpenWindows(project)
        }
        val toggleCurrentCustomTitleListener = ActionListener {
            val enabled = customTitleSettings.isCustomTitleDisabled()
            customTitleSettings.setCustomTitleEnabled(enabled)
            WindowTitleApplier.applyToCurrentOpenProject(project)
            refreshButtonText()
        }

        toggleAllColorsButton.addActionListener(toggleAllColorsListener)
        toggleCurrentColorButton.addActionListener(toggleCurrentColorListener)
        cyclePanelDirectionButton.addActionListener(cyclePanelDirectionListener)
        toggleAllTitlesButton.addActionListener(toggleAllTitlesListener)
        toggleCurrentTitleButton.addActionListener(toggleCurrentTitleListener)
        resetTitleNumberingButton.addActionListener(resetTitleNumberingListener)
        toggleCurrentCustomTitleButton.addActionListener(toggleCurrentCustomTitleListener)

        refreshButtonText()

        panel.add(buildButtonRow(toggleAllColorsButton, toggleCurrentColorButton, cyclePanelDirectionButton))
        panel.add(Box.createVerticalStrut(8))
        panel.add(buildButtonRow(toggleAllTitlesButton, toggleCurrentTitleButton, resetTitleNumberingButton))
        panel.add(Box.createVerticalStrut(8))
        panel.add(buildButtonRow(toggleCurrentCustomTitleButton))

        val content = ContentFactory.getInstance().createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)

        val listenerPairs = listOf(
            toggleAllColorsButton to toggleAllColorsListener,
            toggleCurrentColorButton to toggleCurrentColorListener,
            cyclePanelDirectionButton to cyclePanelDirectionListener,
            toggleAllTitlesButton to toggleAllTitlesListener,
            toggleCurrentTitleButton to toggleCurrentTitleListener,
            resetTitleNumberingButton to resetTitleNumberingListener,
            toggleCurrentCustomTitleButton to toggleCurrentCustomTitleListener,
        )
        allButtonListeners[project] = listenerPairs

        // Also clean up when the project closes normally (belt-and-suspenders alongside the
        // explicit removeAllButtonListeners() call in performCleanup for the update/unload path).
        Disposer.register(toolWindow.disposable) {
            allButtonListeners.remove(project)?.forEach { (button, listener) ->
                button.removeActionListener(listener)
            }
        }
    }

    /**
     * Wraps [text] in an HTML centre block so that Swing renders it with
     * automatic word-wrapping when the button is narrower than the full label.
     */
    private fun wrapTextInHtmlCenter(text: String) = "<html><center>$text</center></html>"

    /**
     * Styles a button that operates on **all** open windows.
     *
     * Applies a steel-blue border (theme-aware via [JBColor]) and bold font
     * to visually signal wide-impact actions.
     */
    private fun styleAsAllButton(button: JButton) {
        val borderColor = JBColor(Color(0x4682B4), Color(0x58A6FF))
        button.border = BorderFactory.createLineBorder(borderColor, 2, true)
        button.font = button.font.deriveFont(Font.BOLD)
    }

    /**
     * Styles a button that operates on the **current** window only.
     *
     * Applies a muted-green border (theme-aware via [JBColor]) to signal
     * a scoped, lower-impact action.
     */
    private fun styleAsCurrentButton(button: JButton) {
        val borderColor = JBColor(Color(0x5A8A5A), Color(0x3FB950))
        button.border = BorderFactory.createLineBorder(borderColor, 1, true)
    }

    /**
     * Styles the cycle-direction button.
     *
     * Applies a warm-amber border (theme-aware via [JBColor]) to distinguish
     * it as a mode/configuration control rather than an enable/disable toggle.
     */
    private fun styleAsCycleButton(button: JButton) {
        val borderColor = JBColor(Color(0xB8860B), Color(0xD29922))
        button.border = BorderFactory.createLineBorder(borderColor, 1, true)
    }

    /**
     * Styles the reset button.
     *
     * Applies a warm-amber border (theme-aware via [JBColor]) to distinguish
     * it as a mode/configuration control rather than an enable/disable toggle.
     */
    private fun styleAsResetButton(button: JButton) {
        val borderColor = JBColor(Color(0xdc4fe3), Color(0xdc4fe3))
        button.border = BorderFactory.createLineBorder(borderColor, 1, true)
    }

    /**
     * Creates a horizontal row of buttons that each fill an equal share of the available width.
     */
    private fun buildButtonRow(vararg buttons: JButton): JPanel {
        val row = JPanel(GridLayout(1, buttons.size, 8, 0))
        buttons.forEach { row.add(it) }
        return row
    }
}