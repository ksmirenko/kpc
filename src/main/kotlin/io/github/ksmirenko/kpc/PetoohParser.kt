package io.github.ksmirenko.kpc

import java.util.ArrayList

/**
 * Translates PETOOH code to an array of Tokens.
 *
 * This translator deals with an extended version of PETOOH, with
 * "kukarek" instruction added, which reads a byte (ASCII character)
 * from the input and saves it at the data pointer.
 */
class PetoohParser {
    fun parse(code : String) : Array<Token> {
        val tokens = ArrayList<Token>()
        var curPosition = 0
        while (curPosition < code.length) {
            val token = when {
                code.startsWith("Kudah", curPosition) -> {
                    curPosition += 5
                    Token.NEXT
                }
                code.startsWith("kudah", curPosition) -> {
                    curPosition += 5
                    Token.PREV
                }
                code.startsWith("Ko", curPosition) -> {
                    curPosition += 2
                    Token.INC
                }
                code.startsWith("kO", curPosition) -> {
                    curPosition += 2
                    Token.DEC
                }
                code.startsWith("Kukarek", curPosition) -> {
                    curPosition += 7
                    Token.WRITE
                }
                code.startsWith("kukarek", curPosition) -> { // non-standard instruction
                    curPosition += 7
                    Token.READ
                }
                code.startsWith("Kud", curPosition) -> {
                    curPosition += 3
                    Token.LBRACKET
                }
                code.startsWith("kud", curPosition) -> {
                    curPosition += 3
                    Token.RBRACKET
                }
                else -> {
                    curPosition += 1
                    null
                }
            }
            if (token != null) tokens.add(token)
        }
        return tokens.toTypedArray()
    }
}