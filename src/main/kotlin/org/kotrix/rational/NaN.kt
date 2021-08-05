package org.kotrix.rational

object NaN : Rational {
    override val numerator: UInt
        get() = 0U
    override val denominator: UInt
        get() = 0U
    override val sign: Sign
        get() = throw Exception("NaN does not have a Sign")

    override fun toString(): String = "NaN"
}