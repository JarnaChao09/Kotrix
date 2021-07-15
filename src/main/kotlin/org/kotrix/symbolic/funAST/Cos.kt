package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.extensions.cos
import org.kotrix.symbolic.funAST.extensions.sin

data class Cos(val a: Fun): Fun() {
    override val variables: Set<Variable>
        get() = this.a.variables

    override fun simplify(): Fun = cos(a.simplify())

    override fun stringify(): String = "cos(${a.stringify()})"

    override fun diff(by: Variable): Fun = -sin(a) * a.diff(by)

    override fun fullEval(value: Map<Fun, Scalar>): Double =
        kotlin.math.cos(a.fullEval(value))

    override fun partialEval(value: Map<Fun, Scalar>): Fun =
        Cos(a.partialEval(value))

    override fun toString(): String = "Cos(${this.a})"

    override fun sub(replace: Variable, with: Fun): Fun = Cos(a.sub(replace, with))

    override fun copy(): Fun = Cos(this.a)
}