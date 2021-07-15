package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.extensions.cos
import org.kotrix.symbolic.funAST.extensions.sin

data class Sin(val a: Fun): Fun() {
    override val variables: Set<Variable>
        get() = this.a.variables

    override fun simplify(): Fun = sin(a.simplify())

    override fun stringify(): String = "sin(${a.stringify()})"

    override fun diff(by: Variable): Fun = cos(a) * a.diff(by)

    override fun fullEval(value: Map<Fun, Scalar>): Double =
        kotlin.math.sin(a.fullEval(value))

    override fun partialEval(value: Map<Fun, Scalar>): Fun =
        Sin(a.partialEval(value))

    override fun toString(): String = "Sin(${this.a})"

    override fun sub(replace: Variable, with: Fun): Fun = Sin(a.sub(replace, with))

    override fun copy(): Fun = Sin(this.a)
}