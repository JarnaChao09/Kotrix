package org.kotrix.symbolic.funAST

interface Integrable<V, T : Integrable<V, T>> {
    fun integrate(by: V): T
}