package com.demo.window_title

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import com.intellij.util.Alarm
import java.awt.Frame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

object WindowTitleApplier {

    private val counter = AtomicInteger(1)
    private val projectNumbers = ConcurrentHashMap<Project, Int>()
    private val alarms = ConcurrentHashMap<Project, Alarm>()

    fun apply(project: Project) {
        val app = ApplicationManager.getApplication()

        app.invokeLater {
            val frame = WindowManager.getInstance().getFrame(project) ?: return@invokeLater

            val number = projectNumbers.computeIfAbsent(project) {
                counter.getAndIncrement()
            }

            // Initial apply
            updateTitle(frame, number)

            // 👇 Reapply on focus
            reapplyOnFocus(project)

            // 👇 Add persistent alarm (polling)
            startTitleEnforcer(project)
        }
    }

    fun applyToAllOpenProjects() {
        ApplicationManager.getApplication().invokeLater {
            ProjectManager.getInstance().openProjects.forEach { project ->
                apply(project)
            }
        }
    }

    fun remove(project: Project) {
        ApplicationManager.getApplication().invokeLater {
            val frame = WindowManager.getInstance().getFrame(project) ?: return@invokeLater

            alarms.remove(project)?.cancelAllRequests()
            projectNumbers.remove(project)

            val currentTitle = frame.title ?: return@invokeLater
            val cleanedTitle = currentTitle.replace(Regex("^\\[\\d+]\\s*"), "")

            if (currentTitle != cleanedTitle) {
                frame.title = cleanedTitle
            }
        }
    }

    fun removeFromAllOpenProjects() {
        ApplicationManager.getApplication().invokeLater {
            ProjectManager.getInstance().openProjects.forEach { project ->
                remove(project)
            }
        }
    }

    private fun updateTitle(frame: Frame, number: Int) {
        val originalTitle = frame.title ?: return

        val cleanedTitle = originalTitle.replace(Regex("^\\[\\d+]\\s*"), "")
        val newTitle = "[$number] $cleanedTitle"

        if (frame.title != newTitle) {
            frame.title = newTitle
        }
    }

    fun reapplyOnFocus(project: Project) {
        val frame = WindowManager.getInstance().getFrame(project) ?: return

        frame.addWindowFocusListener(object : WindowAdapter() {
            override fun windowGainedFocus(e: WindowEvent?) {
                val number = projectNumbers[project] ?: return
                updateTitle(frame, number)
            }
        })
    }

    private fun startTitleEnforcer(project: Project) {
        val alarm = Alarm(Alarm.ThreadToUse.SWING_THREAD, project)
        alarms[project] = alarm

        val task = object : Runnable {
            override fun run() {
                val frame = WindowManager.getInstance().getFrame(project)
                val number = projectNumbers[project]

                if (frame != null && number != null) {
                    updateTitle(frame, number)
                }

                alarm.addRequest(this, 1500)
            }
        }

        alarm.addRequest(task, 1500)
    }
}