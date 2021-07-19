package org.kotrix.matrix

import org.kotrix.utils.Size
import org.kotrix.utils.Slice
import org.kotrix.utils.by
import org.kotrix.vector.Vector
import org.kotrix.vector.VectorBase
import org.kotrix.vector.toVector
import java.lang.IllegalArgumentException
import java.util.stream.Stream
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.KClass

@Suppress()
open class Matrix<T>(val dim: Size = Size(3, 3), val initBlock: (r: Int, c: Int) -> T):
    MatrixBase<T> where T: Any {
    sealed class Error(e: String): Throwable(e) {
        data class DimensionMisMatch(val e: String = "Dimension mismatch"): Error(e)

        data class NotRegular(val e: String = "Not Regular Matrix"): Error(e)
    }

    constructor(x: Int, y: Int, initBlock: (Int) -> T): this(dim = Size(
        x,
        y
    ), initBlock = { _, _ -> initBlock(0)})

    constructor(vector: Vector<T>, asCol: Boolean = false):
            this(
                dim = if (asCol) Size(vector.size, 1) else Size(
                    1,
                    vector.size
                ),
                initBlock = if (asCol) { i, _ -> vector[i]} else { _, i -> vector[i]  }
            )

    constructor(matListOfVector: Vector<Vector<T>>): this(
        dim = Size(matListOfVector.size, matListOfVector[0].size),
        initBlock = { r, c -> matListOfVector[r][c] }
    )

    constructor(matrix: Matrix<T>): this(dim = matrix.size, initBlock = { r, c -> matrix[r, c]})

    constructor(dim1: Size, asCols: Boolean, initBlock: (Int) -> T): this(dim = dim1, initBlock = if (asCols) { i, _ -> initBlock(i) } else { _, i -> initBlock(i) })

    sealed class Scope<T> where T: Any {
        class Base<T: Any>: Scope<T>()

        val matrix: Vector<Vector<T>> = Vector(0) { Vector.nulls<T>(0) }

        operator fun Vector<T>.unaryPlus(): Scope<T> =
            this@Scope.also { this@Scope.matrix.append(this) }
    }

    companion object {
        inline fun <reified T: Any> empty(dim: Size): Matrix<T> {
            return Matrix(dim, asCols = true) { T::class.java.getDeclaredConstructor().newInstance() }
        }

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun <T: Any> nulls(dim: Size): Matrix<T> {
            return Matrix(dim, asCols = true) { Any() as T }
        }

        @JvmStatic
        fun <T: Any> of(vararg matrix: List<T>): Matrix<T> {
            val elements = Vector(matrix.size) { Vector.nulls<T>() }
            for (i in matrix.indices) {
                elements[i] = Vector(matrix[i])
            }
            return Matrix(elements)
        }

        @JvmStatic
        fun <T: Any> of(dim: Size, vararg elements: T): Matrix<T> {
            val mat = nulls<T>(dim)
            for (i in 0 until dim.x) {
                for (j in 0 until dim.y) {
                    mat[i, j] = elements[dim.x * i + j]
                }
            }
            return mat
        }
    }

    class MatrixIterator<U>(private val value: Matrix<U>): Iterator<U> where U: Any {
        private var currentX = 0
        private var currentY = 0

        override fun hasNext(): Boolean {
            return currentX < value.dim.x
        }

        override fun next(): U {
            val ret = value[currentX, currentY++]
            currentY = if (currentY == value.dim.y) 0 else currentY
            currentX += if (currentY == 0) 1 else 0
            return ret
        }
    }

    class MatrixIteratorWithIndex<U>(private val value: Matrix<U>): Iterator<Triple<U, Int, Int>> where U: Any {
        private var currentX = 0
        private var currentY = 0

        override fun hasNext(): Boolean {
            return currentX < value.dim.x
        }

        override fun next(): Triple<U, Int, Int> {
            val storedX = currentX
            val storedY = currentY
            val ret = value[currentX, currentY++]
            currentY = if (currentY == value.dim.y) 0 else currentY
            currentX += if (currentY == 0) 1 else 0
            return Triple(ret, storedX, storedY)
        }
    }

    override var size: Size = dim

    protected open var internalMatrix: Vector<Vector<T>> =
        Vector(dim.x) { i ->
            Vector(dim.y) { j ->
                initBlock(
                    i,
                    j
                )
            }
        }

    override val type: KClass<out T> by lazy { this.internalMatrix[0][0]::class }

    override val rowLength
        get() = size.x

    override val colLength
        get() = size.y

    open val t: Matrix<T>
        get() = this.transpose()

    open val vector: Vector<out Vector<T>>
        get() = this.toVector()

    open val list: List<List<T>>
        get() = this.toList()

    open val stream
        get() = this.stream()

    override fun toVector(): Vector<out Vector<T>> = Vector(this.internalMatrix)

    override fun toList(): List<List<T>> {
        val ret = MutableList(this.size.x) { emptyList<T>() }
        var index = 0
        for (i in this.internalMatrix) {
            ret[index++] = i.toList()
        }
        return ret
    }

    override operator fun iterator(): Iterator<T> = MatrixIterator(this)

    override val withIndices: Iterator<Triple<T, Int, Int>>
        get() = MatrixIteratorWithIndex(this)

    override operator fun get(index: Int): Vector<T> =
        Vector(internalMatrix[index])

    override operator fun get(indexSlice: Slice): Matrix<T> =
        Matrix(internalMatrix[indexSlice])

    override operator fun get(indexR: Int, indexC: Int): T = internalMatrix[indexR][indexC]

    override operator fun get(indexRSlice: Slice, indexC: Int): Vector<T> {
        val ret = Vector(indexRSlice.size) { initBlock(0, 0) }
        val copy = this[indexRSlice]
        var r = 0
        for (i in indexRSlice) {
            ret[r++] = copy[(this.rowLength + i) % this.rowLength, indexC]
        }
        return ret
    }

    override operator fun get(indexR: Int, indexCSlice: Slice): Matrix<T> {
        val ret = Matrix(1, indexCSlice.size) { initBlock(0, 0) }
        val copy = this[indexR]
        var c = 0
        for (i in indexCSlice) {
            ret[0, c++] = copy[(this.colLength + i) % this.colLength]
        }
        return ret
    }

    override operator fun get(indexRSlice: Slice, indexCSlice: Slice): Matrix<T> {
        val ret = Matrix(indexRSlice.size, indexCSlice.size) { initBlock(0, 0) }
        val copy = this[indexRSlice]
        for ((r, i) in indexRSlice.withIndex()) {
            var c = 0
            for (j in indexCSlice) {
                ret[(this.rowLength + r) % this.rowLength, c++] = copy[(this.rowLength + i) % this.rowLength, (this.colLength + j) % this.colLength]
            }
        }
        return ret
    }

    override operator fun set(index: Int, value: Vector<T>) {
        if (this.colLength != value.size) throw IllegalArgumentException("VECTOR SIZE ${value.size} INCOMPATIBLE WITH ${this.dim}")
        this.internalMatrix[index] = value
    }

    override operator fun set(indexR: Int, indexC: Int, value: T) {
        this.internalMatrix[indexR][indexC] = value
    }

    override operator fun set(indexSlice: Slice, value: MatrixBase<T>) {
        if (indexSlice.end > this.rowLength) throw IllegalArgumentException("MATRIX SIZE ${value.size} INCOMPATIBLE WITH ${this.dim}")
        value as Matrix<T>
        this.internalMatrix[indexSlice] = value.internalMatrix
    }

    override fun set(indexRSlice: Slice, indexC: Int, value: Vector<T>) {
        var r = 0
        for (i in indexRSlice) {
            this[i, indexC] = value[r++]
        }
    }

    override fun set(indexR: Int, indexCSlice: Slice, value: Vector<T>) {
        var c = 0
        for (i in indexCSlice) {
            this[indexR, i] = value[c++]
        }
    }

    override fun set(indexRSlice: Slice, indexCSlice: Slice, value: MatrixBase<T>) {
        for ((r, i) in indexRSlice.withIndex()) {
            var c = 0
            for (j in indexCSlice) {
                this[i, j] = value[r, c++]
            }
        }
    }

    override fun transpose(): Matrix<T> {
        val ret = Matrix(this.colLength, this.rowLength) { initBlock(0, 0) }
        for (i in 0 until ret.rowLength) {
            for (j in 0 until ret.colLength) {
                ret[i, j] = this[j, i]
            }
        }
        return ret
    }

    override fun stream(): Stream<T> {
        val ret: Stream.Builder<T> = Stream.builder()
        for (v in this) {
            ret.add(v)
        }
        return ret.build()
    }

    override fun rowAppend(other: MatrixBase<T>): Matrix<T> = this.also { other as Matrix<T>; it.internalMatrix.appendAll(other.internalMatrix); it.size = it.internalMatrix.size by it.internalMatrix[0].size }

    override fun removeRow(indexR: Int): Vector<T> =
            this.internalMatrix.removeAt(indexR)

    override fun colAppend(other: MatrixBase<T>): Matrix<T> {
        other as Matrix<T>
        for (i in 0 until min(this.internalMatrix.size, other.internalMatrix.size)) {
            this.internalMatrix[i].appendAll(other.internalMatrix[i])
        }
        return this.also { it.size = it.internalMatrix.size by it.internalMatrix[0].size }
    }

    override fun removeCol(indexC: Int): Vector<T> {
        val ret = Vector.nulls<T>()

        this.internalMatrix.forEach {
            ret append it.removeAt(indexC)
        }

        return ret
    }

    private operator fun String.times(maxLength: Int): String {
        var dummy = ""
        for (i in 0 until maxLength) {
            dummy += this
        }
        return dummy
    }

    override fun toString(): String {
        val retString = List(size = 0) { Vector(0) { "" } }.toMutableList()
        var maxLength = 0
        internalMatrix.forEach { it.forEach { i -> maxLength = max(maxLength, i.toString().length) } }
        for (r  in internalMatrix) {
            val dummy = Vector(size = 0) { "" }
            for (c in r) {
                dummy append (" " * (maxLength - c.toString().length) + c.toString())
            }
            retString += dummy
        }
        return retString
            .joinToString(prefix = "[", postfix = "]", separator = ",\n ")
            .replace("<", "[")
            .replace(">", "]")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix<*>

        if (internalMatrix != other.internalMatrix) return false

        return true
    }

    override fun equal(other: MatrixBase<T>): BooleanMatrix =
        if (this.size != other.size)
            throw Error.DimensionMisMatch()
        else
            BooleanMatrix(this.size) { r, c -> this[r, c] == other[r, c]}

    override fun hashCode(): Int {
        var result = dim.hashCode()
        result = 31 * result + initBlock.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + internalMatrix.hashCode()
        return result
    }

    override fun component1(): Vector<out T> =
        this[0]

    override fun component2(): Vector<out T> =
        this[1]

    override fun component3(): Vector<out T> =
        this[2]

    override fun component4(): Vector<out T> =
        this[3]
}
