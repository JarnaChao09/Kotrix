package org.kotrix.symbolic

interface Differentiable<V, T: Differentiable<V, T>> {
    fun diff(by: V): T
}