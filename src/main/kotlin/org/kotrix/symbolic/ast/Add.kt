package org.kotrix.symbolic.ast

import org.kotrix.symbolic.ast.extensions.*

data class Add(val lhs: Fun, val rhs: Fun) : Fun() {
    override val variables: Set<Variable>
        get() = setOf(*this.lhs.variables.toTypedArray(), *this.rhs.variables.toTypedArray())

    override fun simplify(): Fun {
//        println("calling add simplify: $this")
        val l = lhs.simplify()
        val r = rhs.simplify()
        return when {
            l == 0.scalar -> r // 0 + r -> r

            r == 0.scalar -> l // l + 0 -> l

            l == r -> (2.scalar * l).simplify() // l + r where l == r -> 2 * l

            l is Add -> { // (ll + lr) + r
                val ll = l.lhs.simplify()
                val lr = l.rhs.simplify()

                when (r) {
                    is UnaryPlus -> {
                        when (r.value.simplify()) { // (ll + lr) + +(r.value)
                            ll -> { // (ll + lr) + +(r.value) where r.value == ll -> (2 * ll) + lr
                                ((2 * ll).simplify() + lr).simplify()
                            }
                            lr -> { // (ll + lr) + +(r.value) where r.value == lr -> ll + (2 * lr)
                                (ll + (2 * lr).simplify()).simplify()
                            }
                            else -> l + r
                        }
                    }
                    is UnaryMinus -> {
                        when (r.value.simplify()) { // (ll + lr) + -(r.value)
                            ll -> { // (ll + lr) + -(r.value) where ll == r.value-> lr
                                lr
                            }
                            lr -> { // (ll + lr) + -(r.value) where lr == r.value -> ll
                                ll
                            }
                            else -> l + r
                        }
                    }
                    is Scalar -> {
                        when { // (ll + lr) + r where r is some scalar
                            ll is Scalar -> { // (ll + lr) + r where r and ll are some scalar -> (ll + r) + lr
                                ((ll + r).simplify() + lr).simplify()
                            }
                            lr is Scalar -> { // (ll + lr) + r where r and lr are some scalar -> ll + (lr + r)
                                (ll + (lr + r).simplify()).simplify()
                            }
                            else -> l + r
                        }
                    }
                    is Variable -> {
                        when (r) { // (ll + lr) + r where r is some variable
                            ll -> { // (ll + lr) + r where r is some variable and ll == r -> (2 * ll) + lr
                                ((2 * ll).simplify() + lr).simplify()
                            }
                            lr -> { // (ll + lr) + r where r is some variable and lr == r -> ll + (2 * lr)
                                (ll + (2 * lr).simplify()).simplify()
                            }
                            else -> l + r
                        }
                    }
                    is Add -> {
                        val rl = r.lhs.simplify()
                        val rr = r.rhs.simplify()

                        when { // (ll + lr) + (rl + rr)
                            ll == rl -> { // (ll + lr) + (rl + rr) where ll == rl -> (2 * ll) + lr + rr
                                (((2 * ll).simplify() + lr).simplify() + rr).simplify()
                            }
                            ll == rr -> { // (ll + lr) + (rl + rr) where ll == rr -> (2 * ll) + lr + rl
                                (((2 * ll).simplify() + lr).simplify() + rl).simplify()
                            }
                            lr == rl -> { // (ll + lr) + (rl + rr) where lr == rl -> ll + (2 * lr) + rr
                                ((ll + (2 * lr).simplify()).simplify() + rr).simplify()
                            }
                            lr == rr -> { // (ll + lr) + (rl + rr) where lr == rr -> ll + (2 * lr) + rl
                                ((ll + (2 * lr).simplify()).simplify() + rl).simplify()
                            }
                            else -> l + r
                        }
                    }
                    is Subtract -> {
                        val rl = r.lhs.simplify()
                        val rr = r.rhs.simplify()

                        when { // (ll + lr) + (rl - rr)
                            ll == rl -> { // (ll + lr) + (rl - rr) where ll == rl -> (2 * ll) + lr - rr
                                (((2 * ll).simplify() + lr).simplify() - rr).simplify()
                            }
                            ll == rr -> { // (ll + lr) + (rl - rr) where ll == rr -> lr + rl
                                (lr + rl).simplify()
                            }
                            lr == rl -> { // (ll + lr) + (rl - rr) where lr == rl -> ll + (2 * lr) - rr
                                ((ll + (2 * lr).simplify()) - rr).simplify()
                            }
                            lr == rr -> { // (ll + lr) + (rl - rr) where lr == rr -> ll + lr
                                (ll + rl).simplify()
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
                                when { // ((lll * llr) + lr) + (rl * rr)
                                    lll == rl -> { // ((lll * llr) + lr) + (rl * rr) where lll == rl -> (lll * (llr + rr)) + lr
                                        ((lll * (llr + rr).simplify()).simplify() + lr).simplify()
                                    }
                                    lll == rr -> { // ((lll * llr) + lr) + (rl * rr) where lll == rr -> (lll * (llr + rl)) + lr
                                        ((lll * (llr + rl).simplify()).simplify() + lr).simplify()
                                    }
                                    llr == rl -> { // ((lll * llr) + lr) + (rl * rr) where llr == rl -> ((lll + rr) * llr) + lr
                                        (((lll + rr).simplify() * llr).simplify() + lr).simplify()
                                    }
                                    llr == rr -> { // ((lll * llr) + lr) + (rl * rr) where llr == rr -> ((lll + rl) * llr) + lr
                                        (((lll + rl).simplify() * llr).simplify() + lr).simplify()
                                    }
                                    else -> l + r
                                }
                            }
                            lr is Times -> {
                                val lrl = lr.lhs.simplify()
                                val lrr = lr.rhs.simplify()
                                when { // (ll + (lrl * lrr)) + (rl * rr)
                                    lrl == rl -> { // (ll + (lrl * lrr)) + (rl * rr) where lrl == rl -> ll + (lrl * (lrr + rr))
                                        (ll + (lrl * (lrr + rr).simplify()).simplify()).simplify()
                                    }
                                    lrl == rr -> { // (ll + (lrl * lrr)) + (rl * rr) where lrl == rr -> ll + (lrl * (lrr + rl))
                                        (ll + (lrl * (lrr + rl).simplify()).simplify()).simplify()
                                    }
                                    lrr == rl -> { // (ll + (lrl * lrr)) + (rl * rr) where lrr == rl -> ll + ((lrl + rr) * lrr)
                                        (ll + ((lrl + rr).simplify() * lrr).simplify()).simplify()
                                    }
                                    lrr == rr -> { // (ll + (lrl * lrr)) + (rl * rr) where lrr == rr -> ll + ((lrl + rr) * lrr)
                                        (ll + ((lrl + rl).simplify() * lrr).simplify()).simplify()
                                    }
                                    else -> l + r
                                }
                            }
                            else -> l + r
                        }
                    }
                    is Divide -> {
                        val rn = r.numerator.simplify()
                        val rd = r.denominator.simplify()

                        when { // (ll + lr) + (rn / rd)
                            ll is Divide -> { // ((lln / lld) + lr) + (rn / rd)
                                val lln = ll.numerator.simplify()
                                val lld = ll.denominator.simplify()
                                if (lld == rd) { // ((lln / lld) + lr) + (rn / rd) where lld == rd -> ((lln + rn) / lld) + lr
                                    (((lln + rn).simplify() / lld).simplify() + lr).simplify()
                                } else {
                                    l + r // todo check for branches if both denominators are not equal
                                }
                            }
                            lr is Divide -> { // (ll + (lrn / lrd)) + (rn / rd)
                                val lrn = lr.numerator.simplify()
                                val lrd = lr.denominator.simplify()
                                if (lrd == rd) { // (ll + (lrn / lrd)) + (rn / rd) where lrd == rd -> ll + ((lrn + rn) / lld)
                                    (ll + ((lrn + rn).simplify() / lrd).simplify()).simplify()
                                } else {
                                    l + r // todo check for branches if both denominators are not equal
                                }
                            }
                            else -> l + r
                        }
                    }
                    is Power -> { // todo complete branch
                        when { // (ll + lr) + (b ^ e)
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
                    is Ln, is Sin, is Cos -> {
                        when (r) {
                            ll -> { // (ll + lr) + r where ll == r and r is Ln, Sin, or Cos -> (2 * ll) + lr
                                ((2 * ll) + lr).simplify()
                            }
                            lr -> { // (ll + lr) + r where lr == r and r is Ln, Sin, or Cos -> ll + (2 * lr)
                                (ll + (2 * lr)).simplify()
                            }
                            else -> l + r
                        }
                    }
//                    is Vector -> TODO()
                    is Series -> TODO()
                }
            }

            l is Subtract -> { // (ll - lr) + r
                val ll = l.lhs.simplify()
                val lr = l.rhs.simplify()

                when (r) {
                    is UnaryPlus -> {
                        when (r.value.simplify()) { // (ll - lr) + +(r.value)
                            ll -> { // (ll - lr) + +(r.value) where r.value == ll -> (2 * ll) - lr
                                ((2 * ll).simplify() - lr).simplify()
                            }
                            lr -> { // (ll - lr) + +(r.value) where r.value == lr -> ll
                                return ll
                            }
                            else -> l + r
                        }
                    }
                    is UnaryMinus -> {
                        when (r.value.simplify()) { // (ll - lr) + -(r.value)
                            ll -> { // (ll - lr) + -(r.value) where r.value == ll -> -lr
                                -lr
                            }
                            lr -> { // (ll - lr) + -(r.value) where r.value == lr -> ll - (2 * lr)
                                (ll - (2 * lr).simplify()).simplify()
                            }
                            else -> l + r
                        }
                    }
                    is Scalar -> {
                        when { // (ll - lr) + r where r is some scalar
                            ll is Scalar -> { // (ll - lr) + r where ll and r are some scalar -> (ll + r) - lr
                                ((ll + r).simplify() - lr).simplify()
                            }
                            lr is Scalar -> { // (ll - lr) + r where lr and r are some scalar -> ll - (lr + r)
                                (ll - (lr + r).simplify()).simplify()
                            }
                            else -> l + r
                        }
                    }
                    is Variable -> {
                        when (r) { // (ll - lr) + r where r is some variable
                            ll -> { // (ll - lr) + r where r is some variable and ll == r -> (2 * ll) - lr
                                ((2 * ll).simplify() - lr).simplify()
                            }
                            lr -> { // (ll - lr) + r where r is some variable and lr == r -> ll - (2 * lr)
                                (ll - (2 * lr).simplify()).simplify()
                            }
                            else -> l + r
                        }
                    }
                    is Add -> {
                        val rl = r.lhs.simplify()
                        val rr = r.rhs.simplify()

                        when { // (ll - lr) + (rl + rr)
                            ll == rl -> { // (ll - lr) + (rl + rr) where ll == rl -> (2 * ll) - lr + rr
                                (((2 * ll).simplify() - lr).simplify() + rr).simplify()
                            }
                            ll == rr -> { // (ll - lr) + (rl + rr) where ll == rr -> (2 * ll) - lr + rl
                                (((2 * ll).simplify() - lr).simplify() + rl).simplify()
                            }
                            lr == rl -> { // (ll - lr) + (rl + rr) where lr == rl -> ll + rr
                                (ll + rr).simplify()
                            }
                            lr == rr -> { // (ll - lr) + (rl + rr) where lr == rr -> ll + rl
                                (ll + rl).simplify()
                            }
                            else -> l + r
                        }
                    }
                    is Subtract -> {
                        val rl = r.rhs.simplify()
                        val rr = r.rhs.simplify()

                        when { // (ll - lr) + (rl - rr)
                            ll == rl -> { // (ll - lr) + (rl - rr) where ll == rl -> (2 * ll) - lr - rr
                                (((2 * ll).simplify() - lr).simplify() - rr).simplify()
                            }
                            ll == rr -> { // (ll - lr) + (rl - rr) where ll == rr -> -lr + rl
                                (-lr + rl).simplify()
                            }
                            lr == rl -> { // (ll - lr) + (rl - rr) where lr == rl -> ll - rr
                                (ll - rr).simplify()
                            }
                            lr == rr -> { // (ll - lr) + (rl - rr) where lr == rr -> ll - (2 * lr) - rl
                                ((ll - (2 * lr).simplify()).simplify() + rl).simplify()
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
                                when { // ((lll * llr) - lr) + (rl * rr)
                                    lll == rl -> { // ((lll * llr) - lr) + (rl * rr) where lll == rl -> lll * (llr + rr) - lr
                                        ((lll * (llr + rr).simplify()).simplify() - lr).simplify()
                                    }
                                    lll == rr -> { // ((lll * llr) - lr) + (rl * rr) where lll == rr -> lll * (llr + rl) - lr
                                        ((lll * (llr + rl).simplify()).simplify() - lr).simplify()
                                    }
                                    llr == rl -> { // ((lll * llr) - lr) + (rl * rr) where llr == rl -> (lll + rr) * llr - lr
                                        (((lll + rr).simplify() * llr).simplify() - lr).simplify()
                                    }
                                    llr == rr -> { // ((lll * llr) - lr) + (rl * rr) where llr == rr -> (lll + rl) * llr - lr
                                        (((lll + rl).simplify() * llr).simplify() - lr).simplify()
                                    }
                                    else -> l + r
                                }
                            }
                            lr is Times -> {
                                val lrl = lr.lhs.simplify()
                                val lrr = lr.lhs.simplify()
                                when { // (ll - (lrl * lrr)) + (rl * rr)
                                    lrl == rl -> { // (ll - (lrl * lrr)) + (rl * rr) where lrl == rl -> ll - (lrl * (lrr + rr))
                                        (ll - (lrl * (lrr + rr).simplify()).simplify()).simplify()
                                    }
                                    lrl == rr -> { // (ll - (lrl * lrr)) + (rl * rr) where lrl == rr -> ll - (lrl * (lrr * rl))
                                        (ll - (lrl * (lrr + rl).simplify()).simplify()).simplify()
                                    }
                                    lrr == rl -> { // (ll - (lrl * lrr)) + (rl * rr) where lrr == rl -> ll - ((lrl + rr) * lrr)
                                        (ll - ((lrl + rr).simplify() * lrr).simplify()).simplify()
                                    }
                                    lrr == rr -> { // (ll - (lrl * lrr)) + (rl * rr) where lrr == rr -> ll - ((lrl + rl) * lrr)
                                        (ll - ((lrl + rl).simplify() * lrr).simplify()).simplify()
                                    }
                                    else -> l + r
                                }
                            }
                            else -> l + r
                        }
                    }
                    is Cos -> TODO()
                    is Divide -> TODO()
                    is Ln -> TODO()
                    is Power -> TODO()
                    is Sin -> TODO()
//                    is Vector -> TODO()
                    is Series -> TODO()
                }
            }

            l is UnaryMinus -> { // -l + r
                val lv = l.value.simplify()
                when { // -(l.value) + r
                    (r is UnaryPlus && r.value.simplify() == lv) // -(l.value) + +(r.value) where l.value == r.value -> 0
                            || (r !is UnaryPlus && r == lv) -> { // -(l.value) + r where r is not an UnaryPlus and l.value == r -> 0
                        0.scalar
                    }
                    else -> l + r
                }
            }

            l is Scalar && r is Scalar -> { // l + r where l and r are both some scalar
                this.evalAllAtZero().scalar
            }

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