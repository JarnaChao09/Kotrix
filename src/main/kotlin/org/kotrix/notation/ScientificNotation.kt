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

    override val decimalForm: Double
        get() = base_ * (10.0).pow(exponent_)

    override fun unaryPlus(): ScientificNotation =
            ScientificNotation(+this.base, this.exponent)

    override fun unaryMinus(): ScientificNotation =
            ScientificNotation(-this.base, this.exponent)

    override fun plus(other: Notation): Notation {
        other as ScientificNotation
        var (thisBase, thisExp) = this
        var (otherBase, otherExp) = other

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
        other as ScientificNotation
        var (thisBase, thisExp) = this
        var (otherBase, otherExp) = other

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
        other as ScientificNotation
        val (thisBase, thisExp) = this
        val (otherBase, otherExp) = other

        val tempBase = thisBase * otherBase
        val exp = floor(log10(tempBase.absoluteValue))
        val newExp = thisExp + otherExp + exp
        val newBase = tempBase * (10.0).pow(-exp)

        return ScientificNotation(newBase, newExp)
    }

    override fun div(other: Notation): Notation {
        other as ScientificNotation
        var (thisBase, thisExp) = this
        var (otherBase, otherExp) = other

        val tempExp = (thisExp - otherExp).absoluteValue

        if (thisExp != otherExp) {
            if (thisExp > otherExp) {
                thisBase *= (10.0).pow(thisExp - otherExp)
            } else {
                otherBase *= (10.0).pow(otherExp - thisExp)
            }
        }

        val tempBase = thisBase / otherBase
        val exp = floor(log10(tempBase.absoluteValue))
        val (newBase, newExp) = (tempBase * (10.0).pow(-exp)) to tempExp - exp.toInt()

        return ScientificNotation(newBase, newExp)
    }

    override fun rem(other: Notation): Notation {
        TODO("Not yet implemented")
    }

    override fun pow(other: Notation): Notation {
        other as ScientificNotation
        val (thisBase, thisExp) = this
        val (otherBase, otherExp) = other

        // TODO ensure no overflow
        val actualValue = other.decimalForm

        val tempBase = thisBase.pow(actualValue)
        val tempExp = thisExp * actualValue
        val expCarry = floor(log10(tempBase.absoluteValue))
        val (newBase, newExp) = (tempBase * (10.0).pow(-expCarry)) to (tempExp + expCarry)

        return ScientificNotation(newBase, newExp)
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