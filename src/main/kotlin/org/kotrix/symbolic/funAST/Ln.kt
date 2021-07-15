package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.extensions.scalar

data class Ln(val a: Fun): Fun() {
    override val variables: Set<Variable>
        get() = this.a.variables

    override fun simplify(): Fun {
        val a_ = a.simplify()
        return when (a_) {
            1.scalar -> 0.scalar
            Math.E.scalar -> 1.scalar
            0.scalar -> Double.MIN_VALUE.scalar
            else -> Ln(a_)
        }
    }

    override fun stringify(): String = "ln(${a.stringify()})"

    override fun diff(by: Variable): Fun = this.a.reciprocal * this.a.diff(by)

    override fun fullEval(value: Map<Fun, Scalar>): Double = when(val calc = a.fullEval(value)) {
        0.0 -> Double.MIN_VALUE
        else -> kotlin.math.ln(calc)
    }

    override fun partialEval(value: Map<Fun, Scalar>): Fun = when (a.partialEval(value)) {
        0.0.scalar -> Double.MIN_VALUE.scalar
        else -> Ln(a.partialEval(value))
    }

    override fun toString(): String = "Ln(${this.a})"

    override fun sub(replace: Variable, with: Fun): Fun = Ln(a.sub(replace, with))

    override fun copy(): Fun = Ln(this.a)
}