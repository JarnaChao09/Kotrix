package org.kotrix.discrete.graph

open class TreeNode<T>(val data: T, protected val childrenNodes: MutableList<TreeNode<T>?> = mutableListOf()): Tree<T> {
    override val children: List<TreeNode<T>>
        get() = this.childrenNodes.map { it!! }

    operator fun plusAssign(child: TreeNode<T>) {
        this.childrenNodes.add(child)
    }

    operator fun plusAssign(childData: T) {
        this += TreeNode(childData)
    }

    override fun toString(): String = "$data [${childrenNodes.joinToString(separator = " ") { 
        if (it?.childrenNodes?.isEmpty() == true) {
            "${it.data}"
        } else {
            "$it"
        }
    }}]"
}