package com.demo.window_title

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.EDT
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.WindowManager
import com.intellij.util.Alarm
import com.intellij.util.ui.accessibility.ScreenReader.isActive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.Frame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration.Companion.milliseconds

object WindowTitleApplier {

    private val counter = AtomicInteger(1)
    private val projectNumbers = ConcurrentHashMap<Project, Int>()
    private val alarms = ConcurrentHashMap<Project, Alarm>()

    private val scopes = mutableMapOf<Project, CoroutineScope>()
    private val focusListeners = ConcurrentHashMap<Project, WindowAdapter>()

    fun applyToCurrentOpenProject(project: Project, enabled: Boolean = true) {
        ApplicationManager.getApplication().invokeLater {
            if (enabled) {
                applyTitleToWindow(project)
            } else {
                removeTitleFromWindow(project)
            }
        }
    }

    fun applyToAllOpenProjects(enabled: Boolean = true) {
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

            projectNumbers.clear()
            counter.set(1)
        }
    }

    private fun removeTitleFromWindow(project: Project) {
        ApplicationManager.getApplication().invokeLater {
            val frame = WindowManager.getInstance().getFrame(project) ?: return@invokeLater

            alarms.remove(project)?.cancelAllRequests()
            scopes.remove(project)?.cancel()

            focusListeners.remove(project)?.let { listener ->
                frame.removeWindowFocusListener(listener)
            }

            val currentTitle = frame.title ?: return@invokeLater
            val cleanedTitle = currentTitle.replace(Regex("^\\[\\d+]\\s*"), "")

            if (currentTitle != cleanedTitle) {
                frame.title = cleanedTitle
            }
        }
    }

    private fun reapplyOnFocus(project: Project) {
        val frame = WindowManager.getInstance().getFrame(project) ?: return
        val listener = object : WindowAdapter() {
            override fun windowGainedFocus(e: WindowEvent?) {
                val number = projectNumbers[project] ?: return
                updateWindowTitle(frame, number)
            }
        }

        focusListeners.remove(project)?.let { oldListener ->
            frame.removeWindowFocusListener(oldListener)
        }
        focusListeners[project] = listener
        frame.addWindowFocusListener(listener)
    }

    private fun getWindowProjectNumber(project: Project): Int = projectNumbers.computeIfAbsent(project) {
        counter.getAndIncrement()
    }

    private fun applyTitleToWindow(project: Project) {
        val frame = WindowManager.getInstance().getFrame(project) ?: return

        val number = getWindowProjectNumber(project)
        updateWindowTitle(frame, number)
        reapplyOnFocus(project)
        startTitleEnforcer(project)
    }

    private fun updateWindowTitle(frame: Frame, number: Int) {
        val originalTitle = frame.title ?: return

        val cleanedTitle = originalTitle.replace(Regex("^\\[\\d+]\\s*"), "")
        val newTitle = "[$number] $cleanedTitle"
        if (frame.title != newTitle) {
            frame.title = newTitle
        }
    }

    private fun startTitleEnforcer(project: Project) {
        scopes.remove(project)?.cancel()
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        scopes[project] = scope

        Disposer.register(project) {
            scopes.remove(project)?.cancel()
            focusListeners.remove(project)?.let { listener ->
                WindowManager.getInstance().getFrame(project)?.removeWindowFocusListener(listener)
            }
        }
        scope.launch {
            while (isActive) {
                val frame = WindowManager.getInstance().getFrame(project)
                val number = projectNumbers[project]
                if (frame != null && number != null) {
                    withContext(Dispatchers.EDT) {
                        updateWindowTitle(frame, number)
                    }
                }
                delay(1500.milliseconds)
            }
        }
    }

}