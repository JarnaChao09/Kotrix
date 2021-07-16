package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.extensions.cos
import org.kotrix.symbolic.funAST.extensions.sin

data class Cos(val angle: Fun): Fun() {
    override val variables: Set<Variable>
        get() = this.angle.variables

    override fun simplify(): Fun = cos(angle.simplify())

    override fun stringify(): String = "cos(${angle.stringify()})"

    override fun diff(by: Variable): Fun = -sin(angle) * angle.diff(by)

    override fun fullEval(value: Map<Fun, Scalar>): Double =
        kotlin.math.cos(this.angle.fullEval(value))

    override fun partialEval(value: Map<Fun, Scalar>): Fun =
        Cos(this.angle.partialEval(value))

    override fun toString(): String = "Cos(${this.angle})"

    override fun sub(replace: Variable, with: Fun): Fun = Cos(angle.sub(replace, with))
}