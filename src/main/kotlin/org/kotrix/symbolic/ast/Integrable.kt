package org.kotrix.symbolic.ast

interface Integrable<V, T : Integrable<V, T>> {
    fun integrate(by: V): T
}