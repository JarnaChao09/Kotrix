package org.kotrix.matrix

infix fun <T: Any> Matrix<T>.eq(other: Matrix<T>): BooleanMatrix =
    this equal other

infix fun <T: Any> Matrix<T>.ne(other: Matrix<T>): BooleanMatrix =
    if (this.size != other.size)
        throw Matrix.Error.DimensionMisMatch()
    else
        BooleanMatrix(this.size) { r, c -> this[r, c] != other[r, c] }

infix fun <T: Number> NumberMatrix<T>.lt(other: NumberMatrix<T>): BooleanMatrix =
    if (this.size != other.size)
        throw Matrix.Error.DimensionMisMatch()
    else
        BooleanMatrix(this.size) { r, c -> this[r, c].toDouble() < other[r, c].toDouble() }

infix fun <T: Number> NumberMatrix<T>.le(other: NumberMatrix<T>): BooleanMatrix =
    if (this.size != other.size)
        throw Matrix.Error.DimensionMisMatch()
    else
        BooleanMatrix(this.size) { r, c -> this[r, c].toDouble() <= other[r, c].toDouble() }

infix fun <T: Number> NumberMatrix<T>.gt(other: NumberMatrix<T>): BooleanMatrix =
    if (this.size != other.size)
        throw Matrix.Error.DimensionMisMatch()
    else
        BooleanMatrix(this.size) { r, c -> this[r, c].toDouble() > other[r, c].toDouble() }

infix fun <T: Number> NumberMatrix<T>.ge(other: NumberMatrix<T>): BooleanMatrix =
    if (this.size != other.size)
        throw Matrix.Error.DimensionMisMatch()
    else
        BooleanMatrix(this.size) { r, c -> this[r, c].toDouble() >= other[r, c].toDouble() }
