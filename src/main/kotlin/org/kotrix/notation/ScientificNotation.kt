package org.kotrix.notation

import kotlin.math.*

data class ScientificNotation(private var base_: Double, private var exponent_: Int): Notation {
    constructor(base: Number, exp: Number): this(base.toDouble(), exp.toInt())

    constructor(regular: Number): this(
            base = regular.toDouble() * (10.0).pow(-floor(log10(regular.toDouble().absoluteValue))),
            exp  = sign(regular.toDouble()) * floor(log10(regular.toDouble().absoluteValue))
    )

    constructor(): this(0)

    init {
        val e = (sign(base_) * floor(log10(base_.absoluteValue))).toInt()
        base_ *= (10.0).pow(-e)
        exponent_ += e
    }

    override val base: Double
        get() = base_

    override val exponent: Int
        get() = exponent_

    override val decimal: Double
        get() = base_ * (10.0).pow(exponent_)

    override fun unaryPlus(): ScientificNotation =
            this.copy(base_ = +this.base)

    override fun unaryMinus(): ScientificNotation =
            this.copy(base_ = -this.base)

    override fun plus(other: Notation): ScientificNotation {
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

    override fun minus(other: Notation): ScientificNotation {
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

    override fun times(other: Notation): ScientificNotation {
        other as ScientificNotation
        val (thisBase, thisExp) = this
        val (otherBase, otherExp) = other

        return ScientificNotation(thisBase * otherBase, thisExp + otherExp)
    }

    override fun div(other: Notation): ScientificNotation {
        other as ScientificNotation
        val (thisBase, thisExp) = this
        val (otherBase, otherExp) = other

        return ScientificNotation(thisBase / otherBase, thisExp - otherExp)
    }

    override fun rem(other: Notation): ScientificNotation =
            this - (other * (this / other).floor())


    override fun pow(other: Notation): Notation {
        other as ScientificNotation
        val (thisBase, thisExp) = this

        // TODO ensure no overflow
        val actualValue = other.decimal

        val tempBase = thisBase.pow(actualValue)
        val tempExp = thisExp * actualValue
        val expCarry = floor(log10(tempBase.absoluteValue))
        val (newBase, newExp) = (tempBase * (10.0).pow(-expCarry)) to (tempExp + expCarry)

        return ScientificNotation(newBase, newExp)
    }

    override fun floor(): ScientificNotation =
            this.copy(base_ = floor(this.base))

    override fun ceil(): ScientificNotation =
            this.copy(base_ = ceil(this.base))

    override fun round(precision: Int): ScientificNotation =
            this.copy(base_ = this.base.round(precision = precision))

    private fun Double.round(precision: Int): Double =
            "%.${precision}f".format(this).toDouble()

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