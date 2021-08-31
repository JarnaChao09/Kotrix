package org.kotrix.vector

import org.kotrix.utils.Slice
import org.kotrix.utils.contains
import kotlin.math.pow

class DoubleVector internal constructor(private val backing: DoubleArray) : MutableNumericVector<Double>, RandomAccess {
    constructor(size: Int) : this(DoubleArray(size)) {
        require(size > 0) {
            "size must be greater than 0"
        }
    }

    constructor(elements: Collection<Double>) : this(DoubleArray(elements.size)) {
        for ((index, element) in elements.withIndex()) {
            backing[index] = element
        }
    }

    override val size: Int
        get() = backing.size

    override val magnitude: Double
        get() {
            var ret = 0.0

            for (e in backing) {
                ret += e * e
            }

            return kotlin.math.sqrt(ret)
        }

    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun isEmpty(): Boolean = false

    override fun contains(element: Double): Boolean = backing.any { it == element }

    override fun iterator(): DoubleIterator = backing.iterator()

    override fun containsAll(elements: Collection<Double>): Boolean {
        for (e in elements) {
            if (!backing.any { it == e }) {
                return false
            }
        }
        return true
    }

    override fun get(index: Int): Double {
        require(index in -size until size) {
            "index $index was not inside range [${-size}, $size)"
        }
        return backing[(size + index) % size]
    }

    override fun get(indexSlice: Slice): NumericVector<Double> {
        require(indexSlice in -size until size) {
            "index slice $indexSlice was not inside range [${-size}, $size)"
        }
        val bret = DoubleArray(indexSlice.size)
        var c = 0
        for (i in indexSlice) {
            bret[c++] = backing[(size + i) % size]
        }
        return DoubleVector(bret)
    }

    override fun indexOf(element: Double): Int = backing.indexOfFirst { it == element }

    override fun lastIndexOf(element: Double): Int = backing.indexOfLast { it == element }

    override fun set(index: Int, element: Double): Double {
        require(index in -size until size) {
            "index $index was not inside range [${-size}, $size)"
        }
        return backing[(size + index) % size].apply {
            this@DoubleVector.backing[(size + index) % size] = element
        }
    }

    override fun set(indexSlice: Slice, elements: Collection<Double>): MutableNumericVector<Double> {
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
        return ret as MutableNumericVector<Double>
    }

    override fun listIterator(): ListIterator<Double> = backing.toList().listIterator()

    override fun listIterator(index: Int): ListIterator<Double> = backing.toList().listIterator(index)

    override fun subVector(slice: Slice): MutableNumericVector<Double> =
        TODO("Decide if sub vectors of numeric vectors is allowed")

    override fun subVector(fromIndex: Int, toIndex: Int): MutableNumericVector<Double> =
        TODO("Decide if sub vectors of numeric vectors is allowed")

    override fun unaryPlus(): NumericVector<Double> = DoubleVector(this)

    override fun unaryMinus(): NumericVector<Double> {
        val ret = DoubleVector(this)

        for (i in 0 until ret.size) {
            ret[i] = -ret[i]
        }
        return ret
    }

    private inline fun perform(
        lhs: NumericVector<Double>,
        rhs: NumericVector<Double>,
        action: (Double, Double) -> Double
    ): NumericVector<Double> {
        for (i in 0 until this.size) {
            val l = if (i < lhs.size) lhs[i] else 0.0
            val r = if (i < rhs.size) rhs[i] else 0.0
            this[i] = action(l, r)
        }
        return this
    }

    override fun plus(rhs: NumericVector<Double>): NumericVector<Double> =
        DoubleVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l + r
        }

    override fun minus(rhs: NumericVector<Double>): NumericVector<Double> =
        DoubleVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l - r
        }

    override fun times(rhs: NumericVector<Double>): NumericVector<Double> =
        DoubleVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l * r
        }

    override fun div(rhs: NumericVector<Double>): NumericVector<Double> =
        DoubleVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l / r
        }

    override fun rem(rhs: NumericVector<Double>): NumericVector<Double> =
        DoubleVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l % r
        }

    override fun pow(rhs: NumericVector<Double>): NumericVector<Double> =
        DoubleVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l.pow(r)
        }

    override fun dot(rhs: NumericVector<Double>): Double {
        require(this.size == rhs.size) {
            "size of vector $this must be the same size as $rhs"
        }
        var ret = 0.0

        for (i in 0 until this.size) {
            ret += this[i] * rhs[i]
        }

        return ret
    }

    override fun cross(rhs: NumericVector<Double>): NumericVector<Double> {
        require((this.size == 2 || this.size == 3) && this.size == rhs.size) {
            "cross product is only defined for 2 and 3 dimensional vectors"
        }
        val ret = DoubleVector(3)
        if (this.size == 2) {
            ret[2] = this[0] * rhs[1] - this[1] * rhs[0]
        } else {
            ret[0] = this[1] * rhs[2] - this[2] * rhs[1]
            ret[1] = this[2] * rhs[0] - this[0] * rhs[2]
            ret[2] = this[0] * rhs[1] - this[1] * rhs[0]
        }

        return ret
    }

    override fun scalarProject(onto: NumericVector<Double>): Double = (this.dot(onto)) / onto.magnitude

    override fun scale(byValue: Double): NumericVector<Double> =
        DoubleVector(DoubleArray(backing.size) { backing[it] * byValue })

    override fun project(onto: NumericVector<Double>): NumericVector<Double> =
        onto.scale(this.dot(onto) / onto.dot(onto))

    private inline fun mutatingPerform(
        lhs: NumericVector<Double>,
        rhs: NumericVector<Double>,
        action: (Double, Double) -> Double
    ) {
        for (i in 0 until this.size) {
            val l = if (i < lhs.size) lhs[i] else 0.0
            val r = if (i < rhs.size) rhs[i] else 0.0
            this[i] = action(l, r)
        }
    }

    override fun plusAssign(rhs: NumericVector<Double>) = this.mutatingPerform(this, rhs) { l, r ->
        l + r
    }

    override fun minusAssign(rhs: NumericVector<Double>) = this.mutatingPerform(this, rhs) { l, r ->
        l - r
    }

    override fun timesAssign(rhs: NumericVector<Double>) = this.mutatingPerform(this, rhs) { l, r ->
        l * r
    }

    override fun divAssign(rhs: NumericVector<Double>) = this.mutatingPerform(this, rhs) { l, r ->
        l / r
    }

    override fun remAssign(rhs: NumericVector<Double>) = this.mutatingPerform(this, rhs) { l, r ->
        l % r
    }

    override fun powAssign(rhs: NumericVector<Double>) = this.mutatingPerform(this, rhs) { l, r ->
        l.pow(r)
    }

    override fun scaleAssign(byValue: Double) {
        for (i in 0 until this.size) {
            this[i] *= byValue
        }
    }

    override fun projectAssign(onto: NumericVector<Double>) {
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