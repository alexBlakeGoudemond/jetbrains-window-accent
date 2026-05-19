package com.window_color_panel.feature.window_title

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.WindowManager
import com.intellij.util.concurrency.AppExecutorUtil
import com.window_color_panel.configuration.persistence.WindowTitleNumberingStateService
import java.awt.Frame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * Applies and maintains a numeric prefix in IDE window titles.
 *
 * The prefix is assigned per open project and is restored when focus changes
 * or when the IDE rewrites the title. This class manages the lifecycle of the
 * title decoration for the current project session.
 */
object WindowTitleApplier {

    private val counter = AtomicInteger(1)
    private val projectNumbers = ConcurrentHashMap<Project, Int>()
    private val focusListeners = ConcurrentHashMap<Project, WindowAdapter>()

    fun applyToCurrentOpenProject(project: Project, enabled: Boolean? = null) {
        val actualEnabled = enabled ?: project.getService(WindowTitleNumberingStateService::class.java).isTitleNumberingEnabled()
        ApplicationManager.getApplication().invokeLater {
            if (actualEnabled) {
                applyTitleToWindow(project)
            } else {
                removeTitleFromWindow(project)
            }
        }
    }

    fun applyToAllOpenProjects(enabled: Boolean? = null) {
        ApplicationManager.getApplication().invokeLater {
            ProjectManager.getInstance().openProjects.forEach { project ->
                applyToCurrentOpenProject(project, enabled)
            }
        }
    }

    fun removeFromAllOpenProjects() {
        ApplicationManager.getApplication().invokeLater {
            ProjectManager.getInstance().openProjects.forEach { project ->
                removeTitleFromWindow(project)
            }
            resetProjectNumbering()
        }
    }

    private fun applyTitleToWindow(project: Project) {
        val number = getWindowProjectNumber(project)

        fun tryApply(retries: Int) {
            val frame = getProjectFrame(project)
            if (frame != null) {
                ApplicationManager.getApplication().invokeLater {
                    updateWindowTitle(frame, number)
                    reapplyOnFocus(project, frame)

                    Disposer.register(project) {
                        cleanupFocusListener(project)
                    }
                }
            } else if (retries > 0) {
                AppExecutorUtil.getAppScheduledExecutorService().schedule({
                    tryApply(retries - 1)
                }, 500, TimeUnit.MILLISECONDS)
            }
        }

        tryApply(60) // Retry for 30 seconds
    }

    private fun removeTitleFromWindow(project: Project) {
        ApplicationManager.getApplication().invokeLater {
            val frame = getProjectFrame(project) ?: return@invokeLater
            removeFocusListener(project, frame)
            stripTitlePrefix(frame)
        }
    }

    private fun getProjectFrame(project: Project): Frame? =
        WindowManager.getInstance().getFrame(project)

    private fun getWindowProjectNumber(project: Project): Int =
        projectNumbers.computeIfAbsent(project) { counter.getAndIncrement() }

    private fun updateWindowTitle(frame: Frame, number: Int) {
        val currentTitle = frame.title ?: return
        val cleanedTitle = stripExistingPrefix(currentTitle)
        val updatedTitle = "[$number] $cleanedTitle"

        if (frame.title != updatedTitle) {
            frame.title = updatedTitle
        }
    }

    private fun stripTitlePrefix(frame: Frame) {
        val currentTitle = frame.title ?: return
        val cleanedTitle = stripExistingPrefix(currentTitle)

        if (frame.title != cleanedTitle) {
            frame.title = cleanedTitle
        }
    }

    private fun stripExistingPrefix(title: String): String =
        title.replace(Regex("^(\\[\\d+]\\s*)+"), "")

    private fun reapplyOnFocus(project: Project, frame: Frame) {
        val listener = createFocusListener(project, frame)
        replaceFocusListener(project, frame, listener)
    }

    private fun createFocusListener(project: Project, frame: Frame): WindowAdapter =
        object : WindowAdapter() {
            override fun windowGainedFocus(e: WindowEvent?) {
                val number = projectNumbers[project] ?: return
                updateWindowTitle(frame, number)
            }
        }

    private fun replaceFocusListener(project: Project, frame: Frame, listener: WindowAdapter) {
        focusListeners.remove(project)?.let { oldListener ->
            frame.removeWindowFocusListener(oldListener)
        }
        focusListeners[project] = listener
        frame.addWindowFocusListener(listener)
    }

    private fun removeFocusListener(project: Project, frame: Frame) {
        focusListeners.remove(project)?.let { listener ->
            frame.removeWindowFocusListener(listener)
        }
    }

    private fun cleanupFocusListener(project: Project) {
        val frame = getProjectFrame(project) ?: return
        removeFocusListener(project, frame)
    }

    fun resetProjectNumbering() {
        projectNumbers.clear()
        counter.set(1)
    }
}