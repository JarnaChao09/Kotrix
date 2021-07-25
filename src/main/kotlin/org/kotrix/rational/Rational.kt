package org.kotrix.rational

import kotlin.math.absoluteValue
import kotlin.math.sign

sealed interface Rational {
    val numerator: Int
    val denominator: Int
    val sign: Sign

    companion object {
        operator fun invoke(numerator: Int, denominator: Int, sign: Sign? = null): Rational {
            val numeratorAbsolute = numerator.absoluteValue
            val denominatorAbsolute = denominator.absoluteValue
            val determinedSign: Sign = sign ?: when (numerator.sign * denominator.sign) {
                1 -> Sign.Positive
                -1 -> Sign.Negative
                else -> { // must be 0
                    when {
                        numerator.sign != 0 -> {
                            when (numerator.sign) {
                                1 -> Sign.Positive
                                -1 -> Sign.Negative
                                else -> throw Exception("Unreachable")
                            }
                        }
                        denominator.sign != 0 -> {
                            when (denominator.sign) {
                                1 -> Sign.Positive
                                -1 -> Sign.Negative
                                else -> throw Exception("Unreachable")
                            }
                        }
                        numerator.sign == denominator.sign -> Sign.Positive // number is NaN just return dummy sign
                        else -> throw Exception("Cannot determine sign of ($numerator / $denominator)")
                    }
                }
            }

            return when {
                numeratorAbsolute == 0 -> {
                    if (denominatorAbsolute != 0) {
                        when (determinedSign) {
                            Sign.Positive -> Zero.Positive
                            Sign.Negative -> Zero.Negative
                        }
                    } else {
                        NaN
                    }
                }
                numeratorAbsolute == 1 && denominatorAbsolute == 0 -> {
                    when (determinedSign) {
                        Sign.Positive -> Infinity.Positive
                        Sign.Negative -> Infinity.Negative
                    }
                }
                numeratorAbsolute == denominatorAbsolute -> {
                    when (determinedSign) {
                        Sign.Positive -> One.Positive
                        Sign.Negative -> One.Negative
                    }
                }
                else -> {
                    createRational(
                        numeratorAbsolute,
                        denominatorAbsolute,
                        determinedSign
                    )
                }
            }
        }

        operator fun invoke(value: String): Rational {
            return parseString(value.replace(" ", "")).let { (n, d, s) ->
                invoke(n, d, s)
            }
        }

        operator fun <T : Number> invoke(number: T): Rational {
            return parseNumber(number).let { (n, d, s) ->
                invoke(n, d, s)
            }
        }

        private fun createRational(numAbs: Int, denAbs: Int, sign: Sign): Rational =
            simplify(numAbs, denAbs).let { (n, d) ->
                RationalImpl(n, d, sign)
            }

        private fun parseNumber(num: Number) = parseString(num.toString())

        private fun parseString(str: String): Triple<Int, Int, Sign> {
            val sign = when {
                str.startsWith("+") -> Sign.Positive
                str.startsWith("-") -> Sign.Negative
                else -> Sign.Positive
            }
            if ("." !in str) {
                return Triple(str.toInt(), 1, sign)
            }

            val numStr = str.replace(".", "")
            val numOfDecimalPlaces = str.substringAfter(".").length

            return Triple(numStr.toInt(), 10.pow(numOfDecimalPlaces), sign)
        }

        private fun simplify(num: Int, den: Int): Pair<Int, Int> {
            val numFactors = num.trialDivision()
            val denFactors = den.trialDivision()

            val newFactors = mutableMapOf<Int, Int>()
            for (p in numFactors.keys union denFactors.keys) {
                val n: Int = numFactors.getOrDefault(p, 0)
                val d: Int = denFactors.getOrDefault(p, 0)

                val new = n - d

                newFactors[p] = new
            }

            var newNum = 1
            var newDen = 1

            for (i in newFactors.entries) {
                when {
                    i.value == 0 -> {
                        continue
                    }
                    i.value > 0 -> {
                        newNum *= i.key.pow(i.value)
                    }
                    i.value < 0 -> {
                        newDen *= i.key.pow(-i.value)
                    }
                }
            }

            return newNum to newDen
        }
    }
}