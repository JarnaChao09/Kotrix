package org.kotrix.rational

sealed class Zero : Rational {
    override val numerator: UInt
        get() = 0U
    override val denominator: UInt
        get() = 1U

    override fun toString(): String = "${when(sign) { Sign.Positive -> "+"; Sign.Negative -> "-" }}(0)"

    object Positive : Zero() {
        override val sign: Sign
            get() = Sign.Positive
    }

    object Negative : Zero() {
        override val sign: Sign
            get() = Sign.Negative
    }
}