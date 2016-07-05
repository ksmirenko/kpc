package io.github.ksmirenko.kpc

import org.junit.Test
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

/**
 * Tests for the Brainfuck compiler.
 * @author Kirill Smirenko, group 271
 */
class CompilerTest {
    @Test fun testOutput() = testCompiler(
            "+++++-+++++.",
            "",
            "${9.toByte().toChar()}"
    )

    @Test fun testOutputCharOverflow() = testCompiler(
            "------.",
            "",
            "${250.toByte().toChar()}"
    )

    @Test fun testNextPrev() = testCompiler(
            ">+++<<++.>.>.",
            "",
            "${2.toByte().toChar()}${0.toByte().toChar()}${3.toByte().toChar()}"
    )

    @Test fun testInput() = testCompiler(
            ",.,.,.",
            "cat",
            "cat"
    )

    @Test fun testInputNextPrev() = testCompiler(
            ",>,>,>,>,>,.<.<.<.<.<.",
            "kotlin",
            "niltok"
    )

    @Test
    fun testWhile() = testCompiler(
            "+++[-].",
            "",
            "${0.toByte().toChar()}"
    )

    @Test
    fun testWhileInner() = testCompiler(
            "+++[>++++[>++++<-]<-]>>.",
            "",
            "${48.toByte().toChar()}"
    )

    @Test
    fun testHelloWorld() = testCompiler(
            "++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---" +
                    ".+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.",
            "",
            "Hello World!\n"
    )

    fun testCompiler(program : String, input : String, output : String) {
        val className = "TestClass"
        val classByteArray = BrainfuckCompiler.generateClassByteArray(program, "TestClass") // TODO: fix

        val out = StringPrintStream()
        System.setIn(StringInputStream(input))
        System.setOut(out)

        val cl = ByteArrayClassLoader()
        val exprClass = cl.loadClass(className, classByteArray)
        val methods = exprClass?.methods
        if (methods == null || methods.isEmpty()) {
            throw AssertionError("No main method was found.")
        }
        for (method in methods) {
            if (method.name != "main") {
                continue
            }
            method.invoke(null, arrayOf<String>())
            assertEquals(output, out.toString())
            return
        }
    }

    class ByteArrayClassLoader() : ClassLoader() {
        fun loadClass(name : String?, buf : ByteArray) : Class<*>? {
            return super.defineClass(name, buf, 0, buf.size)
        }
    }

    class StringInputStream(private val str : String) : InputStream() { // TODO: maybe combine with Readers
        var ptr = 0

        override fun read() = if (ptr < str.length) str[ptr++].toInt() else -1
    }

    class StringPrintStream() : PrintStream(EmptyStream()) {
        private var sb = StringBuilder()

        override fun print(c : Char) {
            sb.append(c)
        }

        override fun toString() = sb.toString()
    }

    class EmptyStream() : OutputStream() {
        override fun write(b : Int) {
        }
    }
}
