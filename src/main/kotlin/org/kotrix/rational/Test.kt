package org.kotrix.rational

fun main() {
    println(Rational(1, 2) + Rational(1, 4))
    println(Rational(1, 3) + Rational(1, 5))
    println(Rational(1) + Rational(2))
    println(Rational(1) + Rational(1, 2))
    println(Rational(1, 2) + Rational(1, 2))

    println()

    println(Rational(1, 2) - Rational(1, 4))
    println(Rational(1, 3) - Rational(1, 5))
    println(Rational(1) - Rational(2))
    println(Rational(1) - Rational(1, 2))
    println(Rational(1, 2) - Rational(1, 2))

    println()

    println(Rational(1, 2) * Rational(1, 4))
    println(Rational(1, 3) * Rational(1, 5))
    println(Rational(1) * Rational(2))
    println(Rational(1) * Rational(1, 2))
    println(Rational(1, 2) * Rational(1, 2))

    println()

    println(Rational(1, 2) / Rational(1, 4))
    println(Rational(1, 3) / Rational(1, 5))
    println(Rational(1) / Rational(2))
    println(Rational(1) / Rational(1, 2))
    println(Rational(1, 2) / Rational(1, 2))
}