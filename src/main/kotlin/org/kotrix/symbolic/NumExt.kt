package org.kotrix.symbolic

val Number.const: Constant
    get() = Constant(this)

operator fun Number.plus(other: Fun) = this.const + other

operator fun Number.minus(other: Fun) = this.const - other

operator fun Number.times(other: Fun) = this.const * other

operator fun Number.div(other: Fun) = this.const / other

infix fun Number.pow(other: Fun)= this.const pow other