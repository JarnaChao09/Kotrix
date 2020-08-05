package org.kotrix.symbolic

fun main() {
//    val x = Variable("x")
//
//    println(x)
//
//    val y = x + 10
//
//    println(y)
//
//    val z by Var()
//
//    val t = y + z
//
//    println(t)
//
//    println(t(x to 2, z to 4))
//
//    val t2 = t + 2.const * z
//
//    println(t2.stringify())
//
//    println(t2.variables)

    val x by Var()

//    val y = x pow 2
//
//    println(y.diff(x).simpleString())

//    println(y.diff(x).evalAllAtZero().simpleString())
//
//    val y1 = x + 10
//
//    val y1Prime = d(y1) / d(x)
//
//    println(y1Prime(x to 2).simpleString())
//
//    val a by Var()
//
//    val b by Var()
//
//    val c = a + b
//
//    println(c(b to 2).simpleString())
//
//    println(c.sub(a, Variable("t") pow 2).simpleString())
//
//    val test2 = ((x pow 2) + 2 * x) * ((x pow 3) + 4)
//
//    println((((test2.diff(x).simplify() as Sum).b as Sum).a as Sum).a)
//
//    println(test2.diff(x).simpleString())
//
//    val test3 = (x pow 2) * (x pow 6)
//
//    println(test3.simpleString())
//
//    val test4 = (x pow 2) * (3 * (x pow 2))
//
//    println(test4.simpleString())
//
//    val test5 = (4 * (x pow 2)) * (3 * (x pow 2))
//
//    println(test5.simpleString())
//
//    val test6 = (2 * (x pow 2)) + (4 * (x pow 2))
//
//    println(test6.simpleString())
//
//    val test7 = (4 + (2 * x)) + (4 * x)
//
//    println(test7.simpleString())
//
//    val test8 = ((x pow 2) + (x pow 3)) * 4
//
//    println(test8.simpleString())

    val test9 = (2 * (x pow 3) + (x pow 2)) + (x pow 3)

    println(test9.stringify())

    println(test9.simpleString())
}
