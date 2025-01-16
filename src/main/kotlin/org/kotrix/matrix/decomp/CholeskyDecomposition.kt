package org.kotrix.matrix.decomp

import org.kotrix.matrix.DoubleMatrix
import org.kotrix.matrix.NumberMatrix
import kotlin.math.sqrt

class CholeskyDecomposition(matrix: DoubleMatrix, type: Algorithm) {
    enum class Algorithm {
        CHOLESKY_BANACHIEWICZ,
        CHOLESKY_CROUT,
    }
    constructor(mat: NumberMatrix<*>, type: Algorithm): this(mat.toDoubleMatrix(), type)

    private val lower = DoubleMatrix(matrix.shape)

    init {
        when (type) {
            Algorithm.CHOLESKY_BANACHIEWICZ -> {
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
            Algorithm.CHOLESKY_CROUT -> {
                // Cholesky-Crout algorithm
                for (j in matrix.colRange) {
                    val sum = (0..<j).sumOf {
                        lower[j, it] * lower[j, it]
                    }

                    lower[j, j] = sqrt(matrix[j, j] - sum)

                    for (i in (j+1)..<matrix.rowLength) {
                        val sum = (0..<j).sumOf {
                            lower[i, it] * lower[j, it]
                        }

                        lower[i, j] = 1.0 / lower[j, j] * (matrix[i, j] - sum)
                    }
                }
            }
        }
    }

    operator fun component1(): DoubleMatrix = lower

    operator fun component2(): DoubleMatrix = lower.t
}