package org.kotrix.symbolic.funAST

interface Differentiable<V, T: Differentiable<V, T>> {
    fun diff(by: V): T
}