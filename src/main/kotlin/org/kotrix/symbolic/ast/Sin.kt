package org.kotrix.symbolic.ast

import org.kotrix.symbolic.ast.extensions.*

data class Sin(val angle: Fun) : Fun() {
    override val variables: Set<Variable>
        get() = this.angle.variables

    override fun simplify(): Fun {
        return when (val a = angle.simplify()) {
            is Scalar -> {
                when (a) {
                    0.scalar, (2.0 * kotlin.math.PI).scalar, (kotlin.math.PI).scalar -> 0.scalar
                    (kotlin.math.PI / 2.0).scalar -> 1.scalar
                    ((3.0 * kotlin.math.PI) / 2.0).scalar -> (-1).scalar
                    else -> kotlin.math.sin(a.value).scalar
                }
            }

            else -> sin(a)
        }
    }

    override fun stringify(): String = "sin(${angle.stringify()})"

    override fun diff(by: Variable): Fun = cos(angle) * angle.diff(by)

    override fun fullEval(value: Map<Fun, Scalar>): Double =
        kotlin.math.sin(angle.fullEval(value))

    override fun partialEval(value: Map<Fun, Scalar>): Fun =
        Sin(angle.partialEval(value))

    override fun toString(): String = "Sin(${this.angle})"

    override fun sub(replace: Variable, with: Fun): Fun = Sin(angle.sub(replace, with))
}