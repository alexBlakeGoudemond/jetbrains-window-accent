package com.window_accent.feature.window_title

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/** Converts a Unicode code point to a two-char surrogate-pair String (for SMP code points). */
private fun cp(codePoint: Int): String = String(Character.toChars(codePoint))

@DisplayName("TitleTextStyler Tests")
class TitleTextStylerTest {

    // -------------------------------------------------------------------------
    // toBold — lowercase letters
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("toBold - each ASCII lowercase letter maps to its Mathematical Bold Small equivalent")
    fun testToBoldLowercaseLetters() {
        assertEquals(cp(0x1D41A), TitleTextStyler.toBold("a"))
        assertEquals(cp(0x1D41B), TitleTextStyler.toBold("b"))
        assertEquals(cp(0x1D433), TitleTextStyler.toBold("z"))
    }

    // -------------------------------------------------------------------------
    // toBold — uppercase letters
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("toBold - each ASCII uppercase letter maps to its Mathematical Bold Capital equivalent")
    fun testToBoldUppercaseLetters() {
        assertEquals(cp(0x1D400), TitleTextStyler.toBold("A"))
        assertEquals(cp(0x1D401), TitleTextStyler.toBold("B"))
        assertEquals(cp(0x1D419), TitleTextStyler.toBold("Z"))
    }

    // -------------------------------------------------------------------------
    // toBold — digits
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("toBold - each ASCII digit maps to its Mathematical Bold Digit equivalent")
    fun testToBoldDigits() {
        assertEquals(cp(0x1D7CE), TitleTextStyler.toBold("0"))
        assertEquals(cp(0x1D7CF), TitleTextStyler.toBold("1"))
        assertEquals(cp(0x1D7D7), TitleTextStyler.toBold("9"))
    }

    // -------------------------------------------------------------------------
    // toBold — non-alphanumeric passthrough
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("toBold - non-alphanumeric characters are passed through unchanged")
    fun testToBoldPassthroughChars() {
        assertEquals(" ", TitleTextStyler.toBold(" "))
        assertEquals("-", TitleTextStyler.toBold("-"))
        assertEquals("!", TitleTextStyler.toBold("!"))
        assertEquals("_", TitleTextStyler.toBold("_"))
    }

    // -------------------------------------------------------------------------
    // toBold — edge cases
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("toBold - empty string returns empty string")
    fun testToBoldEmptyString() {
        assertEquals("", TitleTextStyler.toBold(""))
    }

    @Test
    @DisplayName("toBold - mixed content applies bold only to letters and digits")
    fun testToBoldMixedContent() {
        val result = TitleTextStyler.toBold("prod-1")
        val expected = cp(0x1D429) + cp(0x1D42B) + cp(0x1D428) + cp(0x1D41D) + "-" + cp(0x1D7CF)
        assertEquals(expected, result)
    }

    // -------------------------------------------------------------------------
    // toItalic — lowercase letters
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("toItalic - each ASCII lowercase letter maps to its Mathematical Italic Small equivalent")
    fun testToItalicLowercaseLetters() {
        assertEquals(cp(0x1D44E), TitleTextStyler.toItalic("a"))
        assertEquals(cp(0x1D44F), TitleTextStyler.toItalic("b"))
        assertEquals(cp(0x1D467), TitleTextStyler.toItalic("z"))
    }

    @Test
    @DisplayName("toItalic - lowercase 'h' maps to Planck constant symbol U+210E (U+1D455 is absent from Unicode)")
    fun testToItalicSpecialCaseH() {
        assertEquals("\u210E", TitleTextStyler.toItalic("h"))
    }

    // -------------------------------------------------------------------------
    // toItalic — uppercase letters
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("toItalic - each ASCII uppercase letter maps to its Mathematical Italic Capital equivalent")
    fun testToItalicUppercaseLetters() {
        assertEquals(cp(0x1D434), TitleTextStyler.toItalic("A"))
        assertEquals(cp(0x1D435), TitleTextStyler.toItalic("B"))
        assertEquals(cp(0x1D44D), TitleTextStyler.toItalic("Z"))
    }

    // -------------------------------------------------------------------------
    // toItalic — digits passthrough (no italic digit block in Unicode)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("toItalic - digits are passed through unchanged (no italic Unicode digit block)")
    fun testToItalicDigitsPassthrough() {
        assertEquals("0", TitleTextStyler.toItalic("0"))
        assertEquals("9", TitleTextStyler.toItalic("9"))
        assertEquals("42", TitleTextStyler.toItalic("42"))
    }

    // -------------------------------------------------------------------------
    // toItalic — non-alphanumeric passthrough
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("toItalic - non-alphanumeric characters are passed through unchanged")
    fun testToItalicPassthroughChars() {
        assertEquals(" ", TitleTextStyler.toItalic(" "))
        assertEquals("-", TitleTextStyler.toItalic("-"))
        assertEquals("!", TitleTextStyler.toItalic("!"))
    }

    // -------------------------------------------------------------------------
    // toItalic — edge cases
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("toItalic - empty string returns empty string")
    fun testToItalicEmptyString() {
        assertEquals("", TitleTextStyler.toItalic(""))
    }

    @Test
    @DisplayName("toItalic - mixed content applies italic only to letters, digits and non-alphanumeric pass through")
    fun testToItalicMixedContent() {
        val result = TitleTextStyler.toItalic("dev-1")
        val expected = cp(0x1D451) + cp(0x1D452) + cp(0x1D463) + "-" + "1"
        assertEquals(expected, result)
    }
}

