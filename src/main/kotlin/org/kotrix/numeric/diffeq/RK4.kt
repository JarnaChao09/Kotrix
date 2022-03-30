package org.kotrix.numeric.diffeq

private fun RK4(
    initialStart: Double,
    initialValue: Double,
    steps: Int,
    delta: Double,
    firstDerivative: (Double, Double) -> Double,
): Double {
    var x = initialStart
    var y = initialValue

    for(i in 0 until steps) {
        val k1 = delta * firstDerivative(x, y)
        val k2 = delta * firstDerivative(x + 0.5 * delta, y + 0.5 * k1)
        val k3 = delta * firstDerivative(x + 0.5 * delta, y + 0.5 * k2)
        val k4 = delta * firstDerivative(x + delta, y + k3)
        x += delta
        y += (k1 + 2 * k2 + 2 * k3 + k4) / 6.0
    }

    return y
}

private fun RK4SecondOrder(
    initialStart: Double,
    initialValue: Double,
    initialDerivative: Double,
    steps: Int,
    delta: Double,
    secondDerivative: (Double, Double, Double) -> Double,
): Double {
    var x = initialStart
    var y = initialValue
    var yPrime = initialDerivative

    for(i in 0 until steps) {
        val j1 = delta * secondDerivative(x, y, yPrime)
        val k1 = delta * yPrime
        val j2 = delta * secondDerivative(x + 0.5 * delta, y + 0.5 * k1, yPrime + 0.5 * j1)
        val k2 = delta * (yPrime + 0.5 * j1)
        val j3 = delta * secondDerivative(x + 0.5 * delta, y + 0.5 * k2, yPrime + 0.5 * j2)
        val k3 = delta * (yPrime + 0.5 * j2)
        val j4 = delta * secondDerivative(x + delta, y + k3, yPrime + j3)
        val k4 = delta * (yPrime + j3)
        x += delta
        yPrime += (j1 + 2 * j2 + 2 * j3 + j4) / 6.0
        y += (k1 + 2 * k2 + 2 * k3 + k4) / 6.0
    }

    return y
}

fun rungeKutta4(
    initialStart: Double,
    initialValue: Double,
    toFind: Double,
    delta: Double = 1e-2,
    firstDerivative: (Double, Double) -> Double,
): Double {
    return RK4(
        initialStart,
        initialValue,
        kotlin.math.ceil(kotlin.math.abs(toFind - initialStart) / delta).toInt(),
        if(initialStart > toFind) -delta else delta,
        firstDerivative
    )
}

fun rungeKutta4(
    initialStart: Double,
    initialValue: Double,
    initialDerivative: Double,
    toFind: Double,
    delta: Double = 1e-2,
    secondDerivative: (Double, Double, Double) -> Double,
): Double {
    return RK4SecondOrder(
        initialStart,
        initialValue,
        initialDerivative,
        kotlin.math.ceil(kotlin.math.abs(toFind - initialStart) / delta).toInt(),
        if(initialStart > toFind) -delta else delta,
        secondDerivative,
    )
}