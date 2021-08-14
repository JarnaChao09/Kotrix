package org.kotrix.discrete.booleanAlgebraAST

interface EvalBool<X, Y, R: BooleanAlgebra> {
    fun fullEval(value: Map<X, Y>): Boolean

    @Suppress("UNCHECKED_CAST")
    fun eval(value: Map<X, Y>): R =
            Constant(this.fullEval(value)) as R
}