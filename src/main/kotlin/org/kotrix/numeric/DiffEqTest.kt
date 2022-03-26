package org.kotrix.numeric

import org.kotrix.numeric.diffeq.euler

fun main() {
    val x0 = 0.0
    val y0 = 0.0
    val xn = 3.0
    println(
        "Approximation of dy/dx = 2 * x, given initial conditions of ($x0, $y0), y($xn) = ${
            euler(x0, y0, xn) { x: Double, _: Double ->
                2 * x
            }
        }"
    )
}