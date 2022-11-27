package org.kotrix

import kotlin.system.measureTimeMillis
import kotlin.math.absoluteValue

/*
https://users.wpi.edu/~walker/MA510/HANDOUTS/w.gander,w.gautschi,Adaptive_Quadrature,BIT_40,2000,84-101.pdf
chapter 3 adaptive simpsons
 */
fun integralScan(a: Double, b: Double, eps: Double, f: (Double) -> Double): Double /*List<Double>*/ {
    val m = (a + b) / 2
    val fa = f(a)
    val fm = f(m)
    val fb = f(b)

    var i = (b - a) / 8 * (
            fa + fm + fb +
                    f(a + 0.9501 * (b - a)) +
                    f(a + 0.2311 * (b - a)) +
                    f(a + 0.6068 * (b - a)) +
                    f(a + 0.4860 * (b - a)) +
                    f(a + 0.8913 * (b - a))
            )
    if (i == 0.0) {
        i = b - a
    }
    i *= eps / Math.ulp(1.0)

//    val values = mutableListOf(0.0)
//    val sums = mutableListOf(0.0)
    var sum = 0.0

    fun helper(a: Double, m: Double, b: Double, fa: Double, fm: Double, fb: Double) {
        val h = (b - a) / 4
        val ml = a + h
        val mr = b - h
        val fml = f(ml)
        val fmr = f(mr)
        var i1 = h / 1.5 * (fa + 4 * fm + fb)
        val i2 = h / 3 * (fa + 4 * (fml + fmr) + 2 * fm + fb)
        i1 = (16 * i2 - i1) / 15
        if (i + (i1 - i2) == i || m <= a || b <= m) {
//             values.add(b)
//             sums.add(sums.last() + i1)
            sum += i1
        } else {
            helper(a, ml, m, fa, fml, fm)
            helper(m, mr, b, fm, fmr, fb)
        }
    }

    helper(a, m, b, fa, fm, fb)

    return sum
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


fun main() {
    println("Testing")
//     var res1: List<Double>
    var res1: Double
    var res2: Double
    var totalDuration1 = 0.0
    var totalDuration2 = 0.0
    var totalError1    = 0.0
    var totalError2    = 0.0
    var totalCalls1    = 0
    var totalCalls2    = 0

    val f = { x: Double ->
        kotlin.math.sqrt(x)
//         kotlin.math.sin(x*x)
    }
    val eps = 1e-8
    val a = 0.0
    val b = 1.0
//     val actualAns = -0.022318845283755027
    val actualAns = 2.0 / 3.0
    repeat(100) {
        totalDuration1 += measureTimeMillis {
            res1 = integralScan(a, b, eps) { x -> totalCalls1++; f(x) }
        }
        totalDuration2 += measureTimeMillis {
            res2 = adaptiveSimpsonMethod(a, b, eps=eps) { x -> totalCalls2++; f(x) }
        }

        totalError1 += (res1 - (actualAns)).absoluteValue
        totalError2 += (res2 - (actualAns)).absoluteValue
    }
    println("total(ms): ${totalDuration1}, avg(ms): ${totalDuration1 / 100}, avg err: ${totalError1 / 100} avg calls: ${totalCalls1 / 100}")
    println("total(ms): ${totalDuration2}, avg(ms): ${totalDuration2 / 100}, avg err: ${totalError2 / 100} avg calls: ${totalCalls2 / 100}")
}