package org.kotrix.discrete.graph

sealed interface GraphVertex<E> {
    var data: E
    var inDegree: Int
    var outDegree: Int
}

data class GenericVertex<E>(
    override var data: E,
    override var inDegree: Int = 0,
    override var outDegree: Int = 0,
) : GraphVertex<E>