package org.kotrix.rational

infix fun Int.pow(other: Int): Int {
    var ret = 1
    for (i in 0 until other) {
        ret *= this
    }

    return ret
}

infix fun UInt.pow(other: UInt): UInt {
    var ret = 1U
    for (i in 0U until other) {
        ret *= this
    }

    return ret
}

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