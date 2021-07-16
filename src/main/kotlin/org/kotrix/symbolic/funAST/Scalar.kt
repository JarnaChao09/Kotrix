package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.extensions.scalar

data class Scalar(val value: Double): Fun() {
    constructor(n: Number): this(n.toDouble())

    override fun stringify(): String = when (value) {
        kotlin.math.PI -> "PI"
        kotlin.math.E -> "E"
        else -> "$value"
    }

    override fun diff(by: Variable): Fun = 0.scalar

    override fun fullEval(value: Map<Fun, Scalar>): Double = this.value

    override fun partialEval(value: Map<Fun, Scalar>): Fun = this.eval(value)

    override fun toString(): String = "Constant(${this.value})"

    override fun simplify(): Fun = this

    override fun sub(replace: Variable, with: Fun): Fun = this
}