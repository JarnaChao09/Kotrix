package org.kotrix.matrix

import org.kotrix.matrix.*
import org.kotrix.matrix.decomp.CholeskyDecomposition
import org.kotrix.matrix.decomp.QRDecomposition
import org.kotrix.utils.*
import org.kotrix.vector.*

fun runTest(algorithm: CholeskyDecomposition.Algorithm) {
    println("running tests with $algorithm algorithm")
    val test1 = DoubleMatrix.of(3 by 3,
        4,  12, -16,
        12,  37, -43,
        -16, -43,  98,
    )
    val (l1, lt1) = CholeskyDecomposition(test1, algorithm)
    val llt1 = l1 matMult lt1

    println("l1=\n$l1\nlt1=\n$lt1\n\noriginal=\n${test1}\nrecomputed=\n${llt1}\n${if (test1 == llt1) "equal" else "not equal"}\n\n")

    val test2 = DoubleMatrix.of(3 by 3,
        25.0, 15.0, -5.0,
        15.0, 18.0,  0.0,
        -5.0,  0.0, 11.0,
    )
    val (l2, lt2) = CholeskyDecomposition(test2, algorithm)
    val llt2 = l2 matMult lt2

    println("l2=\n$l2\nlt2=\n$lt2\n\noriginal=\n${test2}\nrecomputed=\n${llt2}\n${if (test2 == llt2) "equal" else "not equal"}\n\n")

    val test3 = DoubleMatrix.of(4 by 4,
        18.0, 22.0,  54.0,  42.0,
        22.0, 70.0,  86.0,  62.0,
        54.0, 86.0, 174.0, 134.0,
        42.0, 62.0, 134.0, 106.0,
    )
    val (l3, lt3) = CholeskyDecomposition(test3, algorithm)
    val llt3 = l3 matMult lt3

    println("l3=\n$l3\nlt3=\n$lt3\n\noriginal=\n${test3}\nrecomputed=\n${llt3}\n${if (test3 == llt3) "equal" else "not equal"}\n\n")
}

fun main() {
    // runTest(CholeskyDecomposition.Algorithm.CHOLESKY_BANACHIEWICZ)
    // runTest(CholeskyDecomposition.Algorithm.CHOLESKY_CROUT)

    val matrix = DoubleMatrix.of(3 by 3,
        1.0, 1.0, 0.0,
        1.0, 0.0, 1.0,
        0.0, 1.0, 1.0,
    )

    // val matrix = DoubleMatrix.of(3 by 3,
    //     12.0, -51.0,   4.0,
    //      6.0, 167.0, -68.0,
    //     -4.0,  24.0, -41.0,
    // )

    val (Q, R) = QRDecomposition(matrix)

    println("$Q\n\n$R\n\n${Q matMult R}")

    // val backing = arrayOf(
    //     arrayOf(2.0, -3.0, 4.0, 3.0),
    //     arrayOf(6.0, -9.0, 12.0, 8.0),
    //     arrayOf(4.0, -5.0, 6.0, 7.0),
    //     arrayOf(2.0, 1.0, -1.0, 8.0),
    // )
    // val mat = DoubleMatrix(4 by 4) { r, c -> backing[r][c] }
    // println(mat)
    // println()
    // val (l, u, p) = mat.lup
    // println(l)
    // println()
    // println(u)
    // println()
    // println(p)
    // println()
    // println(p matMult mat)
    // println()
    // println(p.inv matMult l matMult u)
//    val testS: Slice = 0 to 2
//    for (i: Int in testS) {
//        println(i)
//    }
//
//    val test1 = DoubleVector(5) { 10.0 }
//
//    println("Addition: ${test1 + 2}")
//    println("Addition: ${2 + test1}")
//
//    println("Subtraction: ${test1 - 2}")
//    println("Subtraction: ${2 - test1}")
//
//    println("Multiplication: ${2 * test1}")
//    println("Multiplication: ${test1 * 2}")
//
//    println("Division: ${2 / test1}")
//    println("Division: ${test1 / 2}")
//
//    println("Remainder: ${2 % test1}")
//    println("Remainder: ${test1 % 2}")
//
//    println("Exponent: ${test1 pow 2}")
//    println("Exponent: ${2 pow test1}")
//
//    println("To Matrix: ${test1.toMatrix(asCol = false)}")
//    println("To Matrix:\n${test1.toMatrix()}")
//
//    val test2 = IntVector(5) { 10 } + test1
//    val test3 = test1 + IntVector(5) { 10 }
//
//    var size = Size(10, 10)
//    var size1 = Size(10, 10)
//
//    println(size + size1)
//
//    println(size - size1)
//
//    println(2 * size)
//
//    println(size1 * 2)
//
//    val test = DoubleMatrix(3 by 3) { i, j -> 3 * i + j + 1.0 }
//
//    test.forEach(Selector.STRICT_UPPER) { e -> println(e) }
//
//    println("---------")
//
//    for ((v, r, c) in test.withIndices) {
//        println("$v $r $c")
//    }
//
//    println("${test.dim}")
//
//    println("---------")
//
//    println(test)
//
//    println("---------")
//
//    println(test.array.contentDeepToString())
//
//    println("---------")
//
//    println(test + test)
//
//    println("---------")
//
//    println(test - test)
//
//    println("---------")
//
//    println(test * test)
//
//    println("---------")
//
//    println(test / test)
//
//    println("---------")
//
//    println(test % test)
//
//    println("---------")
//
//    println(test pow test)
//
//    println("---------")
//
//    println(test.t)
//
//    println("---------")
//
//    for (i in test) {
//        println(i)
//    }
//
//    println("---------")
//
//    println(test[1, 1])
//
//    println("---------")
//
//    println(test[0])
//
//    println("---------")
//
//    println(test[0 sliceTo 2])
//
//    println("---------")
//
//    println(test[0 sliceTo 2, 1])
//
//    println("---------")
//
//    println(test[1, 1 sliceTo 3])
//
//    println("---------")
//
//    println(test[0 sliceTo 2, 1 sliceTo 3])
//
//    println("---------")
//
//    test[0, 0] = 2.0
//
//    println(test)
//
//    println("---------")
//
//    test[0] = DoubleVector.of(4.0, 5.0, 6.0)
//
//    println(test)
//
//    println("---------")
//
//    test[0 sliceTo 2] = DoubleMatrix(2 by 3) { i, j -> listOf(listOf( 4.0, 5.0, 6.0), listOf(7.0, 8.0, 9.0))[i][j]}
//
//    println(test)
//
//    println("---------")
//
//    test[0 sliceTo 2, 1] = DoubleVector.of(10.0, 11.0)
//
//    println(test)
//
//    println("---------")
//
//    test[0 sliceTo 2, 0 sliceTo 2] = DoubleMatrix(2 by 2) { i, j -> listOf(listOf(10.0, 11.0), listOf(12.0, 13.0))[i][j] }
//
//    println(test)
//
//    val matMultTest1 = DoubleMatrix(2 by 3) { r, c -> 3 * r + c + 1.0 }
//    val matMultTest2 = DoubleMatrix(3 by 2) { r, c -> 2 * r + c + 1.0 }
//
//    println("First:\n$matMultTest1")
//    println("Second:\n$matMultTest2")
//    println("MatrixMult:\n${matMultTest1 matMult matMultTest2}")
//
//    println(Matrix.of(listOf("a", "b", "c"), listOf("c", "d", "e")))
//
//    val test1 = IntMatrix(vectorOfVector = Vector(3) { IntVector(3) { i -> i + 2 } }, asColVectors = true)
//    val test2 = IntMatrix(vectorOfVector = Vector(3) { IntVector(3) { i -> i + 2 } }, asColVectors = false)
//
//    val test4 = test1.rowAppend(test2)
//    val test5 = test1.colAppend(test2)
//
//    println(test4)
//
//    println(test4.size)
//
//    println(test5)
//
//    println(test5.size)
//
//    val a = DoubleMatrix(2 by 2) { r, c -> listOf(listOf(1.0, 2.0), listOf(3.0, 4.0))[r][c] }
//
//    val (l, u, p) = a.lup
//
//    println(l)
//
//    println(u)
//
//    println(p)
//
//    println(l matMult u)
//    println(p matMult a)
//
//    val b = a.lup.solve(DoubleVector(2) { i -> listOf(2.0, 5.0)[i] })
//
//    println(b)
//
//    val normal = IntMatrix.of(3 by 3, 1, 1, 0, 0, 1, 1, 1, 0, 1)
//
//    val notNormal = IntMatrix.of(2 by 2, 1, 2, 3, 4)
//
//    println(normal)
//
//    println(notNormal)
//
//    println(normal.isNormal())
//
//    println(notNormal.isNormal())
//
//    val test11 = DoubleMatrix(3 by 3) { i, j -> 3 * i + j + 1.0 }
//    val test21 = DoubleMatrix(3 by 3) { i, j -> 3 * i + j + 1.0 }
//
//    test11.forEach { x -> println(x) }
//    test11.map { x -> x * 2.0 }
//
//    val d = test11 pow test21
//    test11 powAssign test21
//
//    println(d eq test11)
//
//    val c = test11 eq test21
//    val d = c or c
//    val e = c xor c
//    val f = c and c
//    val g = !c
//
//    val mat = IntMatrix.of(2 by 2, 1, 2, 3, 4)
//
//    println(mat)
//
//    println("-----------")
//
//    println(mat.map(Selector.DIAGONAL) { x -> x * 2 })
//
//    println("-----------")
//
//    println(mat.mapIndexed(Selector.OFF_DIAGONAL) { x, r, c -> x + r + c })
//
//    val vec = intVector {
//        !11 push 4 append 6 times 5
//        repeat(5) {
//            !10 push 2
//        }
//    }
//    println(vec)
//
//    val mat = matrix<Int> {
//        +vector<Int> {
//            !11
//            !12
//            !13
//        }
//
//        +vector<Int> {
//            !12
//            !13
//            !14
//        }
//    }
//
//    println(mat)
//
//    val mat = doubleMatrix {
//        !doubleVector {
//            !11
//            !12
//            !13
//        }
//
//        !doubleVector {
//            !12
//            !13
//            !14
//        }
//    }
//
//    println(mat)
//
//    println(doubleVector {
//        !11
//        !12
//        !13
//    })
//
//    val test = IntMatrix.of(2 by 2, 7, 6, 3, 9)
//
//    println(test.rank())
//
//    println(DoubleMatrix.of(2 by 2, 7.0, 6.0, 3.0, 9.0).rank())
//
//    println(test.toDoubleMatrix().inv)
//
//    val test1 = IntMatrix(3 by 3) { i, j -> 3 * i + j + 1 }
//
//    println(test1)
//
//    val temp = test1[0]
//    test1[0] = test1[2]
//    test1[2] = temp
//
//    println(test)
//
//    DoubleMatrix.of(3 by 3, 1, 2, 3, 4, 5, 6, 7, 8, 9).forEach(Selector.DIAGONAL) { println(it) }
//
//    println(IntMatrix.diagonal(9, 5, -3, 4).firstMinor(1, 2))
//    println(DoubleMatrix.diagonal(9.0, 5.0, -3.0, 4.0).firstMinor(1, 2))
//
//    val test = IntMatrix.ones(3 by 3)
//
//    println(test)
//
//    test += IntMatrix.ones(3 by 3)
//
//    println(test)
//
//    println(test + IntMatrix.ones(3 by 3))
//
//    val test = DoubleMatrix.of(2 by 2, 7, 6, 3, 9)
//
//    println(test.determinant())
//
//    println(DoubleMatrix.diagonal(9, 5, -3, 4).cofactor(1, 1))
//
//    println(test.adjugate())
}
