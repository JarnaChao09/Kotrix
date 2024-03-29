package org.kotrix.matrix

import org.kotrix.utils.Shape
import org.kotrix.utils.Slice
import org.kotrix.vector.VectorImplOld
import org.kotrix.vector.VectorBase

import java.util.stream.Stream

import kotlin.reflect.KClass

interface MatrixBase<T>: Iterable<T> where T: Any {
    var shape: Shape

    val type: KClass<out T>

    val rowLength: Int

    val rowRange: IntRange
        get() = 0 until rowLength

    val colLength: Int

    val colRange: IntRange
        get() = 0 until colLength

    fun transpose(): MatrixBase<T>

    fun stream(): Stream<T>

    override operator fun iterator(): Iterator<T>

    val withIndices: Iterator<Triple<T, Int, Int>>

    infix fun equal(other: MatrixBase<T>): BooleanMatrix

    operator fun get(index: Int): VectorImplOld<T>

    operator fun get(indexSlice: Slice): MatrixBase<T>

    operator fun get(indexR: Int, indexC: Int): T

    operator fun get(indexRSlice: Slice, indexCSlice: Slice): MatrixBase<T>

    operator fun get(indexRSlice: Slice, indexC: Int): VectorImplOld<T>

    operator fun get(indexR: Int, indexCSlice: Slice): MatrixBase<T>

    operator fun set(index: Int, value: VectorImplOld<T>)

    operator fun set(indexSlice: Slice, value: MatrixBase<T>)

    operator fun set(indexR: Int, indexC: Int, value: T)

    operator fun set(indexRSlice: Slice, indexCSlice: Slice, value: MatrixBase<T>)

    operator fun set(indexRSlice: Slice, indexC: Int, value: VectorImplOld<T>)

    operator fun set(indexR: Int, indexCSlice: Slice, value: VectorImplOld<T>)

    fun rowAppend(other: MatrixBase<T>): MatrixBase<T>

    fun removeRow(indexR: Int): VectorBase<T>

    fun colAppend(other: MatrixBase<T>): MatrixBase<T>

    fun removeCol(indexC: Int): VectorBase<T>

    fun toVector(): VectorImplOld<out VectorImplOld<T>>

    fun toList(): List<List<T>>

    operator fun component1(): VectorImplOld<out T>

    operator fun component2(): VectorImplOld<out T>

    operator fun component3(): VectorImplOld<out T>

    operator fun component4(): VectorImplOld<out T>
}
