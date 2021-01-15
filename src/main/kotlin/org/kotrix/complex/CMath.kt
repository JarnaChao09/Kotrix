package org.kotrix.complex

import kotlin.math.*

object CMath {
    fun pow(other: Complex, n: Double): Complex =
        other.pow(n)

    fun pow(other: Complex, n: Complex): Complex =
        other.pow(n)

    fun rootN(other: Complex, n: Double): Complex =
        other.rootN(n)

    fun sqrt(other: Complex): Complex =
        other.sqrt

    fun cbrt(other: Complex): Complex =
        other.cbrt

    fun cos(other: Complex): Complex =
        Complex(cos(other.real) * cosh(other.imag), -1 * sin(other.real) * sinh(other.imag))

    fun sin(other: Complex): Complex =
        Complex(sin(other.real) * cosh(other.imag), cos(other.real) * sinh(other.imag))

    fun tan(other: Complex): Complex =
        other.sin / other.cos

    fun csc(other: Complex): Complex =
        Complex.ONE / other.sin

    fun sec(other: Complex): Complex =
        Complex.ONE / other.cos

    fun cot(other: Complex): Complex =
        Complex.ONE / other.tan

    fun sinh(other: Complex): Complex =
        Complex(cos(other.imag) * sinh(other.real), sin(other.imag) * cosh(other.real))

    fun cosh(other: Complex): Complex =
        Complex(cos(other.imag) * cosh(other.real), sin(other.imag) * sinh(other.real))

    fun tanh(other: Complex): Complex =
        other.sinh / other.cosh

    fun csch(other: Complex): Complex =
        Complex.ONE / other.sinh

    fun sech(other: Complex): Complex =
        Complex.ONE / other.cosh

    fun coth(other: Complex): Complex =
        Complex.ONE / other.tanh

    fun ln(other: Complex): Complex =
        Complex(ln(sqrt(other.real.pow(2) + other.imag.pow(2))), atan2(other.imag, other.real))

    fun arccos(other: Complex): Complex =
        Complex(PI / 2.0, (Complex(0.0, other) + (Complex.ONE - other.pow(2.0)).sqrt).ln)

    fun arcsin(other: Complex): Complex =
        Complex(0.0, -Complex.ONE * (Complex(0.0, other) + (Complex.ONE - other.pow(2.0)).sqrt).ln)

    fun arctan(other: Complex): Complex =
        Complex(0.0, 0.5) * ((Complex.ONE - Complex(0.0, other)).ln - (Complex.ONE + Complex(0.0, other)).ln)

    fun arccsc(other: Complex): Complex = other.reciprocal.arcsin

    fun arcsec(other: Complex): Complex = other.reciprocal.arccos

    fun arccot(other: Complex): Complex = other.reciprocal.arctan

    fun arcsinh(other: Complex): Complex =
        (Complex(0.0, 1.0) * Complex(0.0, -1.0) * other + (Complex.ONE - (Complex(0.0, -1.0) * other).pow(2.0)).sqrt).ln

    fun arccosh(other: Complex): Complex =
        (other + (other - Complex.ONE).sqrt * (Complex.ONE + other).sqrt).ln

    fun arctanh(other: Complex): Complex =
        Complex(0.5, 0.0) * ((other + Complex.ONE) / ((-other) + Complex.ONE)).ln

    fun arccsch(other: Complex): Complex = other.reciprocal.arcsinh

    fun arcsech(other: Complex): Complex = other.reciprocal.arccosh

    fun arccoth(other: Complex): Complex = other.reciprocal.arctanh
}