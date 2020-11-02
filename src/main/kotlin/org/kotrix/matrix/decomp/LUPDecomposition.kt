package org.kotrix.matrix.decomp

import org.kotrix.matrix.DoubleMatrix
import org.kotrix.matrix.IntMatrix
import org.kotrix.matrix.Matrix
import org.kotrix.matrix.NumberMatrix
import org.kotrix.utils.by
import org.kotrix.vector.DoubleVector

import kotlin.math.min
import kotlin.math.abs

class LUPDecomposition(matrix: DoubleMatrix) {
    constructor(matrix: NumberMatrix<*>): this(matrix.toDoubleMatrix())

    val lu: Array<DoubleArray> = matrix.doubleArray

    val rowCount: Int = matrix.size.x

    val colCount: Int = matrix.size.y

    val pivots: IntArray = IntArray(rowCount) { i -> i }

    var pivotSign: Int = 1

    val luColJ: DoubleArray = DoubleArray(rowCount)

    init {
        for (j in 0 until this.colCount) {
            for (i in 0 until this.rowCount) {
                this.luColJ[i] = this.lu[i][j]
            }

            for (i in 0 until this.rowCount) {
                val luRowI = this.lu[i]

                val kmax = min(i, j)
                var s = 0.0
                for (k in 0 until kmax) {
                    s += luRowI[k] * this.luColJ[k]
                }

                luColJ[i] -= s
                luRowI[j] = luColJ[i]
            }

            var p = j
            for (i in (j+1) until this.rowCount) {
                if (abs(luColJ[i]) > abs(luColJ[p])) {
                    p = i
                }
            }
            if (p != j) {
                for (k in 0 until colCount) {
                    val t = this.lu[p][k]; this.lu[p][k] = this.lu[j][k]; this.lu[j][k] = t
                }
                val k = this.pivots[p]; this.pivots[p] = this.pivots[j]; this.pivots[j] = k
                pivotSign = -pivotSign
            }

            if (j < rowCount && this.lu[j][j] != 0.0) {
                for (i in (j+1) until this.rowCount) {
                    this.lu[i][j] = this.lu[i][j] / this.lu[j][j]
                }
            }
        }
    }

    fun solve(b: DoubleMatrix): DoubleMatrix {
        if (this.singular)
            throw Matrix.Error.NotRegular("Matrix is Singular")

        if (b.rowLength != this.rowCount)
            throw Matrix.Error.DimensionMisMatch()

        val nx = b.colLength
        val m = this.pivots.map { i -> b[i].toList().toDoubleArray() }

        // Solve L * Y = P * b
        for (k in 0 until this.colCount) {
            for (i in (k+1) until this.colCount) {
                for (j in 0 until nx) {
                    m[i][j] -= m[k][j] * this.lu[i][k]
                }
            }
        }

        // Solve U * m = Y
        for (k in (this.colCount - 1) downTo 0) {
            for (j in 0 until nx) {
                m[k][j] = m[k][j] / this.lu[k][k]
            }
            for (i in 0 until k) {
                for (j in 0 until nx) {
                    m[i][j] -= m[k][j] * this.lu[i][k]
                }
            }
        }
        return DoubleMatrix(nx by nx) { r, c -> m[r][c] }
    }

    fun solve(_b: DoubleVector): DoubleVector {
        val b = _b.toArray()

        if (b.size != this.rowCount) {
            throw Matrix.Error.DimensionMisMatch()
        }

        val m = this.pivots.map { index -> b[index] }.toMutableList()

        // Solve L * Y = P * b
        for (k in 0 until this.colCount) {
            for (i in (k+1) until this.colCount) {
                m[i] -= m[k] * this.lu[i][k]
            }
        }

        // Solve U * m = Y
        for (k in (this.colCount - 1) downTo 0) {
            m[k] = m[k] / this.lu[k][k]
            for (i in 0 until k) {
                m[i] -= m[k] * this.lu[i][k]
            }
        }
        return DoubleVector(m.size) { i -> m[i] }
    }

    val l
        get() = this.l()

    val u
        get() = this.u()

    val p
        get() = this.p()

    operator fun component1(): DoubleMatrix = l

    operator fun component2(): DoubleMatrix = u

    operator fun component3(): DoubleMatrix = p

    private fun l(): DoubleMatrix =
        DoubleMatrix(this.rowCount by min(this.rowCount, this.colCount)) { r, c ->
            when {
                r > c -> this.lu[r][c]
                r == c -> 1.0
                else -> 0.0
            }
        }

    private fun u(): DoubleMatrix =
        DoubleMatrix(min(this.rowCount, this.colCount) by this.colCount) { r, c ->
            when {
                r <= c -> this.lu[r][c]
                else -> 0.0
            }
        }

    private fun p(): DoubleMatrix {
        val rows = Array(this.rowCount) { IntArray(this.rowCount) }
        this.pivots.forEachIndexed { p, i -> rows[i][p] = 1 }
        return DoubleMatrix(rows.size by rows[0].size) { r, c -> rows[r][c].toDouble() }
    }

    fun toArray(): Array<DoubleMatrix> =
        arrayOf(l, u ,p)

    val singular: Boolean
        get() {
            for (j in 0 until this.colCount) {
                if (this.lu[j][j] == 0.0) {
                    return true
                }
            }
            return false
        }

    val det: Double
        get() {
            if (this.rowCount != this.colCount)
                throw Matrix.Error.DimensionMisMatch()
            var d = this.pivotSign.toDouble()
            for (i in 0 until this.colCount) {
                d *= this.lu[i][i]
            }
            return d
        }
}