package io.github.ksmirenko.kpc

import org.junit.Test
import kotlin.test.assertEquals

class TokenInterpreterTest {
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
        val sw = StringPrintStream()
        interpreter.run(parser.parse(program), StringInputStream(input), sw)
        assertEquals(output, sw.toString())
    }
}