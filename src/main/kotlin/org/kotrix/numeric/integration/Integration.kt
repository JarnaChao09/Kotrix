package org.kotrix.numeric.integration

import kotlin.math.absoluteValue


/*
 @file:PROTOTYPE
 */

object RiemannIntegration {
    fun leftSum(from: Number, to: Number, numOfRec: UInt = 100U, function: (Double) -> Double): Double {
        val f = from.toDouble()
        val t = to.toDouble()
        var ret = 0.0
        val increment = ((t - f) / numOfRec.toDouble())
        val x: (Int) -> Double = {
            f + it * increment
        }
        for (i in 0 until numOfRec.toInt()) {
            ret += function(x(i)) * increment
        }
        return ret
    }

    fun rightSum(from: Number, to: Number, numOfRec: UInt = 100U, function: (Double) -> Double): Double {
        val f = from.toDouble()
        val t = to.toDouble()
        var ret = 0.0
        val increment = ((t - f) / numOfRec.toDouble())
        val x: (Int) -> Double = {
            f + it * increment
        }
        for (i in 0 until numOfRec.toInt()) {
            ret += function(x(i + 1)) * increment
        }
        return ret
    }

    fun midPointSum(from: Number, to: Number, numOfRec: UInt = 100U, function: (Double) -> Double): Double {
        val f = from.toDouble()
        val t = to.toDouble()
        var ret = 0.0
        val increment = ((t - f) / numOfRec.toDouble())
        val x: (Int) -> Double = {
            f + it * increment + increment / 2
        }
        for (i in 0 until numOfRec.toInt()) {
            ret += function(x(i)) * increment
        }
        return ret
    }
}

object NewtonCoatesIntegration {
    fun trapezoidalSum(from: Number, to: Number, numOfRec: UInt = 100U, function: (Double) -> Double): Double {
        val f = from.toDouble()
        val t = to.toDouble()
        val increment = ((t - f) / numOfRec.toDouble())
        val x: (Int) -> Double = {
            f + it * increment
        }
        val outerMultiple = ((t - f) / (2 * numOfRec.toDouble()))
        var ret = function(x(0)) * outerMultiple
        for (i in 1 until numOfRec.toInt()) {
            // outer multiple of (t - f) / 2n cancels out with 2 * f(x_i) so we just use the value in increment instead
            ret += function(x(i)) * increment
        }
        ret += function(x(numOfRec.toInt())) * outerMultiple
        return ret
    }

    fun simpsonsMethod(from: Number, to: Number, steps: UInt = 10000U, function: (Double) -> Double): Double {
        val f = from.toDouble()
        val t = to.toDouble()
        val h = ((t - f) / steps.toDouble())
        val x: (Int) -> Double = {
            f + it * h
        }
        val func: (Double) -> Double = {
            (function(it) + 4 * function(it + h / 2.0) + function(it + h)) / 6.0
        }
        var ret = 0.0
        for (i in 0 until steps.toInt()) {
            ret += func(x(i)) * h
        }
        return ret
    }

    private fun quadSimpsonsMemory(
        a: Double,
        fa: Double,
        b: Double,
        fb: Double,
        function: (Double) -> Double
    ): Triple<Double, Double, Double> {
        val m = (a + b) / 2.0
        val fm = function(m)
        return Triple(m, fm, (b - a).absoluteValue / 6 * (fa + 4 * fm + fb))
    }

    private fun quadASR(
        a: Double,
        fa: Double,
        b: Double,
        fb: Double,
        eps: Double,
        whole: Double,
        m: Double,
        fm: Double,
        f: (Double) -> Double
    ): Double {
        val (lm, flm, left) = quadSimpsonsMemory(a, fa, m, fm, f)
        val (rm, frm, right) = quadSimpsonsMemory(m, fm, b, fb, f)

        val delta = left + right - whole

        return if (delta.absoluteValue <= 15 * eps) {
            left + right + delta / 15
        } else {
            quadASR(a, fa, m, fm, eps / 2.0, left, lm, flm, f) + quadASR(m, fm, b, fb, eps / 2.0, right, rm, frm, f)
        }
    }

    fun adaptiveSimpsonMethod(
        from: Number,
        to: Number,
        eps: Double = 1E-15,
        function: (Double) -> Double
    ): Double {
        val a = from.toDouble()
        val b = to.toDouble()
        val (fa, fb) = function(a) to function(b)
        val (m, fm, whole) = quadSimpsonsMemory(a, fa, b, fb, function)

        return quadASR(a, fa, b, fb, eps, whole, m, fm, function)
    }
}