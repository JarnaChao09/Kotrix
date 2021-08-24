package org.kotrix.symbolic.funAST

/*
 @file:PROTOTYPE
 */

object GaussLegendre {
    fun integrate(from: Number, to: Number, degree: Number = 7, function: (Double) -> Double): Double {
        val a = from.toDouble()
        val b = to.toDouble()

        val (p, q) = (b minusPlus a) / 2.0

        val (roots, weights) = Legendre.evaluate(degree)

        var sum = 0.0

        for (i in 0 until degree.toInt()) {
            sum += weights[i] * function(p * roots[i] + q)
        }

        return p * sum
    }
}

private infix fun Double.minusPlus(rhs: Double): Pair<Double, Double> = (this - rhs) to (this + rhs)

private operator fun Pair<Double, Double>.div(rhs: Double): Pair<Double, Double> = (first / rhs) to (second / rhs)