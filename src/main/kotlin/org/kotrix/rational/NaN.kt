package org.kotrix.rational

object NaN : Rational {
    override val numerator: Int
        get() = 0
    override val denominator: Int
        get() = 0
    override val sign: Sign
        get() = throw Exception("NaN does not have a Sign")

    override fun toString(): String = "NaN"
}