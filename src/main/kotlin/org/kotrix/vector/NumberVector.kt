package org.kotrix.vector

abstract class NumberVector<T>(length: Int = 10, initBlock: (Int) -> T): Vector<T>(length, initBlock) where T: Number {
    abstract operator fun plus(other: NumberVector<T>): NumberVector<T>

    abstract operator fun minus(other: NumberVector<T>): NumberVector<T>

    abstract operator fun times(other: NumberVector<T>): NumberVector<T>

    abstract operator fun div(other: NumberVector<T>): NumberVector<T>

    abstract operator fun rem(other: NumberVector<T>): NumberVector<T>

    abstract infix fun pow(other: NumberVector<T>): NumberVector<T>

    abstract operator fun plusAssign(other: NumberVector<T>)

    abstract operator fun minusAssign(other: NumberVector<T>)

    abstract operator fun timesAssign(other: NumberVector<T>)

    abstract operator fun divAssign(other: NumberVector<T>)

    abstract operator fun remAssign(other: NumberVector<T>)

    abstract infix fun powAssign(other: NumberVector<T>)

    abstract operator fun unaryPlus(): NumberVector<T>

    abstract operator fun unaryMinus(): NumberVector<T>

    abstract infix fun dot(other: NumberVector<T>): T

    abstract infix fun cross(other: NumberVector<T>): NumberVector<T>

    abstract fun toArray(): Array<T>

    abstract fun toIntVector(): IntVector

    abstract fun toDoubleVector(): DoubleVector
}