package org.kotrix.symbolic.funAST

interface Simplify<X: Fun> {
    fun simplify(): X
}