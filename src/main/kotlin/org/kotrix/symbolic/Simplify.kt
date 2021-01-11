package org.kotrix.symbolic

interface Simplify<X: Fun> {
    fun simplify(): X
}