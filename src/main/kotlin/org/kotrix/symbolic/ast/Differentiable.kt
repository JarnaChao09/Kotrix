package org.kotrix.symbolic.ast

interface Differentiable<V, T: Differentiable<V, T>> {
    fun diff(by: V): T
}