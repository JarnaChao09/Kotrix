package org.kotrix.discrete.graph

// TODO("REIMPLEMENT GRAPH INTERFACE WITH NEW EDGE AND VERTEX SEALED INTERFACES AND DATA CLASSES")
// TODO("FIGURE OUT HOW DSL WILL WORK TO MODEL INTERFACE")
interface Graph<D, V: GraphVertex<D>, E: GraphEdge<V>> : Iterable<V> {
    operator fun contains(value: D): Boolean
    operator fun contains(vertex: V): Boolean
    operator fun contains(edge: E): Boolean
}