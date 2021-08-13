package org.kotrix.symbolic

import org.kotrix.symbolic.funAST.delegates.Parse
import org.kotrix.symbolic.funAST.delegates.Var
import org.kotrix.symbolic.funAST.extensions.*
import org.kotrix.symbolic.parse.Lexer
import org.kotrix.symbolic.parse.Parser

//fun main() {
//
//    val lexer = Lexer("<1, 2 + 2, 2.5, 3 * 3, 3.5 / 3.5>")
//
//    val vectorTokens = lexer.generateTokens()
//
//    val parsedVector = Parser(vectorTokens).parse()
//
//    println(parsedVector.simpleString())
//
//    val lexer = Lexer("(1.5 + 2) * 3 ^ -4")
//
//    val test1 = lexer.generateTokens()
//
//    val test2 = lexer("(1 + 2.5) * 3.2 ^ -4.6").generateTokens()
//
//    println(test1)
//
//    println(test2)

//    val parser = Parser(test1)
//
//    val ptest1 = parser.parse()
//
//    val ptest2 = parser(test2).parse()
//
//    println(ptest1.simpleString())
//
//    println(ptest2.simpleString())
//
//    println(ptest1.evalAllAtZero())
//
//    println(ptest2.evalAllAtZero())
//
//    val ptest3 by Parse("(1.5 + 2) * 3 ^ -4")
//
//    println(ptest3.simpleString())
//
//    println("ptest1 and ptest3 are equal: ${ptest1 == ptest3}")
//}

fun main() {
    val x by Var()

    println(run {
        (2 + x) + 2
    }.simpleString())

//    val x2 = (2 + x) + x
//
//    println(x2.stringify())
//
//    println(x2.simplify())
//
//    println(x2.simpleString())
//
//    val x3 = (x + x) + (2 * x)
//
//    println(x3.stringify())
//
//    println(x3.simpleString())
//
//    val x4 = (x + 2) + (2 + x)
//
//    val x5 = ((2 * x) + 2 + 2)
//
//    println(x4.stringify())
//
//    println(x5)
//
//    println(x4.simpleString())
//
//    println(x5.simpleString())
//
//    val x1 = x / (2 * x)
//
//    println(x1.simpleString())
//
//    val y = x pow 2
//
//    println(y.simpleString())
//
//    println(y.diff(x).simpleString())
//
//    println(y.diff(x).evalAllAtZero())
//
//    println(y.diff(x).eval(x withValue 4).simpleString())
//
//    println(y.eval(x withValue 8).simpleString())
}
