package org.kotrix.numeric.diffeq

private fun eulerStepFirstOrder(
    initialStart: Double,
    initialValue: Double,
    steps: Int,
    delta: Double,
    firstDerivative: (Double, Double) -> Double,
): Double {
    var x = initialStart
    var y = initialValue

    for (i in 0 until steps) {
        val currentDerivative = firstDerivative(x, y)
        x += delta
        y += currentDerivative * delta
    }

    return y
}

private fun eulerStepSecondOrder(
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
        val currentSecondDerivative = secondDerivative(x, y, yPrime)
        x += delta
        yPrime += currentSecondDerivative * delta
        y += yPrime * delta
    }

    return y
}

/**
 * Approximates the solution to a given differential equation (order 1)
 *
 * given dy/dx and initial conditions of y_0 = y(x_0)
 * this function returns y(x_n)
 *
 * @param initialStart x_0, initial start value
 * @param initialValue y_0, initial value of the equation
 * @param toFind x_n, the x value we are trying to approximate the y value for
 * @param delta how much to increment x by
 * @param firstDerivative dy/dx, the differential equation, parameters in the order of (x, y)
 * @return y(x_n)
 */
fun euler(
    initialStart: Double,
    initialValue: Double,
    toFind: Double,
    delta: Double = 1e-5,
    firstDerivative: (Double, Double) -> Double,
): Double {
    return eulerStepFirstOrder(
        initialStart,
        initialValue,
        kotlin.math.ceil(kotlin.math.abs(toFind - initialStart) / delta).toInt(),
        if(initialStart > toFind) -delta else delta,
        firstDerivative,
    )
}

/**
 * Approximates the solution to a given differential equation (order 2)
 *
 * given d(dy/dx)/dx and initial conditions of y_0 = y(x_0) and y'_0 = y'(x_0)
 * this function returns y(x_n)
 *
 * @param initialStart x_0, initial start value
 * @param initialValue y_0, initial value of the equation
 * @param initialDerivative y'_0, initial derivative of the equation
 * @param toFind x_n, the x value we are trying to approximate the y value for
 * @param delta how much to increment x by
 * @param secondDerivative d(dy/dx)/dx, the differential equation, parameters in the order of (x, y, y')
 * @return y(x_n)
 */
fun euler(
    initialStart: Double,
    initialValue: Double,
    initialDerivative: Double,
    toFind: Double,
    delta: Double = 1e-5,
    secondDerivative: (Double, Double, Double) -> Double,
): Double {
    return eulerStepSecondOrder(
        initialStart,
        initialValue,
        initialDerivative,
        kotlin.math.ceil(kotlin.math.abs(toFind - initialStart) / delta).toInt(),
        if(initialStart > toFind) -delta else delta,
        secondDerivative,
    )
}