package org.kotrix.discrete.booleanAlgebraAST

data class Not(val expr: BooleanAlgebra) : BooleanAlgebra() {
    override val variables: Set<Variable>
        get() = expr.variables

    override fun stringify(): String =
        "!(${expr.stringify()})"

    override fun fullEval(value: Map<BooleanAlgebra, Constant>): Boolean {
        val res = expr.fullEval(value)

        return !res
    }
}