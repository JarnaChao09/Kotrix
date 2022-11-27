package org.kotrix.symbolic.ast.extensions

import org.kotrix.symbolic.ast.Fun
import org.kotrix.symbolic.ast.Scalar

val Number.scalar: Scalar
    get() = Scalar(this)

operator fun Number.plus(other: Fun) = this.scalar + other

operator fun Number.minus(other: Fun) = this.scalar - other

operator fun Number.times(other: Fun) = this.scalar * other

operator fun Number.div(other: Fun) = this.scalar / other

infix fun Number.pow(other: Fun)= this.scalar pow other