package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.extensions.scalar
import org.kotrix.symbolic.funAST.extensions.*

data class Add(val lhs: Fun, val rhs: Fun): Fun() {
    override val variables: Set<Variable>
        get() = setOf(*this.lhs.variables.toTypedArray(), *this.rhs.variables.toTypedArray())

    override fun simplify(): Fun {
        val l = lhs.simplify()
        val r = rhs.simplify()
        return when {
            l == 0.scalar -> r

            r == 0.scalar -> l

//            a_ is Product && b_ is Product -> { // cancels out lines 225 && 228
//                when {
//                    a_.a.simplify() == b_.a.simplify() -> (a_.b.simplify() + b_.b.simplify()).simplify() * a_.a.simplify()
//                    a_.b.simplify() == b_.a.simplify() -> (a_.a.simplify() + b_.b.simplify()).simplify() * a_.b.simplify()
//                    a_.a.simplify() == b_.b.simplify() -> (a_.b.simplify() + b_.a.simplify()).simplify() * a_.a.simplify()
//                    a_.b.simplify() == b_.b.simplify() -> (a_.a.simplify() + b_.a.simplify()).simplify() * a_.b.simplify()
//                    else -> a_ + b_
//                }
//            }

            l is Add -> {
                when(r) {
                    is Times -> {
                        when {
                            l.lhs is Times -> {
                                when {
                                    l.lhs.lhs.simplify() == r.lhs.simplify() ->
                                        ((l.lhs.lhs.simplify() * (l.lhs.rhs.simplify() + r.rhs.simplify()).simplify()).simplify() + l.rhs.simplify()).simplify()
                                    l.lhs.rhs.simplify() == r.lhs.simplify() ->
                                        (((l.lhs.lhs.simplify() + r.rhs.simplify()).simplify() * l.lhs.rhs.simplify()).simplify() + l.rhs.simplify()).simplify()
                                    l.lhs.lhs.simplify() == r.rhs.simplify() ->
                                        ((l.lhs.lhs.simplify() * (l.lhs.rhs.simplify() + r.lhs.simplify()).simplify()).simplify() + l.rhs.simplify()).simplify()
                                    l.lhs.rhs.simplify() == r.rhs.simplify() ->
                                        (((l.lhs.lhs.simplify() + r.lhs.simplify()).simplify() * l.lhs.rhs.simplify()).simplify() + l.rhs.simplify()).simplify()
                                    else -> l + r
                                }
                            }
                            l.rhs is Times -> {
                                when {
                                    l.rhs.lhs.simplify() == r.lhs.simplify() ->
                                        ((l.rhs.lhs.simplify() * (l.rhs.rhs.simplify() + r.rhs.simplify()).simplify()).simplify() + l.lhs.simplify()).simplify()
                                    l.rhs.rhs.simplify() == r.lhs.simplify() ->
                                        (((l.rhs.lhs.simplify() + r.rhs.simplify()).simplify() * l.rhs.rhs.simplify()).simplify() + l.lhs.simplify()).simplify()
                                    l.rhs.lhs.simplify() == r.rhs.simplify() ->
                                        ((l.rhs.lhs.simplify() * (l.rhs.rhs.simplify() + r.lhs.simplify()).simplify()).simplify() + l.lhs.simplify()).simplify()
                                    l.rhs.rhs.simplify() == r.rhs.simplify() ->
                                        (((l.rhs.lhs.simplify() + r.lhs.simplify()).simplify() * l.rhs.rhs.simplify()).simplify() + l.lhs.simplify()).simplify()
                                    else -> l + r
                                }
                            }
                            else -> l + r
                        }
                    }
                    is Power -> {
                        val a_a_ = l.lhs.simplify()
                        val a_b_ = l.rhs.simplify()
                        when {
                            a_a_ is Times -> {
                                val a_a_a_ = a_a_.lhs.simplify()
                                val a_a_b_ = a_a_.rhs.simplify()
                                when {
                                    a_a_a_ == r -> {
                                        ((a_a_b_ + 1).simplify() * r) + a_b_
                                    }
                                    a_a_b_ == r -> {
                                        ((a_a_a_ + 1).simplify() * r) + a_b_
                                    }
                                    else -> {
                                        l + r
                                    }
                                }
                            }
                            a_b_ is Times -> {
                                val a_b_a_ = a_b_.lhs.simplify()
                                val a_b_b_ = a_b_.rhs.simplify()
                                when {
                                    a_b_a_ == r -> {
                                        ((a_b_b_ + 1).simplify() * r) + a_a_
                                    }
                                    a_b_b_ == r -> {
                                        ((a_b_a_ + 1).simplify() * r) + a_a_
                                    }
                                    else -> {
                                        l + r
                                    }
                                }
                            }
                            else -> {
                                l + r
                            }
                        }
                    }
//                    is Constant -> {
//                        TODO()
//                    }
                    is Variable -> {
                        TODO()
                    }
                    is Add -> {
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
                    else -> l + r
                }
            }

            l is Scalar && r is Scalar -> this.evalAllAtZero().scalar

            else -> l + r
        }
    }

    override fun stringify(): String = "(${lhs.stringify()} + ${rhs.stringify()})"

    override fun diff(by: Variable): Fun = this.lhs.diff(by) + this.rhs.diff(by)

    override fun fullEval(value: Map<Fun, Scalar>): Double = lhs.fullEval(value) + rhs.fullEval(value)

    override fun partialEval(value: Map<Fun, Scalar>): Fun = lhs.partialEval(value) + rhs.partialEval(value)

    override fun toString(): String = "Add(${this.lhs}, ${this.rhs})"

    override fun sub(replace: Variable, with: Fun): Fun = lhs.sub(replace, with) + rhs.sub(replace, with)
}