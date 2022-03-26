package org.kotrix.numeric.diffeq

private fun eulerStep(
    initialStart: Double,
    initialValue: Double,
    steps: Int,
    delta: Double,
    derivative: (Double, Double) -> Double,
): Double {
    var x = initialStart
    var y = initialValue

    for (i in 0 until steps) {
        val currentDerivative = derivative(x, y)
        x += delta
        y += currentDerivative * delta
    }

    return y
}

/**
 * Approximates the solution to a given differential equation
 *
 * given dy/dx and initial conditions of y_0 = y(x_0)
 * this function returns y(x_n)
 *
 * @param initialStart x_0, initial start value
 * @param initialValue y_0, initial value of the equation
 * @param toFind x_n, the x value we are trying to approximate the y value for
 * @param delta how much to increment x by
 * @param derivative dy/dx, the differential equation
 * @return
 */
fun euler(
    initialStart: Double,
    initialValue: Double,
    toFind: Double,
    delta: Double = 1e-5,
    derivative: (Double, Double) -> Double
): Double {
    return eulerStep(
        initialStart,
        initialValue,
        kotlin.math.ceil((toFind - initialStart) / delta).toInt(),
        delta,
        derivative,
    )
}