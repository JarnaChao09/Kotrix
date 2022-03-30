package org.kotrix.discrete.graph

fun main() {
//    val root = TreeNode(10)
//    root += TreeNode(20)
//    root += 100
//    root.children[0] += 80
//
//    println(root)
//
//    val binaryRoot = BinaryTreeNode(100)
//    binaryRoot[BinaryTreeNode.Tree.LEFT] = 200
//    binaryRoot[BinaryTreeNode.Tree.RIGHT] = 300
//
//    println(binaryRoot)
//
//    binaryRoot[BinaryTreeNode.Tree.RIGHT] = null
//
//    println(binaryRoot)
//
//    binaryRoot[BinaryTreeNode.Tree.RIGHT] = 400
//
//    binaryRoot[BinaryTreeNode.Tree.RIGHT]!![BinaryTreeNode.Tree.LEFT] = 500
//
//    binaryRoot.preOrder(::println)

    /*
         1
      2     3
    4   5
     */
    val binaryRoot = BinaryTreeNode(
        1,
        BinaryTreeNode(
            2,
            BinaryTreeNode(4),
            BinaryTreeNode(5)
        ),
        BinaryTreeNode(3)
    )

    binaryRoot.preOrder(::println)
    println()
    binaryRoot.inOrder(::println)
    println()
    binaryRoot.postOrder(::println)
}