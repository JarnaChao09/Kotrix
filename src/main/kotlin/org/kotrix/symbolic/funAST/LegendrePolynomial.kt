package org.kotrix.symbolic.funAST

import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.cos

/*
 @file:PROTOTYPE
 */

object Legendre {
    private fun p(n: UInt, x: Double): Double = when (n) {
        0U -> {
            1.0
        }
        1U -> {
            x
        }
        else -> {
            (2 * n.toDouble() - 1) * x * polynomial(
                (n - 1U).toInt(),
                x
            ) - (n.toDouble() - 1) * polynomial((n - 2U).toInt(), x)
        }
    }

    private fun dp(n: UInt, x: Double): Double = n.toDouble() * (x * polynomial(n.toInt(), x) - polynomial((n - 1U).toInt(), x)) / (x * x - 1)

    fun polynomial(n: Number, x: Number): Double =
        if (x.toDouble() == 1.0) 1.0 else p(
            n.toInt().toUInt(),
            x.toDouble()
        ) / if (n.toDouble() == 0.0) 1.0 else n.toDouble()

    fun derivativePolynomial(n: Number, x: Number): Double = dp(n.toInt().toUInt(), x.toDouble())

    private fun solveRootsAndWeights(degree: UInt): Pair<DoubleArray, DoubleArray> {
        val n = degree.toInt()
        val roots = DoubleArray(n)
        val weights = DoubleArray(n)

        for (i in 1..n) {
            var x0 = cos(PI * (i - 0.25) / (n + 0.5))
            var x1: Double
            do {
                x1 = x0
                x0 -= polynomial(n, x0) / derivativePolynomial(n, x0)
            } while (x0 != x1)

            x1 = derivativePolynomial(n, x0)
            roots[i - 1] = x0
            weights[i - 1] = 2.0 / ((1 - x0 * x0) * x1 * x1)
        }

        return roots to weights
    }

    fun evaluate(n: Number) = solveRootsAndWeights(n.toInt().toUInt())
}