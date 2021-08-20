package org.kotrix.algebra.primitives

import org.kotrix.algebra.Ring

@Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE", "EXTENSION_SHADOWED_BY_MEMBER")
object ByteRing : Ring<Byte> {
    override inline fun number(value: Number): Byte = value.toByte()

    override inline val zero: Byte
        get() = (0).toByte()

    override inline val one: Byte
        get() = (1).toByte()

    override inline fun add(lhs: Byte, rhs: Byte): Byte = (lhs + rhs).toByte()

    override inline fun multiply(lhs: Byte, rhs: Byte): Byte = (lhs * rhs).toByte()

    override inline fun Byte.unaryPlus(): Byte      = (+this).toByte()
    override inline fun Byte.unaryMinus(): Byte     = (-this).toByte()
    override inline fun Byte.plus(rhs: Byte): Byte  = (this + rhs).toByte()
    override inline fun Byte.minus(rhs: Byte): Byte = (this - rhs).toByte()
    override inline fun Byte.times(rhs: Byte): Byte = (this * rhs).toByte()
}