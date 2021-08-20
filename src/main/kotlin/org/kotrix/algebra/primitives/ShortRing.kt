package org.kotrix.algebra.primitives

import org.kotrix.algebra.Ring

@Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE", "EXTENSION_SHADOWED_BY_MEMBER")
object ShortRing : Ring<Short> {
    override inline fun number(value: Number): Short = value.toShort()

    override inline val zero: Short
        get() = (0).toShort()

    override inline val one: Short
        get() = (1).toShort()

    override inline fun add(lhs: Short, rhs: Short): Short = (lhs + rhs).toShort()

    override inline fun multiply(lhs: Short, rhs: Short): Short = (lhs * rhs).toShort()

    override inline fun Short.unaryPlus(): Short       = (+this).toShort()
    override inline fun Short.unaryMinus(): Short      = (-this).toShort()
    override inline fun Short.plus(rhs: Short): Short  = (this + rhs).toShort()
    override inline fun Short.minus(rhs: Short): Short = (this - rhs).toShort()
    override inline fun Short.times(rhs: Short): Short = (this * rhs).toShort()
}