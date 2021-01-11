package org.kotrix.discrete.graph

sealed class Weight {
    object UnWeighted

    data class Numeric(val weight: Double): Weight(), Comparable<Numeric> {
        override fun compareTo(other: Numeric): Int = this.weight.compareTo(other.weight)
    }

    // TODO better naming scheme for any type weight
    data class Weighted<E: Comparable<E>>(val weight: E): Weight(), Comparable<Weighted<E>> {
        override fun compareTo(other: Weighted<E>): Int = this.weight.compareTo(other.weight)
    }
}
