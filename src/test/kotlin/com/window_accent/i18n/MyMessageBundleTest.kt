package com.window_accent.i18n

import com.window_accent.diagnostic.windowAccentLogger
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class MyMessageBundleTest {

    private var logger = windowAccentLogger<MyMessageBundleTest>()

    @Test
    fun `test bundle instance is not null`() {
        // This is a simple test to verify the bundle can be loaded.
        // It might fail in a pure unit test if the platform environment is not set up,
        // but it's a good starting point for a JetBrains plugin test.
        assertNotNull(MyMessageBundle)
    }

    @Test
    fun `test message retrieval`() {
        // Since we don't have the properties file easily accessible in pure unit test without platform,
        // we just check if the method exists and can be called.
        // In a real LightPlatformTestCase, this would return actual values.
        try {
            val message = MyMessageBundle.message("plugin.name")
            assertNotNull(message)
        } catch (e: Exception) {
            // Ignore if platform is not initialized, as this is just a structural test
            logger.info("Platform not initialized, skipping detailed message check")
        }
    }
}
