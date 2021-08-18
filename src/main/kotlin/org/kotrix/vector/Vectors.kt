package org.kotrix.vector

import org.kotrix.utils.Slice

//-----------------------------------------------------Interfaces-----------------------------------------------------//

interface Vector<out E> : Collection<E> {
    // Query Operations
    override val size: Int
    override fun isEmpty(): Boolean
    override fun contains(element: @UnsafeVariance E): Boolean
    override fun iterator(): Iterator<E>

    // Bulk Operations
    override fun containsAll(elements: Collection<@UnsafeVariance E>): Boolean

    // Positional Access Operations
    operator fun get(index: Int): E

    // Slice Access Operations
    operator fun get(indexSlice: Slice): Vector<E>

    // Search Operations
    fun indexOf(element: @UnsafeVariance E): Int

    fun lastIndexOf(element: @UnsafeVariance E): Int

    // List Iterators
    fun listIterator(): ListIterator<E>

    fun listIterator(index: Int): ListIterator<E>

    // View
    fun subVector(fromIndex: Int, toIndex: Int): Vector<E>

    fun subVector(slice: Slice): Vector<E>
}

interface MutableVector<E> : Vector<E>, MutableCollection<E> {
    // Modification Operations
    override fun add(element: E): Boolean

    override fun remove(element: E): Boolean

    // Bulk Modification Operations
    override fun addAll(elements: Collection<E>): Boolean

    fun addAll(index: Int, elements: Collection<E>): Boolean

    override fun removeAll(elements: Collection<E>): Boolean
    override fun retainAll(elements: Collection<E>): Boolean
    override fun clear()

    // Position Access Operations
    operator fun set(index: Int, element: E): E

    // Slice Access Operations
    operator fun set(indexSlice: Slice, elements: Collection<E>): MutableVector<E>

    fun add(index: Int, element: E)

    fun removeAt(index: Int): E

    // List Iterators
    override fun listIterator(): MutableListIterator<E>

    override fun listIterator(index: Int): MutableListIterator<E>

    // View
    override fun subVector(fromIndex: Int, toIndex: Int): MutableVector<E>

    override fun subVector(slice: Slice): MutableVector<E>
}

// todo once algebraic data types are in, change type constraint
interface NumericVector<E> : Vector<E> where E : Number {
    // Properties
    val magnitude: Double

    // Unary Operations
    operator fun unaryPlus(): NumericVector<E>

    operator fun unaryMinus(): NumericVector<E>

    // Binary Operations
    operator fun plus(rhs: NumericVector<E>): NumericVector<E>

    operator fun minus(rhs: NumericVector<E>): NumericVector<E>

    operator fun times(rhs: NumericVector<E>): NumericVector<E>

    operator fun div(rhs: NumericVector<E>): NumericVector<E>

    operator fun rem(rhs: NumericVector<E>): NumericVector<E>

    fun pow(rhs: NumericVector<E>): NumericVector<E>

    fun dot(rhs: NumericVector<E>): E

    fun cross(rhs: NumericVector<E>): NumericVector<E>

    fun scalarProject(onto: NumericVector<E>): E

    fun project(onto: NumericVector<E>): NumericVector<E>
}

// todo once algebraic data types are in, change type constraint
interface MutableNumericVector<E> : NumericVector<E>, MutableVector<E> where E : Number {
    // Binary Modification Operations
    operator fun plusAssign(rhs: NumericVector<E>)

    operator fun minusAssign(rhs: NumericVector<E>)

    operator fun timesAssign(rhs: NumericVector<E>)

    operator fun divAssign(rhs: NumericVector<E>)

    operator fun remAssign(rhs: NumericVector<E>)

    fun powAssign(rhs: NumericVector<E>)

    fun projectAssign(onto: NumericVector<E>)
}

//-----------------------------------------------------Internals------------------------------------------------------//

internal object EmptyIterator : ListIterator<Nothing> {
    override fun hasNext(): Boolean = false
    override fun hasPrevious(): Boolean = false
    override fun next(): Nothing = throw NoSuchElementException()
    override fun nextIndex(): Int = 0
    override fun previous(): Nothing = throw NoSuchElementException()
    override fun previousIndex(): Int = -1
}

internal object EmptyVector : Vector<Nothing>, RandomAccess {
    override fun equals(other: Any?): Boolean = other is Vector<*> && other.isEmpty()
    override fun hashCode(): Int = 1
    override fun toString(): String = "<>"

    override val size: Int get() = 0
    override fun isEmpty(): Boolean = true
    override fun contains(element: Nothing): Boolean = false
    override fun containsAll(elements: Collection<Nothing>): Boolean = elements.isEmpty()

    override fun get(index: Int): Nothing =
        throw IndexOutOfBoundsException("Empty vector doesn't contain element at index $index.")

    override fun get(indexSlice: Slice): Vector<Nothing> =
        throw IndexOutOfBoundsException("Empty vector doesn't contain elements at slice $indexSlice")

    override fun indexOf(element: Nothing): Int = -1
    override fun lastIndexOf(element: Nothing): Int = -1

    override fun iterator(): Iterator<Nothing> = EmptyIterator
    override fun listIterator(): ListIterator<Nothing> = EmptyIterator
    override fun listIterator(index: Int): ListIterator<Nothing> {
        if (index != 0) throw IndexOutOfBoundsException("Index: $index")
        return EmptyIterator
    }

    override fun subVector(fromIndex: Int, toIndex: Int): Vector<Nothing> {
        if (fromIndex == 0 && toIndex == 0) return this
        throw IndexOutOfBoundsException("fromIndex: $fromIndex, toIndex: $toIndex")
    }

    override fun subVector(slice: Slice): Vector<Nothing> {
        if (slice.start == 0 && slice.end == 0) return this
        throw IndexOutOfBoundsException("slice: $slice")
    }
}

//-----------------------------------------------------Functions------------------------------------------------------//

fun <T> emptyVector(): Vector<T> = EmptyVector

fun <T> vectorOf(): Vector<T> = emptyVector()

fun <T> vectorOf(vararg elements: T): Vector<T> = if (elements.isNotEmpty()) elements.asVector() else emptyVector()

fun <T> mutableVectorOf(): MutableVector<T> = GenericVector()

fun <T> mutableVectorOf(vararg elements: T): MutableVector<T> =
    if (elements.isEmpty()) GenericVector() else GenericVector(elements.toList())

fun <T : Any> vectorOfNotNull(vararg elements: T?): Vector<T> = elements.filterNotNull().toVector()

@Suppress("FunctionName")
inline fun <T> Vector(size: Int, init: (index: Int) -> T): Vector<T> = MutableVector(size, init)

@Suppress("FunctionName")
inline fun <T> MutableVector(size: Int, init: (index: Int) -> T): MutableVector<T> {
    val vector = GenericVector<T>(size)
    repeat(size) { vector.add(init(it)) }
    return vector
}

//-----------------------------------------------------Extensions-----------------------------------------------------//

fun <T> Array<T>.asVector(): Vector<T> = GenericVector(this.toList())

fun <T> Collection<T>.toVector(): Vector<T> = GenericVector(this)

fun <T> Collection<T>.toMutableVector(): MutableVector<T> = GenericVector(this)

val <T> Vector<T>.lastIndex: Int
    get() = this.size - 1

fun <T> Vector<T>?.orEmpty(): Vector<T> = this ?: emptyVector()

fun <T : Comparable<T>> Vector<T?>.binarySearch(element: T?, fromIndex: Int = 0, toIndex: Int = size): Int {
    rangeCheck(size, fromIndex, toIndex)

    var low = fromIndex
    var high = toIndex - 1

    while (low <= high) {
        val mid = (low + high).ushr(1) // safe from overflows
        val midVal = get(mid)
        val cmp = compareValues(midVal, element)

        if (cmp < 0)
            low = mid + 1
        else if (cmp > 0)
            high = mid - 1
        else
            return mid // key found
    }
    return -(low + 1)  // key not found
}

fun <T> Vector<T>.binarySearch(element: T, comparator: Comparator<in T>, fromIndex: Int = 0, toIndex: Int = size): Int {
    rangeCheck(size, fromIndex, toIndex)

    var low = fromIndex
    var high = toIndex - 1

    while (low <= high) {
        val mid = (low + high).ushr(1) // safe from overflows
        val midVal = get(mid)
        val cmp = comparator.compare(midVal, element)

        if (cmp < 0)
            low = mid + 1
        else if (cmp > 0)
            high = mid - 1
        else
            return mid // key found
    }
    return -(low + 1)  // key not found
}

inline fun <T, K : Comparable<K>> Vector<T>.binarySearchBy(
    key: K?,
    fromIndex: Int = 0,
    toIndex: Int = size,
    crossinline selector: (T) -> K?
): Int =
    binarySearch(fromIndex, toIndex) { compareValues(selector(it), key) }

fun <T> Vector<T>.binarySearch(fromIndex: Int = 0, toIndex: Int = size, comparison: (T) -> Int): Int {
    rangeCheck(size, fromIndex, toIndex)

    var low = fromIndex
    var high = toIndex - 1

    while (low <= high) {
        val mid = (low + high).ushr(1) // safe from overflows
        val midVal = get(mid)
        val cmp = comparison(midVal)

        if (cmp < 0)
            low = mid + 1
        else if (cmp > 0)
            high = mid - 1
        else
            return mid // key found
    }
    return -(low + 1)  // key not found
}

/**
 * Checks that `from` and `to` are in
 * the range of [0..size] and throws an appropriate exception, if they aren't.
 */
private fun rangeCheck(size: Int, fromIndex: Int, toIndex: Int) {
    when {
        fromIndex > toIndex -> throw IllegalArgumentException("fromIndex ($fromIndex) is greater than toIndex ($toIndex).")
        fromIndex < 0 -> throw IndexOutOfBoundsException("fromIndex ($fromIndex) is less than zero.")
        toIndex > size -> throw IndexOutOfBoundsException("toIndex ($toIndex) is greater than size ($size).")
    }
}
