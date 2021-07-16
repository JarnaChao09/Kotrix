package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.extensions.pow
import org.kotrix.symbolic.funAST.extensions.scalar

data class Divide(val lhs: Fun, val rhs: Fun): Fun() {
    override val variables: Set<Variable>
        get() = setOf(*this.lhs.variables.toTypedArray(), *this.rhs.variables.toTypedArray())

    override fun simplify(): Fun {
        val l = lhs.simplify()
        val r = rhs.simplify()
        return when {
            l == 0.scalar && r != 0.scalar -> 0.scalar

            r == 1.scalar -> l

            l == r -> 1.scalar

            l is Scalar && r is Scalar && r.value != 0.0 -> this.evalAllAtZero().scalar

            else -> l / r
        }
    }

    override fun sub(replace: Variable, with: Fun): Fun = lhs.sub(replace, with) / rhs.sub(replace, with)

    override fun stringify(): String = "(${this.lhs.stringify()} / ${this.rhs.stringify()})"

    override fun diff(by: Variable): Fun = ((rhs * lhs.diff(by)) - (lhs * rhs.diff(by))) / (rhs pow 2)

    override fun partialEval(value: Map<Fun, Scalar>): Fun = lhs.partialEval(value) / rhs.partialEval(value)

    override fun fullEval(value: Map<Fun, Scalar>): Double = lhs.fullEval(value) / rhs.fullEval(value)

    override fun toString(): String = "Divide(${this.lhs}, ${this.rhs})"
}
