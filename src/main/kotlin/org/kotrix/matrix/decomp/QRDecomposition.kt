package org.kotrix.matrix.decomp

import org.kotrix.matrix.DoubleMatrix
import org.kotrix.utils.by
import org.kotrix.utils.sliceTo
import org.kotrix.vector.dot

class QRDecomposition(matrix: DoubleMatrix) {
    public val Q: DoubleMatrix = DoubleMatrix(matrix.shape.x by matrix.shape.x)

    public val R: DoubleMatrix = DoubleMatrix(matrix.shape)

    init {
        for (i in matrix.rowRange) {
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

    operator fun component1(): DoubleMatrix = Q

    operator fun component2(): DoubleMatrix = R
}