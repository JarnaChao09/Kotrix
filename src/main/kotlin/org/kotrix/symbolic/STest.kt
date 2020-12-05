package org.kotrix.symbolic

fun main() {
    val x by Var()

    val y = x pow 2

    println(y.diff(x).simpleString())

    println(y.diff(x).evalAllAtZero().simpleString())
}
