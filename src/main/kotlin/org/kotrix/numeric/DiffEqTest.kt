package org.kotrix.numeric

import org.kotrix.numeric.diffeq.euler
import org.kotrix.numeric.diffeq.rungeKutta4

fun main() {
    val x00 = 0.0
    val x01 = 5.0
    val y00 = 0.0
    val y01 = 25.0
    val xn = 3.0

    val eulerEstimation = euler(x00, y00, xn) { x: Double, _: Double ->
        2 * x
    }
    val rungeKuttaEstimation = rungeKutta4(x00, y00, xn) { x: Double, _: Double ->
        2 * x
    }
    println(
        "Euler Approximation of dy/dx = 2 * x, given initial conditions of ($x00, $y00), y($xn) = $eulerEstimation, error = ${
            kotlin.math.abs(
                eulerEstimation - 9.0
            )
        }"
    )

    println(
        "RK4 Approximation of dy/dx = 2 * x, given initial conditions of ($x00, $y00), y($xn) = $rungeKuttaEstimation, error = ${
            kotlin.math.abs(
                rungeKuttaEstimation - 9.0
            )
        }"
    )

    println("Exact: ${9.0}\n")

//    println(
//        "Approximation of dy/dx = 2 * x, given initial conditions of ($x01, $y01), y($xn) = ${
//            euler(x01, y01, xn) { x: Double, _: Double ->
//                2 * x
//            }
//        }"
//    )
//
    val euler2Estimation = euler(0.0, 1.0, 0.0, 1.0) { _: Double, y: Double, yPrime: Double ->
        -2 * yPrime - 2 * y
    }

    val rungeKutta2Estimation = rungeKutta4(0.0, 1.0, 0.0, 1.0) { _: Double, y: Double, yPrime: Double ->
        -2 * yPrime - 2 * y
    }
    //          0.5083259859995251390727160162587141299640797003923103676087958856
    val exact = 0.508325985999525
    println(
        "Euler Approximation of d(dy/dx)/dx = -2y' - 2y, given initial conditions of y(0) = 1 and y'(0) = 0, y(1) = $euler2Estimation, error = ${
            kotlin.math.abs(
                euler2Estimation - exact
            )
        }"
    )
    println(
        "RK4 Approximation of d(dy/dx)/dx = -2y' - 2y, given initial conditions of y(0) = 1 and y'(0) = 0, y(1) = $rungeKutta2Estimation, error = ${
            kotlin.math.abs(
                rungeKutta2Estimation - exact
            )
        }"
    )
    println("Exact: $exact")
}