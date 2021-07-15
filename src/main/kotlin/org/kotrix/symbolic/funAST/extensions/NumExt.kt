package org.kotrix.symbolic.funAST.extensions

import org.kotrix.symbolic.funAST.Fun
import org.kotrix.symbolic.funAST.Scalar

val Number.scalar: Scalar
    get() = Scalar(this)

operator fun Number.plus(other: Fun) = this.scalar + other

operator fun Number.minus(other: Fun) = this.scalar - other

operator fun Number.times(other: Fun) = this.scalar * other

operator fun Number.div(other: Fun) = this.scalar / other

infix fun Number.pow(other: Fun)= this.scalar pow other