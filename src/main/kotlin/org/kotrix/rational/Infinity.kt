package org.kotrix.rational

sealed class Infinity : Rational {
    override val numerator: UInt
        get() = 1U
    override val denominator: UInt
        get() = 0U

    override fun toString(): String = "${when(sign) { Sign.Positive -> "+"; Sign.Negative -> "-" }}(Infinity)"

    object Positive : Infinity() {
        override val sign: Sign
            get() = Sign.Positive
    }

    object Negative : Infinity() {
        override val sign: Sign
            get() = Sign.Negative
    }
}