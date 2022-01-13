package org.kotrix.discrete.booleanAlgebraAST

data class Iff(
    val leftexpr: BooleanAlgebra,
    val rightexpr: BooleanAlgebra
) : BooleanAlgebra() {
    override val variables: Set<Variable>
        get() = leftexpr.variables union rightexpr.variables

    override fun stringify(): String =
        "(${leftexpr.stringify()} <=> ${rightexpr.stringify()})"

    override fun fullEval(value: Map<BooleanAlgebra, Constant>): Boolean {
        val left = leftexpr.fullEval(value)
        val right = rightexpr.fullEval(value)

        return left == right
    }
}