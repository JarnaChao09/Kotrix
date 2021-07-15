package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.extensions.scalar
import org.kotrix.symbolic.funAST.interfaces.Differentiable
import org.kotrix.symbolic.funAST.interfaces.EvalFun
import org.kotrix.symbolic.funAST.interfaces.Simplify
import org.kotrix.utils.Stringify
import kotlin.math.E

sealed class Fun(
    open val variables: Set<Variable> = emptySet()
): Simplify<Fun>, Stringify, Differentiable<Variable, Fun>, EvalFun<Fun, Scalar, Fun> {
    val reciprocal: Fun
        get() = Power(this, (-1).scalar)

    val exp: Fun
        get() = Power(E.scalar, this)

    operator fun unaryPlus(): Fun = this

    operator fun unaryMinus(): Fun = Product((-1).scalar, this)

    operator fun plus(other: Fun): Fun = Sum(this, other)

    operator fun minus(other: Fun): Fun = Sum(this, -other)

    operator fun times(other: Fun): Fun = Product(this, other)

    operator fun div(other: Fun): Fun = Product(this, other.reciprocal)

    fun eval(vararg values: Pair<Fun, Scalar>) = this.eval(mapOf(*values))

    fun evalAllAtZero(): Fun {
        val map = emptyMap<Fun, Scalar>().toMutableMap()
        for (i in variables) {
            map[i] = 0.scalar
        }
        return this.eval(map)
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

    override fun simplify(): Fun = this

    fun simpleString(): String = this.simplify().stringify()

    abstract fun sub(replace: Variable, with: Fun): Fun

    abstract fun copy(): Fun
}