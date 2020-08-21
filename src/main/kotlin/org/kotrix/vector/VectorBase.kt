package org.kotrix.vector

import org.kotrix.matrix.Matrix
import org.kotrix.utils.Slice
import kotlin.reflect.KClass

interface VectorBase<T>: Collection<T> where T: Any {
    val type: KClass<out T>

    val list: List<T>

    val indices: IntRange

    val withIndices: Iterator<IndexedValue<T>>

    infix fun append(other: T)

    infix fun push(other: T)

    fun put(other: T, at: Int)

    fun remove(other: T): Boolean

    fun removeAt(index: Int): T

    infix fun appendAll(value: VectorBase<T>)

    override fun equals(other: Any?): Boolean

    operator fun get(index: Int): T

    operator fun get(indexSlice: Slice): VectorBase<T>

    operator fun set(index: Int, value: T)

    operator fun set(indexSlice: Slice, value: VectorBase<T>)

    override operator fun contains(element: T): Boolean

    operator fun invoke(): VectorBase<T>

    fun forEach(action: (T) -> Unit)

    fun forEachIndexed(action: (T, index: Int) -> Unit)

    fun map(action: (T) -> T): VectorBase<T>

    fun mapIndexed(action: (T, index: Int) -> T): VectorBase<T>

    infix fun equal(other: Vector<T>): Vector<Boolean>

    fun toMatrix(asCol: Boolean = true): Matrix<T>

    fun toList(): List<T>
}