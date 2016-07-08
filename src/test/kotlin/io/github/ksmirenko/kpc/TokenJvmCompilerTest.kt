package io.github.ksmirenko.kpc

import org.junit.Test
import kotlin.test.assertEquals

class TokenJvmCompilerTest {
    val parser = BrainfuckParser()
    val compiler = TokenJvmCompiler()

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
        val classByteArray = compiler.generateClassByteArray(parser.parse(program), "TestClass")

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
}
