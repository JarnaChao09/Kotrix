package org.kotrix.rational

data class RationalImpl(
    override val numerator: UInt,
    override val denominator: UInt,
    override val sign: Sign
): Rational {
    override fun toString(): String =
        "${when (sign) { Sign.Positive -> "+"; Sign.Negative -> "-" }}($numerator${if (denominator == 1U) "" else " / $denominator"})"
}