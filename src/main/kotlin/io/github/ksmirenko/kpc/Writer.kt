package io.github.ksmirenko.kpc

import java.io.File

/**
 * Translates Tokens into Brainfuck or PETOOH code and writes to a file.
 */
class Writer {
    fun writeBrainfuck(tokens : Array<Token>, outputFileName : String) {
        val outputFile = File(outputFileName)
        val stringBuilder = StringBuilder()
        tokens.forEach {
            stringBuilder.append(when (it) {
                Token.NEXT -> '>'
                Token.PREV -> '<'
                Token.INC -> '+'
                Token.DEC -> '-'
                Token.WRITE -> '.'
                Token.READ -> ','
                Token.LBRACKET -> '['
                Token.RBRACKET -> ']'
            })
        }
        outputFile.writeText(stringBuilder.toString())
    }

    fun writePetooh(tokens : Array<Token>, outputFileName : String) {
        val outputFile = File(outputFileName)
        val stringBuilder = StringBuilder()
        tokens.forEach {
            stringBuilder.append(when (it) {
                Token.NEXT -> "Kudah"
                Token.PREV -> "kudah"
                Token.INC -> "Ko"
                Token.DEC -> "kO"
                Token.WRITE -> "Kukarek"
                Token.READ -> "kukarek"
                Token.LBRACKET -> "Kud"
                Token.RBRACKET -> "kud"
            })
        }
        outputFile.writeText(stringBuilder.toString())
    }
}