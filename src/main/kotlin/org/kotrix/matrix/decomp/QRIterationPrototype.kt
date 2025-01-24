package org.kotrix.matrix.decomp

import org.kotrix.matrix.DoubleMatrix
import org.kotrix.matrix.matMult
import org.kotrix.utils.by
import org.kotrix.vector.DoubleVector
import kotlin.math.absoluteValue

fun algorithm(matrix: DoubleMatrix, tolerance: Double = 1e-12, maxIteration: Int = 1000): DoubleVector {
    require(matrix.shape.x == matrix.shape.y) {
        "Input matrix must be square"
    }

    var curr = matrix.toDoubleMatrix()
    var prev: DoubleMatrix

    var diff = Double.POSITIVE_INFINITY
    var i = 0

    while (i < maxIteration && diff > tolerance) {
        prev = curr.toDoubleMatrix()

        val (Q, R) = QRDecomposition(prev, QRDecomposition.Algorithm.MODIFIED_GRAM_SCHMIDT)

        curr = R matMult Q

        diff = (curr - prev).maxOf {
            it.absoluteValue
        }
        i++
    }

    println(curr)

    return DoubleVector(matrix.shape.x) {
        curr[it, it]
    } as DoubleVector
}

fun main() {
    val matrix1 = DoubleMatrix.of(2 by 2,
        4.0,  1.0,
        2.0, -1.0
    )

    val eigenvalues1 = algorithm(matrix1)
    println(eigenvalues1)

    // todo: figure out how to use QR iteration to solve for complex eigenvalues
    val matrix2 = DoubleMatrix.of(6 by 6,
         7,  3,  4,  11, -9, -2,
        -6,  4, -5,   7,  1, 12,
        -1, -9,  2,   2,  9,  1,
        -8,  0, -1,   5,  0,  8,
        -4,  3, -5,   7,  2, 10,
         6,  1,  4, -11, -7, -1,
    )

    val eigenvalues2 = algorithm(matrix2)
    println(eigenvalues2)
}