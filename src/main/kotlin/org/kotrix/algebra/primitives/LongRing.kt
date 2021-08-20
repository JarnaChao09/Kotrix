package org.kotrix.algebra.primitives

import org.kotrix.algebra.Ring

@Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE", "EXTENSION_SHADOWED_BY_MEMBER",
    "RemoveRedundantCallsOfConversionMethods"
)
object LongRing : Ring<Long> {
    override inline fun number(value: Number): Long = value.toLong()

    override inline val zero: Long
        get() = (0).toLong()

    override inline val one: Long
        get() = (1).toLong()

    override inline fun add(lhs: Long, rhs: Long): Long = (lhs + rhs).toLong()

    override inline fun multiply(lhs: Long, rhs: Long): Long = (lhs * rhs).toLong()

    override inline fun Long.unaryPlus(): Long      = (+this).toLong()
    override inline fun Long.unaryMinus(): Long     = (-this).toLong()
    override inline fun Long.plus(rhs: Long): Long  = (this + rhs).toLong()
    override inline fun Long.minus(rhs: Long): Long = (this - rhs).toLong()
    override inline fun Long.times(rhs: Long): Long = (this * rhs).toLong()
}