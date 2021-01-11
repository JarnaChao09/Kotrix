package org.kotrix.notation

fun main() {
    val test1 = ScientificNotation(100)
    val test2 = ScientificNotation(5)

    println("x= $test1")
    println("y= $test2")
    println("x+y= ${test1 + test2}")
    println("x-y= ${test1 - test2}")
    println("x*y= ${test1 * test2}")
    println("x/y= ${test1 / test2}")
    println("x%y= ${test1 % test2}")
    println("x^y= ${test1 pow test2}")
    println(ScientificNotation(1, -15) pow ScientificNotation(2))
    println(ScientificNotation(10, 2))
    println(EngineeringNotation(10000))
    println(EngineeringNotation(100000))
    println(EngineeringNotation(1000000))
    println(EngineeringNotation(.1234))
    println(EngineeringNotation(.12345678))
    println(EngineeringNotation(.0123456))
}