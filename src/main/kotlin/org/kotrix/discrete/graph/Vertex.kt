package org.kotrix.discrete.graph

data class Vertex<E>(val data: E, var degree: Int = 0)
