package org.kotrix.symbolic.ast

import kotlin.math.pow
import org.kotrix.symbolic.ast.extensions.*

data class Power(val base: Fun, val exponent: Fun): Fun() {
    override val variables: Set<Variable>
        get() = setOf(*this.base.variables.toTypedArray(), *this.exponent.variables.toTypedArray())

    override fun simplify(): Fun {
        val b = base.simplify()
        val e = exponent.simplify()
        return when {
            b == 0.scalar -> 0.scalar
            e == 0.scalar -> 1.scalar
            e == 1.scalar -> b
            b is Scalar && e is Scalar -> this.evalAllAtZero().scalar
            else -> b pow e
        }
    }

    override fun stringify(): String = "(${base.stringify()} ^ ${exponent.stringify()})"

    override fun diff(by: Variable): Fun =
        when {
            this.base is Scalar && this.exponent !is Scalar ->
                this * Ln(this.base) * this.exponent.diff(by)
            this.base !is Scalar && this.exponent is Scalar ->
                if (this.exponent == 1.scalar)
                    Ln(this.base) * this.base.diff(by)
                else
                    this.exponent * (this.base pow (this.exponent.value - 1).scalar) * this.base.diff(by)
            else -> {
                this.exponent * (this.base pow (this.exponent - 1.scalar)) * this.base.diff(by) + (this.base pow (this.exponent)) * Ln(
                    this.base
                ) * this.exponent.diff(by)
            }
        }

    override fun fullEval(value: Map<Fun, Scalar>): Double =
        base.fullEval(value).pow(exponent.fullEval(value))

    override fun partialEval(value: Map<Fun, Scalar>): Fun = base.partialEval(value) pow exponent.partialEval(value)

    override fun toString(): String = "Power(${this.base}, ${this.exponent})"

    override fun sub(replace: Variable, with: Fun): Fun = base.sub(replace, with) pow exponent.sub(replace, with)
}