package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.extensions.scalar
import org.kotrix.symbolic.funAST.extensions.*

data class Sum(val a: Fun, val b: Fun): Fun() {
    override val variables: Set<Variable>
        get() = setOf(*this.a.variables.toTypedArray(), *this.b.variables.toTypedArray())

    override fun simplify(): Fun {
        val a_ = a.simplify()
        val b_ = b.simplify()
        return when {
            a_ == 0.scalar -> b_

            b_ == 0.scalar -> a_

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

            a_ is Scalar && b_ is Scalar && a_.value != kotlin.math.PI && b_.value != kotlin.math.PI -> this.evalAllAtZero()

            else -> a_ + b_
        }
    }

    override fun stringify(): String = "(${a.stringify()} + ${b.stringify()})"

    override fun diff(by: Variable): Fun = this.a.diff(by) + this.b.diff(by)

    override fun fullEval(value: Map<Fun, Scalar>): Double = a.fullEval(value) + b.fullEval(value)

    override fun partialEval(value: Map<Fun, Scalar>): Fun = a.partialEval(value) + b.partialEval(value)

    override fun toString(): String = "Sum(${this.a}, ${this.b})"

    override fun sub(replace: Variable, with: Fun): Fun = a.sub(replace, with) + b.sub(replace, with)

    override fun copy(): Fun = Sum(this.a, this.b)
}