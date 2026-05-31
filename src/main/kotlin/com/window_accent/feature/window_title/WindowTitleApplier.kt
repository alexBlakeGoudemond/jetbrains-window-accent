package com.window_accent.feature.window_title

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.WindowManager
import com.intellij.util.concurrency.AppExecutorUtil
import com.window_accent.PluginLifecycleListener
import com.window_accent.configuration.persistence.WindowTitleNumberingStateService
import com.window_accent.feature.window_color.WindowColorApplier
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
class WindowTitleApplier {

    companion object {
        private val INSTANCE = WindowTitleApplier()
        fun getInstance(): WindowTitleApplier = INSTANCE
        
        fun applyToCurrentOpenProject(project: Project, enabled: Boolean? = null) = getInstance().applyToCurrentOpenProject(project, enabled)
        fun applyToAllOpenProjects(enabled: Boolean? = null) = getInstance().applyToAllOpenProjects(enabled)
        fun removeFromAllOpenProjects() = getInstance().removeFromAllOpenProjects()
        fun removeFromAllOpenProjectsSync() = getInstance().removeFromAllOpenProjectsSync()
        fun cancelAllPendingOperations() = getInstance().cancelAllPendingOperations()
        fun resetProjectNumbering() = getInstance().resetProjectNumbering()
    }

    private val LOG = logger<PluginLifecycleListener>()

    private val counter = AtomicInteger(1)
    private val projectNumbers = ConcurrentHashMap<Project, Int>()
    private val focusListeners = ConcurrentHashMap<Project, WindowAdapter>()
    private val titleListeners = ConcurrentHashMap<Project, java.beans.PropertyChangeListener>()
    private val pendingTasks = ConcurrentHashMap<Project, java.util.concurrent.ScheduledFuture<*>>()

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
            removeFromAllOpenProjectsInternal()
        }
    }

    fun removeFromAllOpenProjectsSync() {
        LOG.info("[Window Accent] removeFromAllOpenProjectsSync triggered")
        ProjectManager.getInstance().openProjects.forEach { project ->
            removeTitleFromWindowSync(project)
        }
        resetProjectNumbering()
    }

    fun cancelAllPendingOperations() {
        pendingTasks.values.forEach { it.cancel(false) }
        pendingTasks.clear()
    }

    private fun removeFromAllOpenProjectsInternal() {
        ProjectManager.getInstance().openProjects.forEach { project ->
            removeTitleFromWindow(project)
        }
        resetProjectNumbering()
    }

    private fun applyTitleToWindow(project: Project) {
        val number = getWindowProjectNumber(project)

        fun tryApply(retries: Int) {
            val frame = getProjectFrame(project)
            if (frame != null) {
                ApplicationManager.getApplication().invokeLater {
                    updateWindowTitle(frame, number)
                    reapplyOnFocus(project, frame)
                    reapplyOnTitleChange(project, frame)

                    Disposer.register(project) {
                        cleanupListeners(project)
                    }
                    pendingTasks.remove(project)
                }
            } else if (retries > 0) {
                val future = AppExecutorUtil.getAppScheduledExecutorService().schedule({
                    tryApply(retries - 1)
                }, 500, TimeUnit.MILLISECONDS)
                pendingTasks[project] = future
            } else {
                pendingTasks.remove(project)
            }
        }

        tryApply(60) // Retry for 30 seconds
    }

    private fun removeTitleFromWindowSync(project: Project) {
        val frame = getProjectFrame(project) ?: return
        removeListeners(project, frame)
        stripTitlePrefix(frame)
    }

    private fun removeTitleFromWindow(project: Project) {
        ApplicationManager.getApplication().invokeLater {
            val frame = getProjectFrame(project) ?: return@invokeLater
            removeListeners(project, frame)
            stripTitlePrefix(frame)
        }
    }

    private fun getProjectFrame(project: Project): Frame? =
        WindowManager.getInstance().getFrame(project)

    internal fun getWindowProjectNumber(project: Project): Int =
        projectNumbers.computeIfAbsent(project) { counter.getAndIncrement() }

    internal fun updateWindowTitle(frame: Frame, number: Int) {
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

    internal fun stripExistingPrefix(title: String): String =
        title.replace(Regex("^(\\[\\d+]\\s*)+"), "")

    private fun reapplyOnFocus(project: Project, frame: Frame) {
        val listener = createFocusListener(project, frame)
        replaceFocusListener(project, frame, listener)
    }

    private fun reapplyOnTitleChange(project: Project, frame: Frame) {
        val listener = createTitleListener(project, frame)
        replaceTitleListener(project, frame, listener)
    }

    private fun createFocusListener(project: Project, frame: Frame): WindowAdapter =
        object : WindowAdapter() {
            override fun windowGainedFocus(e: WindowEvent?) {
                val number = projectNumbers[project] ?: return
                updateWindowTitle(frame, number)
            }
        }

    private fun createTitleListener(project: Project, frame: Frame): java.beans.PropertyChangeListener =
        java.beans.PropertyChangeListener { event ->
            if ("title" == event.propertyName) {
                val newTitle = event.newValue as? String ?: return@PropertyChangeListener
                val number = projectNumbers[project] ?: return@PropertyChangeListener
                val expectedPrefix = "[$number] "
                if (!newTitle.startsWith(expectedPrefix)) {
                    updateWindowTitle(frame, number)
                }
            }
        }

    private fun replaceFocusListener(project: Project, frame: Frame, listener: WindowAdapter) {
        focusListeners.remove(project)?.let { oldListener ->
            frame.removeWindowFocusListener(oldListener)
        }
        focusListeners[project] = listener
        frame.addWindowFocusListener(listener)
    }

    private fun replaceTitleListener(project: Project, frame: Frame, listener: java.beans.PropertyChangeListener) {
        titleListeners.remove(project)?.let { oldListener ->
            frame.removePropertyChangeListener("title", oldListener)
        }
        titleListeners[project] = listener
        frame.addPropertyChangeListener("title", listener)
    }

    private fun removeListeners(project: Project, frame: Frame) {
        focusListeners.remove(project)?.let { listener ->
            frame.removeWindowFocusListener(listener)
        }
        titleListeners.remove(project)?.let { listener ->
            frame.removePropertyChangeListener("title", listener)
        }
    }

    private fun cleanupListeners(project: Project) {
        val frame = getProjectFrame(project) ?: return
        removeListeners(project, frame)
    }

    fun resetProjectNumbering() {
        projectNumbers.clear()
        counter.set(1)
    }
}