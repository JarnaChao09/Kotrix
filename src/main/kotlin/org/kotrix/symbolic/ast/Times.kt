package org.kotrix.symbolic.ast

import org.kotrix.symbolic.ast.extensions.*

data class Times(val lhs: Fun, val rhs: Fun): Fun() {
    override val variables: Set<Variable>
        get() = setOf(*this.lhs.variables.toTypedArray(), *this.rhs.variables.toTypedArray())

    override fun simplify(): Fun {
//        println("calling times simplify $this")
        val l = lhs.simplify()
        val r = rhs.simplify()
        return when {
            l == 0.scalar || r == 0.scalar -> 0.scalar

            l == 1.scalar -> r

            r == 1.scalar -> l

            l is Add && r !is Add ->
                ((l.lhs.simplify() * r).simplify() + (l.rhs.simplify() * r).simplify()).simplify()

            l !is Add && r is Add ->
                ((r.lhs.simplify() * l).simplify() + (r.rhs.simplify() * l).simplify()).simplify()

            l is Add && r is Add ->
                ((l.lhs.simplify() * r.lhs.simplify()).simplify() + (l.lhs.simplify() * r.rhs.simplify()).simplify() + (l.rhs.simplify() * r.lhs.simplify()).simplify() + (l.rhs.simplify() * r.rhs.simplify()).simplify()).simplify()

            l is Power && r == l.base.simplify() -> (l.base.simplify() pow ((l.exponent.simplify() + 1.scalar).simplify())).simplify()

            r is Power && l == r.base.simplify() -> (r.base.simplify() pow ((r.exponent.simplify() + 1.scalar).simplify())).simplify()

            l is Power && r is Power && l.base.simplify() == r.base.simplify() ->
                l.base.simplify() pow ((l.exponent.simplify() + r.exponent.simplify()).simplify())

            l is Power && r is Times ->
                when {
                    r.lhs.simplify() is Power -> {
                        (l * r.lhs.simplify()).simplify() * r.rhs.simplify()
                    }
                    r.rhs.simplify() is Power -> {
                        r.lhs.simplify() * (l * r.rhs.simplify()).simplify()
                    }
                    r.lhs.simplify() == l.base.simplify() -> {
                        r.rhs.simplify() * (l.base.simplify() pow ((l.exponent.simplify() + 1.scalar).simplify()))
                    }
                    r.rhs.simplify() == l.base.simplify() -> {
                        r.lhs.simplify() * (l.base.simplify() pow ((l.exponent.simplify() + 1.scalar).simplify()))
                    }
                    else -> {
                        l.simplify() * r.simplify()
                    }
                }.simplify()

            r is Power && l is Times ->
                when {
                    l.lhs.simplify() is Power -> {
                        l.rhs.simplify() * (r * l.lhs.simplify()).simplify()
                    }
                    l.rhs.simplify() is Power -> {
                        l.lhs.simplify() * (r * l.rhs.simplify()).simplify()
                    }
                    l.lhs.simplify() == r.base.simplify() -> {
                        l.rhs.simplify() * (r.base.simplify() pow ((r.exponent.simplify() + 1.scalar).simplify()))
                    }
                    l.rhs.simplify() == r.base.simplify() -> {
                        l.lhs.simplify() * (r.base.simplify() pow ((r.exponent.simplify() + 1.scalar).simplify()))
                    }
                    else -> {
                        l.simplify() * r.simplify()
                    }
                }.simplify()

            l is Times && r is Times -> {
                when {
                    l.lhs.simplify() is Power -> {
                        if (r.lhs.simplify() is Power) {
                            l.rhs.simplify() * r.rhs.simplify() * (l.lhs.simplify() * r.lhs.simplify()).simplify()
                        } else {
                            l.rhs.simplify() * r.lhs.simplify() * (l.lhs.simplify() * r.rhs.simplify()).simplify()
                        }
                    }
                    l.rhs.simplify() is Power -> {
                        if (r.lhs.simplify() is Power) {
                            l.lhs.simplify() * r.rhs.simplify() * (l.rhs.simplify() * r.lhs.simplify()).simplify()
                        } else {
                            l.lhs.simplify() * r.lhs.simplify() * (l.rhs.simplify() * r.rhs.simplify()).simplify()
                        }
                    }
                    r.lhs.simplify() is Power -> {
                        if (l.lhs.simplify() is Power) {
                            r.rhs.simplify() * l.rhs.simplify() * (r.lhs.simplify() * l.lhs.simplify()).simplify()
                        } else {
                            r.rhs.simplify() * l.lhs.simplify() * (r.lhs.simplify() * l.rhs.simplify()).simplify()
                        }
                    }
                    r.rhs.simplify() is Power -> {
                        if (l.lhs.simplify() is Power) {
                            r.lhs.simplify() * l.rhs.simplify() * (r.rhs.simplify() * l.lhs.simplify()).simplify()
                        } else {
                            r.lhs.simplify() * l.lhs.simplify() * (r.rhs.simplify() * l.rhs.simplify()).simplify()
                        }
                    }
                    else -> l * r
                }.simplify()
            }

            l is Scalar && r is Times -> {
                if (r.lhs.simplify() is Scalar) {
                    (l * r.lhs.simplify()).simplify() * r.rhs.simplify()
                } else {
                    (l * r.rhs.simplify()).simplify() * r.lhs.simplify()
                }
            }

            l is Scalar && r is Scalar -> this.evalAllAtZero().scalar

            else -> l * r
        }
    }

    override fun stringify(): String = "(${this.lhs.stringify()} * ${this.rhs.stringify()})"

    override fun diff(by: Variable): Fun = this.lhs * this.rhs.diff(by) + this.rhs * this.lhs.diff(by)

    override fun fullEval(value: Map<Fun, Scalar>): Double = lhs.fullEval(value) * rhs.fullEval(value)

    override fun partialEval(value: Map<Fun, Scalar>): Fun = lhs.partialEval(value) * rhs.partialEval(value)

    override fun toString(): String = "Times(${this.lhs}, ${this.rhs})"

    override fun sub(replace: Variable, with: Fun): Fun = lhs.sub(replace, with) * rhs.sub(replace, with)
}