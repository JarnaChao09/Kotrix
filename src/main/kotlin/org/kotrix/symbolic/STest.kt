package org.kotrix.symbolic

import org.kotrix.symbolic.funAST.delegates.Var
import org.kotrix.symbolic.funAST.extensions.at
import org.kotrix.symbolic.funAST.extensions.pow
import org.kotrix.symbolic.parse.Lexer

fun main() {
    val x by Var()

    val y = x pow 2

    println(y.simpleString())

    println(y.diff(x).simpleString())

    println(y.diff(x).evalAllAtZero().simpleString())

    println(y.diff(x).eval(x at 4).simpleString())

    println(y.eval(x at 8).simpleString())

    val lexer = Lexer("(1.5 + 2) * 3 ^ -4")

    val test1 = lexer.generateTokens()

    val test2 = lexer("(1 + 2.5) * 3.2 ^ -4.6").generateTokens()

    println(test1)

    println(test2)
}
