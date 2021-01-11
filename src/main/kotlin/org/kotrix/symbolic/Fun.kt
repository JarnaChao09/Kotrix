package org.kotrix.symbolic

import org.kotrix.utils.Stringify
import kotlin.math.E
import kotlin.math.pow

sealed class Fun(
    open val variables: Set<Variable> = emptySet()
): Simplify<Fun>, Stringify, Differentiable<Variable, Fun>, EvalFun<Fun, Constant, Fun> {
    val reciprocal: Fun
        get() = Power(this, (-1).const)

    val exp: Fun
        get() = Power(E.const, this)

    operator fun unaryPlus(): Fun = this

    operator fun unaryMinus(): Fun = Product((-1).const, this)

    operator fun plus(other: Fun): Fun = Sum(this, other)

    operator fun minus(other: Fun): Fun = Sum(this, -other)

    operator fun times(other: Fun): Fun = Product(this, other)

    operator fun div(other: Fun): Fun = Product(this, other.reciprocal)

    fun eval(vararg values: Pair<Fun, Constant>) = this.eval(mapOf(*values))

    fun evalAllAtZero(): Fun {
        val map = emptyMap<Fun, Constant>().toMutableMap()
        for (i in variables) {
            map[i] = 0.const
        }
        return this.eval(map)
    }

    fun evalAllAt(value: Number = 0): Fun {
        val map = emptyMap<Fun, Constant>().toMutableMap()
        for (i in variables) {
            map[i] = value.const
        }
        return this.eval(map)
    }

    operator fun invoke(value: Map<Fun, Constant>) = this.eval(value)

    operator fun invoke(vararg values: Pair<Fun, Constant>) = this.eval(*values)

    override fun simplify(): Fun = this

    fun simpleString(): String = this.simplify().stringify()

    abstract fun sub(replace: Variable, with: Fun): Fun

    abstract fun copy(): Fun
}

data class Constant(val value: Double): Fun() {
    constructor(n: Number): this(n.toDouble())

    override fun stringify(): String = when (value) {
        kotlin.math.PI -> "PI"
        kotlin.math.E -> "E"
        else -> "$value"
    }

    override fun diff(by: Variable): Fun = 0.const

    override fun fullEval(value: Map<Fun, Constant>): Double = this.value

    override fun partialEval(value: Map<Fun, Constant>): Fun = this.eval(value)

    override fun toString(): String = "Constant(${this.value})"

    override fun sub(replace: Variable, with: Fun): Fun = this

    override fun copy(): Fun = Constant(this.value)
}

data class Variable(val name: String): Fun() {
    override val variables: Set<Variable>
        get() = setOf(this)

    class NoValueException(x: Variable): Exception("No Value Given for ${x.stringify()}")

    override fun stringify(): String = name

    override fun diff(by: Variable): Fun =
        if (this == by) 1.const else 0.const

    override fun fullEval(value: Map<Fun, Constant>): Double {
        loop@for ((i, j) in value) {
            when(i) {
                this -> return j.value
                else -> continue@loop
            }
        }
        throw NoValueException(this)
    }

    override fun partialEval(value: Map<Fun, Constant>): Fun {
        loop@for ((i, j) in value) {
            when(i) {
                this -> return j
                else -> continue@loop
            }
        }
        return this
    }

    override fun toString(): String = this.name

    override fun sub(replace: Variable, with: Fun): Fun =
        if (this == replace) with else this

    override fun copy(): Fun = Variable(this.name)
}

data class Sum(val a: Fun, val b: Fun): Fun() {
    override val variables: Set<Variable>
        get() = setOf(*this.a.variables.toTypedArray(), *this.b.variables.toTypedArray())

    override fun simplify(): Fun {
        val a_ = a.simplify()
        val b_ = b.simplify()
        return when {
            a_ == 0.const -> b_

            b_ == 0.const -> a_

//            a_ is Product && b_ is Product -> { // cancels out lines 225 && 228
//                when {
//                    a_.a.simplify() == b_.a.simplify() -> (a_.b.simplify() + b_.b.simplify()).simplify() * a_.a.simplify()
//                    a_.b.simplify() == b_.a.simplify() -> (a_.a.simplify() + b_.b.simplify()).simplify() * a_.b.simplify()
//                    a_.a.simplify() == b_.b.simplify() -> (a_.b.simplify() + b_.a.simplify()).simplify() * a_.a.simplify()
//                    a_.b.simplify() == b_.b.simplify() -> (a_.a.simplify() + b_.a.simplify()).simplify() * a_.b.simplify()
//                    else -> a_ + b_
//                }
//            }

            a_ is Sum -> {
                when(b_) {
                    is Product -> {
                        when {
                            a_.a is Product -> {
                                when {
                                    a_.a.a.simplify() == b_.a.simplify() ->
                                        ((a_.a.a.simplify() * (a_.a.b.simplify() + b_.b.simplify()).simplify()).simplify() + a_.b.simplify()).simplify()
                                    a_.a.b.simplify() == b_.a.simplify() ->
                                        (((a_.a.a.simplify() + b_.b.simplify()).simplify() * a_.a.b.simplify()).simplify() + a_.b.simplify()).simplify()
                                    a_.a.a.simplify() == b_.b.simplify() ->
                                        ((a_.a.a.simplify() * (a_.a.b.simplify() + b_.a.simplify()).simplify()).simplify() + a_.b.simplify()).simplify()
                                    a_.a.b.simplify() == b_.b.simplify() ->
                                        (((a_.a.a.simplify() + b_.a.simplify()).simplify() * a_.a.b.simplify()).simplify() + a_.b.simplify()).simplify()
                                    else -> a_ + b_
                                }
                            }
                            a_.b is Product -> {
                                when {
                                    a_.b.a.simplify() == b_.a.simplify() ->
                                        ((a_.b.a.simplify() * (a_.b.b.simplify() + b_.b.simplify()).simplify()).simplify() + a_.a.simplify()).simplify()
                                    a_.b.b.simplify() == b_.a.simplify() ->
                                        (((a_.b.a.simplify() + b_.b.simplify()).simplify() * a_.b.b.simplify()).simplify() + a_.a.simplify()).simplify()
                                    a_.b.a.simplify() == b_.b.simplify() ->
                                        ((a_.b.a.simplify() * (a_.b.b.simplify() + b_.a.simplify()).simplify()).simplify() + a_.a.simplify()).simplify()
                                    a_.b.b.simplify() == b_.b.simplify() ->
                                        (((a_.b.a.simplify() + b_.a.simplify()).simplify() * a_.b.b.simplify()).simplify() + a_.a.simplify()).simplify()
                                    else -> a_ + b_
                                }
                            }
                            else -> a_ + b_
                        }
                    }
                    is Power -> {
                        val a_a_ = a_.a.simplify()
                        val a_b_ = a_.b.simplify()
                        when {
                            a_a_ is Product -> {
                                val a_a_a_ = a_a_.a.simplify()
                                val a_a_b_ = a_a_.b.simplify()
                                when {
                                    a_a_a_ == b_ -> {
                                        ((a_a_b_ + 1).simplify() * b_) + a_b_
                                    }
                                    a_a_b_ == b_ -> {
                                        ((a_a_a_ + 1).simplify() * b_) + a_b_
                                    }
                                    else -> {
                                        a_ + b_
                                    }
                                }
                            }
                            a_b_ is Product -> {
                                val a_b_a_ = a_b_.a.simplify()
                                val a_b_b_ = a_b_.b.simplify()
                                when {
                                    a_b_a_ == b_ -> {
                                        ((a_b_b_ + 1).simplify() * b_) + a_a_
                                    }
                                    a_b_b_ == b_ -> {
                                        ((a_b_a_ + 1).simplify() * b_) + a_a_
                                    }
                                    else -> {
                                        a_ + b_
                                    }
                                }
                            }
                            else -> {
                                a_ + b_
                            }
                        }
                    }
//                    is Constant -> {
//                        TODO()
//                    }
                    is Variable -> {
                        TODO()
                    }
                    is Sum -> {
                        TODO()
                    }
                    is Ln -> {
                        TODO()
                    }
                    is Sin -> {
                        TODO()
                    }
                    is Cos -> {
                        TODO()
                    }
                    else -> a_ + b_
                }
            }

            a_ is Constant && b_ is Constant && a_.value != kotlin.math.PI && b_.value != kotlin.math.PI -> this.evalAllAtZero()

            else -> a_ + b_
        }
    }

    override fun stringify(): String = "(${a.stringify()} + ${b.stringify()})"

    override fun diff(by: Variable): Fun = this.a.diff(by) + this.b.diff(by)

    override fun fullEval(value: Map<Fun, Constant>): Double = a.fullEval(value) + b.fullEval(value)

    override fun partialEval(value: Map<Fun, Constant>): Fun = a.partialEval(value) + b.partialEval(value)

    override fun toString(): String = "Sum(${this.a}, ${this.b})"

    override fun sub(replace: Variable, with: Fun): Fun = a.sub(replace, with) + b.sub(replace, with)

    override fun copy(): Fun = Sum(this.a, this.b)
}

data class Product(val a: Fun, val b: Fun): Fun() {
    override val variables: Set<Variable>
        get() = setOf(*this.a.variables.toTypedArray(), *this.b.variables.toTypedArray())

    override fun simplify(): Fun {
        val a_ = a.simplify()
        val b_ = b.simplify()
        return when {
            a_ == 0.const || b_ == 0.const -> 0.const

            a_ == 1.const -> b_

            b_ == 1.const -> a_

            a_ is Sum && b_ !is Sum ->
                ((a_.a.simplify() * b_).simplify() + (a_.b.simplify() * b_).simplify()).simplify()

            a_ !is Sum && b_ is Sum ->
                ((b_.a.simplify() * a_).simplify() + (b_.b.simplify() * a_).simplify()).simplify()

            a_ is Sum && b_ is Sum ->
                ((a_.a.simplify() * b_.a.simplify()).simplify() + (a_.a.simplify() * b_.b.simplify()).simplify() + (a_.b.simplify() * b_.a.simplify()).simplify() + (a_.b.simplify() * b_.b.simplify()).simplify()).simplify()

            a_ is Power && b_ == a_.base.simplify() -> (a_.base.simplify() pow ((a_.exponent.simplify() + 1.const).simplify())).simplify()

            b_ is Power && a_ == b_.base.simplify() -> (b_.base.simplify() pow ((b_.exponent.simplify() + 1.const).simplify())).simplify()

            a_ is Power && b_ is Power && a_.base.simplify() == b_.base.simplify() ->
                a_.base.simplify() pow ((a_.exponent.simplify() + b_.exponent.simplify()).simplify())

            a_ is Power && b_ is Product ->
                when {
                    b_.a.simplify() is Power -> {
                        (a_ * b_.a.simplify()).simplify() * b_.b.simplify()
                    }
                    b_.b.simplify() is Power -> {
                        b_.a.simplify() * (a_ * b_.b.simplify()).simplify()
                    }
                    b_.a.simplify() == a_.base.simplify() -> {
                        b_.b.simplify() * (a_.base.simplify() pow ((a_.exponent.simplify() + 1.const).simplify()))
                    }
                    b_.b.simplify() == a_.base.simplify() -> {
                        b_.a.simplify() * (a_.base.simplify() pow ((a_.exponent.simplify() + 1.const).simplify()))
                    }
                    else -> {
                        a_.simplify() * b_.simplify()
                    }
                }.simplify()

            b_ is Power && a_ is Product ->
                when {
                    a_.a.simplify() is Power -> {
                        a_.b.simplify() * (b_ * a_.a.simplify()).simplify()
                    }
                    a_.b.simplify() is Power -> {
                        a_.a.simplify() * (b_ * a_.b.simplify()).simplify()
                    }
                    a_.a.simplify() == b_.base.simplify() -> {
                        a_.b.simplify() * (b_.base.simplify() pow ((b_.exponent.simplify() + 1.const).simplify()))
                    }
                    a_.b.simplify() == b_.base.simplify() -> {
                        a_.a.simplify() * (b_.base.simplify() pow ((b_.exponent.simplify() + 1.const).simplify()))
                    }
                    else -> {
                        a_.simplify() * b_.simplify()
                    }
                }.simplify()

            a_ is Product && b_ is Product -> {
                when {
                    a_.a.simplify() is Power -> {
                        if (b_.a.simplify() is Power) {
                            a_.b.simplify() * b_.b.simplify() * (a_.a.simplify() * b_.a.simplify()).simplify()
                        } else {
                            a_.b.simplify() * b_.a.simplify() * (a_.a.simplify() * b_.b.simplify()).simplify()
                        }
                    }
                    a_.b.simplify() is Power -> {
                        if (b_.a.simplify() is Power) {
                            a_.a.simplify() * b_.b.simplify() * (a_.b.simplify() * b_.a.simplify()).simplify()
                        } else {
                            a_.a.simplify() * b_.a.simplify() * (a_.b.simplify() * b_.b.simplify()).simplify()
                        }
                    }
                    b_.a.simplify() is Power -> {
                        if (a_.a.simplify() is Power) {
                            b_.b.simplify() * a_.b.simplify() * (b_.a.simplify() * a_.a.simplify()).simplify()
                        } else {
                            b_.b.simplify() * a_.a.simplify() * (b_.a.simplify() * a_.b.simplify()).simplify()
                        }
                    }
                    b_.b.simplify() is Power -> {
                        if (a_.a.simplify() is Power) {
                            b_.a.simplify() * a_.b.simplify() * (b_.b.simplify() * a_.a.simplify()).simplify()
                        } else {
                            b_.a.simplify() * a_.a.simplify() * (b_.b.simplify() * a_.b.simplify()).simplify()
                        }
                    }
                    else -> a_ * b_
                }.simplify()
            }

            a_ is Constant && b_ is Product -> {
                if (b_.a.simplify() is Constant) {
                    (a_ * b_.a.simplify()).simplify() * b_.b.simplify()
                } else {
                    (a_ * b_.b.simplify()).simplify() * b_.a.simplify()
                }
            }

            a_ is Constant &&
                    b_ is Constant &&
                    a_.value != kotlin.math.PI &&
                    b_.value != kotlin.math.PI ->
                this.evalAllAtZero()

            else -> a_ * b_
        }
    }

    override fun stringify(): String = "(${this.a.stringify()} * ${this.b.stringify()})"

    override fun diff(by: Variable): Fun = this.a * this.b.diff(by) + this.b * this.a.diff(by)

    override fun fullEval(value: Map<Fun, Constant>): Double = a.fullEval(value) * b.fullEval(value)

    override fun partialEval(value: Map<Fun, Constant>): Fun = a.partialEval(value) * b.partialEval(value)

    override fun toString(): String = "Product(${this.a}, ${this.b})"

    override fun sub(replace: Variable, with: Fun): Fun = a.sub(replace, with) * b.sub(replace, with)

    override fun copy(): Fun = Product(this.a, this.b)
}

data class Power(val base: Fun, val exponent: Fun): Fun() {
    override val variables: Set<Variable>
        get() = setOf(*this.base.variables.toTypedArray(), *this.exponent.variables.toTypedArray())

    override fun simplify(): Fun {
        val base_ = base.simplify()
        val exp_ = exponent.simplify()
        return when {
            base_ == 0.const -> 0.const
            exp_ == 0.const -> 1.const
            exp_ == 1.const -> base_
            base_ is Constant && exp_ is Constant -> this.evalAllAtZero()
            else -> base_ pow exp_
        }
    }

    override fun stringify(): String = "(${base.stringify()} ^ ${exponent.stringify()})"

    override fun diff(by: Variable): Fun =
        when {
            this.base is Constant && this.exponent !is Constant ->
                this * Ln(this.base) * this.exponent.diff(by)
            this.base !is Constant && this.exponent is Constant ->
                if (this.exponent == 1.const)
                    Ln(this.base) * this.base.diff(by)
                else
                    this.exponent * (this.base pow (this.exponent.value - 1).const) * this.base.diff(by)
            else -> {
                this.exponent * (this.base pow (this.exponent - 1.const)) * this.base.diff(by) + (this.base pow (this.exponent)) * Ln(
                    this.base
                ) * this.exponent.diff(by)
            }
        }

    override fun fullEval(value: Map<Fun, Constant>): Double =
        base.fullEval(value).pow(exponent.fullEval(value))

    override fun partialEval(value: Map<Fun, Constant>): Fun = base.partialEval(value) pow exponent.partialEval(value)

    override fun toString(): String = "Power(${this.base}, ${this.exponent})"

    override fun sub(replace: Variable, with: Fun): Fun = base.sub(replace, with) pow exponent.sub(replace, with)

    override fun copy(): Fun = Power(this.base, this.exponent)
}

data class Ln(val a: Fun): Fun() {
    override val variables: Set<Variable>
        get() = this.a.variables

    override fun simplify(): Fun {
        val a_ = a.simplify()
        return when (a_) {
            1.const -> 0.const
            Math.E.const -> 1.const
            0.const -> Double.MIN_VALUE.const
            else -> Ln(a_)
        }
    }

    override fun stringify(): String = "ln(${a.stringify()})"

    override fun diff(by: Variable): Fun = this.a.reciprocal * this.a.diff(by)

    override fun fullEval(value: Map<Fun, Constant>): Double = when(val calc = a.fullEval(value)) {
        0.0 -> Double.MIN_VALUE
        else -> kotlin.math.ln(calc)
    }

    override fun partialEval(value: Map<Fun, Constant>): Fun = when (a.partialEval(value)) {
        0.0.const -> Double.MIN_VALUE.const
        else -> Ln(a.partialEval(value))
    }

    override fun toString(): String = "Ln(${this.a})"

    override fun sub(replace: Variable, with: Fun): Fun = Ln(a.sub(replace, with))

    override fun copy(): Fun = Ln(this.a)
}

data class Sin(val a: Fun): Fun() {
    override val variables: Set<Variable>
        get() = this.a.variables

    override fun simplify(): Fun = sin(a.simplify())

    override fun stringify(): String = "sin(${a.stringify()})"

    override fun diff(by: Variable): Fun = cos(a) * a.diff(by)

    override fun fullEval(value: Map<Fun, Constant>): Double =
        kotlin.math.sin(a.fullEval(value))

    override fun partialEval(value: Map<Fun, Constant>): Fun =
        Sin(a.partialEval(value))

    override fun toString(): String = "Sin(${this.a})"

    override fun sub(replace: Variable, with: Fun): Fun = Sin(a.sub(replace, with))

    override fun copy(): Fun = Sin(this.a)
}

data class Cos(val a: Fun): Fun() {
    override val variables: Set<Variable>
        get() = this.a.variables

    override fun simplify(): Fun = cos(a.simplify())

    override fun stringify(): String = "cos(${a.stringify()})"

    override fun diff(by: Variable): Fun = -sin(a) * a.diff(by)

    override fun fullEval(value: Map<Fun, Constant>): Double =
        kotlin.math.cos(a.fullEval(value))

    override fun partialEval(value: Map<Fun, Constant>): Fun =
        Cos(a.partialEval(value))

    override fun toString(): String = "Cos(${this.a})"

    override fun sub(replace: Variable, with: Fun): Fun = Cos(a.sub(replace, with))

    override fun copy(): Fun = Cos(this.a)
}