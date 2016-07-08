package io.github.ksmirenko.kpc

interface IParser {
    fun parse(code : String) : Array<Token>
}