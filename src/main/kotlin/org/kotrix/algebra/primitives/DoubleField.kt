package org.kotrix.algebra.primitives

import org.kotrix.algebra.Field

@Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE", "EXTENSION_SHADOWED_BY_MEMBER",
    "RemoveRedundantCallsOfConversionMethods"
)
object DoubleField : Field<Double> {
    override inline fun number(value: Number) = value.toDouble()

    override inline val zero: Double
        get() = (0.0).toDouble()

    override inline val one: Double
        get() = (1.0).toDouble()

    override inline fun add(lhs: Double, rhs: Double): Double = (lhs + rhs).toDouble()

    override inline fun multiply(lhs: Double, rhs: Double): Double = (lhs * rhs).toDouble()

    override inline fun divide(lhs: Double, rhs: Double): Double = (lhs / rhs).toDouble()

    override inline fun Double.unaryPlus(): Double        = (+this).toDouble()
    override inline fun Double.unaryMinus(): Double       = (-this).toDouble()
    override inline fun Double.plus(rhs: Double): Double  = (this + rhs).toDouble()
    override inline fun Double.minus(rhs: Double): Double = (this - rhs).toDouble()
    override inline fun Double.times(rhs: Double): Double = (this * rhs).toDouble()
    override inline fun Double.div(rhs: Double): Double   = (this / rhs).toDouble()
}