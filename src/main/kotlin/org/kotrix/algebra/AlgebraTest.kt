package org.kotrix.algebra

import org.kotrix.algebra.primitives.extensions.Field
import org.kotrix.algebra.primitives.extensions.Ring

fun main() {
    with(Double.Field) {
        val x = number(10)

        println(x)

        println(x + one)

        println(x * zero)
    }

    println()

    with(Float.Field) {
        val x = number(10)

        println(x)

        println(x + one)

        println(x * zero)
    }

    println()

    with(Byte.Ring) {
        val x = number(10)

        println(x)

        println(x + one)

        println(x * zero)
    }

    println()

    with(Short.Ring) {
        val x = number(10)

        println(x)

        println(x + one)

        println(x * zero)
    }

    println()

    with(Int.Ring) {
        val x = number(10)

        println(x)

        println(x + one)

        println(x * zero)
    }

    println()

    with(Long.Ring) {
        val x = number(10)

        println(x)

        println(x + one)

        println(x * zero)
    }
}