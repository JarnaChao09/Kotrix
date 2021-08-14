package org.kotrix.vector

import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class BooleanVector(length: Int = 10, initBlock: (Int) -> Boolean = { false }): VectorImpl<Boolean>(length, initBlock) {
    constructor(length: Int = 10, initValue: Boolean): this(length, initBlock = { initValue })

    override val type: KClass<out Boolean>
        get() = super.type

    override val list: List<Boolean>
        get() = super.list

    val array: BooleanArray
        get() = this.toArray()

    fun toArray(): BooleanArray {
        return BooleanArray(this.size) { i -> this[i] }
    }

    operator fun not(): BooleanVector =
        BooleanVector(this.size) { i -> !this[i] }
}

infix fun BooleanVector.and(other: BooleanVector): BooleanVector =
    if (this.size != other.size)
        throw IllegalArgumentException("${this.size} != ${other.size}")
    else
        BooleanVector(this.size) { i -> this[i] && other[i] }

infix fun BooleanVector.or(other: BooleanVector): BooleanVector =
    if (this.size != other.size)
        throw IllegalArgumentException("${this.size} != ${other.size}")
    else
        BooleanVector(this.size) { i -> this[i] || other[i] }

infix fun BooleanVector.xor(other: BooleanVector): BooleanVector =
    if (this.size != other.size)
        throw IllegalArgumentException("${this.size} != ${other.size}")
    else
        BooleanVector(this.size) { i -> this[i] xor other[i] }
