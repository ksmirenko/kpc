package io.github.ksmirenko.kpc

import java.nio.file.Files
import java.nio.file.Paths

fun main(args : Array<String>) {
    println("Hello, Kotlin!")

    val parser = BrainfuckParser()
    val compiler = TokenJvmCompiler()
    val program = "+++[-]+." // prints 0x0001 symbol
    // generate class
    val className = "TestClass"
    val classByteArray = compiler.generateClassByteArray(parser.parse(program), className)
    // save to file
    val targetFile = Paths.get("$className.class")
    Files.write(targetFile, classByteArray)
}