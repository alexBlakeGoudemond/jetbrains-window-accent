package com.window_accent

import com.intellij.openapi.util.Disposer
import com.intellij.openapi.project.Project
import com.intellij.util.Alarm
import com.window_accent.feature.window_color.WindowColorApplier
import com.window_accent.feature.window_title.WindowTitleApplier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import java.lang.ref.WeakReference
import java.awt.Frame
import javax.swing.JFrame
import java.awt.event.WindowAdapter
import java.util.concurrent.atomic.AtomicBoolean
import java.beans.PropertyChangeListener

@Suppress("UNCHECKED_CAST")
class PluginUnloadingVerificationTest {

    private lateinit var windowTitleApplier: WindowTitleApplier
    private lateinit var mockProject1: Project

    @BeforeEach
    fun setup() {
        windowTitleApplier = WindowTitleApplier()
        mockProject1 = mock(Project::class.java)
    }

    @Test
    @DisplayName("WindowTitleApplier#cancelAllPendingOperations() releases closures")
    fun unloadingWindowTitleApplierReleasesClosures() {
        val closuresMap = privateField("projectDisposeClosures")
            .get(windowTitleApplier) as MutableMap<Project, () -> Unit>

        var closure: (() -> Unit)? = { /* no-op test closure */ }
        closuresMap[mockProject1] = closure!! // inserts the test closure into the applier’s internal map
        val closureRef = WeakReference(closure)
        Assertions.assertNotNull(closureRef.get(), "Weak reference should point to closure before cleanup")
        closure = null

        windowTitleApplier.cancelAllPendingOperations()
        awaitGc(closureRef)

        Assertions.assertTrue(closuresMap.isEmpty(), "projectDisposeClosures should be empty after cleanup")
        Assertions.assertFalse(
            closuresMap.values.any { it === closureRef.get() },
            "Cleanup should remove any strong map reference to the weak-referenced closure"
        )
    }

    @Test
    @DisplayName("WindowTitleApplier#cancelAllPendingOperations() releases listeners")
    fun unloadingWindowTitleApplierReleasesListeners() {
        val focusListeners = privateField("focusListeners")
            .get(windowTitleApplier) as MutableMap<Project, Pair<WindowAdapter, Frame>>
        val titleListeners = privateField("titleListeners")
            .get(windowTitleApplier) as MutableMap<Project, Pair<PropertyChangeListener, Frame>>

        val frame: Frame = JFrame("cleanup-test-frame")
        focusListeners[mockProject1] = object : WindowAdapter() {} to frame
        titleListeners[mockProject1] = PropertyChangeListener {} to frame

        windowTitleApplier.cancelAllPendingOperations()
        (frame as JFrame).dispose()

        Assertions.assertTrue(focusListeners.isEmpty(), "focusListeners should be empty after cleanup")
        Assertions.assertTrue(titleListeners.isEmpty(), "titleListeners should be empty after cleanup")
        Assertions.assertTrue(
            privateField("isShuttingDown").getBoolean(windowTitleApplier),
            "isShuttingDown should be true after cleanup"
        )

        val retryAlarm = privateField("retryAlarm").get(windowTitleApplier) as Alarm
        val retryAlarmDisposed = privateField("retryAlarmDisposed").get(windowTitleApplier) as AtomicBoolean
        Assertions.assertTrue(retryAlarmDisposed.get(), "retryAlarmDisposed flag should be true after cleanup")
        Assertions.assertTrue(Disposer.isDisposed(retryAlarm), "retryAlarm should be disposed after cleanup")
    }

    @Test
    @DisplayName("WindowColorApplier#cancelCoroutines() disposes alarms")
    fun unloadingWindowColorApplierReleasesAlarms() {
        WindowColorApplier.cancelCoroutines()

        val clazz = WindowColorApplier::class.java
        val retryAlarmField = clazz.getDeclaredField("retryAlarm").apply { isAccessible = true }
        val retryAlarmDisposedField = clazz.getDeclaredField("retryAlarmDisposed").apply { isAccessible = true }
        val isShuttingDownField = clazz.getDeclaredField("isShuttingDown").apply { isAccessible = true }

        val retryAlarm = retryAlarmField.get(WindowColorApplier) as Alarm
        val retryAlarmDisposed = retryAlarmDisposedField.get(WindowColorApplier) as AtomicBoolean
        val isShuttingDown = isShuttingDownField.getBoolean(WindowColorApplier)

        Assertions.assertTrue(isShuttingDown, "WindowColorApplier should be marked as shutting down")
        Assertions.assertTrue(retryAlarmDisposed.get(), "WindowColorApplier retry alarm should be marked disposed")
        Assertions.assertTrue(Disposer.isDisposed(retryAlarm), "WindowColorApplier retry alarm should be disposed")
    }

    private fun privateField(name: String): java.lang.reflect.Field {
        val field = WindowTitleApplier::class.java.getDeclaredField(name)
        field.isAccessible = true
        return field
    }

    private fun awaitGc(vararg refs: WeakReference<*>) {
        repeat(20) {
            System.gc()
            Thread.sleep(50)
            if (refs.all { it.get() == null }) return
        }
    }

}
