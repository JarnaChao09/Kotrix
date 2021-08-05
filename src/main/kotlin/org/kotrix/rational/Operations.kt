package org.kotrix.rational

operator fun Rational.unaryPlus()  = Rational(this.numerator, this.denominator, this.sign)

operator fun Rational.unaryMinus() = Rational(this.numerator, this.denominator, when (this.sign) {
    Sign.Positive -> Sign.Negative
    Sign.Negative -> Sign.Positive
})

operator fun Rational.component1() = this.numerator

operator fun Rational.component2() = this.denominator

operator fun Rational.component3() = this.sign

operator fun Rational.plus(rhs: Rational): Rational {
    val (leftMultiple, rightMultiple, newDenominator) = ensureDenominatorsAreEqual(this, rhs)

    var ( leftNumerator, _,  leftSign) = this
    var (rightNumerator, _, rightSign) = rhs

    leftNumerator  *=  leftMultiple
    rightNumerator *= rightMultiple

    val (newNumerator, newSign) = when {
        leftSign == rightSign -> {
            leftNumerator + rightNumerator to leftSign
        }
        leftSign != rightSign -> {
            when {
                leftSign == Sign.Positive && rightSign == Sign.Negative -> {
                    if (rightNumerator > leftNumerator) {
                        rightNumerator - leftNumerator to Sign.Negative
                    } else {
                        leftNumerator - rightNumerator to Sign.Positive
                    }
                }
                leftSign == Sign.Negative && rightSign == Sign.Positive -> {
                    if (leftNumerator > rightNumerator) {
                        leftNumerator - rightNumerator to Sign.Negative
                    } else {
                        rightNumerator - leftNumerator to Sign.Positive
                    }
                }
                else -> throw Exception("Unreachable")
            }
        }
        else -> throw Exception("Unreachable")
    }

    return Rational(newNumerator, newDenominator, newSign)
}

operator fun Rational.minus(rhs: Rational): Rational = this + (-rhs)

/**
 * Ensures that denominators are equal in order to perform add operation
 *
 * @return a triple containing three components
 *         first  = value to multiply lhs numerator by
 *         second = value to multiply rhs numerator by
 *         third  = new denominator for both lhs and rhs
 */
private fun ensureDenominatorsAreEqual(lhs: Rational, rhs: Rational): Triple<UInt, UInt, UInt> {
    val (_, ld, _) = lhs
    val (_, rd, _) = rhs

    if (ld == rd) return Triple(1U, 1U, ld)

    var (lm, rm) = 1U to 1U

    val nd = when {
        ld % rd == 0U -> {
            rm = ld / rd

            ld
        }
        rd % ld == 0U -> {
            lm = rd / ld

            rd
        }
        else -> {
            lm = rd
            rm = ld

            ld * rd
        }
    }

    return Triple(lm, rm, nd)
}