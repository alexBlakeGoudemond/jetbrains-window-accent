package com.window_accent.feature.window_title

/**
 * Transforms plain Latin text into Unicode Mathematical Alphanumeric Symbol
 * equivalents, producing a visual bold, italic, or double-struck effect in contexts that render
 * only plain text — such as OS native window title bars.
 *
 * Only ASCII letters (A–Z, a–z) and digits (0–9) are transformed; all other
 * characters (spaces, hyphens, punctuation, etc.) are passed through unchanged.
 *
 * **Unicode ranges used**
 *
 * | Style         | Capitals                     | Small letters  | Digits         |
 * |---------------|------------------------------|----------------|----------------|
 * | Bold          | U+1D400–1D419                | U+1D41A–1D433  | U+1D7CE–1D7D7  |
 * | Italic        | U+1D434–1D44D                | U+1D44E–1D467* | — (unchanged)  |
 * | Double-struck | U+1D538–U+1D551**            | U+1D552–1D56B  | U+1D7D8–1D7E1  |
 *
 * *U+1D455 (italic small h) is absent from Unicode; the Planck constant
 * symbol ℎ (U+210E) is used in its place per Unicode convention.
 *
 * **Some double-struck capitals use legacy Letterlike Symbols:
 * ℂ (C), ℍ (H), ℕ (N), ℙ (P), ℚ (Q), ℝ (R), ℤ (Z).
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

    /** Offset from ASCII 'A' to Mathematical Double-Struck Capital A (U+1D538). */
    private const val DOUBLE_STRUCK_CAPITAL_OFFSET = 0x1D538 - 'A'.code

    /** Offset from ASCII 'a' to Mathematical Double-Struck Small a (U+1D552). */
    private const val DOUBLE_STRUCK_SMALL_OFFSET = 0x1D552 - 'a'.code

    /** Offset from ASCII '0' to Mathematical Double-Struck Digit Zero (U+1D7D8). */
    private const val DOUBLE_STRUCK_DIGIT_OFFSET = 0x1D7D8 - '0'.code

    /**
     * Mathematical Italic Small H (U+210E — Planck constant symbol).
     *
     * U+1D455 is absent from Unicode; U+210E is the designated substitute
     * for italic lowercase h in Mathematical Alphanumeric Symbols.
     */
    private const val ITALIC_SMALL_H = '\u210E'
    private const val DOUBLE_STRUCK_CAPITAL_C = '\u2102' // ℂ
    private const val DOUBLE_STRUCK_CAPITAL_H = '\u210D' // ℍ
    private const val DOUBLE_STRUCK_CAPITAL_N = '\u2115' // ℕ
    private const val DOUBLE_STRUCK_CAPITAL_P = '\u2119' // ℙ
    private const val DOUBLE_STRUCK_CAPITAL_Q = '\u211A' // ℚ
    private const val DOUBLE_STRUCK_CAPITAL_R = '\u211D' // ℝ
    private const val DOUBLE_STRUCK_CAPITAL_Z = '\u2124' // ℤ

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

    /**
     * Returns [text] with each ASCII letter or digit replaced by its
     * Mathematical Double-Struck Unicode equivalent.
     *
     * Example: `"new-42"` → `"𝕟𝕖𝕨-𝟜𝟚"`
     */
    fun toDoubleStruck(text: String): String = buildString {
        for (ch in text) {
            when (ch) {
                'C'         -> append(DOUBLE_STRUCK_CAPITAL_C)
                'H'         -> append(DOUBLE_STRUCK_CAPITAL_H)
                'N'         -> append(DOUBLE_STRUCK_CAPITAL_N)
                'P'         -> append(DOUBLE_STRUCK_CAPITAL_P)
                'Q'         -> append(DOUBLE_STRUCK_CAPITAL_Q)
                'R'         -> append(DOUBLE_STRUCK_CAPITAL_R)
                'Z'         -> append(DOUBLE_STRUCK_CAPITAL_Z)
                in 'A'..'Z' -> appendCodePoint(ch.code + DOUBLE_STRUCK_CAPITAL_OFFSET)
                in 'a'..'z' -> appendCodePoint(ch.code + DOUBLE_STRUCK_SMALL_OFFSET)
                in '0'..'9' -> appendCodePoint(ch.code + DOUBLE_STRUCK_DIGIT_OFFSET)
                else        -> append(ch)
            }
        }
    }
}
