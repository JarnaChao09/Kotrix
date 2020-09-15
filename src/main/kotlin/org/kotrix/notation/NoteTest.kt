package org.kotrix.notation

fun main() {
    val test1 = ScientificNotation(100)
    val test2 = ScientificNotation(5)

    println(test1)
    println(test2)
    println(test1 + test2)
    println(test1 - test2)
    println(test1 * test2)
    println(test1 / test2)
    // println(test1 % test2)
    println(test1 pow test2)
    println(ScientificNotation(1, -15) pow ScientificNotation(2))
}