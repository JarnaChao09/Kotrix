package org.kotrix.symbolic.funAST.extensions

import org.kotrix.symbolic.funAST.Cos
import org.kotrix.symbolic.funAST.Fun
import org.kotrix.symbolic.funAST.Power
import org.kotrix.symbolic.funAST.Sin

infix fun Fun.pow(other: Fun): Fun = Power(this, other)

operator fun Fun.plus(other: Number) = this + other.scalar

operator fun Fun.minus(other: Number) = this - other.scalar

operator fun Fun.times(other: Number) = this * other.scalar

operator fun Fun.div(other: Number) = this / other.scalar

infix fun Fun.pow(other: Number) = this pow other.scalar

infix fun Fun.withValue(other: Number) = this to other.scalar

fun sin(x: Fun): Fun = Sin(x)

fun cos(x: Fun): Fun = Cos(x)

fun tan(x: Fun): Fun = sin(x) / cos(x)

fun csc(x: Fun): Fun = sin(x).reciprocal

fun sec(x: Fun): Fun = cos(x).reciprocal

fun cot(x: Fun): Fun = tan(x).reciprocal