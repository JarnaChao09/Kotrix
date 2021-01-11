package org.kotrix.discrete

fun <T> List<T>.cartesianProduct(repeat: Int = 1) =
        cProduct(*Array(
                if (repeat > 0)
                    repeat
                else
                    throw IllegalArgumentException(
                            "Cartesian Product operation can only be applied a positive number of times"
                    )
        ) { this })

fun <T> List<T>.cartesianProduct(vararg lists: List<T>) =
        cProduct(this, *lists)

private fun <T> cProduct(vararg lists: List<T>): Sequence<List<T>> = sequence {
    require(lists.map { it.size.toLong() }.reduce(Long::times) <= Int.MAX_VALUE) {
        "Cartesian Product can not produce results who's size exceed Int.MAX_VALUE"
    }

    val num = lists.size
    val lengths = mutableListOf<Int>()
    val remaining = mutableListOf(1)

    lists.reversed().forEach {
        lengths.add(0, it.size)
        remaining.add(0, it.size * remaining[0])
    }

    val n = remaining.removeAt(0)

    (0 until n).forEach { i ->
        val ret = mutableListOf<T>()
        (0 until num).forEach {
            val e = i / remaining[it] % lengths[it]
            ret.add(lists[it, e])
        }
        yield(ret.toList())
    }
}

private operator fun <T> Array<out List<T>>.get(r: Int, c: Int): T = this[r][c]
