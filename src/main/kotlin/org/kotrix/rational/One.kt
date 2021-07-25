package org.kotrix.rational

sealed class One : Rational {
    override val numerator: Int
        get() = 1
    override val denominator: Int
        get() = 1

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