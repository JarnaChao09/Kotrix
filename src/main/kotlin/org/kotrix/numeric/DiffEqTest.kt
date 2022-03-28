package org.kotrix.numeric

import org.kotrix.numeric.diffeq.euler

fun main() {
    val x00 = 0.0
    val x01 = 5.0
    val y00 = 0.0
    val y01 = 25.0
    val xn = 3.0
    println(
        "Approximation of dy/dx = 2 * x, given initial conditions of ($x00, $y00), y($xn) = ${
            euler(x00, y00, xn) { x: Double, _: Double ->
                2 * x
            }
        }"
    )

    println(
        "Approximation of dy/dx = 2 * x, given initial conditions of ($x01, $y01), y($xn) = ${
            euler(x01, y01, xn) { x: Double, _: Double ->
                2 * x
            }
        }"
    )

    println(
        "Approximation of d(dy/dx)/dx = -2y' - 2y, given initial conditions of y(0) = 1 and y'(0) = 0, y(1) = ${
            euler(0.0, 1.0, 0.0, 1.0) { _: Double, y: Double, yPrime: Double ->
                -2 * yPrime - 2 * y
            }
        }"
    )
}