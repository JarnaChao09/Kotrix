package org.kotrix.vector

abstract class AbstractMutableVector<E> protected constructor() : AbstractMutableCollection<E>(), MutableVector<E>{
    abstract override fun add(index: Int, element: E)
    abstract override fun removeAt(index: Int): E
    abstract override fun set(index: Int, element: E): E
    abstract override fun set(indexSlice: org.kotrix.utils.Slice, elements: Collection<E>): MutableVector<E>

    override fun add(element: E): Boolean {
        add(size, element)
        return true
    }


}