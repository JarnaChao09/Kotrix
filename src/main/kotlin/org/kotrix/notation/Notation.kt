package org.kotrix.notation

import org.kotrix.utils.Stringify

interface Notation: Comparable<Notation>, Stringify {
    val base: Double

    val exponent: Int

    override fun stringify(): String {
        if (exponent == 0 || exponent == -0) {
            return "$base"
        }
        return "${base}E$exponent"
    }

    operator fun unaryPlus(): Notation

    operator fun unaryMinus(): Notation

    operator fun plus(other: Notation): Notation

    operator fun minus(other: Notation): Notation

    operator fun times(other: Notation): Notation

    operator fun div(other: Notation): Notation

    operator fun rem(other: Notation): Notation
}