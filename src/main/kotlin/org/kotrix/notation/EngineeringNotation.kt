package org.kotrix.notation

import kotlin.math.*

data class EngineeringNotation(private var base_: Double, private var exponent_: Int): Notation {
    constructor(base: Number, exp: Number): this(base.toDouble(), exp.toInt())

    constructor(regular: Number): this(
            base = regular.toDouble() * (10.0).pow(-floor(log10(regular.toDouble().absoluteValue))),
            exp  = sign(regular.toDouble()) * floor(log10(regular.toDouble().absoluteValue))
    )

    constructor(): this(0)

    init {
        exponent_ = when(exponent_) {
            in 0..2 -> {
                base_ *= (10.0).pow(exponent_)
                0
            }
            else -> {
                val eMod3 = exponent_.absoluteValue % 3
                val e = 3 - eMod3
                if (exponent_ < 0) {
                    base_ *= (10.0).pow(e)
                    exponent_ - e
                } else {
                    base_ *= (10.0).pow(3 - e)
                    exponent_ - eMod3
                }
            }
        }
    }

    override val base: Double
        get() = base_

    override val exponent: Int
        get() = exponent_

    override val decimal: Double
        get() = base_ * (10.0).pow(exponent_)

    override fun unaryPlus(): Notation =
            this.copy(base_ = +this.base)

    override fun unaryMinus(): Notation =
            this.copy(base_ = -this.base)

    override fun plus(other: Notation): Notation {
        TODO("Not yet implemented")
    }

    override fun minus(other: Notation): Notation {
        TODO("Not yet implemented")
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

    override fun pow(other: Notation): Notation {
        TODO("Not yet implemented")
    }

    override fun floor(): Notation {
        TODO("Not yet implemented")
    }

    override fun ceil(): Notation {
        TODO("Not yet implemented")
    }

    override fun round(precision: Int): Notation {
        TODO("Not yet implemented")
    }

    override fun compareTo(other: Notation): Int {
        TODO("Not yet implemented")
    }

}