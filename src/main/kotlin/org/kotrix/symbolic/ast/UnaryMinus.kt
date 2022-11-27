package org.kotrix.symbolic.ast

// TODO determine is UnaryMinus object is a better option over Times(-1, value)
data class UnaryMinus(val value: Fun): Fun() {
    override val variables: Set<Variable>
        get() = setOf(*this.value.variables.toTypedArray())

    override fun simplify(): Fun = UnaryMinus(this.value.simplify())

    override fun sub(replace: Variable, with: Fun): Fun = UnaryMinus(this.value.sub(replace, with))

    override fun stringify(): String = "(-${this.value.stringify()})"

    override fun diff(by: Variable): Fun = UnaryMinus(this.value.diff(by))

    override fun partialEval(value: Map<Fun, Scalar>): Fun = UnaryMinus(this.value.partialEval(value))

    override fun fullEval(value: Map<Fun, Scalar>): Double = -this.value.fullEval(value)

    override fun toString(): String = "UnaryMinus(${this.value})"
}
