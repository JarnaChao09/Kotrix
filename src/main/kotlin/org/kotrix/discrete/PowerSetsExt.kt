package org.kotrix.discrete

private tailrec fun <T> powerSet(left: Collection<T>, acc: Set<Set<T>>): Set<Set<T>> =
    when {
        left.isEmpty() -> acc
        else -> powerSet(left = left.drop(1), acc + acc.map { it + left.first() })
    }

val <T> Collection<T>.powerSetSize: Int
    get() {
        var ret = 1
        for (i in 0 until this.size) {
            ret *= 2
        }
        return ret
    }

val <T> Collection<T>.powerSet: Set<Set<T>>
    get() = powerSet(this, setOf(setOf()))