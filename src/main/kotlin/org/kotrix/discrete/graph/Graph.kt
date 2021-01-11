package org.kotrix.discrete.graph

interface Graph<VE, EW : Weight, ET: Edge<VE, EW>> {
    val edges: Set<ET>

    fun addEdge(origin: Vertex<VE>, target: Vertex<VE>)

    fun getEdge(origin: Vertex<VE>, target: Vertex<VE>)

    fun getEdgeWeight(origin: Vertex<VE>, target: Vertex<VE>)
}