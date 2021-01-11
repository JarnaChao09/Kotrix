package org.kotrix.matrix

infix fun IntMatrix.pow(other: IntMatrix): IntMatrix =
    this.pow(other)

infix fun IntMatrix.pow(other: DoubleMatrix): DoubleMatrix =
    this.toDoubleMatrix() pow other

infix fun DoubleMatrix.pow(other: DoubleMatrix): DoubleMatrix =
    this.pow(other)

infix fun DoubleMatrix.pow(other: IntMatrix): DoubleMatrix =
    this pow other.toDoubleMatrix()

infix fun <T: Number> NumberMatrix<T>.powAssign(other: NumberMatrix<T>)= this.powAssign(other)

infix fun IntMatrix.dot(other: IntMatrix): IntMatrix =
    this.dot(other)

infix fun IntMatrix.dot(other: DoubleMatrix): DoubleMatrix =
    this.toDoubleMatrix() dot other

infix fun DoubleMatrix.dot(other: DoubleMatrix): DoubleMatrix =
    this.dot(other)

infix fun DoubleMatrix.dot(other: IntMatrix): DoubleMatrix =
    this dot other.toDoubleMatrix()

infix fun IntMatrix.cross(other: IntMatrix): IntMatrix =
    this.cross(other)

infix fun IntMatrix.cross(other: DoubleMatrix): DoubleMatrix =
    this.toDoubleMatrix() cross other

infix fun DoubleMatrix.cross(other: DoubleMatrix): DoubleMatrix =
    this.cross(other)

infix fun DoubleMatrix.cross(other: IntMatrix): DoubleMatrix =
    this cross other.toDoubleMatrix()

infix fun IntMatrix.matMult(other: IntMatrix): IntMatrix =
    this.matMult(other)

infix fun IntMatrix.matMult(other: DoubleMatrix): DoubleMatrix =
    this.toDoubleMatrix() matMult other

infix fun DoubleMatrix.matMult(other: DoubleMatrix): DoubleMatrix =
    this.matMult(other)

infix fun DoubleMatrix.matMult(other: IntMatrix): DoubleMatrix =
    this matMult other.toDoubleMatrix()

infix fun IntMatrix.matDiv(other: IntMatrix): IntMatrix =
    this.matDiv(other)

infix fun IntMatrix.matDiv(other: DoubleMatrix): DoubleMatrix =
    this.toDoubleMatrix() matDiv other

infix fun DoubleMatrix.matDiv(other: DoubleMatrix): DoubleMatrix =
    this.matDiv(other)

infix fun DoubleMatrix.matDiv(other: IntMatrix): DoubleMatrix =
    this matDiv other.toDoubleMatrix()


