package org.kotrix.vector

interface NumberVectors<T>: VectorBase<T> where T: Number {
    val magnitude: Double

    operator fun plus(other: NumberVectors<T>): NumberVectors<T>

    operator fun minus(other: NumberVectors<T>): NumberVectors<T>

    operator fun times(other: NumberVectors<T>): NumberVectors<T>

    operator fun div(other: NumberVectors<T>): NumberVectors<T>

    operator fun rem(other: NumberVectors<T>): NumberVectors<T>

    fun pow(other: NumberVectors<T>): NumberVectors<T>

    operator fun plusAssign(other: NumberVectors<T>)

    operator fun minusAssign(other: NumberVectors<T>)

    operator fun timesAssign(other: NumberVectors<T>)

    operator fun divAssign(other: NumberVectors<T>)

    operator fun remAssign(other: NumberVectors<T>)

    fun powAssign(other: NumberVectors<T>)

    operator fun unaryPlus(): NumberVectors<T>

    operator fun unaryMinus(): NumberVectors<T>

    fun dot(other: NumberVectors<T>): T

    fun cross(other: NumberVectors<T>): NumberVectors<T>

    fun scalarProject(other: NumberVectors<T>): Double

    fun projectOnto(other: NumberVectors<T>): NumberVectors<T>

    fun toArray(): Array<T>

    fun toIntVector(): IntVectorOld

    fun toDoubleVector(): DoubleVector
}