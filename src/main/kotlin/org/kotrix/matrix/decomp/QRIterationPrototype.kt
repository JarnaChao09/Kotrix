package org.kotrix.matrix.decomp

import org.kotrix.complex.Complex
import org.kotrix.complex.toComplex
import org.kotrix.matrix.DoubleMatrix
import org.kotrix.matrix.matMult
import org.kotrix.utils.by
import kotlin.math.absoluteValue

fun eigenvalues2x2(matrix: DoubleMatrix): Pair<Complex, Complex> {
    require(matrix.shape == 2 by 2) {
        "eigenvalues2x2 only supports 2 x 2 matrices"
    }

    /*
        |---------------|
        | a - l     b   |
    det |               | = (a - l)(d - l) - bc = l^2 - (a + d)l + (ad - bc)
        |   c     d - l |                                  |           |
        |---------------|                                Trace    determinant

    let
        a' = 1
        b' = -(a + d)
        c' = ad - bc
    in the following quadratic formula

    -b' +- (b'^2 - 4a'c')^(1/2)   -(-(a + d)) +- ((-(a + d))^2 - 4(ad - bc))^(1/2)
    --------------------------- = ------------------------------------------------
                2a'                                       2

            Tr(A)   a + d
    let m = ----- = -----, p = det(A) = ad - bc
              2       2

    this allows for the following reduction

    -(-(a + d)) +- ((-(a + d))^2 - 4(ad - bc))^(1/2)   Tr(A) +- (Tr(A)^2 - 4det(A))^(1/2)
    ------------------------------------------------ = ----------------------------------
                            2                                         2

    Tr(A) +- (Tr(A)^2 - 4det(A))^(1/2)   Tr(A)    (Tr(A)^2 - 4det(A))^(1/2)         / Tr(A)^2 - 4p \ 1/2
    ---------------------------------- = ----- +- ------------------------- = m +- |  ------------  |
                   2                       2                  2                     \       4      /

                                       /                 \ 1/2
          / Tr(A)^2 - 4p \ 1/2        |   / Tr(A) \ 2     |
    m +- |  ------------  |    = m +- |  |  -----  | - p  |    = m +- (m^2 - p)^(1/2)
          \       4      /            |   \   2   /       |
                                       \                 /

    therefore
        l1 = m + (m^2 - p)^(1/2)
        l2 = m - (m^2 - p)^(1/2)
    */

    val m = (matrix.trace() / 2.0).toComplex()
    val p = matrix.determinant().toComplex()

    val sqrt = (m * m - p).sqrt

    return (m + sqrt) to (m - sqrt)
}

/**
 * Performs QR iteration to converge on the Real Schur Decomposition
 *
 * This is defined by the following iteration scheme:
 *
 * Let A_0 = A
 * A_k = Q_k * R_k where Q_k is an orthonormal matrix and R_k is an upper triangular matrix
 * A_(k+1) = R_k * Q_k = Q_k^-1 * Q_k * R_k * Q_k = Q_k^-1 A_k * Q_k = Q_k^T * A_k * Q_k
 *
 * Giving enough iteration steps (and the correct conditions), this algorithm will converge on the Real Schur
 * Decomposition of A = Q * R * Q^-1 where Q is an orthogonal matrix and R is a block upper triangular matrix
 */
fun realSchurDecomposition(matrix: DoubleMatrix, tolerance: Double = 1e-12, maxIteration: Int = 1000): Pair<DoubleMatrix, DoubleMatrix> {
    require(matrix.shape.x == matrix.shape.y) {
        "Input matrix must be square"
    }

    var curr = matrix.toDoubleMatrix()
    var prev: DoubleMatrix

    var q = DoubleMatrix.identity(matrix.shape.x)

    var diff = Double.POSITIVE_INFINITY
    var i = 0

    while (i < maxIteration && diff > tolerance) {
        prev = curr.toDoubleMatrix()

        val (Q, R) = QRDecomposition(prev, QRDecomposition.Algorithm.MODIFIED_GRAM_SCHMIDT)

        curr = R matMult Q
        q = q matMult Q

        diff = (curr - prev).maxOf {
            it.absoluteValue
        }
        i++
    }

    return q to curr
}

/**
 * Utilizes the Real Schur Decomposition of the input matrix to calculate eigenvalues.
 *
 * Let A be the input matrix and its Real Schur Decomposition be defined as A = Q * R * Q^-1
 * where Q is an orthogonal matrix (meaning Q^-1 = Q^T) and R is a block upper triangular matrix
 *
 * The eigenvalues of A can be calculated from R as A is similar to R therefore the eigenvalues are the same
 * Since R is a block upper triangular matrix, the eigenvalues of R are the eigenvalues of the blocks on the diagonal
 *
 * If the block is a singular scalar value (1 x 1 matrix), that value is a real eigenvalue of the original matrix
 *
 * If the block is a 2 x 2 square matrix, the complex conjugate pair of eigenvalues of said square matrix are a
 * complex conjugate pair of eigenvalues of the original matrix
 */
fun algorithm(matrix: DoubleMatrix, tolerance: Double = 1e-12, maxIteration: Int = 1000): List<Complex> {
    // Real Schur Decomposition
    // note: due to it being a real schur decomposition, the eigenvectors are not the columns of q
    // if the eigenvectors contain complex values
    val (_, r) = realSchurDecomposition(matrix, tolerance, maxIteration)

    return buildList {
        var i = 0
        val n = matrix.shape.x

        while (i < n) {
            if (i + 1 < n) {
                if (r[i + 1, i].absoluteValue <= tolerance) {
                    add(r[i, i].toComplex())
                    i++
                } else {
                    val square = DoubleMatrix.of(2 by 2,
                            r[i, i],     r[i, i + 1],
                        r[i + 1, i], r[i + 1, i + 1],
                    )

                    val (e1, e2) = eigenvalues2x2(square)
                    add(e1)
                    add(e2)
                    i += 2
                }
            } else {
                add(r[i, i].toComplex())
                i++
            }
        }
    }
}

fun DoubleMatrix.test(values: String) {
    println("=== QR iteration [$values] ==")
    algorithm(this).forEach(::println)
    println()
    if (this.shape == 2 by 2) {
        println("== mean product formula [$values] ==")
        eigenvalues2x2(this).also { (e1, e2) ->
            println(e1)
            println(e2)
            println()
        }
    }
}

fun main() {
    DoubleMatrix.of(2 by 2,
        4.0,  1.0,
        2.0, -1.0
    ).test("4.37228, -1.37228")

    DoubleMatrix.of(2 by 2,
        3.0, 1.0,
        4.0, 1.0,
    ).test("4.23607, -0.236068")

    DoubleMatrix.of(6 by 6,
        7,  3,  4,  11, -9, -2,
        -6,  4, -5,   7,  1, 12,
        -1, -9,  2,   2,  9,  1,
        -8,  0, -1,   5,  0,  8,
        -4,  3, -5,   7,  2, 10,
        6,  1,  4, -11, -7, -1,
    ).test("2.91676 + 13.248i, 2.91676 - 13.248i, 5 + 6i, 5 - 6i, 1.58324 + 1.41555i, 1.58324 - 1.41555i")

    DoubleMatrix.of(4 by 4,
        2, 0, 0, 0,
        1, 2, 0, 0,
        0, 1, 3, 0,
        0, 0, 1, 3,
    ).test("3, 3, 2, 2")

    DoubleMatrix.of(2 by 2,
        -5, 2,
        -7, 4,
    ).test("-3, 2")

    DoubleMatrix.of(3 by 3,
         5, -10, -5,
         2,  14,  2,
        -4,  -8,  6,
    ).test("10, 10, 5")

    DoubleMatrix.of(3 by 3,
         2, 2, -2,
         1, 3, -1,
        -1, 1,  1,
    ).test("4, 2, 0")

    DoubleMatrix.of(3 by 3,
         33, 105, 105,
         10,  28,  30,
        -20, -60, -62,
    ).test("-2, -2, 3")

    DoubleMatrix.of(3 by 3,
        1, 2, 4,
        0, 4, 7,
        0, 0, 6,
    ).test("1, 4, 6")

    DoubleMatrix.of(2 by 2,
        1, 1,
        1, 0,
    ).test("1.61801, -0.618034")

    DoubleMatrix.of(2 by 2,
        1, -1,
        1,  1,
    ).test("1 + i, 1 - i")

    DoubleMatrix.of(3 by 3,
        0.8, -0.6, 0,
        0.6,  0.8, 0,
          1,    2, 2,
    ).test("2, 0.8 + 0.6i, 0.8 - 0.6i")

    DoubleMatrix.of(3 by 3,
        0, 0,  2,
        1, 0, -5,
        0, 1,  4,
    ).test("2, 1, 1")

    // problem: if the matrix is already orthogonal, the algorithm fails
    DoubleMatrix.of(3 by 3,
        0, 0, -1,
        1, 0,  0,
        0, 1,  0,
    ).test("-1, 0.5 + 0.866025i, 0.5 - 0.866025i")
}