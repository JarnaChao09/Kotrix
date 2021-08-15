package org.kotrix.vector

import org.kotrix.utils.sliceTo

fun main() {
    val test1 = vectorOf<Int>()
    val test2 = mutableVectorOf(1, 2, 3)
    val test3 = Vector(10) { 0 }.toMutableVector()
    println(test1)
    println(test2)
    println(test3)

    test2[-1] = 20
    test3[2 sliceTo 5] = Vector(3) { it + 1 }

    println(test2)
    println(test3)

    val mutableVector = mutableVectorOf(1, 2, 3, 4, 5)
    val subVector = mutableVector.subVector(1, 3)

    println(mutableVector)
    println(subVector)

    mutableVector[2] = 100

    println(mutableVector)
    println(subVector)

    subVector[0] = 200

    println(mutableVector)
    println(subVector)
}