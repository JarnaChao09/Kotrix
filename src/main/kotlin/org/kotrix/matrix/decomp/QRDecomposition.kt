package org.kotrix.matrix.decomp

import org.kotrix.matrix.DoubleMatrix
import org.kotrix.utils.by
import org.kotrix.utils.sliceTo
import org.kotrix.vector.dot

class QRDecomposition(matrix: DoubleMatrix, type: Algorithm) {
    enum class Algorithm {
        GRAM_SCHMIDT,
        MODIFIED_GRAM_SCHMIDT,
    }
    val Q: DoubleMatrix = DoubleMatrix(matrix.shape.x by matrix.shape.x)

    val R: DoubleMatrix = DoubleMatrix(matrix.shape)

    init {
        when (type) {
            Algorithm.GRAM_SCHMIDT -> {
                for (i in matrix.rowRange) {
                    // u = a_i
                    val u = matrix[0 sliceTo matrix.rowLength, i]

                    for (j in 0..<i) {
                        u -= Q[0 sliceTo Q.rowLength, j] * (matrix[0 sliceTo matrix.rowLength, i] dot Q[0 sliceTo Q.rowLength, j])
                    }
                    Q[0 sliceTo matrix.rowLength, i] = u / u.magnitude
                }

                for (i in matrix.rowRange) {
                    for (j in i..<matrix.colLength) {
                        R[i, j] = matrix[0 sliceTo matrix.rowLength, j] dot Q[0 sliceTo Q.rowLength, i]
                    }
                }
            }
            Algorithm.MODIFIED_GRAM_SCHMIDT -> {
                val u = matrix.toDoubleMatrix()

                // note: algorithm reference uses `n` for both loops
                // however, reading deeper into the algorithm
                // it would follow that the outer loop is over the row range
                // and the inner loop is over the column range
                // and the reference just assumed n x n matrices

                for (j in matrix.rowRange) {
                    R[j, j] = u[0 sliceTo u.rowLength, j].magnitude
                    Q[0 sliceTo Q.rowLength, j] = u[0 sliceTo u.rowLength, j] / R[j, j]

                    for (k in (j+1)..<matrix.colLength) {
                        R[j, k] = Q[0 sliceTo Q.rowLength, j] dot u[0 sliceTo u.rowLength, k]
                        // note: Ambiguity between assign operator candidates when using -=:
                        // fun minusAssign(other: NumberVectors<Double>): Unit
                        // fun set(indexRSlice: Slice, indexC: Int, value: VectorImplOld<Double>): Unit
                        u[0 sliceTo u.rowLength, k] = u[0 sliceTo u.rowLength, k] - (Q[0 sliceTo Q.rowLength, j] * R[j, k])
                    }
                }
            }
        }
    }

    operator fun component1(): DoubleMatrix = Q

    operator fun component2(): DoubleMatrix = R
}