package org.kotrix.discrete.graph

sealed interface GraphVertex<E, I> {
    var data: E
    var id: I
    var inDegree: Int
    var outDegree: Int
}

data class GenericVertex<E, I>(
    override var data: E,
    override var id: I,
    override var inDegree: Int = 0,
    override var outDegree: Int = 0,
) : GraphVertex<E, I>