package org.kotrix.vector

import org.kotrix.utils.Slice
import org.kotrix.utils.contains

class BooleanVector internal constructor(private val backing: BooleanArray) : Vector<Boolean>, RandomAccess {
    constructor(size: Int) : this(BooleanArray(size)) {
        require(size > 0) {
            "size must be greater than 0"
        }
    }

    constructor(elements: Collection<Boolean>) : this(BooleanArray(elements.size)) {
        for ((index, element) in elements.withIndex()) {
            backing[index] = element
        }
    }

    override val size: Int
        get() = backing.size


    @Suppress("OvERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun isEmpty(): Boolean = false

    override fun contains(element: Boolean): Boolean = backing.contains(element)

    override fun iterator(): Iterator<Boolean> = backing.iterator()

    override fun containsAll(elements: Collection<Boolean>): Boolean {
        for (e in elements) {
            if (e !in backing) {
                return false
            }
        }
        return true
    }

    override fun get(index: Int): Boolean {
        require(index in -size until size) {
            "index $index was not inside range [${-size}, $size)"
        }
        return backing[(size + index) % size]
    }

    override fun get(indexSlice: Slice): Vector<Boolean> {
        require(indexSlice in -size until size) {
            "index slice $indexSlice was not inside range [${-size}, $size)"
        }
        val bret = BooleanArray(indexSlice.size)
        var c = 0
        for (i in indexSlice) {
            bret[c++] = backing[(size + i) % size]
        }
        return BooleanVector(bret)
    }

    override fun indexOf(element: Boolean): Int = backing.indexOf(element)

    override fun lastIndexOf(element: Boolean): Int = backing.lastIndexOf(element)

    operator fun set(index: Int, element: Boolean) {
        require(index in -size until size) {
            "index $index was not inside range [${-size}, $size)"
        }
        this.backing[(size + index) % size] = element
    }

    operator fun set(indexSlice: Slice, elements: Collection<Boolean>) {
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

    override fun listIterator(): ListIterator<Boolean> {
        TODO("Not yet implemented")
    }

    override fun listIterator(index: Int): ListIterator<Boolean> {
        TODO("Not yet implemented")
    }

    override fun subVector(fromIndex: Int, toIndex: Int): Vector<Boolean> {
        TODO("Since Boolean Vectors will \"act\" like numeric vectors to some extent, is sub vectoring them allowed?")
    }

    override fun subVector(slice: Slice): Vector<Boolean> {
        TODO("Since Boolean Vectors will \"act\" like numeric vectors to some extent, is sub vectoring them allowed?")
    }

    private inline fun perform(
        lhs: BooleanVector,
        rhs: BooleanVector,
        action: (Boolean, Boolean) -> Boolean
    ): BooleanVector {
        for (i in 0 until this.size) {
            val l = if (i < lhs.size) lhs[i] else false
            val r = if (i < rhs.size) rhs[i] else false
            this[i] = action(l, r)
        }
        return this
    }

    fun and(rhs: BooleanVector): BooleanVector =
        BooleanVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l && r
        }

    fun or(rhs: BooleanVector): BooleanVector =
        BooleanVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l || r
        }

    fun xor(rhs: BooleanVector): BooleanVector =
        BooleanVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l xor r
        }

    operator fun not(): BooleanVector = BooleanVector(this.size).apply {
        for (i in 0 until this.size) {
            this[i] = !this@BooleanVector[i]
        }
    }

    // todo rewrite all other mutationPerform functions to match this one
    private inline fun mutatingPerform(
        rhs: BooleanVector,
        crossinline action: (Boolean, Boolean) -> Boolean
    ) {
        this.perform(this, rhs, action)
    }

    fun andAssign(rhs: BooleanVector) = this.mutatingPerform(rhs) { l, r ->
        l && r
    }

    fun orAssign(rhs: BooleanVector) = this.mutatingPerform(rhs) { l, r ->
        l || r
    }

    fun xorAssign(rhs: BooleanVector) = this.mutatingPerform(rhs) { l, r ->
        l xor r
    }

    fun dot(rhs: BooleanVector): Boolean {
        require(this.size == rhs.size) {
            "size of vector $this must be the same size as $rhs"
        }
        var ret = false

        for (i in 0 until this.size) {
            ret = ret || this[i] && rhs[i]
        }

        return ret
    }
}

fun BooleanArray.asVector(): BooleanVector = BooleanVector(this)
fun BooleanArray.toVector(): BooleanVector = BooleanVector(this.copyOf())

fun Collection<Boolean>.toBooleanVector(): BooleanVector = BooleanVector(this)

