package org.kotrix.complex

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

/**
 * Test class for org.kotrix.complex
 */
class ComplexTest: StringSpec({
    "Complex.Companion.ZERO should be 0.0 + 0.0i" {
        Complex.ZERO shouldBe Complex(0, 0)
    }

    "Complex.Companion.ONE should be 1.0 + 0.0i" {
        Complex.ONE shouldBe Complex(1, 0)
    }

    "Conjugate of 1.0 + 2.0i should be 1.0 - 2.0i" {
        Complex(1, 2).conjugate shouldBe Complex(1, -2)
    }

    /** Complex Basic Operators **/

    /** Unary Plus and Unary Minus **/

    "Complex.unaryPlus of 1.0 + 1.0i should be 1.0 + 1.0i" {
        +Complex(1, 1) shouldBe Complex(1, 1)
    }

    "Complex.unaryMinus of 1.0 + 1.0i should be -1.0 + -1.0i" {
        -Complex(1, 1) shouldBe Complex(-1, -1)
    }

    /** Complex Basic Arithmetic + - * / **/

    "Complex.plus: (1.0 + 1.0i) + (2.0 + -1.0i) should be 3.0 + 0.0i" {
        Complex(1, 1) + Complex(2, -1) shouldBe Complex(3, 0)
    }

    "Complex.minus: (1.0 + 1.0i) - (2.0 + -1.0i) should be -1.0 + 2.0i" {
        Complex(1, 1) - Complex(2, -1) shouldBe Complex(-1, 2)
    }

    "Complex.times: (1.0 + 1.0i) * (2.0 + 3.0i) should be -1.0 + 5i" {
        Complex(1, 1) * Complex(2, 3) shouldBe Complex(-1, 5)
    }

    "Complex.div: (2.0 + 4.0i) / (6.0 + 8.0i) should be 0.44 + 0.08i" {
        Complex(2, 4) / Complex(6, 8) shouldBe Complex(0.44, 0.08)
    }
})