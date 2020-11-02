package org.kotrix.matrix

import org.kotrix.utils.Size
import org.kotrix.utils.Slice
import org.kotrix.vector.Vector
import org.kotrix.vector.VectorBase

import java.util.stream.Stream

import kotlin.reflect.KClass

interface MatrixBase<T>: Iterable<T> where T: Any {
    var size: Size

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

    operator fun get(index: Int): Vector<T>

    operator fun get(indexSlice: Slice): MatrixBase<T>

    operator fun get(indexR: Int, indexC: Int): T

    operator fun get(indexRSlice: Slice, indexCSlice: Slice): MatrixBase<T>

    operator fun get(indexRSlice: Slice, indexC: Int): Vector<T>

    operator fun get(indexR: Int, indexCSlice: Slice): MatrixBase<T>

    operator fun set(index: Int, value: Vector<T>)

    operator fun set(indexSlice: Slice, value: MatrixBase<T>)

    operator fun set(indexR: Int, indexC: Int, value: T)

    operator fun set(indexRSlice: Slice, indexCSlice: Slice, value: MatrixBase<T>)

    operator fun set(indexRSlice: Slice, indexC: Int, value: Vector<T>)

    operator fun set(indexR: Int, indexCSlice: Slice, value: Vector<T>)

    fun rowAppend(other: MatrixBase<T>): MatrixBase<T>

    fun removeRow(indexR: Int): VectorBase<T>

    fun colAppend(other: MatrixBase<T>): MatrixBase<T>

    fun removeCol(indexC: Int): VectorBase<T>

    fun toVector(): Vector<out Vector<T>>

    fun toList(): List<List<T>>

    operator fun component1(): Vector<out T>

    operator fun component2(): Vector<out T>

    operator fun component3(): Vector<out T>

    operator fun component4(): Vector<out T>
}
