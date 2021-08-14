package org.kotrix.matrix.decomp

import org.kotrix.complex.Complex
import org.kotrix.complex.complex
import org.kotrix.matrix.DoubleMatrix
import org.kotrix.matrix.Matrix
import org.kotrix.matrix.NumberMatrix
import org.kotrix.vector.VectorImpl
import kotlin.math.*

class EigenvalueDecomposition(matrix: DoubleMatrix) {
    constructor(matrix1: NumberMatrix<*>): this(matrix1 as DoubleMatrix)

    val size = matrix.rowLength

    var d = DoubleArray(size)

    var e = DoubleArray(size)

    var v: Array<DoubleArray>

    var h: Array<DoubleArray> = Array(size) { DoubleArray(size) }

    var ort: DoubleArray = DoubleArray(size)

    val symmetric = matrix.isSymmetric()

    init {
        if (symmetric) {
            v = matrix.doubleArray
            tridiagonalize()
            diagonalize()
        } else {
            v = Array(size) { DoubleArray(size) }
            h = matrix.doubleArray
            reduceToHessenberg()
            hessenbergToRealSchur()
        }
    }

    /**after ComplexMatrix creation, change return type**/
    fun eigenvectorMatrix(): Matrix<Complex> =
        Matrix(buildEigenvectors().t)

    /**after ComplexMatrix creation, change return type**/
    fun eigenvectorMatrixInv(): Matrix<Complex> {
        var r = Matrix(buildEigenvectors())
        if (!symmetric) {
//            r = r.t.inv
        }
        return r
    }

    /**after ComplexVector creation, change return type**/
    fun eigenvalues(): VectorImpl<Complex> {
        val values = VectorImpl(d.size) { i -> Complex(d[i]) }
        e.forEachIndexed { i, imag -> values[i] = if (imag != 0.0) Complex(values[i], imag) else values[i] }
        return values
    }

    fun eigenvalueMatrix(): DoubleMatrix {
        return DoubleMatrix.diagonal(*eigenvalues().toList().toTypedArray())
    }

    /**after ComplexMatrix creation, change return type**/
    private fun buildEigenvectors(): Matrix<Complex> {
        val ret = VectorImpl(this.e.size) { VectorImpl(0) { Complex(0, 0) } }
        for ((i, imag) in e.withIndex()) {
            if (imag == 0.0) {
                ret[i] = VectorImpl(this.size) { j -> this.v[j][i].complex }
            } else if (imag > 0.0) {
                ret[i] = VectorImpl(this.size) { j -> Complex(this.v[j][i], this.v[j][i + 1])}
            } else {
                ret[i] = VectorImpl(this.size) { j -> Complex(this.v[j][i - 1], -this.v[j][i])}
            }
        }
        return Matrix(ret)
    }

    /**
     * Symmetric Householder reduction to tridiagonal form.
     */
    private fun tridiagonalize() {
        /*
        #  This is derived from the Algol procedures tred2 by
        Bowdler, Martin, Reinsch, and Wilkinson, Handbook for
        Auto. Comp., Vol.ii-Linear Algebra, and the corresponding
        Fortran subroutine in EISPACK.
         */

        for (j in 0 until size) {
            this.d[j] = this.v[size - 1][j]
        }

        // Householder reduction to tridiagonal form.

        for (i in (size - 1) downTo (0 + 1)) {

            // Scale to avoid under/overflow.

            var scale = 0.0
            var h = 0.0
            for (k in 0 until i) {
                scale += abs(this.d[k])
            }
            if (scale == 0.0) {
                this.e[i] = this.d[i - 1]
                for (j in 0 until i) {
                    this.d[j] = this.v[i - 1][j]
                    this.v[i][j] = 0.0
                    this.v[j][i] = 0.0
                }
            } else {

                // Generate Householder vector.

                for (k in 0 until i) {
                    this.d[k] /= scale
                    h += this.d[k] * this.d[k]
                }
                var f = this.d[i - 1]
                var g = sqrt(h)
                if (f > 0.0) {
                    g = -g
                }
                this.e[i] = scale * g
                h -= f * g
                this.d[i - 1] = f - g
                this.d[i - 1] = f - g
                for (j in 0 until i) {
                    this.e[j] = 0.0
                }

                // Apply similarity transformation to remaining columns.

                for (j in 0 until i) {
                    f = this.d[j]
                    this.v[j][i] = f
                    g = this.e[j] + this.v[j][j] * f
                    for (k in (j+1) until i) {
                        g += this.v[k][j] * this.d[k]
                        this.e[k] += this.v[k][j] * f
                    }
                    this.e[j] = g
                }
                f = 0.0
                for (j in 0 until i) {
                    this.e[j] /= h
                    f += this.e[j] * this.d[j]
                }
                val hh = f / (h + h)
                for (j in 0 until i) {
                    this.e[j] -= hh * this.d[j]
                }
                for (j in 0 until i) {
                    f = this.d[j]
                    g = this.e[j]
                    for (k in j until i) {
                        this.v[k][j] -= (f * this.e[k] + g * this.d[k])
                    }
                    this.d[j] = this.v[i - 1][j]
                    this.v[i][j] = 0.0
                }
            }
            this.d[i] = h
        }

        // Accumulate transformations.

        for (i in 0 until size - 1) {
            this.v[size - 1][i] = this.v[i][i]
            this.v[i][i] = 1.0
            val h = this.d[i + 1]
            if (h != 0.0) {
                for (k in 0..i) {
                    this.d[k] = this.v[k][i + 1] / h
                }
                for (j in 0..i) {
                    var g = 0.0
                    for (k in 0..i) {
                        g += this.v[k][i + 1] * this.v[k][j]
                    }
                    for (k in 0..i) {
                        this.v[k][j] -= g * this.d[k]
                    }
                }
            }
            for (k in 0..i) {
                this.v[k][i + 1] = 0.0
            }
        }
        for (j in 0 until size) {
            this.d[j] = this.v[size - 1][j]
            this.v[size - 1][j] = 0.0
        }
        this.v[size - 1][size - 1] = 1.0
        this.e[0] = 0.0
    }

    /**
     * Symmetric tridiagonal QL algorithm.
     */
    private fun diagonalize() {
        /*
        This is derived from the Algol procedures tql2, by
        Bowdler, Martin, Reinsch, and Wilkinson, Handbook for
        Auto. Comp., Vol.ii-Linear Algebra, and the corresponding
        Fortran subroutine in EISPACK.
         */

        for (i in 1 until size) {
            e[i - 1] = e[i]
        }
        e[size - 1] = 0.0

        var f = 0.0
        var tst1 = 0.0
        val eps = 2.2204460492503131e-16

        for (l in 0 until size) {

            // Find small subdiagonal element

            tst1 = max(tst1, d[l].absoluteValue + e[l].absoluteValue)
            var m = l
            while (m < size) {
                if (e[m].absoluteValue <= eps*tst1) {
                    break
                }
                m += 1
            }

            // If m == l, @d[l] is an eigenvalue,
            // otherwise, iterate.

            if (m > l) {
                var iter = 0
                do {
                    iter += 1

                    // Compute implicit shift

                    var g = d[l]
                    var p = (d[l + 1] - g) / (2.0 * e[l])
                    var r = hypot(p, 1.0)
                    if (p < 0.0) {
                        r = -r
                    }
                    d[l] = e[l] / (p + r)
                    d[l + 1] = e[l] * (p + r)
                    val dl1 = d[l + 1]
                    var h_ = g - d[l]
                    for (i in (l + 2) until size) {
                        d[i] -= h_
                    }
                    f += h_

                    // Implicit QL transformation.

                    p = d[m]
                    var c = 1.0
                    var c2 = c
                    var c3 = c
                    var el1 = e[l + 1]
                    var s = 0.0
                    var s2 = 0.0
                    for (i in (m - 1) downTo l) {
                        c3 = c2
                        c2 = c
                        s2 = s
                        g = c * e[i]
                        h_ = c * p
                        r = hypot(p, e[i])
                        e[i + 1] = s * r
                        s = e[i] / r
                        c = p / r
                        p = c * d[i] - s * g
                        d[i + 1] = h_ + s * (c * g + s * d[i])

                        // Accumulate transformation.

                        for (k in 0 until size) {
                            h_ = v[k][i + 1]
                            v[k][i + 1] = s * v[k][i] + c * h_
                            v[k][i] = c * v[k][i] - s * h_
                        }
                    }
                    p = -s * s2 * c3 * el1 * e[l] / dl1
                    e[l] = s * p
                    d[l] = c * p

                    // Check for convergence.

                } while (e[l].absoluteValue > eps * tst1)
            }
            d[l] = d[l] + f
            e[l] = 0.0
        }

        // Sort eigenvalues and corresponding vectors.

        for (i in 0..(size - 2)) {
            var k = i
            var p = d[i]
            for (j in (i + 1) until size) {
                if (d[j] < p) {
                    k = j
                    p = d[j]
                }
            }
            if (k != i) {
                d[k] = d[i]
                d[i] = p
                for (j in 0 until size) {
                    p = v[j][i]
                    v[j][i] = v[j][k]
                    v[j][k] = p
                }
            }
        }
    }

    /**
     * Nonsymmetric reduction to Hessenberg form.
     */
    private fun reduceToHessenberg() {
        /*
        #  This is derived from the Algol procedures orthes and ortran,
        by Martin and Wilkinson, Handbook for Auto. Comp.,
        Vol.ii-Linear Algebra, and the corresponding
        Fortran subroutines in EISPACK.
         */

        val low = 0
        val high = size - 1

        for (m in (low + 1) until high) {

            // Scale column

            var scale = 0.0
            for (i in m..high) {
                scale += scale + this.h[i][m - 1].absoluteValue
            }
            if (scale != 0.0) {

                // Compute HouseHolder transformation

                var h = 0.0
                for (i in high.downTo(m)) {
                    this.ort[i] = this.h[i][m - 1] / scale
                    h += this.ort[i] * this.ort[i]
                }
                var g = sqrt(h)
                if (this.ort[m] > 0.0) {
                    g = -g
                }
                h -= this.ort[m] * g
                this.ort[m] = this.ort[m] - g

                // Apply HouseHolder similarity transformation
                // this.h = (I - u * u' / h) * this.h * (I - u * u') / h

                for (j in m until this.size) {
                    var f = 0.0
                    for (i in high downTo m) {
                        f += this.ort[i] * this.h[i][j]
                    }
                    f /= h
                    for (i in m..high) {
                        this.h[i][j] -= f * this.ort[i]
                    }
                }

                for (i in 0..high) {
                    var f = 0.0
                    for (j in high downTo m) {
                        f += this.ort[j] * this.h[i][j]
                    }
                    f /= h
                    for (j in m..high) {
                        this.h[i][j] -= f * this.ort[j]
                    }
                }
                this.ort[m] = scale * this.ort[m]
                this.h[m][m - 1] = scale * g
            }
        }

        // Accumulate transformations (Algol's ortran)

        for (i in 0 until size) {
            for (j in 0 until size) {
                this.v[i][j] = if (i == j) 1.0 else 0.0
            }
        }

        for (m in (high - 1) downTo (low+1)) {
            if (this.h[m][m - 1] != 0.0) {
                for (i in (m+1)..(high)) {
                    this.ort[i] = this.h[i][m - 1]
                }
                for (j in m..high) {
                    var g = 0.0
                    for (i in m..high) {
                        g += this.ort[i] * this.v[i][j]
                    }
                    // Double Division avoids possible underflow
                    g = (g / this.ort[m]) / this.h[m][m - 1]
                    for (i in m..high) {
                        this.v[i][j] += g * this.ort[i]
                    }
                }
            }
        }
    }

    /**
     * Nonsymmetric reduction from Hessenberg to real Schur form.
     */
    private fun hessenbergToRealSchur() {

        /*
        This is derived from the Algol procedure hqr2,
        by Martin and Wilkinson, Handbook for Auto. Comp.,
        Vol.ii-Linear Algebra, and the corresponding
        Fortran subroutine in EISPACK.
         */

        // Initialize

        val nn = this.size
        var n = nn - 1
        val low = 0
        val high = nn - 1
        val eps = 2.2204460492503131e-16
        var exshift = 0.0
        var p = 0.0
        var q = 0.0
        var r = 0.0
        var s = 0.0
        var z = 0.0

        // Store roots isolated by balanc and compute matrix norm

        var norm = 0.0
        for (i in 0 until nn) {
            if (i < low || i > high) {
                this.d[i] = this.h[i][i]
                this.e[i] = 0.0
            }
            for (j in maxOf(i - 1, 0) until nn) {
                norm += norm + this.h[i][j].absoluteValue
            }
        }

        // Outer loop over eigenvalue index

        var iter = 0
        while (n >= low) {

            // Look for a single small sub-diagonal element

            var l = n
            while (l > low) {
                s = this.h[l - 1][l - 1].absoluteValue + this.h[l][l].absoluteValue
                if (s == 0.0) {
                    s = norm
                }
                if (this.h[l][l - 1].absoluteValue < eps * s) {
                    break
                }
                l -= 1
            }

            // Check for convergence
            // One root found

            if (l == n) {
                this.h[n][n] = this.h[n][n] + exshift
                this.d[n] = this.h[n][n]
                this.e[n] = 0.0
                n -= 1
                iter = 0
            }

            // Two roots found

            else if (l == n - 1) {
                val w = this.h[n][n - 1] * this.h[n - 1][n]
                p = (this.h[n - 1][n - 1] - this.h[n][n]) / 2.0
                q = p * p + w
                z = sqrt(q.absoluteValue)
                this.h[n][n] = this.h[n][n] + exshift
                this.h[n - 1][n - 1] = this.h[n - 1][n - 1] + exshift
                var x = this.h[n][n]

                // Real Pair

                if (q >= 0) {
                    if (p >= 0) {
                        z += p
                    } else {
                        z = p - z
                    }
                    this.d[n - 1] = x + z
                    this.d[n] = this.d[n - 1]
                    if (z != 0.0) {
                        this.d[n] = x - w / z
                    }
                    this.e[n - 1] = 0.0
                    this.e[n] = 0.0
                    x = this.h[n][n - 1]
                    s = x.absoluteValue + z.absoluteValue
                    p = x / s
                    q = z / s
                    r = sqrt(p * p + q * q)
                    p /= r
                    q /= r

                    // Row modification

                    for (j in (n - 1) until nn) {
                        z = this.h[n - 1][j]
                        this.h[n - 1][j] = q * z + p * this.h[n][j]
                        this.h[n][j] = q * this.h[n][j] - p * z
                    }

                    // Column modification

                    for (i in 0..n) {
                        z = this.h[i][n - 1]
                        this.h[i][n - 1] = q * z + p * this.h[i][n]
                        this.h[i][n] = q * this.h[i][n] - p * z
                    }

                    // Accumulate transformations

                    for (i in low..high) {
                        z = this.v[i][n - 1]
                        this.v[i][n - 1] = q * z + p * this.v[i][n]
                        this.v[i][n] = q * this.v[i][n] - p * z
                    }
                }

                // Complex pair

                else {
                    this.d[n - 1] = x + p
                    this.d[n] = x + p
                    this.e[n - 1] = z
                    this.e[n] = -z
                }
                n -= 2
                iter = 0
            }

            // No convergence yet

            else {

                // Form shift

                var x = this.h[n][n]
                var y = 0.0
                var w = 0.0
                if (l < n) {
                    y = this.h[n - 1][n - 1]
                    w = this.h[n][n - 1] * this.h[n - 1][n]
                }

                // Wilkinson's original ad hoc shift

                if (iter == 10) {
                    exshift += x
                    for (i in low..n) {
                        this.h[i][i] -= x
                    }
                    s = this.h[n][n - 1].absoluteValue + this.h[n - 1][n - 2].absoluteValue
                    y = 0.75 * s
                    x = 0.75 * s
                    w = -0.4375 * s * s
                }

                // MATLAB's new ad hoc shift

                if (iter == 30) {
                    s = (y - x) / 2.0
                    s *= s + w
                    if (s > 0) {
                        s = sqrt(s)
                        if (y < x) {
                            s = -s
                        }
                        s = x - w / ((y - x) / 2.0 + s)
                        for (i in low..high) {
                            this.h[i][i] -= s
                        }
                        exshift += s
                        w = 0.964
                        y = 0.964
                        x = 0.964
                    }
                }

                iter += 1 // (Could check iteration count here)

                // Look for two consecutive small sub-diagonal elements

                var m = n - 2
                while (m >= l) {
                    z = this.h[m][m]
                    r = x - z
                    s = y - z
                    p = (r * s - w) / this.h[m + 1][m] + this.h[m][m + 1]
                    q = this.h[m+1][m+1] - z - r - s
                    r = this.h[m+2][m+1]
                    s = p.absoluteValue + q.absoluteValue + r.absoluteValue
                    p /= s
                    q /= s
                    r /= s
                    if (m == l) {
                        break
                    }
                    if (this.h[m][m - 1].absoluteValue * (q.absoluteValue + r.absoluteValue) <
                        eps * (p.absoluteValue * (this.h[m - 1][m - 1].absoluteValue + z.absoluteValue +
                                this.h[m + 1][m + 1].absoluteValue))) {
                        break
                    }
                    m -= 1
                }

                for (i in (m + 2)..n) {
                    this.h[i][i - 2] = 0.0
                    if (i > m + 2) {
                        this.h[i][i - 3] = 0.0
                    }
                }

                // Double QR step involving rows l:n and columns m:n

                for (k in m until n) {
                    val notlast = (k != n -1)
                    if (k != m) {
                        p = this.h[k][k - 1]
                        q = this.h[k + 1][k - 1]
                        r = if (notlast) this.h[k + 2][k - 1] else 0.0
                        x = p.absoluteValue + q.absoluteValue + r.absoluteValue
                        if (x == 0.0) continue
                        p /= x
                        q /= x
                        r /= x
                    }
                    s = sqrt(p * p + q * q + r * r)
                    if (p < 0) {
                        s = -s
                    }
                    if (s != 0.0) {
                        if (k != m) {
                            this.h[k][k - 1] = -s * x
                        } else if (l != m) {
                            this.h[k][k - 1] = -this.h[k][k - 1]
                        }
                        p += s
                        x = p / s
                        y = q / s
                        z = r / s
                        q /= p
                        r /= p

                        // Row modification

                        for (j in k until nn) {
                            p = this.h[k][j] + q * this.h[k + 1][j]
                            if (notlast) {
                                p += r * this.h[k + 2][j]
                                this.h[k + 2][j] = this.h[k + 2][j] - p * z
                            }
                            this.h[k][j] = this.h[k][j] - p * x
                            this.h[k + 1][j] = this.h[k + 1][j] - p * y
                        }

                        // Column modification

                        for (i in 0..(minOf(n, k + 3))) {
                            p = x * this.h[i][k] + y * this.h[i][k + 1]
                            if (notlast) {
                                p += z * this.h[i][k + 2]
                                this.h[i][k + 2] = this.h[i][k + 2] - p * r
                            }
                            this.h[i][k] = this.h[i][k] - p
                            this.h[i][k + 1] = this.h[i][k + 1] - p * q
                        }

                        // Accumulate transformation

                        for (i in low..high) {
                            p = x * this.v[i][k] + y * this.v[i][k + 1]
                            if (notlast) {
                                p += z * this.v[i][k + 2]
                                this.v[i][k + 2] = this.v[i][k + 2] - p * r
                            }
                            this.v[i][k] = this.v[i][k] - p
                            this.v[i][k + 1]= this.v[i][k + 1] - p * q
                        }
                    }
                }
            }
        }

        // Backsubstitute to find vectors of upper triangular form

        if (norm == 0.0) {
            return
        }

        for (k in (nn - 1) downTo 0) {
            p = this.d[k]
            q = this.e[k]

            // Real vector

            if (q == 0.0) {
                var l = k
                this.h[k][k] = 1.0
                for (i in (k - 1) downTo 0) {
                    val w = this.h[i][i] - p
                    r = 0.0
                    for (j in l..k) {
                        r += this.h[i][j] * this.h[j][k]
                    }
                    if (this.e[i] < 0.0) {
                        z = w
                        s = r
                    } else {
                        l = i
                        if (this.e[i] == 0.0) {
                            if (w != 0.0)
                                this.h[i][k] = -r / w
                            else
                                this.h[i][k] = -r / (eps * norm)
                        }

                        // Solve real equations

                        else {
                            val x = this.h[i][i + 1]
                            val y = this.h[i + 1][i]
                            q = (this.d[i] - p) * (this.d[i] - p) + this.e[i] * this.e[i]
                            val t = (x * s - z * r) / q
                            this.h[i][k] = t
                            if (x.absoluteValue > z.absoluteValue) {
                                this.h[i + 1][k] = (-r - w * t) / x
                            } else {
                                this.h[i + 1][k] = (-s - y * t) / z
                            }
                        }

                        // Overflow control

                        val t = this.h[i][k].absoluteValue
                        if ((eps * t) * t > 1) {
                            for (j in i..k) {
                                this.h[j][k] = this.h[j][k] / t
                            }
                        }
                    }
                }
            }

            // Complex Vector

            else if (q < 0) {
                var l = n - 1


                // Last vector component imaginary so matrix is triangular

                if (this.h[n][n - 1].absoluteValue > this.h[n - 1][n].absoluteValue) {
                    this.h[n-1][n-1] = q / this.h[n][n-1]
                    this.h[n-1][n] = -(this.h[n][n] - p) / this.h[n][n-1]
                } else {
                    val (cdivr, cdivi) =
                        Complex(0.0, -this.h[n - 1][n]) / Complex(this.h[n - 1][n - 1] - p, q)
                    this.h[n - 1][n - 1] = cdivr
                    this.h[n - 1][n] = cdivi
                }
                this.h[n][n - 1] = 0.0
                this.h[n][n] = 1.0
                for (i in (n - 2) downTo 0) {
                    var ra = 0.0
                    var sa = 0.0

                    for (j in l..n) {
                        ra += this.h[i][j] * this.h[j][n - 1]
                        sa += this.h[i][j] * this.h[j][n]
                    }
                    val w = this.h[i][i] - p

                    if (this.e[i] < 0.0) {
                        z = w
                        r = ra
                        s = sa
                    } else {
                        l = i
                        if (this.e[i] == 0.0) {
                            val (cdivr, cdivi) = Complex(-ra, -sa) / Complex(w, q)
                            this.h[i][n - 1] = cdivr
                            this.h[i][n] = cdivi
                        } else {

                            // Solve complex equations

                            val x = this.h[i][i+1]
                            val y = this.h[i+1][i]
                            var vr = (this.d[i] - p) * (this.d[i] - p) + this.e[i] * this.e[i] - q * q
                            val vi = (this.d[i] - p) * 2.0 * q
                            if (vr == 0.0 && vi == 0.0) {
                                vr = eps * norm * (w.absoluteValue + q.absoluteValue +
                                        x.absoluteValue + y.absoluteValue + z.absoluteValue)
                            }
                            val (cdivr, cdivi) = Complex(x*r-z*ra+q*sa, x*s-z*sa-q*ra) / Complex(vr, vi)
                            this.h[i][n-1] = cdivr
                            this.h[i][n] = cdivi
                            if (x.absoluteValue > (z.absoluteValue + q.absoluteValue)) {
                                this.h[i + 1][n - 1] = (-ra - w * this.h[i][n - 1] + q * this.h[i][n]) / x
                                this.h[i + 1][n] = (-sa - w * this.h[i][n] - q * this.h[i][n - 1]) / x
                            } else {
                                val (cdivr1, cdivi1) = Complex(-r-y*this.h[i][n-1], -s-y*this.h[i][n]) / Complex(z, q)
                                this.h[i+1][n-1] = cdivr1
                                this.h[i+1][n] = cdivi1
                            }
                        }

                        // Overflow control

                        val t = maxOf(this.h[i][n - 1].absoluteValue, this.h[i][n].absoluteValue)
                        if ((eps * t) * t > 1) {
                            for (j in i..n) {
                                this.h[j][n - 1] = this.h[j][n - 1] / t
                                this.h[j][n] = this.h[j][n] / t
                            }
                        }
                    }
                }
            }
        }

        // Vectors of isolated roots

        for (i in 0 until nn) {
            if (i < low || i > high) {
                for (j in i until nn) {
                    z = 0.0
                    for (k in low..minOf(j, high)) {
                        z += this.v[i][k] * this.h[k][j]
                    }
                    this.v[i][j] = z
                }
            }
        }
    }
}