package org.kotrix.algebra.primitives

import org.kotrix.algebra.Field

@Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE", "EXTENSION_SHADOWED_BY_MEMBER",
    "RemoveRedundantCallsOfConversionMethods"
)
object FloatField : Field<Float> {
    override inline fun number(value: Number) = value.toFloat()

    override inline val zero: Float
        get() = (0.0).toFloat()

    override inline val one: Float
        get() = (1.0).toFloat()

    override inline fun add(lhs: Float, rhs: Float): Float = (lhs + rhs).toFloat()

    override inline fun multiply(lhs: Float, rhs: Float): Float = (lhs * rhs).toFloat()

    override inline fun divide(lhs: Float, rhs: Float): Float = (lhs / rhs).toFloat()

    override inline fun Float.unaryPlus(): Float       = (+this).toFloat()
    override inline fun Float.unaryMinus(): Float      = (-this).toFloat()
    override inline fun Float.plus(rhs: Float): Float  = (this + rhs).toFloat()
    override inline fun Float.minus(rhs: Float): Float = (this - rhs).toFloat()
    override inline fun Float.times(rhs: Float): Float = (this * rhs).toFloat()
    override inline fun Float.div(rhs: Float): Float   = (this / rhs).toFloat()
}