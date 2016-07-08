package io.github.ksmirenko.kpc

import org.junit.Test
import org.junit.Assert

class PetoohParserTest {
    @Test fun testPetoohParser() {
        val bTokens = BrainfuckParser().parse(",---<-]>+.[")
        val pTokens = PetoohParser().parse("kukarekkOkOkO kudah kO kud-Kudah Ko Kukarek Kud")
        Assert.assertArrayEquals(bTokens, pTokens)
    }
}