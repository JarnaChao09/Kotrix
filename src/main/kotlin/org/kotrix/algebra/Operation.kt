package org.kotrix.algebra

// TODO("CHANGE TO USE CONTEXT RECEIVERS")

interface Operation<T> where T : Any {
    fun number(value: Number): T
}

interface GroupOperation<T> : Operation<T> where T : Any {
    fun add(lhs: T, rhs: T): T

    operator fun T.unaryPlus(): T

    operator fun T.unaryMinus(): T

    operator fun T.plus(rhs: T): T = add(this, rhs)

    operator fun T.minus(rhs: T): T = add(this, -rhs)
}

interface RingOperation<T> : GroupOperation<T> where T : Any {
    fun multiply(lhs: T, rhs: T): T

    operator fun T.times(rhs: T): T = multiply(this, rhs)
}

interface FieldOperation<T> : RingOperation<T> where T : Any {
    fun divide(lhs: T, rhs: T): T

    operator fun T.div(rhs: T): T = divide(this, rhs)
}

interface SpaceOperation<T> : FieldOperation<T> where T : Any {
    fun scale(value: T, by: Double): T

    operator fun T.times(rhs: Number): T = scale(this, rhs.toDouble())

    operator fun T.div(rhs: Number): T = scale(this, 1.0 / rhs.toDouble())

    operator fun Number.times(rhs: T): T = scale(rhs, this.toDouble())
}

interface Group<T> : GroupOperation<T> where T : Any {
    val zero: T
}

interface Ring<T> : Group<T>, RingOperation<T> where T : Any {
    val one: T
}

interface Field<T> : Ring<T>, FieldOperation<T> where T : Any

// todo determine if Space is appropriate name or use Module instead
interface Space<T> : Field<T>, SpaceOperation<T> where T : Any