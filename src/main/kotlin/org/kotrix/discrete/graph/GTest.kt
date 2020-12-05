package org.kotrix.discrete.graph

fun main() {
    val root = TreeNode(10)
    root += TreeNode(20)
    root += 100
    root.children[0] += 10

    println(root)
}