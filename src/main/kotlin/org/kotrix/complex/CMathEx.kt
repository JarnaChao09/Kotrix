package org.kotrix.complex

fun Number.toComplex(): Complex =
    Complex(this)

val Number.complex: Complex
    get() = Complex(this)

val Number.conjugate: Complex
    get() = Complex(this).conjugate

operator fun Number.plus(other: Complex): Complex =
    this.complex + other

operator fun Number.minus(other: Complex): Complex =
    this.complex - other

operator fun Number.times(other: Complex): Complex =
    this.complex * other

operator fun Number.div(other: Complex): Complex =
    this.complex / other

operator fun Complex.plus(other: Number): Complex =
    this + other.complex

operator fun Complex.minus(other: Number): Complex =
    this - other.complex

operator fun Complex.times(other: Number): Complex =
    this * other.complex

operator fun Complex.div(other: Number): Complex =
    this / other.complex