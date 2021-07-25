package org.kotrix.rational

data class RationalImpl(
    override val numerator: Int,
    override val denominator: Int,
    override val sign: Sign
): Rational {
    override fun toString(): String =
        "${when (sign) { Sign.Positive -> "+"; Sign.Negative -> "-" }}($numerator / $denominator)"
}