package org.kotrix.notation

import kotlin.math.*

data class ScientificNotation(private var base_: Double, private var exponent_: Int): Notation {
    constructor(base: Number, exp: Number): this(base.toDouble(), exp.toInt())

    constructor(regular: Number): this(
            regular.toDouble() * (10.0).pow(-floor(log10(regular.toDouble().absoluteValue))),
            sign(regular.toDouble()) * floor(log10(regular.toDouble().absoluteValue))
    )

    constructor(): this(0)

    override val base: Double
        get() = base_

    override val exponent: Int
        get() = exponent_

    override fun unaryPlus(): ScientificNotation =
            ScientificNotation(+this.base, this.exponent)

    override fun unaryMinus(): ScientificNotation =
            ScientificNotation(-this.base, this.exponent)

    override fun plus(other: Notation): Notation {
        var (thisBase, thisExp) = this
        var (otherBase, otherExp) = other.base to other.exponent

        val newExp = max(thisExp, otherExp)

        if (thisExp != otherExp) {
            if (thisExp > otherExp) {
                otherBase /= (10.0).pow(thisExp - otherExp)
            } else {
                thisBase /= (10.0).pow(otherExp - thisExp)
            }
        }

        return ScientificNotation(thisBase + otherBase, newExp)
    }

    override fun minus(other: Notation): Notation {
        var (thisBase, thisExp) = this
        var (otherBase, otherExp) = other.base to other.exponent

        val newExp = max(thisExp, otherExp)

        if (thisExp != otherExp) {
            if (thisExp > otherExp) {
                otherBase /= (10.0).pow(thisExp - otherExp)
            } else {
                thisBase /= (10.0).pow(otherExp - thisExp)
            }
        }

        return ScientificNotation(thisBase - otherBase, newExp)
    }

    override fun times(other: Notation): Notation {
        TODO("Not yet implemented")
    }

    override fun div(other: Notation): Notation {
        TODO("Not yet implemented")
    }

    override fun rem(other: Notation): Notation {
        TODO("Not yet implemented")
    }

    override fun compareTo(other: Notation): Int {
        val expCheck = exponent_.compareTo(other.exponent)
        return if (expCheck == 0) {
            base_.compareTo(other.base)
        } else {
            expCheck
        }
    }

    override fun toString(): String =
            this.stringify()
}