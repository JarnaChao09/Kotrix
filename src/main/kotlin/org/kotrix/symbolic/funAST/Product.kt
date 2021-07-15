package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.extensions.pow
import org.kotrix.symbolic.funAST.extensions.scalar

data class Product(val a: Fun, val b: Fun): Fun() {
    override val variables: Set<Variable>
        get() = setOf(*this.a.variables.toTypedArray(), *this.b.variables.toTypedArray())

    override fun simplify(): Fun {
        val a_ = a.simplify()
        val b_ = b.simplify()
        return when {
            a_ == 0.scalar || b_ == 0.scalar -> 0.scalar

            a_ == 1.scalar -> b_

            b_ == 1.scalar -> a_

            a_ is Sum && b_ !is Sum ->
                ((a_.a.simplify() * b_).simplify() + (a_.b.simplify() * b_).simplify()).simplify()

            a_ !is Sum && b_ is Sum ->
                ((b_.a.simplify() * a_).simplify() + (b_.b.simplify() * a_).simplify()).simplify()

            a_ is Sum && b_ is Sum ->
                ((a_.a.simplify() * b_.a.simplify()).simplify() + (a_.a.simplify() * b_.b.simplify()).simplify() + (a_.b.simplify() * b_.a.simplify()).simplify() + (a_.b.simplify() * b_.b.simplify()).simplify()).simplify()

            a_ is Power && b_ == a_.base.simplify() -> (a_.base.simplify() pow ((a_.exponent.simplify() + 1.scalar).simplify())).simplify()

            b_ is Power && a_ == b_.base.simplify() -> (b_.base.simplify() pow ((b_.exponent.simplify() + 1.scalar).simplify())).simplify()

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
                        b_.b.simplify() * (a_.base.simplify() pow ((a_.exponent.simplify() + 1.scalar).simplify()))
                    }
                    b_.b.simplify() == a_.base.simplify() -> {
                        b_.a.simplify() * (a_.base.simplify() pow ((a_.exponent.simplify() + 1.scalar).simplify()))
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
                        a_.b.simplify() * (b_.base.simplify() pow ((b_.exponent.simplify() + 1.scalar).simplify()))
                    }
                    a_.b.simplify() == b_.base.simplify() -> {
                        a_.a.simplify() * (b_.base.simplify() pow ((b_.exponent.simplify() + 1.scalar).simplify()))
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

            a_ is Scalar && b_ is Product -> {
                if (b_.a.simplify() is Scalar) {
                    (a_ * b_.a.simplify()).simplify() * b_.b.simplify()
                } else {
                    (a_ * b_.b.simplify()).simplify() * b_.a.simplify()
                }
            }

            a_ is Scalar &&
                    b_ is Scalar &&
                    a_.value != kotlin.math.PI &&
                    b_.value != kotlin.math.PI ->
                this.evalAllAtZero()

            else -> a_ * b_
        }
    }

    override fun stringify(): String = "(${this.a.stringify()} * ${this.b.stringify()})"

    override fun diff(by: Variable): Fun = this.a * this.b.diff(by) + this.b * this.a.diff(by)

    override fun fullEval(value: Map<Fun, Scalar>): Double = a.fullEval(value) * b.fullEval(value)

    override fun partialEval(value: Map<Fun, Scalar>): Fun = a.partialEval(value) * b.partialEval(value)

    override fun toString(): String = "Product(${this.a}, ${this.b})"

    override fun sub(replace: Variable, with: Fun): Fun = a.sub(replace, with) * b.sub(replace, with)

    override fun copy(): Fun = Product(this.a, this.b)
}