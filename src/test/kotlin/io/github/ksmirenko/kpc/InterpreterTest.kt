package io.github.ksmirenko.kpc

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite
import java.io.Reader
import java.io.Writer
import kotlin.test.assertEquals

/**
 * Tests for the Brainfuck interpreter.
 * @author Kirill Smirenko, group 271
 */
class InterpreterTest {
    val parser = BrainfuckParser()
    val interpreter = TokenInterpreter()

    @Test fun testSimpleOutput() = testInterpreter(
            "+++++-+++++.",
            "",
            "${9.toByte().toChar()}"
    )

    @Test fun testByteOverflow() = testInterpreter(
            "------.",
            "",
            "${250.toByte().toChar()}"
    )

    @Test fun testInputNextPrev() = testInterpreter(
            ",>,>,>,>,>,.<.<.<.<.<.",
            "kotlin",
            "niltok"
    )

    @Test fun testHelloWorld() = testInterpreter(
            "++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---" +
                    ".+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.",
            "",
            "Hello World!\n"
    )

    fun testInterpreter(program : String, input : String, output : String) {
        val sw = StringWriter()
        interpreter.run(parser.parse(program), StringReader(input), sw)
        assertEquals(output, sw.toString())
    }

    /**
     * Custom Reader for testing.
     */
    class StringReader(private val str : String) : Reader() {
        var ptr = 0

        override fun read() = if (ptr < str.length) str[ptr++].toInt() else -1

        override fun read(cbuf : CharArray?, off : Int, len : Int) : Int {
            throw UnsupportedOperationException()
        }

        override fun close() {
        }
    }

    /**
     * Custom Writer for testing.
     */
    class StringWriter() : Writer() {
        private var sb = StringBuilder()

        override fun write(cbuf : CharArray?, off : Int, len : Int) {
            throw UnsupportedOperationException()
        }

        override fun write(str : String) {
            sb.append(str)
        }

        override fun flush() {
        }

        override fun close() {
        }

        override fun toString() = sb.toString()
    }
}