package org.kotrix.vector

infix fun IntVectorOld.pow(other: IntVectorOld): IntVectorOld = this.pow(other)

infix fun IntVectorOld.pow(other: DoubleVector): DoubleVector = this.pow(other)

infix fun IntVectorOld.dot(other: IntVectorOld): Int = this.dot(other)

infix fun IntVectorOld.dot(other: DoubleVector): Double = this.dot(other)

infix fun IntVectorOld.powAssign(other: IntVectorOld) = this.powAssign(other)

infix fun IntVectorOld.cross(other: IntVectorOld): IntVectorOld = this.cross(other)

infix fun IntVectorOld.cross(other: DoubleVector): DoubleVector = this.cross(other)


infix fun DoubleVector.pow(other: IntVectorOld): DoubleVector = this.pow(other)

infix fun DoubleVector.pow(other: DoubleVector): DoubleVector = this.pow(other)

infix fun DoubleVector.dot(other: IntVectorOld): Double = this.dot(other)

infix fun DoubleVector.dot(other: DoubleVector): Double = this.dot(other)

infix fun DoubleVector.powAssign(other: DoubleVector) = this.powAssign(other)

infix fun DoubleVector.cross(other: IntVectorOld): DoubleVector = this.cross(other)

infix fun DoubleVector.cross(other: DoubleVector): DoubleVector = this.cross(other)