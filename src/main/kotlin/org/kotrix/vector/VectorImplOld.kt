package org.kotrix.vector

import org.kotrix.matrix.Matrix
import org.kotrix.utils.Slice
import org.kotrix.utils.extensions.*
import kotlin.reflect.KClass
import kotlin.streams.toList

open class VectorImplOld<T>(size: Int = 10, initBlock: (Int) -> T) : VectorBase<T> where T : Any {
    constructor(size: Int = 10, initValue: T) : this(size, initBlock = { initValue })

    constructor(list: List<T>) : this(size = list.size, initBlock = { i -> list[i] })

    constructor(vec: VectorImplOld<T>) : this(size = vec.size, initBlock = { i -> vec[i] })

    sealed class Scope<T> where T : Any {
        val actions: MutableList<Scope<T>> = emptyList<Scope<T>>().toMutableList()

        class Base<T : Any> : Scope<T>()

        class Error(msg: String = "Illegal Type was found") : Throwable(msg)

        data class Append<T : Any>(val value: T, var times: Int = 1) : Scope<T>()

        data class Push<T : Any>(val value: T, var times: Int = 1) : Scope<T>()

        data class Put<T : Any>(val value: T, val at: Int, var times: Int = 1) : Scope<T>()

        data class Repeat<T : Any>(val times: Int, val repeat: Scope<T>.() -> Scope<T>) : Scope<T>() {
            fun run(): Scope<T> {
                val base = Base<T>().repeat()
                for (i in base.actions) {
                    when (i) {
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

    class VectorIterator<U>(val value: VectorImplOld<U>) : Iterator<U> where U : Any {
        private var current = 0
        override fun hasNext(): Boolean {
            return current < value.size
        }

        override fun next(): U {
            return value[current++]
        }

    }

    class VectorIteratorWithIndices<U>(val value: VectorImplOld<U>) : Iterator<IndexedValue<U>> where U : Any {
        private var current = 0
        override fun hasNext(): Boolean {
            return current < value.size
        }

        override fun next(): IndexedValue<U> =
            IndexedValue(current, value[current++])
    }

    companion object {
        @JvmStatic
        inline fun <reified T : Any> empty(size: Int = 1): VectorImplOld<T> =
            try {
                VectorImplOld(size) { T::class.java.getDeclaredConstructor().newInstance() }
            } catch (e: Exception) {
                throw IllegalArgumentException("Cannot instantiate instance of ${T::class.java}")
            }

        @JvmStatic
        fun <T : Any> of(vararg elements: T): VectorImplOld<T> =
            VectorImplOld(elements.size) { i -> elements[i] }

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> nulls(size: Int = 0): VectorImplOld<T> {
            return VectorImplOld(size) { Any() as T }
        }

        operator fun <T : Any> invoke(size: Int): VectorImplOld<T> =
            nulls(size = size)
    }

    override fun appendAll(value: VectorBase<T>) {
        for (i in value) {
            this.append(i)
        }
    }

    override fun append(other: T) {
        this.buffer.add(other)
    }

    override fun push(other: T) {
        this.buffer.add(0, other)
    }

    override fun put(other: T, at: Int) {
        this.buffer.add(at, other)
    }

    override fun remove(other: T): Boolean =
        this.buffer.remove(other)

    override fun removeAt(index: Int): T =
        this.buffer.removeAt(index)

    protected var buffer: MutableList<T> = List(size) { i -> initBlock(i) }.toMutableList()

    override val size: Int
        get() = this.buffer.size

    override val type: KClass<out T>
        get() = buffer[0]::class

    override val list: List<T>
        get() = this.toList()

    override fun toString(): String {
        if (size == 0) {
            return "<>"
        }
        val retString = List(0) { "" }.toMutableList()
        val maxLength: Int = buffer.map { x -> x }.stream().mapToInt { x -> x.toString().length }.sorted().toList()
            .toMutableList()[this.size - 1] + 1
        for (i in this.buffer) {
            val currentLength = i.toString().length
            retString += " " * (maxLength - currentLength) + i.toString()
        }
        return retString.joinToString(prefix = "<", postfix = ">", separator = ",")
    }

    override operator fun iterator(): Iterator<T> =
        VectorIterator(this)

    override val indices: IntRange
        get() = 0 until this.size

    override val withIndices: Iterator<IndexedValue<T>>
        get() = VectorIteratorWithIndices(this)

    override operator fun get(index: Int): T = this.buffer[(size + index) % size]

    override operator fun get(indexSlice: Slice): VectorImplOld<T> {
        val ret = nulls<T>()
        val subList: MutableList<T> = emptyList<T>().toMutableList()
        for (i: Int in indexSlice) {
            subList.add(this.buffer[(size + i) % size])
        }
        ret.buffer = subList
        return ret
    }

    override operator fun set(index: Int, value: T) {
        this.buffer[(size + index) % size] = value
    }

    override operator fun set(indexSlice: Slice, value: VectorBase<T>) {
        require(value.size == indexSlice.size) {
            "Size ${value.size} of inputted vector does not equal size ${indexSlice.size} of proposed slice"
        }

        var counter = 0
        for (i in indexSlice) {
            this[(size + i) % size] = value[counter++]
        }
    }

    override fun toMatrix(asCol: Boolean): Matrix<T> =
        Matrix(this, asCol = asCol)

    override fun toList(): List<T> =
        List(this.size) { i -> this[i] }

    override operator fun contains(element: T): Boolean =
        element in this.buffer

    override fun hashCode(): Int {
        return buffer.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VectorImplOld<*>

        if (buffer != other.buffer) return false

        return true
    }

    override fun equal(other: VectorImplOld<T>): BooleanVector =
        BooleanVector(this.size) { i -> this[i] == other[i] }

    override fun <U : VectorBase<T>> contains(element: U): Boolean =
        this.containsAll(element)

    override fun containsAll(elements: Collection<T>): Boolean =
        this.buffer.containsAll(elements)

    override fun isEmpty(): Boolean =
        this.buffer.isEmpty()
}
