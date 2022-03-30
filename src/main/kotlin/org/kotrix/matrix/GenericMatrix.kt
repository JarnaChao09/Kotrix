package org.kotrix.matrix

import org.kotrix.utils.Shape
import org.kotrix.utils.Slice
import org.kotrix.utils.by
import org.kotrix.vector.MutableVector

open class GenericMatrix<E> internal constructor(
    private val flatBuffer: MutableList<E>,
    private var currentShape: Shape
) : RandomAccess {
    constructor() : this(mutableListOf(), Shape(0, 0))

    constructor(initialShape: Shape = 3 by 3) : this(ArrayList<E>(initialShape.size).toMutableList(), initialShape)

    constructor(shape: Shape, flatttenedElements: Collection<E>) : this(flatttenedElements.toMutableList(), shape)

    var shape: Shape
        get() {
            return currentShape
        }
        set(value) {
            require(currentShape.size == value.size) {
                "Cannot reshape matrix of $currentShape into $value"
            }
            currentShape = value
        }

    fun isEmpty(): Boolean = flatBuffer.isEmpty()

    fun contains(element: E): Boolean = flatBuffer.contains(element)

    // iterator

    fun containsAll(elements: Collection<E>): Boolean = flatBuffer.containsAll(elements)

    fun get(index: Int): MutableVector<E> {
        TODO()
    }

    // fun get(indexSlice: Slice): GenericMatrix<E>


}