package com.window_accent

import com.intellij.openapi.project.Project
import com.window_accent.feature.window_title.WindowTitleApplier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import java.lang.ref.WeakReference

@Suppress("UNCHECKED_CAST")
class PluginUnloadingVerificationTest {

    private lateinit var applier: WindowTitleApplier
    private lateinit var mockProject1: Project

    @BeforeEach
    fun setup() {
        applier = WindowTitleApplier()
        mockProject1 = mock(Project::class.java)
    }

    @Test
    @DisplayName("cancelAllPendingOperations should release disposer closures for GC")
    fun testCancelAllPendingOperationsReleasesDisposerClosures() {
        val closuresMap = privateField("projectDisposeClosures")
            .get(applier) as MutableMap<Project, () -> Unit>

        var closure: (() -> Unit)? = { /* no-op test closure */ }
        closuresMap[mockProject1] = closure!! // inserts the test closure into the applier’s internal map
        val closureRef = WeakReference(closure)
        Assertions.assertNotNull(closureRef.get(), "Weak reference should point to closure before cleanup")
        closure = null

        applier.cancelAllPendingOperations()
        awaitGc(closureRef)

        Assertions.assertTrue(closuresMap.isEmpty(), "projectDisposeClosures should be empty after cleanup")
        Assertions.assertFalse(
            closuresMap.values.any { it === closureRef.get() },
            "Cleanup should remove any strong map reference to the weak-referenced closure"
        )
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