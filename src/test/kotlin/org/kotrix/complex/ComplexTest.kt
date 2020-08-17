package org.kotrix.complex

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf

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

    "Complex.reciprocal of 1 + 1i should be 0.5 + -0.5i" {
        Complex(1, 1).reciprocal shouldBe Complex(0.5, -0.5)
    }

    "Complex.conjugate of 1.0 + 2.0i should be 1.0 - 2.0i" {
        Complex(1, 2).conjugate shouldBe Complex(1, -2)
    }

    "Complex.radius of 3 + 4i should be 5.0" {
        Complex(3, 4).radius shouldBe 5.0
        Complex(3, 4).radius.shouldBeTypeOf<Double>()
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

    /** Complex Advance Arithmetic
     * power, root, trigonometry
     */

    /** Power **/

    "Complex.pow(Double): (3 + 4i) ^ 3 should be -117 + 44i" {
        Complex(3, 4).pow(3.0) shouldBe Complex(-117, 44)
    }

    "Complex.pow(Complex): (3 + 4i) ^ (1 + 2i) should be approximately -0.4198131755619574 + -0.6604516942073323" {
        Complex(3, 4).pow(Complex(1, 2)) shouldBe Complex(-0.4198131755619574, -0.6604516942073323)
    }

    "Complex.squared: (3 + 4i) ^ 2 should be -7 + 24i" {
        Complex(3, 4).squared shouldBe Complex(-7, 24)
    }

    /** Root **/

    "Complex.rootN(Double): (3 + 4i).rootN(3) should be approximately 1.6289371459221758 + 0.5201745023045458i" {
        Complex(3, 4).rootN(3.0) shouldBe Complex(1.6289371459221758, 0.5201745023045458)
    }

    "Complex.rootN(Double): (3 + 4i).rootN(3) should be (3 + 4i) ^ (1 / 3)" {
        Complex(3, 4).rootN(3.0) shouldBe Complex(3, 4).pow(1.0 / 3.0)
    }

    "Complex.sqrt: (3 + 4i).sqrt should be 2 + i" {
        Complex(3, 4).sqrt shouldBe Complex(2, 1)
    }

    "Complex.sqrt: (3 + 4i).sqrt should be (3 + 4i).rootN(2)" {
        Complex(3, 4).sqrt shouldBe Complex(3, 4).rootN(2.0)
    }

    "Complex.sqrt: (3 + 4i).sqrt should be (3 + 4i) ^ (1 / 2)" {
        Complex(3, 4).sqrt shouldBe Complex(3, 4).pow(1.0 / 2.0)
    }

    "Complex.cbrt: (3 + 4i).cbrt should be approximately 1.6289371459221758 + 0.5201745023045458i" {
        Complex(3, 4).cbrt shouldBe Complex(1.6289371459221758, 0.5201745023045458)
    }

    "Complex.cbrt: (3 + 4i).cbrt should be (3 + 4i).rootN(3)" {
        Complex(3, 4).cbrt shouldBe Complex(3, 4).rootN(3.0)
    }

    "Complex.cbrt: (3 + 4i).cbrt should be (3 + 4i) ^ (1 / 3)" {
        Complex(3, 4).cbrt shouldBe Complex(3, 4).pow(1.0 / 3.0)
    }

    /** Trigonometry **/

    "Complex.cos: (3 + 4i).cos should be approximately -27.0349456030742246 + -3.8511533348117775i" {
        Complex(3, 4).cos shouldBe Complex(-27.0349456030742246, -3.851153334811777)
    }

    "Complex.cos: (3 + 4i).cos should be CMath.cos(3 + 4i)" {
        Complex(3, 4).cos shouldBe CMath.cos(Complex(3, 4))
    }

    "Complex.sin: (3 + 4i).sin should be approximately 3.8537380379193773 + -27.01681325800392" {
        Complex(3, 4).sin shouldBe Complex(3.853738037919377, -27.016813258003932)
    }

    "Complex.sin: (3 + 4i).sin should be CMath.sin(3 + 4i)" {
        Complex(3, 4).sin shouldBe CMath.sin(Complex(3, 4))
    }

    "Complex.tan: (3 + 4i).tan should be approximately -1.8734620462949035E-4 + 0.9993559873814731" {
        Complex(3, 4).tan shouldBe Complex(-1.8734620462949035E-4, 0.9993559873814731)
    }

    "Complex.tan: (3 + 4i).tan should be CMath.tan(3 + 4i)" {
        Complex(3, 4).tan shouldBe CMath.tan(Complex(3, 4))
    }
})