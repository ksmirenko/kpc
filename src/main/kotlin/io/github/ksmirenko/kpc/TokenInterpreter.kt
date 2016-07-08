package io.github.ksmirenko.kpc

import java.io.InputStream
import java.io.PrintStream

class TokenInterpreter {
    private val memorySize = 30000

    fun run(tokens : Array<Token>, reader : InputStream, writer : PrintStream) {
        val byteZero : Byte = 0
        val memory = Array(memorySize) { byteZero }
        var memPtr = 0
        var commPtr = 0
        while (commPtr < tokens.size) {
            when (tokens[commPtr]) {
                Token.NEXT ->
                    memPtr = (memPtr + 1) % memorySize
                Token.PREV ->
                    memPtr = (memPtr - 1 + memorySize) % memorySize
                Token.INC ->
                    memory[memPtr]++
                Token.DEC ->
                    memory[memPtr]--
                Token.WRITE ->
                    writer.print(memory[memPtr].toChar())
                Token.READ ->
                    memory[memPtr] = reader.read().toByte()
                Token.LBRACKET ->
                    if (memory[memPtr] == byteZero) {
                        var leftBracketCount = 1
                        while (leftBracketCount > 0) {
                            when (tokens[++commPtr]) {
                                Token.LBRACKET -> leftBracketCount++
                                Token.RBRACKET -> leftBracketCount--
                                else -> { // just to avoid warnings
                                }
                            }
                        }
                    }
                Token.RBRACKET ->
                    if (memory[memPtr] != byteZero) {
                        var rightBracketCount = 1
                        while (rightBracketCount > 0) {
                            when (tokens[--commPtr]) {
                                Token.RBRACKET -> rightBracketCount++
                                Token.LBRACKET -> rightBracketCount--
                                else -> {
                                }
                            }
                        }
                    }
            }
            commPtr++
        }
    }
}