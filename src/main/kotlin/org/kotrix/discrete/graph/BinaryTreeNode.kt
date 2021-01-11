package org.kotrix.discrete.graph

open class BinaryTreeNode<T>(
    data: T,
    private var leftNode: BinaryTreeNode<T>? = null,
    private var rightNode: BinaryTreeNode<T>? = null
):
    TreeNode<T>(
        data = data,
        childrenNodes = mutableListOf(leftNode, rightNode),
    ) {

    enum class Tree {
        LEFT, RIGHT
    }

    operator fun get(child: Tree): BinaryTreeNode<T>? = when(child) {
        Tree.LEFT -> leftNode
        Tree.RIGHT -> rightNode
    }

    operator fun set(child: Tree, other: BinaryTreeNode<T>?) = when(child) {
        Tree.LEFT -> leftNode = other
        Tree.RIGHT -> rightNode = other
    }

    operator fun set(child: Tree, other: T) = when(child) {
        Tree.LEFT -> leftNode = BinaryTreeNode(other)
        Tree.RIGHT -> rightNode = BinaryTreeNode(other)
    }

    override fun toString(): String =
        "${
            if(leftNode != null) 
                "$leftNode <- " 
            else 
                ""
        }$data${
            if(rightNode != null) 
                " -> $rightNode" 
            else 
                ""
        }"

    fun preOrder(visit: (T) -> Unit) {
        visit(this.data)
        this[Tree.LEFT]?.preOrder(visit)
        this[Tree.RIGHT]?.preOrder(visit)
    }

    fun inOrder(visit: (T) -> Unit) {
        this[Tree.LEFT]?.inOrder(visit)
        visit(this.data)
        this[Tree.RIGHT]?.inOrder(visit)
    }

    fun postOrder(visit: (T) -> Unit) {
        this[Tree.LEFT]?.postOrder(visit)
        this[Tree.RIGHT]?.postOrder(visit)
        visit(this.data)
    }
}