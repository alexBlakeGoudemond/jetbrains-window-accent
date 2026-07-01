package com.window_accent.configuration.persistence

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/** Builds a supplementary-character String from its code point (produces a surrogate pair). */
private fun smp(codePoint: Int): String = String(Character.toChars(codePoint))

@DisplayName("UnicodeXmlSanitizer Tests")
class UnicodeXmlSanitizerTest {

    // -------------------------------------------------------------------------
    // encode — passthrough cases (no supplementary characters)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("encode - empty string returns empty string")
    fun testEncodeEmpty() {
        assertEquals("", UnicodeXmlSanitizer.encode(""))
    }

    @Test
    @DisplayName("encode - ASCII-only string is returned unchanged")
    fun testEncodeAscii() {
        assertEquals("prod", UnicodeXmlSanitizer.encode("prod"))
    }

    @Test
    @DisplayName("encode - BMP special characters are returned unchanged")
    fun testEncodeBmpSpecialChars() {
        assertEquals("hello – world", UnicodeXmlSanitizer.encode("hello – world"))
    }

    @Test
    @DisplayName("encode - string with spaces and punctuation is returned unchanged")
    fun testEncodePunctuation() {
        assertEquals("hello, world! (test)", UnicodeXmlSanitizer.encode("hello, world! (test)"))
    }

    // -------------------------------------------------------------------------
    // encode — supplementary characters (emoji)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("encode - single emoji is replaced with escape sequence")
    fun testEncodeSingleEmoji() {
        val rocket = smp(0x1F680)  // 🚀
        assertEquals("\\u{1F680}", UnicodeXmlSanitizer.encode(rocket))
    }

    @Test
    @DisplayName("encode - another emoji produces the correct escape sequence")
    fun testEncodePartyPopper() {
        val party = smp(0x1F389)  // 🎉
        assertEquals("\\u{1F389}", UnicodeXmlSanitizer.encode(party))
    }

    @Test
    @DisplayName("encode - emoji at the start of a mixed string")
    fun testEncodeEmojiAtStart() {
        val rocket = smp(0x1F680)
        assertEquals("\\u{1F680} prod", UnicodeXmlSanitizer.encode("$rocket prod"))
    }

    @Test
    @DisplayName("encode - emoji at the end of a mixed string")
    fun testEncodeEmojiAtEnd() {
        val rocket = smp(0x1F680)
        assertEquals("prod \\u{1F680}", UnicodeXmlSanitizer.encode("prod $rocket"))
    }

    @Test
    @DisplayName("encode - emoji in the middle of a mixed string")
    fun testEncodeEmojiInMiddle() {
        val rocket = smp(0x1F680)
        assertEquals("pr\\u{1F680}od", UnicodeXmlSanitizer.encode("pr${rocket}od"))
    }

    @Test
    @DisplayName("encode - multiple consecutive emoji are each escaped")
    fun testEncodeMultipleEmoji() {
        val rocket = smp(0x1F680)
        val party = smp(0x1F389)
        assertEquals("\\u{1F680}\\u{1F389}", UnicodeXmlSanitizer.encode("$rocket$party"))
    }

    @Test
    @DisplayName("encode - multiple emoji separated by text are each escaped")
    fun testEncodeMultipleEmojiWithText() {
        val rocket = smp(0x1F680)
        val party = smp(0x1F389)
        assertEquals("\\u{1F680} party \\u{1F389}", UnicodeXmlSanitizer.encode("$rocket party $party"))
    }

    // -------------------------------------------------------------------------
    // decode — passthrough cases
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("decode - empty string returns empty string")
    fun testDecodeEmpty() {
        assertEquals("", UnicodeXmlSanitizer.decode(""))
    }

    @Test
    @DisplayName("decode - ASCII-only string with no escapes is returned unchanged")
    fun testDecodeAscii() {
        assertEquals("prod", UnicodeXmlSanitizer.decode("prod"))
    }

    @Test
    @DisplayName("decode - string without escape marker is returned unchanged")
    fun testDecodeNoEscapeMarker() {
        assertEquals("hello – world", UnicodeXmlSanitizer.decode("hello – world"))
    }

    // -------------------------------------------------------------------------
    // decode — escape sequences
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("decode - single escape sequence is replaced with the emoji")
    fun testDecodeSingleEscape() {
        val rocket = smp(0x1F680)
        assertEquals(rocket, UnicodeXmlSanitizer.decode("\\u{1F680}"))
    }

    @Test
    @DisplayName("decode - escape sequence at the start of a mixed string")
    fun testDecodeEscapeAtStart() {
        val rocket = smp(0x1F680)
        assertEquals("$rocket prod", UnicodeXmlSanitizer.decode("\\u{1F680} prod"))
    }

    @Test
    @DisplayName("decode - escape sequence at the end of a mixed string")
    fun testDecodeEscapeAtEnd() {
        val rocket = smp(0x1F680)
        assertEquals("prod $rocket", UnicodeXmlSanitizer.decode("prod \\u{1F680}"))
    }

    @Test
    @DisplayName("decode - multiple escape sequences are each replaced")
    fun testDecodeMultipleEscapes() {
        val rocket = smp(0x1F680)
        val party = smp(0x1F389)
        assertEquals("$rocket$party", UnicodeXmlSanitizer.decode("\\u{1F680}\\u{1F389}"))
    }

    @Test
    @DisplayName("decode - lowercase hex digits in escape sequence are accepted")
    fun testDecodeLowercaseHex() {
        val rocket = smp(0x1F680)
        assertEquals(rocket, UnicodeXmlSanitizer.decode("\\u{1f680}"))
    }

    // -------------------------------------------------------------------------
    // round-trip (encode then decode)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("round-trip - plain ASCII survives encode then decode unchanged")
    fun testRoundTripAscii() {
        val original = "prod"
        assertEquals(original, UnicodeXmlSanitizer.decode(UnicodeXmlSanitizer.encode(original)))
    }

    @Test
    @DisplayName("round-trip - single emoji survives encode then decode unchanged")
    fun testRoundTripSingleEmoji() {
        val original = smp(0x1F680)
        assertEquals(original, UnicodeXmlSanitizer.decode(UnicodeXmlSanitizer.encode(original)))
    }

    @Test
    @DisplayName("round-trip - mixed text and emoji survives encode then decode unchanged")
    fun testRoundTripMixed() {
        val rocket = smp(0x1F680)
        val original = "prod $rocket"
        assertEquals(original, UnicodeXmlSanitizer.decode(UnicodeXmlSanitizer.encode(original)))
    }

    @Test
    @DisplayName("round-trip - multiple emoji survive encode then decode unchanged")
    fun testRoundTripMultipleEmoji() {
        val rocket = smp(0x1F680)
        val party = smp(0x1F389)
        val original = "$rocket dev $party"
        assertEquals(original, UnicodeXmlSanitizer.decode(UnicodeXmlSanitizer.encode(original)))
    }

    @Test
    @DisplayName("round-trip - empty string survives encode then decode unchanged")
    fun testRoundTripEmpty() {
        assertEquals("", UnicodeXmlSanitizer.decode(UnicodeXmlSanitizer.encode("")))
    }
}

