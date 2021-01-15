package org.kotrix.discrete

fun main() {
    println(listOf(1,2,3).cartesianProduct(listOf(1.0,2.0,3.0,4.0)).toList())

    println(setOf(1,2,3).powerSet)
    println(setOf(1,2,3).powerSetSize)
}
