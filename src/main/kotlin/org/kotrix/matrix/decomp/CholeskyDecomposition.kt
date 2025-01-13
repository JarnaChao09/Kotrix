package org.kotrix.matrix.decomp

import org.kotrix.matrix.DoubleMatrix
import org.kotrix.matrix.NumberMatrix
import kotlin.math.sqrt

class CholeskyDecomposition(matrix: DoubleMatrix) {
    constructor(mat: NumberMatrix<*>): this(mat.toDoubleMatrix())

    private val lower = DoubleMatrix(matrix.shape)

    init {
        // Cholesky–Banachiewicz algorithm
        for (i in matrix.rowRange) {
            for (j in 0..i) {
                val sum = (0..<j).sumOf {
                    lower[i, it] * lower[j, it]
                }

                lower[i, j] = if (i == j) {
                    sqrt(matrix[i, j] - sum)
                } else {
                    1.0 / lower[j, j] * (matrix[i, j] - sum)
                }
            }
        }
    }

    operator fun component1(): DoubleMatrix = lower

    operator fun component2(): DoubleMatrix = lower.t
}