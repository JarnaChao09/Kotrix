package org.kotrix.discrete.booleanAlgebraAST

data class To(
    val sufficient: BooleanAlgebra,
    val necessary: BooleanAlgebra
) : BooleanAlgebra() {
    override val variables: Set<Variable>
        get() = sufficient.variables union necessary.variables

    override fun stringify(): String =
        "(${sufficient.stringify()} ==> ${necessary.stringify()})"

    override fun fullEval(value: Map<BooleanAlgebra, Constant>): Boolean {
        val left = sufficient.fullEval(value)
        val right = necessary.fullEval(value)

        return !left || right
    }
}