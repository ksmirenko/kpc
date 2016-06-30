package io.github.ksmirenko.kpc

import java.io.Reader
import java.io.Writer

/**
 * Translates Brainfuck code to an array of Tokens.
 */
class BrainfuckReader {
    fun readCode(code : String) {
        val tokens = ArrayList<Token>()
        code.forEach { c ->
            tokens.add(when (c) {
                '>' -> Token.NEXT
                '<' -> Token.PREV
                '+' -> Tokens.INC
                '-' -> Tokens.DEC
                '.' -> Tokens.WRITE
                ',' -> Tokens.READ
                '[' -> Tokens.LBRACKET
                ']' -> Tokens.RBRACKET
            })
        }
        return tokens
    }
}