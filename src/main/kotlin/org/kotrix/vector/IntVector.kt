package org.kotrix.vector

import org.kotrix.utils.Slice
import org.kotrix.utils.contains

class IntVector internal constructor(private val backing: IntArray) : MutableNumericVector<Int>, RandomAccess {
    constructor(size: Int) : this(IntArray(size))

    constructor(elements: Collection<Int>) : this(IntArray(elements.size)) {
        for ((index, element) in elements.withIndex()) {
            backing[index] = element
        }
    }

    override val size: Int
        get() = backing.size

    override val magnitude: Int
        get() {
            var ret = 0.0

            for (e in backing) {
                ret += e * e
            }

            return kotlin.math.sqrt(ret).toInt()
        }

    override fun isEmpty(): Boolean = backing.isEmpty()

    override fun contains(element: Int): Boolean = backing.contains(element)

    override fun iterator(): Iterator<Int> = backing.iterator()

    override fun containsAll(elements: Collection<Int>): Boolean {
        for (e in elements) {
            if (e !in backing) {
                return false
            }
        }
        return true
    }

    override fun get(index: Int): Int {
        require(index in -size until size) {
            "index $index was not inside range [${-size}, $size)"
        }
        return backing[(size + index) % size]
    }

    override fun get(indexSlice: Slice): NumericVector<Int> {
        require(indexSlice in -size until size) {
            "index slice $indexSlice was not inside range [${-size}, $size)"
        }
        val bret = IntArray(indexSlice.size)
        var c = 0
        for (i in indexSlice) {
            bret[c++] = backing[(size + i) % size]
        }
        return IntVector(bret)
    }

    override fun indexOf(element: Int): Int = backing.indexOf(element)

    override fun lastIndexOf(element: Int): Int = backing.lastIndexOf(element)

    override fun set(index: Int, element: Int): Int {
        require(index in -size until size) {
            "index $index was not inside range [${-size}, $size)"
        }
        return backing[(size + index) % size].apply {
            this@IntVector.backing[(size + index) % size] = element
        }
    }

    override fun set(indexSlice: Slice, elements: Collection<Int>): MutableNumericVector<Int> {
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
        return ret as MutableNumericVector<Int>
    }

    override fun listIterator(): ListIterator<Int> = backing.toList().listIterator()

    override fun listIterator(index: Int): ListIterator<Int> = backing.toList().listIterator(index)

    override fun subVector(slice: Slice): MutableNumericVector<Int> =
        TODO("Decide if sub vectors of numeric vectors is allowed")

    override fun subVector(fromIndex: Int, toIndex: Int): MutableNumericVector<Int> =
        TODO("Decide if sub vectors of numeric vectors is allowed")

    override fun unaryPlus(): NumericVector<Int> = IntVector(this)

    override fun unaryMinus(): NumericVector<Int> {
        val ret = IntVector(this)

        for (i in 0 until ret.size) {
            ret[i] = -ret[i]
        }
        return ret
    }

    private inline fun perform(
        lhs: NumericVector<Int>,
        rhs: NumericVector<Int>,
        action: (Int, Int) -> Int
    ): NumericVector<Int> {
        for (i in 0 until this.size) {
            val l = if (i < lhs.size) lhs[i] else 0
            val r = if (i < rhs.size) rhs[i] else 0
            this[i] = action(l, r)
        }
        return this
    }

    override fun plus(rhs: NumericVector<Int>): NumericVector<Int> =
        IntVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l + r
        }

    override fun minus(rhs: NumericVector<Int>): NumericVector<Int> =
        IntVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l - r
        }

    override fun times(rhs: NumericVector<Int>): NumericVector<Int> =
        IntVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l * r
        }

    override fun div(rhs: NumericVector<Int>): NumericVector<Int> =
        IntVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l / r
        }

    override fun rem(rhs: NumericVector<Int>): NumericVector<Int> =
        IntVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l % r
        }

    override fun pow(rhs: NumericVector<Int>): NumericVector<Int> =
        IntVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            var ret = 1
            for (t in 0 until r) {
                ret *= l
            }
            ret
        }

    override fun dot(rhs: NumericVector<Int>): Int {
        require(this.size == rhs.size) {
            "size of vector $this must be the same size as $rhs"
        }
        var ret = 0

        for (i in 0 until this.size) {
            ret += this[i] * rhs[i]
        }

        return ret
    }

    override fun cross(rhs: NumericVector<Int>): NumericVector<Int> {
        require((this.size == 2 || this.size == 3) && this.size == rhs.size) {
            "cross product is only defined for 2 and 3 dimensional vectors"
        }
        val ret = IntVector(3)
        if (this.size == 2) {
            ret[2] = this[0] * rhs[1] - this[1] * rhs[0]
        } else {
            ret[0] = this[1] * rhs[2] - this[2] * rhs[1]
            ret[1] = this[2] * rhs[0] - this[0] * rhs[2]
            ret[2] = this[0] * rhs[1] - this[1] * rhs[0]
        }

        return ret
    }

    override fun scalarProject(onto: NumericVector<Int>): Int = (this.dot(onto)) / onto.magnitude

    override fun scale(byValue: Int): NumericVector<Int> = IntVector(IntArray(backing.size) { backing[it] * byValue })

    override fun project(onto: NumericVector<Int>): NumericVector<Int> = onto.scale(this.dot(onto) / onto.dot(onto))

    private inline fun mutatingPerform(
        lhs: NumericVector<Int>,
        rhs: NumericVector<Int>,
        action: (Int, Int) -> Int
    ) {
        for (i in 0 until this.size) {
            val l = if (i < lhs.size) lhs[i] else 0
            val r = if (i < rhs.size) rhs[i] else 0
            this[i] = action(l, r)
        }
    }

    override fun plusAssign(rhs: NumericVector<Int>) = this.mutatingPerform(this, rhs) { l, r ->
        l + r
    }

    override fun minusAssign(rhs: NumericVector<Int>) = this.mutatingPerform(this, rhs) { l, r ->
        l - r
    }

    override fun timesAssign(rhs: NumericVector<Int>) = this.mutatingPerform(this, rhs) { l, r ->
        l * r
    }

    override fun divAssign(rhs: NumericVector<Int>) = this.mutatingPerform(this, rhs) { l, r ->
        l / r
    }

    override fun remAssign(rhs: NumericVector<Int>) = this.mutatingPerform(this, rhs) { l, r ->
        l % r
    }

    override fun powAssign(rhs: NumericVector<Int>) = this.mutatingPerform(this, rhs) { l, r ->
        var ret = 1
        for (t in 0 until r) {
            ret *= l
        }
        ret
    }

    override fun scaleAssign(byValue: Int) {
        for (i in 0 until this.size) {
            this[i] *= byValue
        }
    }

    override fun projectAssign(onto: NumericVector<Int>) {
        val scaleBy = this.dot(onto) / onto.dot(onto)
        for (i in 0 until this.size) {
            this[i] = onto[i] * scaleBy
        }
    }

    override fun toString(): String {
        if (size == 0) {
            return "<>"
        }
        var ret = "<"
        for ((index, value) in backing.withIndex()) {
            ret += if(index == size - 1) {
                "$value>"
            } else {
                "$value, "
            }
        }
        return ret
    }
}