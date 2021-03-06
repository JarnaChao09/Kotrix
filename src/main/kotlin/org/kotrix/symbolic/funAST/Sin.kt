package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.extensions.cos
import org.kotrix.symbolic.funAST.extensions.scalar
import org.kotrix.symbolic.funAST.extensions.sin

data class Sin(val angle: Fun): Fun() {
    override val variables: Set<Variable>
        get() = this.angle.variables

    override fun simplify(): Fun {
        val a = angle.simplify()

        return when {
            a is Scalar -> {
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