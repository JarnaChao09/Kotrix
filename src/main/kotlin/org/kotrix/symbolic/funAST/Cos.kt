package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.extensions.*

data class Cos(val angle: Fun): Fun() {
    override val variables: Set<Variable>
        get() = this.angle.variables

    override fun simplify(): Fun {
        val a = angle.simplify()

        return when {
            a is Scalar -> {
                when (a) {
                    0.scalar, (2.0 * kotlin.math.PI).scalar -> 1.scalar
                    (kotlin.math.PI / 2.0).scalar, ((3.0 * kotlin.math.PI) / 2.0).scalar -> 0.scalar
                    (kotlin.math.PI).scalar -> (-1).scalar
                    else -> kotlin.math.cos(a.value).scalar
                }
            }

            else -> cos(a)
        }
    }

    override fun stringify(): String = "cos(${angle.stringify()})"

    override fun diff(by: Variable): Fun = -sin(angle) * angle.diff(by)

    override fun fullEval(value: Map<Fun, Scalar>): Double =
        kotlin.math.cos(this.angle.fullEval(value))

    override fun partialEval(value: Map<Fun, Scalar>): Fun =
        Cos(this.angle.partialEval(value))

    override fun toString(): String = "Cos(${this.angle})"

    override fun sub(replace: Variable, with: Fun): Fun = Cos(angle.sub(replace, with))
}