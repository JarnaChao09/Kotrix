package org.kotrix.vector

import org.kotrix.utils.Slice
import org.kotrix.utils.contains

class ByteVector internal constructor(private val backing: ByteArray) : MutableNumericVector<Byte>, RandomAccess {
    constructor(size: Int) : this(ByteArray(size)) {
        require(size > 0) {
            "size must be greater than 0"
        }
    }

        constructor(elements: Collection<Byte>) : this(ByteArray(elements.size)) {
        for ((index, element) in elements.withIndex()) {
            backing[index] = element
        }
    }

    override val size: Int
        get() = backing.size

    override val magnitude: Byte
        get() {
            var ret = 0.0

            for (e in backing) {
                ret += e * e
            }

            return kotlin.math.sqrt(ret).toInt().toByte()
        }

    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun isEmpty(): Boolean = false

    override fun contains(element: Byte): Boolean = backing.contains(element)

    override fun iterator(): ByteIterator = backing.iterator()

    override fun containsAll(elements: Collection<Byte>): Boolean {
        for (e in elements) {
            if (e !in backing) {
                return false
            }
        }
        return true
    }

    override fun get(index: Int): Byte {
        require(index in -size until size) {
            "index $index was not inside range [${-size}, $size)"
        }
        return backing[(size + index) % size]
    }

    override fun get(indexSlice: Slice): NumericVector<Byte> {
        require(indexSlice in -size until size) {
            "index slice $indexSlice was not inside range [${-size}, $size)"
        }
        val bret = ByteArray(indexSlice.size)
        var c = 0
        for (i in indexSlice) {
            bret[c++] = backing[(size + i) % size]
        }
        return ByteVector(bret)
    }

    override fun indexOf(element: Byte): Int = backing.indexOf(element)

    override fun lastIndexOf(element: Byte): Int = backing.lastIndexOf(element)

    override fun set(index: Int, element: Byte): Byte {
        require(index in -size until size) {
            "index $index was not inside range [${-size}, $size)"
        }
        return backing[(size + index) % size].apply {
            this@ByteVector.backing[(size + index) % size] = element
        }
    }

    override fun set(indexSlice: Slice, elements: Collection<Byte>): MutableNumericVector<Byte> {
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
        return ret as MutableNumericVector<Byte>
    }

    override fun listIterator(): ListIterator<Byte> = backing.toList().listIterator()

    override fun listIterator(index: Int): ListIterator<Byte> = backing.toList().listIterator(index)

    override fun subVector(slice: Slice): MutableNumericVector<Byte> =
        TODO("Decide if sub vectors of numeric vectors is allowed")

    override fun subVector(fromIndex: Int, toIndex: Int): MutableNumericVector<Byte> =
        TODO("Decide if sub vectors of numeric vectors is allowed")

    override fun unaryPlus(): NumericVector<Byte> = ByteVector(this)

    override fun unaryMinus(): NumericVector<Byte> {
        val ret = ByteVector(this)

        for (i in 0 until ret.size) {
            ret[i] = (-ret[i]).toByte()
        }
        return ret
    }

    private inline fun perform(
        lhs: NumericVector<Byte>,
        rhs: NumericVector<Byte>,
        action: (Byte, Byte) -> Byte
    ): NumericVector<Byte> {
        for (i in 0 until this.size) {
            val l = if (i < lhs.size) lhs[i] else 0
            val r = if (i < rhs.size) rhs[i] else 0
            this[i] = action(l, r)
        }
        return this
    }

    override fun plus(rhs: NumericVector<Byte>): NumericVector<Byte> =
        ByteVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            (l + r).toByte()
        }

    override fun minus(rhs: NumericVector<Byte>): NumericVector<Byte> =
        ByteVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            (l - r).toByte()
        }

    override fun times(rhs: NumericVector<Byte>): NumericVector<Byte> =
        ByteVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            (l * r).toByte()
        }

    override fun div(rhs: NumericVector<Byte>): NumericVector<Byte> =
        ByteVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            (l / r).toByte()
        }

    override fun rem(rhs: NumericVector<Byte>): NumericVector<Byte> =
        ByteVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            (l % r).toByte()
        }

    override fun pow(rhs: NumericVector<Byte>): NumericVector<Byte> =
        ByteVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            var ret = 1
            for (t in 0 until r) {
                ret *= l
            }
            ret.toByte()
        }

    override fun dot(rhs: NumericVector<Byte>): Byte {
        require(this.size == rhs.size) {
            "size of vector $this must be the same size as $rhs"
        }
        var ret = 0

        for (i in 0 until this.size) {
            ret += this[i] * rhs[i]
        }

        return ret.toByte()
    }

    override fun cross(rhs: NumericVector<Byte>): NumericVector<Byte> {
        require((this.size == 2 || this.size == 3) && this.size == rhs.size) {
            "cross product is only defined for 2 and 3 dimensional vectors"
        }
        val ret = ByteVector(3)
        if (this.size == 2) {
            ret[2] = (this[0] * rhs[1] - this[1] * rhs[0]).toByte()
        } else {
            ret[0] = (this[1] * rhs[2] - this[2] * rhs[1]).toByte()
            ret[1] = (this[2] * rhs[0] - this[0] * rhs[2]).toByte()
            ret[2] = (this[0] * rhs[1] - this[1] * rhs[0]).toByte()
        }

        return ret
    }

    override fun scalarProject(onto: NumericVector<Byte>): Byte = ((this.dot(onto)) / onto.magnitude).toByte()

    override fun scale(byValue: Byte): NumericVector<Byte> =
        ByteVector(ByteArray(backing.size) { (backing[it] * byValue).toByte() })

    override fun project(onto: NumericVector<Byte>): NumericVector<Byte> =
        onto.scale((this.dot(onto) / onto.dot(onto)).toByte())

    private inline fun mutatingPerform(
        lhs: NumericVector<Byte>,
        rhs: NumericVector<Byte>,
        action: (Byte, Byte) -> Byte
    ) {
        for (i in 0 until this.size) {
            val l = if (i < lhs.size) lhs[i] else 0
            val r = if (i < rhs.size) rhs[i] else 0
            this[i] = action(l, r)
        }
    }

    override fun plusAssign(rhs: NumericVector<Byte>) = this.mutatingPerform(this, rhs) { l, r ->
        (l + r).toByte()
    }

    override fun minusAssign(rhs: NumericVector<Byte>) = this.mutatingPerform(this, rhs) { l, r ->
        (l - r).toByte()
    }

    override fun timesAssign(rhs: NumericVector<Byte>) = this.mutatingPerform(this, rhs) { l, r ->
        (l * r).toByte()
    }

    override fun divAssign(rhs: NumericVector<Byte>) = this.mutatingPerform(this, rhs) { l, r ->
        (l / r).toByte()
    }

    override fun remAssign(rhs: NumericVector<Byte>) = this.mutatingPerform(this, rhs) { l, r ->
        (l % r).toByte()
    }

    override fun powAssign(rhs: NumericVector<Byte>) = this.mutatingPerform(this, rhs) { l, r ->
        var ret = (1).toByte()
        for (t in 0 until r) {
            ret = (ret * l).toByte()
        }
        ret
    }

    override fun scaleAssign(byValue: Byte) {
        for (i in 0 until this.size) {
            this[i] = (this[i] * byValue).toByte()
        }
    }

    override fun projectAssign(onto: NumericVector<Byte>) {
        val scaleBy = this.dot(onto) / onto.dot(onto)
        for (i in 0 until this.size) {
            this[i] = (onto[i] * scaleBy).toByte()
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