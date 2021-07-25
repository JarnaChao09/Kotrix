package org.kotrix.rational

sealed class Zero : Rational {
    override val numerator: Int
        get() = 0
    override val denominator: Int
        get() = 1

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