package io.github.ksmirenko.kpc

import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream

class StringInputStream(private val str : String) : InputStream() {
    var ptr = 0

    override fun read() = if (ptr < str.length) str[ptr++].toInt() else -1
}

class StringPrintStream() : PrintStream(EmptyStream()) {
    private val sb = StringBuilder()

    override fun print(c : Char) {
        sb.append(c)
    }

    override fun print(s : String) {
        sb.append(s)
    }

    override fun toString() = sb.toString()
}

class EmptyStream() : OutputStream() {
    override fun write(b : Int) {
    }
}