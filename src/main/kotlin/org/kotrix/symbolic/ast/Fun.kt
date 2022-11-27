package org.kotrix.symbolic.funAST

import org.kotrix.utils.Stringify
import kotlin.math.E
import org.kotrix.symbolic.funAST.extensions.*

sealed class Fun(
    open val variables: Set<Variable> = emptySet()
): Simplify<Fun>, Stringify, Differentiable<Variable, Fun>, EvalFun<Fun, Scalar, Fun> {
    val reciprocal: Fun
        get() = Power(this, (-1).scalar)

    val exp: Fun
        get() = Power(E.scalar, this)

    operator fun unaryPlus(): Fun = UnaryPlus(this)

    operator fun unaryMinus(): Fun = UnaryMinus(this)

    operator fun plus(other: Fun): Fun = Add(this, other)

    operator fun minus(other: Fun): Fun = Subtract(this, other)

    operator fun times(other: Fun): Fun = Times(this, other)

    operator fun div(other: Fun): Fun = Divide(this, other)

    fun eval(vararg values: Pair<Fun, Scalar>) = this.eval(mapOf(*values))

    fun evalAllAtZero(): Double {
        val map = emptyMap<Fun, Scalar>().toMutableMap()
        for (i in variables) {
            map[i] = 0.scalar
        }
        return this.fullEval(map)
    }

    fun evalAllAt(value: Number = 0): Fun {
        val map = emptyMap<Fun, Scalar>().toMutableMap()
        for (i in variables) {
            map[i] = value.scalar
        }
        return this.eval(map)
    }

    operator fun invoke(value: Map<Fun, Scalar>) = this.eval(value)

    operator fun invoke(vararg values: Pair<Fun, Scalar>) = this.eval(*values)

    abstract override fun simplify(): Fun

    fun simpleString(): String = this.simplify().stringify()

    abstract fun sub(replace: Variable, with: Fun): Fun
}