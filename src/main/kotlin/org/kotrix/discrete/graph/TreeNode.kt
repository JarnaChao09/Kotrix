package org.kotrix.discrete.graph

class TreeNode<T>(val data: T, override val children: MutableList<TreeNode<T>> = mutableListOf()): Tree<T> {
    operator fun plusAssign(child: TreeNode<T>) {
        this.children.add(child)
    }

    operator fun plusAssign(childData: T) {
        this += TreeNode(childData)
    }

    override fun toString(): String = "$data [${children.joinToString(separator = " ") { 
        if (it.children.isEmpty()) {
            "${it.data}"
        } else {
            "$it"
        }
    }}]"
}