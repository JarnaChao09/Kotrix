package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.extensions.pow
import org.kotrix.symbolic.funAST.extensions.scalar
import kotlin.math.pow


data class Power(val base: Fun, val exponent: Fun): Fun() {
    override val variables: Set<Variable>
        get() = setOf(*this.base.variables.toTypedArray(), *this.exponent.variables.toTypedArray())

    override fun simplify(): Fun {
        val base_ = base.simplify()
        val exp_ = exponent.simplify()
        return when {
            base_ == 0.scalar -> 0.scalar
            exp_ == 0.scalar -> 1.scalar
            exp_ == 1.scalar -> base_
            base_ is Scalar && exp_ is Scalar -> this.evalAllAtZero()
            else -> base_ pow exp_
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

    override fun copy(): Fun = Power(this.base, this.exponent)
}