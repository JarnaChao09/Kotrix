package org.kotrix.utils

import kotlin.math.round

class Slice(private val startInclusive: Int, private val endExclusive: Int, val step: Int = 1): Iterable<Int> {
    constructor(range: IntRange) : this(startInclusive = range.first, endExclusive = range.last, step = range.step)

    class SliceIterator(private val slice: Slice) : Iterator<Int> {
        private var current: Int = slice.start
        override fun hasNext(): Boolean {
            return current < slice.end
        }

        override fun next(): Int {
            val ret = current
            current += slice.step
            return ret
        }

    }

    val start: Int get() = startInclusive
    val end: Int get() = endExclusive
    val size: Int get() = round((end - start) / step.toDouble()).toInt()

    override fun iterator(): Iterator<Int> {
        return SliceIterator(this)
    }

    override fun toString(): String = "[$startInclusive, $endExclusive)"

    companion object {
        @JvmStatic
        fun createSlice(startIn: Int, endEx: Int) = startIn sliceTo endEx

        @JvmStatic
        fun createSlice(startIn: Int, endEx: Int, step: Int) = startIn sliceTo endEx step step

        @JvmStatic
        fun modifyStep(original: Slice, step: Int) = original step step
    }
}

infix fun Int.sliceTo(other: Int): Slice = Slice(this, other)

infix fun Slice.step(other: Int): Slice =
    Slice(this.start, this.end, step = other)

internal operator fun IntRange.contains(slice: Slice): Boolean = slice.start >= this.first && slice.end < this.last
