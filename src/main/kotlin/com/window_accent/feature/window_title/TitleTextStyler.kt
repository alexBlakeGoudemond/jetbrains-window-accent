package com.window_accent.feature.window_title

/**
 * Transforms plain Latin text into Unicode Mathematical Alphanumeric Symbol
 * equivalents, producing a visual bold or italic effect in contexts that render
 * only plain text — such as OS native window title bars.
 *
 * Only ASCII letters (A–Z, a–z) and digits (0–9) are transformed; all other
 * characters (spaces, hyphens, punctuation, etc.) are passed through unchanged.
 *
 * **Unicode ranges used**
 *
 * | Style  | Capitals       | Small letters  | Digits         |
 * |--------|----------------|----------------|----------------|
 * | Bold   | U+1D400–1D419  | U+1D41A–1D433  | U+1D7CE–1D7D7  |
 * | Italic | U+1D434–1D44D  | U+1D44E–1D467* | — (unchanged)  |
 *
 * *U+1D455 (italic small h) is absent from Unicode; the Planck constant
 * symbol ℎ (U+210E) is used in its place per Unicode convention.
 *
 * **Platform notes**
 *
 * Mathematical Alphanumeric Symbols are regular Unicode code points and display
 * correctly on any OS whose title-bar font includes the Supplementary Multilingual
 * Plane (Windows 10/11 and macOS with their default fonts; Linux depends on the
 * installed font stack).
 */
object TitleTextStyler {

    /** Offset from ASCII 'A' to Mathematical Bold Capital A (U+1D400). */
    private const val BOLD_CAPITAL_OFFSET = 0x1D400 - 'A'.code

    /** Offset from ASCII 'a' to Mathematical Bold Small a (U+1D41A). */
    private const val BOLD_SMALL_OFFSET = 0x1D41A - 'a'.code

    /** Offset from ASCII '0' to Mathematical Bold Digit Zero (U+1D7CE). */
    private const val BOLD_DIGIT_OFFSET = 0x1D7CE - '0'.code

    /** Offset from ASCII 'A' to Mathematical Italic Capital A (U+1D434). */
    private const val ITALIC_CAPITAL_OFFSET = 0x1D434 - 'A'.code

    /** Offset from ASCII 'a' to Mathematical Italic Small a (U+1D44E). */
    private const val ITALIC_SMALL_OFFSET = 0x1D44E - 'a'.code

    /**
     * Mathematical Italic Small H (U+210E — Planck constant symbol).
     *
     * U+1D455 is absent from Unicode; U+210E is the designated substitute
     * for italic lowercase h in Mathematical Alphanumeric Symbols.
     */
    private const val ITALIC_SMALL_H = '\u210E'

    /**
     * Returns [text] with each ASCII letter or digit replaced by its
     * Mathematical Bold Unicode equivalent.
     *
     * Example: `"prod"` → `"𝗽𝗿𝗼𝗱"`
     */
    fun toBold(text: String): String = buildString {
        for (ch in text) {
            when (ch) {
                in 'A'..'Z' -> appendCodePoint(ch.code + BOLD_CAPITAL_OFFSET)
                in 'a'..'z' -> appendCodePoint(ch.code + BOLD_SMALL_OFFSET)
                in '0'..'9' -> appendCodePoint(ch.code + BOLD_DIGIT_OFFSET)
                else         -> append(ch)
            }
        }
    }

    /**
     * Returns [text] with each ASCII letter replaced by its Mathematical Italic
     * Unicode equivalent.
     *
     * Digits have no italic Unicode counterpart and are left unchanged.
     *
     * Example: `"dev"` → `"𝑑𝑒𝑣"`
     */
    fun toItalic(text: String): String = buildString {
        for (ch in text) {
            when (ch) {
                in 'A'..'Z' -> appendCodePoint(ch.code + ITALIC_CAPITAL_OFFSET)
                'h'         -> append(ITALIC_SMALL_H)
                in 'a'..'z' -> appendCodePoint(ch.code + ITALIC_SMALL_OFFSET)
                else        -> append(ch)
            }
        }
    }
}

