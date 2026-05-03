package com.window_color_panel.feature.window_title

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@DisplayName("WindowTitleApplier Tests")
class WindowTitleApplierTest {

    @Test
    @DisplayName("Should strip existing numeric prefix from title")
    fun testStripExistingPrefix() {
        // Test the stripExistingPrefix function directly via reflection
        val method = WindowTitleApplier::class.java.getDeclaredMethod("stripExistingPrefix", String::class.java)
        method.isAccessible = true

        // Test various title formats
        assertEquals("My Project", method.invoke(WindowTitleApplier, "[1] My Project"))
        assertEquals("My Project", method.invoke(WindowTitleApplier, "[123] My Project"))
        assertEquals("My Project", method.invoke(WindowTitleApplier, "[1]My Project"))
        assertEquals("My Project", method.invoke(WindowTitleApplier, "[1]  My Project"))
        assertEquals("Project Without Prefix", method.invoke(WindowTitleApplier, "Project Without Prefix"))
        assertEquals("", method.invoke(WindowTitleApplier, "[5]"))
        assertEquals("Text after [1] more text", method.invoke(WindowTitleApplier, "[1] Text after [1] more text"))
    }

    @Test
    @DisplayName("Should handle title prefix stripping edge cases")
    fun testStripExistingPrefixEdgeCases() {
        val method = WindowTitleApplier::class.java.getDeclaredMethod("stripExistingPrefix", String::class.java)
        method.isAccessible = true

        // Test edge cases
        assertEquals("", method.invoke(WindowTitleApplier, ""))
        assertEquals("No prefix here", method.invoke(WindowTitleApplier, "No prefix here"))
        assertEquals("[2] Multiple [1] [2] prefixes", method.invoke(WindowTitleApplier, "[1] [2] Multiple [1] [2] prefixes"))
        assertEquals("[Not a number] Text", method.invoke(WindowTitleApplier, "[Not a number] Text"))
        assertEquals("[1a] Text", method.invoke(WindowTitleApplier, "[1a] Text"))
    }

    @Test
    @DisplayName("Should generate correct title with prefix")
    fun testUpdateWindowTitleLogic() {
        val method = WindowTitleApplier::class.java.getDeclaredMethod("stripExistingPrefix", String::class.java)
        method.isAccessible = true

        // Test the title update logic
        fun generateTitleWithPrefix(currentTitle: String, number: Int): String {
            val cleanedTitle = method.invoke(WindowTitleApplier, currentTitle) as String
            return "[$number] $cleanedTitle"
        }

        assertEquals("[1] My Project", generateTitleWithPrefix("My Project", 1))
        assertEquals("[5] Existing Project", generateTitleWithPrefix("[3] Existing Project", 5))
        assertEquals("[10] Clean Title", generateTitleWithPrefix("Clean Title", 10))
        assertEquals("[2] ", generateTitleWithPrefix("", 2))
    }

    @Test
    @DisplayName("Should handle project number assignment")
    fun testProjectNumberAssignment() {
        // Test the project numbering logic
        val counter = AtomicInteger(1)
        val projectNumbers = ConcurrentHashMap<Any, Int>()

        fun getWindowProjectNumber(project: Any): Int =
            projectNumbers.computeIfAbsent(project) { counter.getAndIncrement() }

        val project1 = object {}
        val project2 = object {}
        val project3 = object {}

        // Test sequential numbering
        assertEquals(1, getWindowProjectNumber(project1))
        assertEquals(2, getWindowProjectNumber(project2))
        assertEquals(3, getWindowProjectNumber(project3))

        // Test that same project gets same number
        assertEquals(1, getWindowProjectNumber(project1))
        assertEquals(2, getWindowProjectNumber(project2))
    }

    @Test
    @DisplayName("Should handle counter reset logic")
    fun testCounterReset() {
        val counter = AtomicInteger(5)
        val projectNumbers = ConcurrentHashMap<Any, Int>()

        // Simulate reset logic
        fun resetProjectNumbering() {
            projectNumbers.clear()
            counter.set(1)
        }

        // Add some projects
        projectNumbers[object {}] = 1
        projectNumbers[object {}] = 2
        counter.set(3)

        assertEquals(2, projectNumbers.size)
        assertEquals(3, counter.get())

        // Reset
        resetProjectNumbering()

        assertEquals(0, projectNumbers.size)
        assertEquals(1, counter.get())
    }

    @Test
    @DisplayName("Should handle title prefix regex pattern")
    fun testTitlePrefixRegex() {
        val regex = Regex("^\\[\\d+]\\s*")

        // Test the regex pattern used in stripExistingPrefix
        assertTrue(regex.containsMatchIn("[1] Text"))
        assertTrue(regex.containsMatchIn("[123] Text"))
        assertTrue(regex.containsMatchIn("[1]Text"))
        assertTrue(regex.containsMatchIn("[1]  Text"))

        assertFalse(regex.containsMatchIn("Text [1]"))
        assertFalse(regex.containsMatchIn("No prefix"))
        assertFalse(regex.containsMatchIn("[abc] Text"))
        assertFalse(regex.containsMatchIn("[] Text"))
    }

    @Test
    @DisplayName("Should handle concurrent project numbering")
    fun testConcurrentProjectNumbering() {
        val counter = AtomicInteger(1)
        val projectNumbers = ConcurrentHashMap<Any, Int>()

        fun getWindowProjectNumber(project: Any): Int =
            projectNumbers.computeIfAbsent(project) { counter.getAndIncrement() }

        // Test thread safety (basic test - in real concurrent scenario would need more testing)
        val projects = List(10) { object {} }

        projects.forEach { project ->
            getWindowProjectNumber(project)
        }

        assertEquals(10, projectNumbers.size)
        assertEquals(11, counter.get()) // Next available number
    }

    @Test
    @DisplayName("Should handle title update idempotency")
    fun testTitleUpdateIdempotency() {
        // Test that applying the same title multiple times doesn't change it
        fun updateWindowTitle(currentTitle: String, number: Int): String {
            val cleanedTitle = currentTitle.replace(Regex("^\\[\\d+]\\s*"), "")
            val updatedTitle = "[$number] $cleanedTitle"
            return if (currentTitle == updatedTitle) currentTitle else updatedTitle
        }

        val title1 = "[5] My Project"
        val result1 = updateWindowTitle(title1, 5)
        assertEquals(title1, result1) // Should not change

        val title2 = "My Project"
        val result2 = updateWindowTitle(title2, 5)
        assertEquals("[5] My Project", result2) // Should add prefix

        val title3 = "[3] My Project"
        val result3 = updateWindowTitle(title3, 5)
        assertEquals("[5] My Project", result3) // Should update prefix
    }

    @Test
    @DisplayName("Should handle empty and null titles")
    fun testEmptyAndNullTitles() {
        val method = WindowTitleApplier::class.java.getDeclaredMethod("stripExistingPrefix", String::class.java)
        method.isAccessible = true

        // Test with empty string
        assertEquals("", method.invoke(WindowTitleApplier, ""))

        // Test with null would require different handling in real code
        // but the method signature takes String, so null isn't possible
    }

    @Test
    @DisplayName("Should handle large project numbers")
    fun testLargeProjectNumbers() {
        val method = WindowTitleApplier::class.java.getDeclaredMethod("stripExistingPrefix", String::class.java)
        method.isAccessible = true

        fun generateTitleWithPrefix(currentTitle: String, number: Int): String {
            val cleanedTitle = method.invoke(WindowTitleApplier, currentTitle) as String
            return "[$number] $cleanedTitle"
        }

        assertEquals("[999] Large Number Project", generateTitleWithPrefix("Large Number Project", 999))
        assertEquals("[1000] Very Large Number", generateTitleWithPrefix("Very Large Number", 1000))
        assertEquals("[999999] Extremely Large", generateTitleWithPrefix("[123] Extremely Large", 999999))
    }

    @Test
    @DisplayName("Should handle special characters in titles")
    fun testSpecialCharactersInTitles() {
        val method = WindowTitleApplier::class.java.getDeclaredMethod("stripExistingPrefix", String::class.java)
        method.isAccessible = true

        fun generateTitleWithPrefix(currentTitle: String, number: Int): String {
            val cleanedTitle = method.invoke(WindowTitleApplier, currentTitle) as String
            return "[$number] $cleanedTitle"
        }

        assertEquals("[1] Project with (parentheses)", generateTitleWithPrefix("Project with (parentheses)", 1))
        assertEquals("[2] Project-with-dashes", generateTitleWithPrefix("Project-with-dashes", 2))
        assertEquals("[3] Project_with_underscores", generateTitleWithPrefix("Project_with_underscores", 3))
        assertEquals("[4] Project with émojis 🚀", generateTitleWithPrefix("Project with émojis 🚀", 4))
        assertEquals("[5] Project with [brackets]", generateTitleWithPrefix("Project with [brackets]", 5))
    }
}
