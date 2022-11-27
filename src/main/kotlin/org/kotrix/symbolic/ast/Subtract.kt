package org.kotrix.symbolic.ast

import org.kotrix.symbolic.ast.extensions.*

data class Subtract(val lhs: Fun, val rhs: Fun): Fun() {
    override val variables: Set<Variable>
        get() = setOf(*this.lhs.variables.toTypedArray(), *this.rhs.variables.toTypedArray())

    override fun simplify(): Fun {
//        println("calling subtract simplify: $this")
        val l = lhs.simplify()
        val r = rhs.simplify()
        return when {
            l == 0.scalar -> -r

            r == 0.scalar -> l

            l is Scalar && r is Scalar -> this.evalAllAtZero().scalar

            else -> l - r
        }
    }

    override fun sub(replace: Variable, with: Fun): Fun = lhs.sub(replace, with) - rhs.sub(replace, with)

    override fun stringify(): String = "(${lhs.stringify()} - ${rhs.stringify()}"

    override fun diff(by: Variable): Fun = this.lhs.diff(by) - this.rhs.diff(by)

    override fun partialEval(value: Map<Fun, Scalar>): Fun = this.lhs.partialEval(value) - rhs.partialEval(value)

    override fun fullEval(value: Map<Fun, Scalar>): Double = this.lhs.fullEval(value) - rhs.fullEval(value)

    override fun toString(): String = "Subtract(${this.lhs}, ${this.rhs})"
}