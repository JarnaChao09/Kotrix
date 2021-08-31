package org.kotrix.vector

import org.kotrix.utils.Slice
import org.kotrix.utils.contains

class ShortVector internal constructor(private val backing: ShortArray) : MutableNumericVector<Short>, RandomAccess {
    constructor(size: Int) : this(ShortArray(size)) {
        require(size > 0) {
            "size must be greater than 0"
        }
    }

    constructor(elements: Collection<Short>) : this(ShortArray(elements.size)) {
        for ((index, element) in elements.withIndex()) {
            backing[index] = element
        }
    }

    override val size: Int
        get() = backing.size

    override val magnitude: Short
        get() {
            var ret = 0.0

            for (e in backing) {
                ret += e * e
            }

            return kotlin.math.sqrt(ret).toInt().toShort()
        }

    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun isEmpty(): Boolean = false

    override fun contains(element: Short): Boolean = backing.contains(element)

    override fun iterator(): ShortIterator = backing.iterator()

    override fun containsAll(elements: Collection<Short>): Boolean {
        for (e in elements) {
            if (e !in backing) {
                return false
            }
        }
        return true
    }

    override fun get(index: Int): Short {
        require(index in -size until size) {
            "index $index was not inside range [${-size}, $size)"
        }
        return backing[(size + index) % size]
    }

    override fun get(indexSlice: Slice): NumericVector<Short> {
        require(indexSlice in -size until size) {
            "index slice $indexSlice was not inside range [${-size}, $size)"
        }
        val bret = ShortArray(indexSlice.size)
        var c = 0
        for (i in indexSlice) {
            bret[c++] = backing[(size + i) % size]
        }
        return ShortVector(bret)
    }

    override fun indexOf(element: Short): Int = backing.indexOf(element)

    override fun lastIndexOf(element: Short): Int = backing.lastIndexOf(element)

    override fun set(index: Int, element: Short): Short {
        require(index in -size until size) {
            "index $index was not inside range [${-size}, $size)"
        }
        return backing[(size + index) % size].apply {
            this@ShortVector.backing[(size + index) % size] = element
        }
    }

    override fun set(indexSlice: Slice, elements: Collection<Short>): MutableNumericVector<Short> {
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
        return ret as MutableNumericVector<Short>
    }

    override fun listIterator(): ListIterator<Short> = backing.toList().listIterator()

    override fun listIterator(index: Int): ListIterator<Short> = backing.toList().listIterator(index)

    override fun subVector(slice: Slice): MutableNumericVector<Short> =
        TODO("Decide if sub vectors of numeric vectors is allowed")

    override fun subVector(fromIndex: Int, toIndex: Int): MutableNumericVector<Short> =
        TODO("Decide if sub vectors of numeric vectors is allowed")

    override fun unaryPlus(): NumericVector<Short> = ShortVector(this)

    override fun unaryMinus(): NumericVector<Short> {
        val ret = ShortVector(this)

        for (i in 0 until ret.size) {
            ret[i] = (-ret[i]).toShort()
        }
        return ret
    }

    private inline fun perform(
        lhs: NumericVector<Short>,
        rhs: NumericVector<Short>,
        action: (Short, Short) -> Short
    ): NumericVector<Short> {
        for (i in 0 until this.size) {
            val l = if (i < lhs.size) lhs[i] else 0
            val r = if (i < rhs.size) rhs[i] else 0
            this[i] = action(l, r)
        }
        return this
    }

    override fun plus(rhs: NumericVector<Short>): NumericVector<Short> =
        ShortVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            (l + r).toShort()
        }

    override fun minus(rhs: NumericVector<Short>): NumericVector<Short> =
        ShortVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            (l - r).toShort()
        }

    override fun times(rhs: NumericVector<Short>): NumericVector<Short> =
        ShortVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            (l * r).toShort()
        }

    override fun div(rhs: NumericVector<Short>): NumericVector<Short> =
        ShortVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            (l / r).toShort()
        }

    override fun rem(rhs: NumericVector<Short>): NumericVector<Short> =
        ShortVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            (l % r).toShort()
        }

    override fun pow(rhs: NumericVector<Short>): NumericVector<Short> =
        ShortVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            var ret = 1
            for (t in 0 until r) {
                ret *= l
            }
            ret.toShort()
        }

    override fun dot(rhs: NumericVector<Short>): Short {
        require(this.size == rhs.size) {
            "size of vector $this must be the same size as $rhs"
        }
        var ret = 0

        for (i in 0 until this.size) {
            ret += this[i] * rhs[i]
        }

        return ret.toShort()
    }

    override fun cross(rhs: NumericVector<Short>): NumericVector<Short> {
        require((this.size == 2 || this.size == 3) && this.size == rhs.size) {
            "cross product is only defined for 2 and 3 dimensional vectors"
        }
        val ret = ShortVector(3)
        if (this.size == 2) {
            ret[2] = (this[0] * rhs[1] - this[1] * rhs[0]).toShort()
        } else {
            ret[0] = (this[1] * rhs[2] - this[2] * rhs[1]).toShort()
            ret[1] = (this[2] * rhs[0] - this[0] * rhs[2]).toShort()
            ret[2] = (this[0] * rhs[1] - this[1] * rhs[0]).toShort()
        }

        return ret
    }

    override fun scalarProject(onto: NumericVector<Short>): Short = ((this.dot(onto)) / onto.magnitude).toShort()

    override fun scale(byValue: Short): NumericVector<Short> =
        ShortVector(ShortArray(backing.size) { (backing[it] * byValue).toShort() })

    override fun project(onto: NumericVector<Short>): NumericVector<Short> =
        onto.scale((this.dot(onto) / onto.dot(onto)).toShort())

    private inline fun mutatingPerform(
        lhs: NumericVector<Short>,
        rhs: NumericVector<Short>,
        action: (Short, Short) -> Short
    ) {
        for (i in 0 until this.size) {
            val l = if (i < lhs.size) lhs[i] else 0
            val r = if (i < rhs.size) rhs[i] else 0
            this[i] = action(l, r)
        }
    }

    override fun plusAssign(rhs: NumericVector<Short>) = this.mutatingPerform(this, rhs) { l, r ->
        (l + r).toShort()
    }

    override fun minusAssign(rhs: NumericVector<Short>) = this.mutatingPerform(this, rhs) { l, r ->
        (l - r).toShort()
    }

    override fun timesAssign(rhs: NumericVector<Short>) = this.mutatingPerform(this, rhs) { l, r ->
        (l * r).toShort()
    }

    override fun divAssign(rhs: NumericVector<Short>) = this.mutatingPerform(this, rhs) { l, r ->
        (l / r).toShort()
    }

    override fun remAssign(rhs: NumericVector<Short>) = this.mutatingPerform(this, rhs) { l, r ->
        (l % r).toShort()
    }

    override fun powAssign(rhs: NumericVector<Short>) = this.mutatingPerform(this, rhs) { l, r ->
        var ret = 1
        for (t in 0 until r) {
            ret *= l
        }
        ret.toShort()
    }

    override fun scaleAssign(byValue: Short) {
        for (i in 0 until this.size) {
            this[i] = (this[i] * byValue).toShort()
        }
    }

    override fun projectAssign(onto: NumericVector<Short>) {
        val scaleBy = this.dot(onto) / onto.dot(onto)
        for (i in 0 until this.size) {
            this[i] = (onto[i] * scaleBy).toShort()
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