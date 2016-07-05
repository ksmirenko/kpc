package io.github.ksmirenko.kpc

import java.io.Reader
import java.io.Writer

class TokenInterpreter {
    private val memorySize = 30000

    fun run(tokens : Array<Token>, reader : Reader, writer : Writer) {
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
                    writer.write(memory[memPtr].toChar().toString())
                Token.READ ->
                    memory[memPtr] = reader.read().toByte()
                Token.LBRACKET ->
                    if (memory[memPtr] == byteZero) {
                        var leftBracketCount = 1;
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
                        var rightBracketCount = 1;
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