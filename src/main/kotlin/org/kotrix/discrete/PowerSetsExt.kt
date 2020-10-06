package org.kotrix.discrete

import kotlin.math.pow

private tailrec fun <T> powerSet(left: Collection<T>, acc: Set<Set<T>>): Set<Set<T>> =
        when {
            left.isEmpty() -> acc
            else -> powerSet(left = left.drop(1), acc + acc.map { it + left.first() })
        }

val <T> Collection<T>.powerSetSize: Int get() = (2.0).pow(this.size.toDouble()).toInt()

val <T> Collection<T>.powerSet: Set<Set<T>>
    get() = powerSet(this, setOf(setOf()))