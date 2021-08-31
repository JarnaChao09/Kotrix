package org.kotrix.vector

abstract class AbstractVector<out E> protected constructor() : AbstractCollection<E>(), Vector<E> {
    abstract override val size: Int

    override fun iterator(): Iterator<E> = IteratorImpl()

    override fun indexOf(element: @UnsafeVariance E): Int = indexOfFirst { it == element }

    override fun lastIndexOf(element: @UnsafeVariance E): Int = indexOfLast { it == element }

    override fun listIterator(): ListIterator<E> = ListIteratorImpl(0)

    override fun listIterator(index: Int): ListIterator<E> = ListIteratorImpl(index)

    private open inner class IteratorImpl : Iterator<E> {
        protected var index = 0

        override fun hasNext(): Boolean = index < size

        override fun next(): E {
            if (!hasNext()) throw NoSuchElementException()
            return get(index++)
        }
    }

    private open inner class ListIteratorImpl(index: Int) : IteratorImpl(), ListIterator<E> {

        init {
            require(this.index < this@AbstractVector.size) {
                "index $index must be less than ${this@AbstractVector.size}"
            }
            this.index = index
        }

        override fun hasPrevious(): Boolean = index > 0

        override fun nextIndex(): Int = index

        override fun previous(): E {
            if (!hasPrevious()) throw NoSuchElementException()
            return get(--index)
        }

        override fun previousIndex(): Int = index - 1
    }
}