package org.kotrix.vector

import org.kotrix.utils.Slice
import org.kotrix.utils.contains
import kotlin.math.pow

class FloatVector internal constructor(private val backing: FloatArray) : MutableNumericVector<Float>, RandomAccess {
    constructor(size: Int) : this(FloatArray(size)) {
        require(size > 0) {
            "size must be greater than 0"
        }
    }

    constructor(elements: Collection<Float>) : this(FloatArray(elements.size)) {
        for ((index, element) in elements.withIndex()) {
            backing[index] = element
        }
    }

    override val size: Int
        get() = backing.size

    override val magnitude: Float
        get() {
            var ret = 0.0f

            for (e in backing) {
                ret += e * e
            }

            return kotlin.math.sqrt(ret)
        }

    @Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun isEmpty(): Boolean = false

    override fun contains(element: Float): Boolean = backing.any { it == element }

    override fun iterator(): FloatIterator = backing.iterator()

    override fun containsAll(elements: Collection<Float>): Boolean {
        for (e in elements) {
            if (!backing.any { it == e }) {
                return false
            }
        }
        return true
    }

    override fun get(index: Int): Float {
        require(index in -size until size) {
            "index $index was not inside range [${-size}, $size)"
        }
        return backing[(size + index) % size]
    }

    override fun get(indexSlice: Slice): NumericVector<Float> {
        require(indexSlice in -size until size) {
            "index slice $indexSlice was not inside range [${-size}, $size)"
        }
        val bret = FloatArray(indexSlice.size)
        var c = 0
        for (i in indexSlice) {
            bret[c++] = backing[(size + i) % size]
        }
        return FloatVector(bret)
    }

    override fun indexOf(element: Float): Int = backing.indexOfFirst { it == element }

    override fun lastIndexOf(element: Float): Int = backing.indexOfLast { it == element }

    override fun set(index: Int, element: Float): Float {
        require(index in -size until size) {
            "index $index was not inside range [${-size}, $size)"
        }
        return backing[(size + index) % size].apply {
            this@FloatVector.backing[(size + index) % size] = element
        }
    }

    override fun set(indexSlice: Slice, elements: Collection<Float>): MutableNumericVector<Float> {
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
        return ret as MutableNumericVector<Float>
    }

    override fun listIterator(): ListIterator<Float> = backing.toList().listIterator()

    override fun listIterator(index: Int): ListIterator<Float> = backing.toList().listIterator(index)

    override fun subVector(slice: Slice): MutableNumericVector<Float> =
        TODO("Decide if sub vectors of numeric vectors is allowed")

    override fun subVector(fromIndex: Int, toIndex: Int): MutableNumericVector<Float> =
        TODO("Decide if sub vectors of numeric vectors is allowed")

    override fun unaryPlus(): NumericVector<Float> = FloatVector(this)

    override fun unaryMinus(): NumericVector<Float> {
        val ret = FloatVector(this)

        for (i in 0 until ret.size) {
            ret[i] = -ret[i]
        }
        return ret
    }

    private inline fun perform(
        lhs: NumericVector<Float>,
        rhs: NumericVector<Float>,
        action: (Float, Float) -> Float
    ): NumericVector<Float> {
        for (i in 0 until this.size) {
            val l = if (i < lhs.size) lhs[i] else 0.0f
            val r = if (i < rhs.size) rhs[i] else 0.0f
            this[i] = action(l, r)
        }
        return this
    }

    override fun plus(rhs: NumericVector<Float>): NumericVector<Float> =
        FloatVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l + r
        }

    override fun minus(rhs: NumericVector<Float>): NumericVector<Float> =
        FloatVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l - r
        }

    override fun times(rhs: NumericVector<Float>): NumericVector<Float> =
        FloatVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l * r
        }

    override fun div(rhs: NumericVector<Float>): NumericVector<Float> =
        FloatVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l / r
        }

    override fun rem(rhs: NumericVector<Float>): NumericVector<Float> =
        FloatVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l % r
        }

    override fun pow(rhs: NumericVector<Float>): NumericVector<Float> =
        FloatVector(kotlin.math.max(this.size, rhs.size)).perform(this, rhs) { l, r ->
            l.pow(r)
        }

    override fun dot(rhs: NumericVector<Float>): Float {
        require(this.size == rhs.size) {
            "size of vector $this must be the same size as $rhs"
        }
        var ret = 0.0f

        for (i in 0 until this.size) {
            ret += this[i] * rhs[i]
        }

        return ret
    }

    override fun cross(rhs: NumericVector<Float>): NumericVector<Float> {
        require((this.size == 2 || this.size == 3) && this.size == rhs.size) {
            "cross product is only defined for 2 and 3 dimensional vectors"
        }
        val ret = FloatVector(3)
        if (this.size == 2) {
            ret[2] = this[0] * rhs[1] - this[1] * rhs[0]
        } else {
            ret[0] = this[1] * rhs[2] - this[2] * rhs[1]
            ret[1] = this[2] * rhs[0] - this[0] * rhs[2]
            ret[2] = this[0] * rhs[1] - this[1] * rhs[0]
        }

        return ret
    }

    override fun scalarProject(onto: NumericVector<Float>): Float = (this.dot(onto)) / onto.magnitude

    override fun scale(byValue: Float): NumericVector<Float> =
        FloatVector(FloatArray(backing.size) { backing[it] * byValue })

    override fun project(onto: NumericVector<Float>): NumericVector<Float> =
        onto.scale(this.dot(onto) / onto.dot(onto))

    private inline fun mutatingPerform(
        lhs: NumericVector<Float>,
        rhs: NumericVector<Float>,
        action: (Float, Float) -> Float
    ) {
        for (i in 0 until this.size) {
            val l = if (i < lhs.size) lhs[i] else 0.0f
            val r = if (i < rhs.size) rhs[i] else 0.0f
            this[i] = action(l, r)
        }
    }

    override fun plusAssign(rhs: NumericVector<Float>) = this.mutatingPerform(this, rhs) { l, r ->
        l + r
    }

    override fun minusAssign(rhs: NumericVector<Float>) = this.mutatingPerform(this, rhs) { l, r ->
        l - r
    }

    override fun timesAssign(rhs: NumericVector<Float>) = this.mutatingPerform(this, rhs) { l, r ->
        l * r
    }

    override fun divAssign(rhs: NumericVector<Float>) = this.mutatingPerform(this, rhs) { l, r ->
        l / r
    }

    override fun remAssign(rhs: NumericVector<Float>) = this.mutatingPerform(this, rhs) { l, r ->
        l % r
    }

    override fun powAssign(rhs: NumericVector<Float>) = this.mutatingPerform(this, rhs) { l, r ->
        l.pow(r)
    }

    override fun scaleAssign(byValue: Float) {
        for (i in 0 until this.size) {
            this[i] *= byValue
        }
    }

    override fun projectAssign(onto: NumericVector<Float>) {
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