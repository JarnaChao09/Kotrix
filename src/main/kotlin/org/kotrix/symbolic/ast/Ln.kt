package org.kotrix.symbolic.ast

import org.kotrix.symbolic.ast.extensions.*

data class Ln(val value: Fun): Fun() {
    override val variables: Set<Variable>
        get() = this.value.variables

    override fun simplify(): Fun {
        return when (val v = value.simplify()) {
            1.scalar -> 0.scalar
            Math.E.scalar -> 1.scalar
            0.scalar -> Double.MIN_VALUE.scalar
            else -> Ln(v)
        }
    }

    override fun stringify(): String = "ln(${value.stringify()})"

    override fun diff(by: Variable): Fun = this.value.reciprocal * this.value.diff(by)

    override fun fullEval(value: Map<Fun, Scalar>): Double = when(val calc = this.value.fullEval(value)) {
        0.0 -> Double.MIN_VALUE
        else -> kotlin.math.ln(calc)
    }

    override fun partialEval(value: Map<Fun, Scalar>): Fun = when (this.value.partialEval(value)) {
        0.0.scalar -> Double.MIN_VALUE.scalar
        else -> Ln(this.value.partialEval(value))
    }

    override fun toString(): String = "Ln(${this.value})"

    override fun sub(replace: Variable, with: Fun): Fun = Ln(value.sub(replace, with))
}