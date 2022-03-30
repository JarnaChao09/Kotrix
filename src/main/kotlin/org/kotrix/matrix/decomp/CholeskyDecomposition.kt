package org.kotrix.matrix.decomp

import org.kotrix.matrix.DoubleMatrix
import org.kotrix.matrix.NumberMatrix
import kotlin.math.sqrt

class CholeskyDecomposition(matrix: DoubleMatrix) {
    constructor(mat: NumberMatrix<*>): this(mat.toDoubleMatrix())

    private val lower = DoubleMatrix(matrix.shape)

    init {
        for (i in matrix.rowRange) {
            for (j in matrix.colRange) {
                var sum = 0.0
                if (i == j) {
                    for (k in 0 until j) {
                        sum += lower[j,k] * lower[j,k]
                    }
                    lower[i,j] = sqrt(matrix[j,j] - sum)
                } else {
                    for (k in 0 until j) {
                        sum += lower[i,k] * lower[j,k]
                    }
                    if (lower[j,j] > 0) {
                        lower[i,j] = (matrix[i,j] - sum) / lower[j,j]
                    }
                }
            }
        }
    }

    operator fun component1(): DoubleMatrix = lower

    operator fun component2(): DoubleMatrix = lower.t
}