package org.kotrix.rational

sealed class One : Rational {
    override val numerator: UInt
        get() = 1U
    override val denominator: UInt
        get() = 1U

    override fun toString(): String = "${when(sign) { Sign.Positive -> "+"; Sign.Negative -> "-" }}(1)"

    object Positive : One() {
        override val sign: Sign
            get() = Sign.Positive
    }

    object Negative : One() {
        override val sign: Sign
            get() = Sign.Negative
    }
}