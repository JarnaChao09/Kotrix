package org.kotrix.discrete.graph

interface Tree<T> {
    val children: List<Tree<T>>
}