package io.github.ksmirenko.kpc

import java.nio.file.Files
import java.nio.file.Paths
import javax.lang.model.SourceVersion

fun main(args : Array<String>) {
    if (args.size < 1 || args.size > 2 || args[0] == "-h" || args[0] == "--help") {
        printHelp()
        return
    }
    val codeFilename = args[args.size - 1]
    val isInputPetooh = when {
        codeFilename.endsWith(".koko") -> true
        codeFilename.endsWith(".bf") -> false
        else -> {
            println("KPC only accepts .bf and .koko files.")
            return
        }
    }
    val code = String(Files.readAllBytes(Paths.get(codeFilename)))
    val tokenizedCode = (if (isInputPetooh) PetoohParser() else BrainfuckParser()).parse(code)
    // TODO: check the code for mismatched parentheses
    when (args[0]) {
        "-i" -> TokenInterpreter().run(tokenizedCode, System.`in`, System.out)
        "-b" -> Writer().writeBrainfuck(tokenizedCode, "$codeFilename.bf")
        "-p" -> Writer().writePetooh(tokenizedCode, "$codeFilename.koko")
        "-c" -> {
            if (args.size < 3) {
                printHelp()
                return
            }
            makeJvmClassFile(tokenizedCode, args[1])
        }
        else -> makeJvmClassFile(tokenizedCode, "TestClass")
    }
}

private fun makeJvmClassFile(tokenizedCode : Array<Token>, className : String) {
    if (!SourceVersion.isIdentifier(className) || SourceVersion.isKeyword(className)) {
        println("Please provide a valid Java class name.")
        return
    }
    Files.write(
            Paths.get("$className.class"),
            TokenJvmCompiler().generateClassByteArray(tokenizedCode, className)
    )
}

private fun printHelp() {
    println("""Kotlin PETOOH Compiler
Usage:
    kpc [-c <class-name>|-i|-b|-p] <code-file.bf|code-file.koko>
Parameters:
    (none)          compile to JVM class using the default class name TestClass
    -c <class-name> compile to JVM class using the provided class name
    -i              interpret
    -b              translate to Brainfuck
    -p              translate to PETOOH""")
}