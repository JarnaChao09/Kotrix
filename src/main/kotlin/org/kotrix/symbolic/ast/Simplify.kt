package org.kotrix.symbolic.ast

interface Simplify<X: Fun> {
    fun simplify(): X
}