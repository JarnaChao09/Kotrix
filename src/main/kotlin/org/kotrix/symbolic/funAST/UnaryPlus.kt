package org.kotrix.symbolic.funAST

data class UnaryPlus(val value: Fun): Fun() {
    override val variables: Set<Variable>
        get() = setOf(*this.value.variables.toTypedArray())

    override fun simplify(): Fun = UnaryPlus(this.simplify())

    override fun sub(replace: Variable, with: Fun): Fun = UnaryPlus(this.value.sub(replace, with))

    override fun stringify(): String = "(+${this.value.stringify()})"

    override fun diff(by: Variable): Fun = UnaryPlus(this.value.diff(by))

    override fun partialEval(value: Map<Fun, Scalar>): Fun = UnaryPlus(this.value.partialEval(value))

    override fun fullEval(value: Map<Fun, Scalar>): Double = +this.value.fullEval(value)

    override fun toString(): String = "UnaryPlus(${this.value})"
}
