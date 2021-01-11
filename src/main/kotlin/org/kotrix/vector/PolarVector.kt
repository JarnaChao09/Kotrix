package org.kotrix.vector

import org.kotrix.complex.Complex
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.cos

sealed class Angle(val theta: Double) {
    class Radians(angle: Double): Angle(theta = angle)

    class Degrees(angle: Double): Angle(theta = angle * (PI / 180.0))
}

data class PolarVector(val radius: Double, val angle: Double) {
    constructor(radius: Double, angle: Angle): this(radius = radius, angle = angle.theta)

    val complex
        get() = Complex(this.radius * cos(this.angle), this.radius * sin(this.angle))
}

typealias Polar = PolarVector