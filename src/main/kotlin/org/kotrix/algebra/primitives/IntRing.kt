package org.kotrix.algebra.primitives

import org.kotrix.algebra.Ring

@Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE", "EXTENSION_SHADOWED_BY_MEMBER",
    "RemoveRedundantCallsOfConversionMethods"
)
object IntRing : Ring<Int> {
    override inline fun number(value: Number): Int = value.toInt()

    override inline val zero: Int
        get() = (0).toInt()

    override inline val one: Int
        get() = (1).toInt()

    override inline fun add(lhs: Int, rhs: Int): Int = (lhs + rhs).toInt()

    override inline fun multiply(lhs: Int, rhs: Int): Int = (lhs * rhs).toInt()

    override inline fun Int.unaryPlus(): Int     = (+this).toInt()
    override inline fun Int.unaryMinus(): Int    = (-this).toInt()
    override inline fun Int.plus(rhs: Int): Int  = (this + rhs).toInt()
    override inline fun Int.minus(rhs: Int): Int = (this - rhs).toInt()
    override inline fun Int.times(rhs: Int): Int = (this * rhs).toInt()
}