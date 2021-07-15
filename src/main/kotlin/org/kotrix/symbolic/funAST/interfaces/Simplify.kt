package org.kotrix.symbolic.funAST.interfaces

import org.kotrix.symbolic.funAST.Fun

interface Simplify<X: Fun> {
    fun simplify(): X
}