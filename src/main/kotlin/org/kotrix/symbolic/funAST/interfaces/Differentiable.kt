package org.kotrix.symbolic.funAST.interfaces

interface Differentiable<V, T: Differentiable<V, T>> {
    fun diff(by: V): T
}