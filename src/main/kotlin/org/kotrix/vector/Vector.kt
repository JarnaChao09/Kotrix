package org.kotrix.vector

import org.kotrix.matrix.Matrix
import org.kotrix.utils.Slice
import kotlin.reflect.KClass
import kotlin.streams.toList

open class Vector<T>(length: Int = 10, initBlock: (Int) -> T): VectorBase<T> where T: Any {
    constructor(length: Int = 10, initValue: T): this(length, initBlock = { initValue })

    constructor(list: List<T>): this(length = list.size, initBlock = { i -> list[i] })

    constructor(vec: Vector<T>): this(length = vec.length, initBlock = { i -> vec[i] })

    sealed class Scope<T> where T: Any {
        val actions: MutableList<Scope<T>> = emptyList<Scope<T>>().toMutableList()

        class Base<T : Any>: Scope<T>()

        class Error(msg: String = "Illegal Type was found"): Throwable(msg)

        data class Append<T: Any>(val value: T, var times: Int = 1): Scope<T>()

        data class Push<T: Any>(val value: T, var times: Int = 1): Scope<T>()

        data class Put<T: Any>(val value: T, val at: Int, var times: Int = 1): Scope<T>()

        data class Repeat<T: Any>(val times: Int, val repeat: Scope<T>.() -> Scope<T>): Scope<T>() {
            fun run(): Scope<T> {
                val base = Base<T>().repeat()
                for (i in base.actions) {
                    when(i) {
                        is Append -> i.times = this.times
                        is Push -> i.times = this.times
                        is Put -> i.times = this.times
                        else -> throw Error()
                    }
                }
                return base
            }
        }

        operator fun T.unaryPlus(): Scope<T> =
            this@Scope.also { this@Scope.actions.add(Append(this)) }

        operator fun T.not(): Scope<T> =
            this@Scope.also { this@Scope.actions.add(Append(this)) }
    }

    class VectorIterator<U>(val value: Vector<U>): Iterator<U> where U: Any {
        private var current = 0
        override fun hasNext(): Boolean {
            return current < value.length
        }

        override fun next(): U {
            return value[current++]
        }

    }

    class VectorIteratorWithIndices<U>(val value: Vector<U>): Iterator<Pair<U, Int>> where U: Any {
        private var current = 0
        override fun hasNext(): Boolean {
            return current < value.length
        }

        override fun next(): Pair<U, Int> =
            value[current] to current++
    }

    companion object {
        @JvmStatic
        inline fun <reified T: Any> empty(size: Int = 1): Vector<T> {
            return Vector(size) { T::class.java.newInstance() }
        }

        @JvmStatic
        fun <T: Any> of(vararg elements: T): Vector<T> =
            Vector(elements.size) { i -> elements[i] }

        @Suppress("UNCHECKED_CAST")
        fun <T: Any> nulls(size: Int = 1): Vector<T> {
            return Vector(size) { Any() as T }
        }
    }

    override fun appendAll(value: VectorBase<T>) {
        for (i in value) {
            this.append(i)
        }
    }

    override fun append(other: T) {
        this._arr.add(other)
    }

    override fun push(other: T) {
        this._arr.add(0, other)
    }

    override fun put(other: T, at: Int) {
        this._arr.add(at, other)
    }

    override fun remove(other: T): Boolean =
        this._arr.remove(other)

    override fun removeAt(index: Int): T =
        this._arr.removeAt(index)

    protected var _arr: MutableList<T> = List(length) { i -> initBlock(i) }.toMutableList()

    override val length: Int
        get() = this._arr.size

    override val type: KClass<out T>
        get() = _arr[0]::class

    override val list: List<T>
        get() = this.toList()

    override fun toString(): String {
        if (length == 0) {
            return "[]"
        }
        val retString = List(0) { "" }.toMutableList()
        val maxLength: Int = _arr.map { x -> x }.stream().mapToInt { x -> x.toString().length }.sorted().toList().toMutableList()[this.length-1] + 1
        for (i in this._arr) {
            val currentLength = i.toString().length
            retString += " " * (maxLength - currentLength) + i.toString()
        }
        return retString.joinToString(prefix = "[", postfix = "]", separator = ",")
    }

    private operator fun String.times(maxLength: Int): String {
        var dummy = ""
        for (i in 0 until maxLength) {
            dummy += this
        }
        return dummy
    }

    override operator fun iterator(): Iterator<T> =
        VectorIterator(this)

    override fun indices(): IntRange =
        0 until this.length

    override fun withIndices(): Iterator<Pair<T, Int>> =
        VectorIteratorWithIndices(this)

    override operator fun get(index: Int): T = this._arr[(length + index) % length]

    override operator fun get(indexSlice: Slice): VectorBase<T> {
        val ret = nulls<T>()
        val subList: MutableList<T> = emptyList<T>().toMutableList()
        for (i: Int in indexSlice) {
            subList.add(this._arr[(length + i) % length])
        }
        ret._arr = subList
        return ret
    }

    override operator fun set(index: Int, value: T) {
        this._arr[(length + index) % length] = value
    }

    override operator fun set(indexSlice: Slice, value: VectorBase<T>) {
        var counter = 0
        for (i in indexSlice) {
            this[(length + i) % length] = value[counter++]
        }
    }

    override fun forEach(action: (T) -> Unit) {
        for (i in this) {
            action(i)
        }
    }

    override fun forEachIndexed(action: (T, index: Int) -> Unit) {
        var index = 0
        for (i in this) {
            action(i, index++)
        }
    }

    override fun map(action: (T) -> T): VectorBase<T> {
        val ret = nulls<T>()
        ret._arr = emptyList<T>().toMutableList()
        for (i in this) {
            ret.append(action(i))
        }
        return ret
    }

    override fun mapIndexed(action: (T, index: Int) -> T): VectorBase<T> {
        var index = 0
        val ret = nulls<T>()
        ret._arr = emptyList<T>().toMutableList()
        for (i in this) {
            ret.append(action(i, index++))
        }
        return ret
    }

    override fun toMatrix(asCol: Boolean): Matrix<T> =
        Matrix(this, asCol = asCol)

    override fun toList(): List<T> =
        List(this.length) { i -> this[i] }

    override fun contains(other: T): Boolean =
        other in this._arr

    override fun invoke(): VectorBase<T> {
        return nulls()
    }

    override fun hashCode(): Int {
        return _arr.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vector<*>

        if (_arr != other._arr) return false

        return true
    }

    override fun equal(other: Vector<T>): BooleanVector =
        BooleanVector(this.length) { i -> this[i] == other[i] }
}
