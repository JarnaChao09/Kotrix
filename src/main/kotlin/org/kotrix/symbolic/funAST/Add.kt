package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.extensions.scalar
import org.kotrix.symbolic.funAST.extensions.*

data class Add(val lhs: Fun, val rhs: Fun): Fun() {
    override val variables: Set<Variable>
        get() = setOf(*this.lhs.variables.toTypedArray(), *this.rhs.variables.toTypedArray())

    override fun simplify(): Fun {
//        println("calling add simplify: $this")
        val l = lhs.simplify()
        val r = rhs.simplify()
        return when {
            l == 0.scalar -> r

            r == 0.scalar -> l

            l == r -> (2.scalar * l).simplify()

            l is Add -> {
                val ll = l.lhs.simplify()
                val lr = l.rhs.simplify()

                when(r) {
                    is Scalar -> {
                        when {
                            ll is Scalar -> {
                                (ll + r).simplify() + lr
                            }
                            lr is Scalar -> {
                                ll + (lr + r).simplify()
                            }
                            else -> l + r
                        }
                    }
                    is Variable -> {
                        when(r) {
                            ll -> {
                                (2 * r).simplify() + lr
                            }
                            lr -> {
                                ll + (2 * r).simplify()
                            }
                            else -> l + r
                        }
                    }
                    is Add -> {
                        val rl = r.lhs.simplify()
                        val rr = r.rhs.simplify()

                        when {
                            ll == rl -> {
                                (((2 * ll).simplify() + lr).simplify() + rr).simplify()
                            }
                            ll == rr  -> {
                                (((2 * ll).simplify() + lr).simplify() + rl).simplify()
                            }
                            lr == rl -> {
                                ((ll + (2 * lr).simplify()).simplify() + rr).simplify()
                            }
                            lr == rr -> {
                                ((ll + (2 * lr).simplify()).simplify() + rl).simplify()
                            }
                            else -> l + r
                        }
                    }
                    is Times -> {
                        val rl = r.lhs.simplify()
                        val rr = r.rhs.simplify()
                        when {
                            ll is Times -> {
                                val lll = ll.lhs.simplify()
                                val llr = ll.rhs.simplify()
                                when {
                                    lll == rl ->
                                        ((lll * (llr + rr).simplify()).simplify() + lr).simplify()
                                    llr == rl ->
                                        (((lll + rr).simplify() * llr).simplify() + lr).simplify()
                                    lll == rr ->
                                        ((lll * (llr + rl).simplify()).simplify() + lr).simplify()
                                    llr == rr ->
                                        (((lll + rl).simplify() * llr).simplify() + lr).simplify()
                                    else -> l + r
                                }
                            }
                            lr is Times -> {
                                val lrl = lr.lhs.simplify()
                                val lrr = lr.rhs.simplify()
                                when {
                                    lrl == rl ->
                                        ((lrl * (lrr + rr).simplify()).simplify() + ll).simplify()
                                    lrr == rl ->
                                        (((lrl + rr).simplify() * lrr).simplify() + ll).simplify()
                                    lrl == rr ->
                                        ((lrl * (lrr + rl).simplify()).simplify() + ll).simplify()
                                    lrr == rr ->
                                        (((lrl + rl).simplify() * lrr).simplify() + ll).simplify()
                                    else -> l + r
                                }
                            }
                            else -> l + r
                        }
                    }
                    is Power -> {
                        when {
                            ll is Times -> {
                                val lll = ll.lhs.simplify()
                                val llr = ll.rhs.simplify()
                                when {
                                    lll == r -> {
                                        ((llr + 1).simplify() * r) + lr
                                    }
                                    llr == r -> {
                                        ((lll + 1).simplify() * r) + lr
                                    }
                                    else -> {
                                        l + r
                                    }
                                }
                            }
                            lr is Times -> {
                                val lrl = lr.lhs.simplify()
                                val lrr = lr.rhs.simplify()
                                when {
                                    lrl == r -> {
                                        ((lrr + 1).simplify() * r) + ll
                                    }
                                    lrr == r -> {
                                        ((lrl + 1).simplify() * r) + ll
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

            l is UnaryMinus -> {
                val lv = l.value.simplify()
                when {
                    (r is UnaryPlus && r.value.simplify() == lv) || (r !is UnaryPlus && r == lv) -> 0.scalar
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