package org.kotrix.matrix

import org.kotrix.utils.by
import kotlin.math.abs

/**
 * @param current the current resultant matrix in the series
 * @param previous the previous resultant matrix in the series
 *
 * @return true if the matrices have converged based on a tolerance
 */
private fun checkForConvergence(current: DoubleMatrix, previous: DoubleMatrix, tolerance: Double = 1e-15): Boolean {
    var ret = true
    // change to addIndexed (if added)
    for (r in 0 until current.rowLength) {
        for (c in 0 until current.colLength) {
            ret = ret && abs(current[r, c] - previous[r, c]) < tolerance
        }
    }

    return ret
}

operator fun DoubleMatrix.div(n: Double): DoubleMatrix =
    DoubleMatrix(this.size) { r, c ->
        this[r, c] / n
    }

fun DoubleMatrix.pow(rhs: Int): DoubleMatrix {
    return if (rhs == 0) {
        DoubleMatrix.identity(this.rowLength)
    } else {
        var ret = DoubleMatrix(this)
        for (i in 0 until rhs - 1) {
            ret = this matMult ret
        }
        ret
    }
}

fun expm(m: DoubleMatrix, tolerance: Double = 1e-15): DoubleMatrix {
    fun DoubleMatrix.correctZerosForTolerance(tol: Double) {
        for (r in 0 until this.rowLength) {
            for (c in 0 until this.colLength) {
                this[r, c] = if(abs(this[r, c]) < tol) 0.0 else this[r, c]
            }
        }
    }
    require(m.isSquare()) {
        "Matrix exponentiation only operates on square matrices"
    }

    var curr = DoubleMatrix.zeros(m.size)
    var prev = DoubleMatrix.zeros(m.size)
    val factorial = mutableListOf<Double>()

    var i = 0
    while (i == 0 || !checkForConvergence(curr, prev, tolerance)) {
        factorial += if (i == 0) 1.0 else i.toDouble() * factorial[i - 1]

        prev = curr
        curr = curr + (m.pow(i) / factorial[i])

        curr.correctZerosForTolerance(tolerance)

        i++
    }

    return curr
}

fun main() {
    val t = DoubleMatrix.of(2 by 2, 0.0, -kotlin.math.PI, kotlin.math.PI, 0.0)
    val t1 = DoubleMatrix.of(3 by 3, 3.0, 1.0, 4.0, 1.0, 5.0, 9.0, 2.0, 6.0, 5.0)

    println(expm(t))
    println(expm(t1))
}