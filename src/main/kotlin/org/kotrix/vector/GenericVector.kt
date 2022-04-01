package org.kotrix.vector

import org.kotrix.utils.Slice
import org.kotrix.utils.contains

open class GenericVector<E> internal constructor(private val backing: MutableList<E>) : MutableVector<E>, RandomAccess {
    constructor() : this(mutableListOf())

    constructor(initialCapacity: Int = 10) : this(ArrayList<E>(initialCapacity).toMutableList())

    constructor(elements: Collection<E>) : this(elements.toMutableList())

    override val size: Int
        get() = backing.size

    override fun isEmpty(): Boolean = backing.isEmpty()

    override fun contains(element: E): Boolean = backing.contains(element)

    override fun iterator(): MutableIterator<E> = backing.iterator()

    override fun containsAll(elements: Collection<E>): Boolean = backing.containsAll(elements)

    override fun get(index: Int): E {
        require(index in -size until size) {
            "index $index was not inside range [${-size}, $size)"
        }
        return backing[(size + index) % size]
    }

    override fun get(indexSlice: Slice): GenericVector<E> {
        require(indexSlice in -size until size) {
            "index slice $indexSlice was not inside range [${-size}, $size)"
        }
        val ret = mutableListOf<E>()
        for (i in indexSlice) {
            ret.add(backing[(size + i) % size])
        }
        return GenericVector(ret)
    }

    override fun indexOf(element: E): Int = backing.indexOf(element)

    override fun lastIndexOf(element: E): Int = backing.lastIndexOf(element)

    override fun add(element: E): Boolean = backing.add(element)

    override fun remove(element: E): Boolean = backing.remove(element)

    override fun addAll(elements: Collection<E>): Boolean = backing.addAll(elements)

    override fun addAll(index: Int, elements: Collection<E>): Boolean =
        backing.addAll(index = index, elements = elements)

    override fun removeAll(elements: Collection<E>): Boolean = backing.removeAll(elements)

    override fun retainAll(elements: Collection<E>): Boolean = backing.retainAll(elements)

    override fun clear() = backing.clear()

    override fun set(index: Int, element: E) {
        require(index in -size until size) {
            "index $index was not inside range [${-size}, $size)"
        }
        this.backing[(size + index) % size] = element
    }

    override fun set(indexSlice: Slice, elements: Collection<E>) {
        require(indexSlice.size == elements.size) {
            "size of indexSlice $indexSlice must equal size of elements $elements"
        }
        require(indexSlice in -size until size) {
            "index slice $indexSlice was not inside range [${-size}, $size)"
        }
        require(elements.size <= backing.size) {
            "size of new elements $elements cannot be bigger than size of vector $backing"
        }
        val sliceIterator = indexSlice.iterator()
        for (e in elements) {
            backing[(size + sliceIterator.next()) % size] = e
        }
    }

    override fun add(index: Int, element: E) = backing.add(index, element)

    override fun removeAt(index: Int): E = backing.removeAt(index)

    override fun listIterator(): MutableListIterator<E> = backing.listIterator()

    override fun listIterator(index: Int): MutableListIterator<E> = backing.listIterator(index)

    override fun subVector(fromIndex: Int, toIndex: Int): MutableVector<E> =
        GenericVector(backing.subList((size + fromIndex) % size, (size + toIndex) % size))

    override fun subVector(slice: Slice): GenericVector<E> {
        val backingSubList = mutableListOf<E>()
        for(i in slice) {
            backingSubList.add(backing[(size + i) % size])
        }
        return GenericVector(backingSubList.subList(0, backingSubList.size))
    }

    override fun toString(): String {
        if (size == 0) {
            return "[]"
        }
        var ret = "["
        for ((index, value) in backing.withIndex()) {
            ret += if(index == size - 1) {
                "$value]"
            } else {
                "$value, "
            }
        }
        return ret
    }
}