package org.kotrix.discrete.booleanAlgebraAST

data class And(
    val leftop: BooleanAlgebra,
    val rightop: BooleanAlgebra
) : BooleanAlgebra() {
    override val variables: Set<Variable>
        get() = leftop.variables union rightop.variables

    override fun stringify(): String =
        "(${leftop.stringify()} && ${rightop.stringify()})"

    override fun toLaTeX(): String =
        "(${leftop.toLaTeX()} \\land ${rightop.toLaTeX()})"

    override fun fullEval(value: Map<BooleanAlgebra, Constant>): Boolean {
        val left = leftop.fullEval(value)
        val right = rightop.fullEval(value)

        return left && right
    }
}