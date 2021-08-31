package org.kotrix.vector

import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class BooleanVectorOld(length: Int = 10, initBlock: (Int) -> Boolean = { false }): VectorImplOld<Boolean>(length, initBlock) {
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

    operator fun not(): BooleanVectorOld =
        BooleanVectorOld(this.size) { i -> !this[i] }
}

infix fun BooleanVectorOld.and(other: BooleanVectorOld): BooleanVectorOld =
    if (this.size != other.size)
        throw IllegalArgumentException("${this.size} != ${other.size}")
    else
        BooleanVectorOld(this.size) { i -> this[i] && other[i] }

infix fun BooleanVectorOld.or(other: BooleanVectorOld): BooleanVectorOld =
    if (this.size != other.size)
        throw IllegalArgumentException("${this.size} != ${other.size}")
    else
        BooleanVectorOld(this.size) { i -> this[i] || other[i] }

infix fun BooleanVectorOld.xor(other: BooleanVectorOld): BooleanVectorOld =
    if (this.size != other.size)
        throw IllegalArgumentException("${this.size} != ${other.size}")
    else
        BooleanVectorOld(this.size) { i -> this[i] xor other[i] }
