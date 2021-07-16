package org.kotrix.complex

import org.kotrix.vector.Angle
import org.kotrix.vector.Polar
import org.kotrix.vector.PolarVector
import kotlin.math.*

import java.util.stream.IntStream

data class Complex(var real: Double, var imag: Double): Number() {
    constructor(real: Number, imag: Number): this(real.toDouble(), imag.toDouble())

    constructor(real: Number, imag: Complex): this(real.toDouble() - imag.imag, imag.real)

    constructor(real: Complex, imag: Number): this(real.real, imag.toDouble() + real.imag)

    constructor(real: Number): this(real.toDouble(), 0.0)

    constructor(copy: Complex): this(copy.real, copy.imag)

    override fun toByte(): Byte = radius.toInt().toByte()

    override fun toChar(): Char = radius.toInt().toChar()

    override fun toDouble(): Double = radius

    override fun toFloat(): Float = radius.toFloat()

    override fun toInt(): Int = radius.toInt()

    override fun toLong(): Long = radius.toLong()

    override fun toShort(): Short = radius.toInt().toShort()

    companion object {
        val ZERO
            get() = Complex(0.0, 0.0)

        val ONE
            get() = Complex(1.0, 0.0)
    }

    val reciprocal
        get() = ONE / this

    val conjugate
        get() = Complex(real, -imag)

    val radius
        get() = hypot(real, imag)

    val angle
        get() = atan2(imag, real)

    val polar
        get() = PolarVector(radius = radius, angle = Angle.Radians(angle))

    operator fun unaryPlus(): Complex =
        Complex(+real, +imag)

    operator fun unaryMinus(): Complex =
        Complex(-real, -imag)

    operator fun plus(other: Complex): Complex =
        Complex(this.real + other.real, this.imag + other.imag)

    operator fun minus(other: Complex): Complex =
        Complex(this.real - other.real, this.imag - other.imag)

    operator fun times(other: Complex): Complex =
        Complex(
            this.real * other.real - this.imag * other.imag,
            this.imag * other.real + this.real * other.imag
        )

    operator fun div(other: Complex): Complex =
        Complex(
            (this.real * other.real + this.imag * other.imag) / (other.real.pow(2) + other.imag.pow(2)),
            (this.imag * other.real - this.real * other.imag) / (other.real.pow(2) + other.imag.pow(2))
        )

    fun pow(n: Double): Complex =
        if (n % 1 != 0.0) {
            val rNthPower = this.polar.radius.pow(n)
            Complex(rNthPower * cos(this.polar.angle * n), rNthPower * sin(this.polar.angle * n))
        } else {
            var sum = ONE
            IntStream.range(0, n.toInt()).forEach { sum *= this }
            sum
        }

    fun pow(other: Complex): Complex =
        Polar(
            radius = this.polar.radius.pow(other.real) * exp(-other.imag * this.polar.angle),
            angle = Angle.Radians(
                other.real * this.polar.angle + (other.imag * ln(this.polar.radius))
            )
        ).complex

    val floor
        get() = Complex(floor(this.real), floor(this.imag))

    val ceil
        get() = Complex(ceil(this.real), ceil(this.imag))

    val squared
        get() = pow(2.0)

    val sqrt
        get() = rootN(2.0)

    val cbrt
        get() = rootN(3.0)

    fun rootN(n: Double): Complex {
        val rootNR = this.polar.radius.pow(1.0 / n)
        return Complex(rootNR * cos(this.polar.angle / n), rootNR * sin(this.polar.angle / n))
    }

    val cos
        get() = CMath.cos(this)

    val sin
        get() = CMath.sin(this)

    val tan
        get() = CMath.tan(this)

    val csc
        get() = CMath.csc(this)

    val sec
        get() = CMath.sec(this)

    val cot
        get() = CMath.cot(this)

    val sinh
        get() = CMath.sinh(this)

    val cosh
        get() = CMath.cosh(this)

    val tanh
        get() = CMath.tanh(this)

    val csch
        get() = CMath.csch(this)

    val sech
        get() = CMath.sech(this)

    val coth
        get() = CMath.coth(this)

    val ln
        get() = CMath.ln(this)

    val arccos
        get() = CMath.arccos(this)

    val arcsin
        get() = CMath.arcsin(this)

    val arctan
        get() = CMath.arctan(this)

    val arccsc
        get() = CMath.arccsc(this)

    val arcsec
        get() = CMath.arcsec(this)

    val arccot
        get() = CMath.arccot(this)

    val arcsinh
        get() = CMath.arcsinh(this)

    val arccosh
        get() = CMath.arccosh(this)

    val arctanh
        get() = CMath.arctanh(this)

    val arccsch
        get() = CMath.arccsch(this)

    val arcsech
        get() = CMath.arcsech(this)

    val arccoth
        get() = CMath.arccoth(this)
}