package org.kotrix.notation

import org.kotrix.utils.Stringify

interface Notation: Comparable<Notation>, Stringify {
    val base: Double

    val exponent: Int

    val decimal: Double

    override fun stringify(): String =
            if (exponent == 0 || exponent == -0)
                "$base"
            else if(base == 0.0)
                "0.0"
            else
                "${base}E$exponent"

    operator fun unaryPlus(): Notation

    operator fun unaryMinus(): Notation

    operator fun plus(other: Notation): Notation

    operator fun minus(other: Notation): Notation

    operator fun times(other: Notation): Notation

    operator fun div(other: Notation): Notation

    operator fun rem(other: Notation): Notation

    fun pow(other: Notation): Notation

    fun floor(): Notation

    fun ceil(): Notation

    fun round(precision: Int): Notation
}

@Suppress("UNCHECKED_CAST")
infix fun <T: Notation> T.pow(other: T): T {
    return this.pow(other) as T
}