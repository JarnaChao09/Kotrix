package org.kotrix.vector

infix fun IntVectorOld.pow(other: IntVectorOld): IntVectorOld = this.pow(other)

infix fun IntVectorOld.pow(other: DoubleVectorOld): DoubleVectorOld = this.pow(other)

infix fun IntVectorOld.dot(other: IntVectorOld): Int = this.dot(other)

infix fun IntVectorOld.dot(other: DoubleVectorOld): Double = this.dot(other)

infix fun IntVectorOld.powAssign(other: IntVectorOld) = this.powAssign(other)

infix fun IntVectorOld.cross(other: IntVectorOld): IntVectorOld = this.cross(other)

infix fun IntVectorOld.cross(other: DoubleVectorOld): DoubleVectorOld = this.cross(other)


infix fun DoubleVectorOld.pow(other: IntVectorOld): DoubleVectorOld = this.pow(other)

infix fun DoubleVectorOld.pow(other: DoubleVectorOld): DoubleVectorOld = this.pow(other)

infix fun DoubleVectorOld.dot(other: IntVectorOld): Double = this.dot(other)

infix fun DoubleVectorOld.dot(other: DoubleVectorOld): Double = this.dot(other)

infix fun DoubleVectorOld.powAssign(other: DoubleVectorOld) = this.powAssign(other)

infix fun DoubleVectorOld.cross(other: IntVectorOld): DoubleVectorOld = this.cross(other)

infix fun DoubleVectorOld.cross(other: DoubleVectorOld): DoubleVectorOld = this.cross(other)