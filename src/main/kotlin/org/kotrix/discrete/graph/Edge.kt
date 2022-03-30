package org.kotrix.discrete.graph

sealed interface GraphEdge<V : GraphVertex<*, *>> {
    var initialVertex: V
    var arrivalVertex: V

    operator fun component1(): V = this.initialVertex
    operator fun component2(): V = this.arrivalVertex
}

sealed interface WeightedGraphEdge<W : Comparable<W>, V : GraphVertex<*, *>> : GraphEdge<V> {
    var weight: W

    operator fun component3(): W = this.weight
}

sealed interface DirectionalGraphEdge<V : GraphVertex<*, *>> : GraphEdge<V> {
    var head: V
    var tail: V

    override var initialVertex: V
        get() = this.head
        set(value) {
            this.head = value
        }

    override var arrivalVertex: V
        get() = this.tail
        set(value) {
            this.tail = value
        }

    override fun component1(): V = this.head
    override fun component2(): V = this.tail
}

data class GenericEdge<V : GraphVertex<*, *>>(
    override var initialVertex: V,
    override var arrivalVertex: V,
) : GraphEdge<V>

data class GenericWeightedEdge<W : Comparable<W>, V : GraphVertex<*, *>>(
    override var initialVertex: V,
    override var arrivalVertex: V,
    override var weight: W,
) : WeightedGraphEdge<W, V>

data class GenericDirectionalEdge<V : GraphVertex<*, *>>(
    override var head: V,
    override var tail: V,
) : DirectionalGraphEdge<V>

data class GenericDirectionalWeightedEdge<W : Comparable<W>, V : GraphVertex<*, *>>(
    override var head: V,
    override var tail: V,
    override var weight: W,
) : WeightedGraphEdge<W, V>, DirectionalGraphEdge<V>