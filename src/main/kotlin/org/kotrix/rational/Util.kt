package org.kotrix.rational

import kotlin.math.pow

infix fun Int.pow(other: Int): Int = this.toDouble().pow(other.toDouble()).toInt()

fun Int.sieve(): List<Int> {
    val lp = IntArray(this + 1) { 0 }
    val pr = mutableListOf<Int>()

    for (i in 2..this) {
        if (lp[i] == 0) {
            lp[i] = i
            pr.add(i)
        }

        var j = 0
        while (j < pr.size && pr[j] <= lp[i] && i * pr[j] <= this) {
            lp[i * pr[j]] = pr[j]
            j++
        }
    }

    return pr.toList()
}

fun Int.trialDivision(): Map<Int, Int> {
    var n = this
    val primes = sieve()
    val factorization = mutableListOf<Int>()

    for (d in primes) {
        if (d * d > n) {
            break
        }
        while (n % d == 0) {
            factorization.add(d)
            n /= d
        }
    }

    if (n > 1) factorization.add(n)

    return factorization.groupingBy { it }.eachCount()
}