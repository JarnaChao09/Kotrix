package org.kotrix.vector

infix fun IntVector.pow(other: IntVector): IntVector = this.pow(other)

infix fun IntVector.pow(other: DoubleVector): DoubleVector = this.pow(other)

infix fun IntVector.dot(other: IntVector): Int = this.dot(other)

infix fun IntVector.dot(other: DoubleVector): Double = this.dot(other)

infix fun IntVector.powAssign(other: IntVector) = this.powAssign(other)

infix fun IntVector.cross(other: IntVector): IntVector = this.cross(other)

infix fun IntVector.cross(other: DoubleVector): DoubleVector = this.cross(other)


infix fun DoubleVector.pow(other: IntVector): DoubleVector = this.pow(other)

infix fun DoubleVector.pow(other: DoubleVector): DoubleVector = this.pow(other)

infix fun DoubleVector.dot(other: IntVector): Double = this.dot(other)

infix fun DoubleVector.dot(other: DoubleVector): Double = this.dot(other)

infix fun DoubleVector.powAssign(other: DoubleVector) = this.powAssign(other)

infix fun DoubleVector.cross(other: IntVector): DoubleVector = this.cross(other)

infix fun DoubleVector.cross(other: DoubleVector): DoubleVector = this.cross(other)