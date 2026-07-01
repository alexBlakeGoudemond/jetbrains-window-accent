package com.window_accent.configuration.persistence

/**
 * Encodes and decodes supplementary Unicode characters (code points > U+FFFF, such as
 * emoji) to and from an ASCII-safe escape notation for safe XML attribute value storage.
 *
 * IntelliJ's [com.intellij.openapi.components.PersistentStateComponent] framework uses
 * JDOM to serialise state fields as XML attribute values. Depending on the JDOM version
 * bundled with the running IDE, surrogate-pair characters (the Java/Kotlin in-memory
 * representation of supplementary Unicode code points such as emoji) may be written as
 * individual XML character references (e.g. `&#xD83D;`) that are invalid in XML 1.0
 * — causing the emoji to be corrupted or lost when the state file is read back on
 * restart.
 *
 * This utility side-steps the issue by replacing each supplementary code point with the
 * escape notation `\u{XXXXXX}` (a backslash, the letter u, and the uppercase hex code
 * point in curly braces) before the value reaches the XML layer, then reversing the
 * substitution after loading.
 *
 * **Escape format**
 *
 * | Character      | Encoded form  |
 * |----------------|---------------|
 * | 🚀 (U+1F680)  | `\u{1F680}`   |
 * | 🎉 (U+1F389)  | `\u{1F389}`   |
 *
 * **Backward compatibility:** plain ASCII or BMP-only strings (no supplementary
 * characters) are returned unchanged by both [encode] and [decode].
 */
object UnicodeXmlSanitizer {

    /**
     * Regex that matches an encoded supplementary character escape: `\u{XXXXXX}` where
     * XXXXXX is 1–6 uppercase or lowercase hex digits.
     */
    private val ESCAPE_PATTERN = Regex("""\\u\{([0-9A-Fa-f]{1,6})}""")

    /**
     * Encodes all supplementary Unicode code points (> U+FFFF) in [text] as
     * ASCII-safe `\u{XXXXXX}` escape sequences.
     *
     * BMP characters (≤ U+FFFF) are passed through unchanged.
     * Returns [text] unchanged if it contains no supplementary characters.
     *
     * Example: `encode("🚀 prod")` → `"\u{1F680} prod"`
     */
    fun encode(text: String): String {
        if (text.isEmpty()) return text
        if (text.none { it.isHighSurrogate() }) return text
        return buildString {
            var i = 0
            while (i < text.length) {
                val codePoint = text.codePointAt(i)
                if (codePoint > 0xFFFF) {
                    append("\\u{")
                    append(codePoint.toString(16).uppercase())
                    append('}')
                } else {
                    append(text[i])
                }
                i += Character.charCount(codePoint)
            }
        }
    }

    /**
     * Decodes `\u{XXXXXX}` escape sequences back into their original supplementary
     * Unicode characters.
     *
     * Returns [text] unchanged if it contains no `\u{` sequences.
     *
     * Example: `decode("\u{1F680} prod")` → `"🚀 prod"`
     */
    fun decode(text: String): String {
        if (!text.contains("\\u{")) return text
        return buildString {
            var lastEnd = 0
            for (match in ESCAPE_PATTERN.findAll(text)) {
                append(text, lastEnd, match.range.first)
                val codePoint = match.groupValues[1].toInt(16)
                appendCodePoint(codePoint)
                lastEnd = match.range.last + 1
            }
            append(text, lastEnd, text.length)
        }
    }
}


