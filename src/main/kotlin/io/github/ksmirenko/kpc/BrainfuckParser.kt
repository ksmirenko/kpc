package io.github.ksmirenko.kpc

import java.util.ArrayList

/**
 * Translates Brainfuck code to an array of Tokens.
 */
class BrainfuckParser {
    fun parse(code : String) : Array<Token> {
        val tokens = ArrayList<Token>()
        code.forEach { c ->
            val token = when (c) {
                '>' -> Token.NEXT
                '<' -> Token.PREV
                '+' -> Token.INC
                '-' -> Token.DEC
                '.' -> Token.WRITE
                ',' -> Token.READ
                '[' -> Token.LBRACKET
                ']' -> Token.RBRACKET
                else -> null
            }
            if (token != null) tokens.add(token)
        }
        return tokens.toTypedArray()
    }
}