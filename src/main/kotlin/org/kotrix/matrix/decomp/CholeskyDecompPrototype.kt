package org.kotrix.matrix.decomp

import org.kotrix.matrix.DoubleMatrix
import org.kotrix.utils.by
import kotlin.math.max
import kotlin.math.sqrt

fun List<List<Double>>.transpose(): List<List<Double>> {
    val ret = MutableList(this.size) { MutableList(this.size) { 0.0 } }
    for (i in this.indices) {
        for (j in this.indices) {
            ret[i][j] = this[j][i]
        }
    }
    return ret.toList()
}

operator fun String.times(other: Int): String = this.run {
    var ret = ""
    for(i in 0 until other) {
        ret += this
    }
    ret
}

fun List<List<Double>>.matrixString(): String {
    val retString = List(size = 0) { MutableList(0) { "" } }.toMutableList()
    var maxLength = 0
    this.forEach { it.forEach { i -> maxLength = max(maxLength, i.toString().length) } }
    for (r in this) {
        val dummy = MutableList(0) { "" }
        for (c in r) {
            dummy.add((" " * (maxLength - c.toString().length) + c.toString()))
        }
        retString += dummy
    }
    return retString.joinToString(prefix = "[", postfix = "]", separator = ",\n ")
}

fun List<List<Double>>.LLTDecomp(): Pair<List<List<Double>>, List<List<Double>>> {
    val lower = MutableList(this.size) { MutableList(this.size) { 0.0 } }
    for (i in this.indices) {
        for (j in this.indices) {
            var sum = 0.0
            if (j == i) {
                for (k in 0 until j) {
                    sum += lower[j][k] * lower[j][k]
                }
                lower[i][j] = sqrt(this[j][j] - sum)
            } else {
                for (k in 0 until j) {
                    sum += lower[i][k] * lower[j][k]
                }
                if (lower[j][j] > 0) {
                    lower[i][j] = (this[i][j] - sum) / lower[j][j]
                }
            }
        }
    }
    return lower to lower.transpose()
}

fun main() {
    val llt_test = listOf(
            listOf(4.0, 12.0, -16.0),
            listOf(12.0, 37.0, -43.0),
            listOf(-16.0, -43.0, 98.0),
    )
    val (L, L_T) = llt_test.LLTDecomp()
    println("${L.matrixString()}\n${L_T.matrixString()}")

    for (i in 0..1) println()

    val matrix = DoubleMatrix.of(3 by 3, 4,12,-16,12,37,-43,-16,-43,98)
    val (L1, L_T1) = CholeskyDecomposition(matrix, CholeskyDecomposition.Algorithm.CHOLESKY_BANACHIEWICZ)
    println("$L1\n$L_T1")
}
