package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.extensions.*

data class Divide(val numerator: Fun, val denominator: Fun): Fun() {
    override val variables: Set<Variable>
        get() = setOf(*this.numerator.variables.toTypedArray(), *this.denominator.variables.toTypedArray())

    override fun simplify(): Fun {
        val l = numerator.simplify()
        val r = denominator.simplify()
        return when {
            l == 0.scalar && r != 0.scalar -> 0.scalar

            r == 1.scalar -> l

            l == r -> 1.scalar

            l is Scalar && r is Scalar && r.value != 0.0 -> this.evalAllAtZero().scalar

            else -> l / r
        }
    }

    override fun sub(replace: Variable, with: Fun): Fun = numerator.sub(replace, with) / denominator.sub(replace, with)

    override fun stringify(): String = "(${this.numerator.stringify()} / ${this.denominator.stringify()})"

    override fun diff(by: Variable): Fun = ((denominator * numerator.diff(by)) - (numerator * denominator.diff(by))) / (denominator pow 2)

    override fun partialEval(value: Map<Fun, Scalar>): Fun = numerator.partialEval(value) / denominator.partialEval(value)

    override fun fullEval(value: Map<Fun, Scalar>): Double = numerator.fullEval(value) / denominator.fullEval(value)

    override fun toString(): String = "Divide(${this.numerator}, ${this.denominator})"
}
