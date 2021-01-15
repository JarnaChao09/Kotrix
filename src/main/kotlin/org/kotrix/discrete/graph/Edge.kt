package org.kotrix.discrete.graph

sealed class Edge<E, W: Weight>(open val origin: Vertex<E>, open val end: Vertex<E>, open val id: String, open val weight: W?) {
    data class UnDirected<E, W: Weight>(
        override val origin: Vertex<E>,
        override val end: Vertex<E>,
        override val id: String,
        override val weight: W? = null,
    ): Edge<E, W>(origin, end, id, weight)

    data class Directed<E, W: Weight>(
        override val origin: Vertex<E>,
        override val end: Vertex<E>,
        override val id: String,
        override val weight: W? = null,
    ): Edge<E, W>(origin, end, id, weight)
}
