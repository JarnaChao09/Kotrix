package org.kotrix.vector

import org.kotrix.utils.Slice
import org.kotrix.utils.contains

class LongVector internal constructor(private val backing: LongArray) : MutableNumericVector<Long>, RandomAccess {
    constructor(size: Int) : this(LongArray(size)) {
        require(size > 0) {
            "size must be greater than 0"
        }
    }

    constructor(elements: Collection<Long>) : this(LongArray(elements.size)) {
        for ((index, element) in elements.withIndex()) {
            backing[index] = element
        }
    }

    override val size: Int
        get() = backing.size

    override val magnitude: Long
        get() {
            var ret = 0.0

            for (e in backing) {
                ret += e * e
            }

            return kotlin.math.sqrt(ret).toLong()
        }

    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun isEmpty(): Boolean = false

    override fun contains(element: Long): Boolean = backing.contains(element)

    override fun iterator(): LongIterator = backing.iterator()

    override fun containsAll(elements: Collection<Long>): Boolean {
        for (e in elements) {
            if (e !in backing) {
                return false
            }
        }
        return true
    }

    override fun get(index: Int): Long {
        require(index in -size until size) {
            "index $index was not inside range [${-size}, $size)"
        }
        return backing[(size + index) % size]
    }

    override fun get(indexSlice: Slice): NumericVector<Long> {
        require(indexSlice in -size until size) {
            "index slice $indexSlice was not inside range [${-size}, $size)"
        }
        val bret = LongArray(indexSlice.size)
        var c = 0
        for (i in indexSlice) {
            bret[c++] = backing[(size + i) % size]
        }
        return LongVector(bret)
    }

    override fun indexOf(element: Long): Int = backing.indexOf(element)

    override fun lastIndexOf(element: Long): Int = backing.lastIndexOf(element)

    override fun set(index: Int, element: Long): Long {
        require(index in -size until size) {
            "index $index was not inside range [${-size}, $size)"
        }
        return backing[(size + index) % size].apply {
            this@LongVector.backing[(size + index) % size] = element
        }
    }

    override fun set(indexSlice: Slice, elements: Collection<Long>): MutableNumericVector<Long> {
        require(indexSlice.size == elements.size) {
            "size of indexSlice $indexSlice must equal size of elements $elements"
        }
        require(indexSlice in -size until size) {
            "index slice $indexSlice was not inside range [${-size}, $size)"
        }
        require(elements.size <= backing.size) {
            "size of new elements $elements cannot be bigger than size of vector $backing"
        }
        val ret = this[indexSlice]
        val sliceIterator = indexSlice.iterator()
        for (e in elements) {
            backing[(size + sliceIterator.next()) % size] = e
        }
        return ret as MutableNumericVector<Long>
    }

    override fun listIterator(): ListIterator<Long> = backing.toList().listIterator()

    override fun listIterator(index: Int): ListIterator<Long> = backing.toList().listIterator(index)

    override fun subVector(slice: Slice): MutableNumericVector<Long> =
        TODO("Decide if sub vectors of numeric vectors is allowed")

    override fun subVector(fromIndex: Int, toIndex: Int): MutableNumericVector<Long> =
        TODO("Decide if sub vectors of numeric vectors is allowed")

    override fun unaryPlus(): NumericVector<Long> = LongVector(this)

    override fun unaryMinus(): NumericVector<Long> {
        val ret = LongVector(this)

        for (i in 0 until ret.size) {
            ret[i] = -ret[i]
        }
        return ret
    }

    private inline fun perform(
        lhs: NumericVector<Long>,
        rhs: NumericVector<Long>,
        action: (Long, Long) -> Long
    ): NumericVector<Long> {
        for (i in 0 until this.size) {
            val l = if (i < lhs.size) lhs[i] else 0
            val r = if (i < rhs.size) rhs[i] else 0
            this[i] = action(l, r)
        }
        return this
    }

    override fun plus(rhs: NumericVector<Long>): NumericVector<Long> =
        LongVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l + r
        }

    override fun minus(rhs: NumericVector<Long>): NumericVector<Long> =
        LongVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l - r
        }

    override fun times(rhs: NumericVector<Long>): NumericVector<Long> =
        LongVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l * r
        }

    override fun div(rhs: NumericVector<Long>): NumericVector<Long> =
        LongVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l / r
        }

    override fun rem(rhs: NumericVector<Long>): NumericVector<Long> =
        LongVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l % r
        }

    override fun pow(rhs: NumericVector<Long>): NumericVector<Long> =
        LongVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            var ret = 1L
            for (t in 0 until r) {
                ret *= l
            }
            ret
        }

    override fun dot(rhs: NumericVector<Long>): Long {
        require(this.size == rhs.size) {
            "size of vector $this must be the same size as $rhs"
        }
        var ret = 0L

        for (i in 0 until this.size) {
            ret += this[i] * rhs[i]
        }

        return ret
    }

    override fun cross(rhs: NumericVector<Long>): NumericVector<Long> {
        require((this.size == 2 || this.size == 3) && this.size == rhs.size) {
            "cross product is only defined for 2 and 3 dimensional vectors"
        }
        val ret = LongVector(3)
        if (this.size == 2) {
            ret[2] = this[0] * rhs[1] - this[1] * rhs[0]
        } else {
            ret[0] = this[1] * rhs[2] - this[2] * rhs[1]
            ret[1] = this[2] * rhs[0] - this[0] * rhs[2]
            ret[2] = this[0] * rhs[1] - this[1] * rhs[0]
        }

        return ret
    }

    override fun scalarProject(onto: NumericVector<Long>): Long = (this.dot(onto)) / onto.magnitude

    override fun scale(byValue: Long): NumericVector<Long> =
        LongVector(LongArray(backing.size) { backing[it] * byValue })

    override fun project(onto: NumericVector<Long>): NumericVector<Long> =
        onto.scale(this.dot(onto) / onto.dot(onto))

    private inline fun mutatingPerform(
        lhs: NumericVector<Long>,
        rhs: NumericVector<Long>,
        action: (Long, Long) -> Long
    ) {
        for (i in 0 until this.size) {
            val l = if (i < lhs.size) lhs[i] else 0
            val r = if (i < rhs.size) rhs[i] else 0
            this[i] = action(l, r)
        }
    }

    override fun plusAssign(rhs: NumericVector<Long>) = this.mutatingPerform(this, rhs) { l, r ->
        l + r
    }

    override fun minusAssign(rhs: NumericVector<Long>) = this.mutatingPerform(this, rhs) { l, r ->
        l - r
    }

    override fun timesAssign(rhs: NumericVector<Long>) = this.mutatingPerform(this, rhs) { l, r ->
        l * r
    }

    override fun divAssign(rhs: NumericVector<Long>) = this.mutatingPerform(this, rhs) { l, r ->
        l / r
    }

    override fun remAssign(rhs: NumericVector<Long>) = this.mutatingPerform(this, rhs) { l, r ->
        l % r
    }

    override fun powAssign(rhs: NumericVector<Long>) = this.mutatingPerform(this, rhs) { l, r ->
        var ret = 1L
        for (t in 0 until r) {
            ret *= l
        }
        ret
    }

    override fun scaleAssign(byValue: Long) {
        for (i in 0 until this.size) {
            this[i] *= byValue
        }
    }

    override fun projectAssign(onto: NumericVector<Long>) {
        val scaleBy = this.dot(onto) / onto.dot(onto)
        for (i in 0 until this.size) {
            this[i] = onto[i] * scaleBy
        }
    }

    override fun toString(): String {
        var ret = "<"
        for ((index, value) in backing.withIndex()) {
            ret += if (index == size - 1) {
                "$value>"
            } else {
                "$value, "
            }
        }
        return ret
    }
}