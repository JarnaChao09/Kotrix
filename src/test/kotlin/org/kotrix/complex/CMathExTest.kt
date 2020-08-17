package org.kotrix.complex

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeTypeOf

class CMathExTest: StringSpec({
    /** Conversion **/

    "Number.toComplex(): 10.toComplex() should be 10 + 0i" {
        10.toComplex().shouldBeTypeOf<Complex>()
        10.toComplex() shouldBe Complex(10)
    }

    "Number.complex: 10.complex should be 10 + 0i" {
        10.complex.shouldBeTypeOf<Complex>()
        10.complex shouldBe Complex(10)
    }

    "Number.toComplex() should be Number.complex" {
        10.toComplex() shouldBe 10.complex
    }

    "Number.conjugate: 10.conjugate should be 10 + -0i" {
        10.conjugate shouldBe Complex(10).conjugate
    }

    "Number.conjugate: 10.conjugate should not be 10 + 0i" {
        10.conjugate shouldNotBe Complex(10)
    }

    /** Operators **/

    "Number.plus(Complex): 1 + (1 + 2i) should be 2 + 2i" {
        1 + Complex(1, 2) shouldBe Complex(2, 2)
    }

    "Number.minus(Complex): 1 - (1 + 2i) should be 0 + -2i" {
        1 - Complex(1, 2) shouldBe Complex(0, -2)
    }

    "Number.times(Complex): 2 * (1 + 2i) should be 2 + 4i" {
        2 * Complex(1, 2) shouldBe Complex(2, 4)
    }

    "Number.div(Complex): 2 / (1 + 2i) should be 0.4 + -0.8i" {
        2 / Complex(1, 2) shouldBe Complex(0.4, -0.8)
    }

    "Complex.plus(Number): (1 + 2i) + 1 should be 2 + 2i" {
        Complex(1, 2) + 1 shouldBe Complex(2, 2)
    }

    "Complex.minus(Number): (1 + 2i) - 1 should be 0 + 2i" {
        Complex(1, 2) - 1 shouldBe Complex(0, 2)
    }

    "Complex.times(Number): (1 + 2i) * 2 should be 2 + 4i" {
        Complex(1, 2) * 2 shouldBe Complex(2, 4)
    }

    "Complex.div(Number): (1 + 2i) / 2 should be 0.5 + 1i" {
        Complex(1, 2) / 2 shouldBe Complex(0.5, 1)
    }
})