package org.kotrix.rational

sealed class Infinity : Rational {
    override val numerator: Int
        get() = 1
    override val denominator: Int
        get() = 0

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