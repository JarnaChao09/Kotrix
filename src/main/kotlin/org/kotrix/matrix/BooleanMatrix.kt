package org.kotrix.matrix

import org.kotrix.utils.Size
import org.kotrix.utils.by
import org.kotrix.vector.BooleanVector
import org.kotrix.vector.VectorImpl
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class BooleanMatrix(dim: Size, initBlock: (r: Int, c: Int) -> Boolean): Matrix<Boolean>(dim, initBlock) {
    constructor(x: Int, y: Int, initBlock: (Int) -> Boolean = { false }): this(dim = Size(
        x,
        y
    ), initBlock = { _, _ -> initBlock(0)})

    constructor(vectorOfVector: VectorImpl<BooleanVector>, asColVectors: Boolean = false): this(
        dim = if (asColVectors) vectorOfVector[0].size by vectorOfVector.size else vectorOfVector.size by vectorOfVector[0].size,
        initBlock = if (asColVectors) { r, c -> vectorOfVector[c][r] } else { r, c -> vectorOfVector[r][c] }
    )

    constructor(vector: BooleanVector, asCol: Boolean = false):
            this(
                dim = if (asCol) Size(vector.size, 1) else Size(
                    1,
                    vector.size
                ),
                initBlock = if (asCol) { i, _ -> vector[i]} else { _, i -> vector[i]  }
            )

    constructor(matrix: Matrix<Boolean>): this(dim = matrix.dim, initBlock = { r, c -> matrix[r, c] })

    constructor(dim1: Size, asRows: Boolean, initBlock: (Int) -> Boolean): this(dim = dim1, initBlock = if (asRows) { _, i -> initBlock(i) } else { r, _ -> initBlock(r) })

    constructor(dim1: Size): this(dim1, initBlock = { _, _ -> false })

    constructor(): this(Size(3, 3), initBlock = { _, _ -> false })

    override val type: KClass<out Boolean> by lazy { Boolean::class }

    operator fun not(): BooleanMatrix =
        BooleanMatrix(this.size) { r, c -> !this[r, c] }
}

infix fun BooleanMatrix.and(other: BooleanMatrix): BooleanMatrix =
    if (this.size != other.size)
        throw IllegalArgumentException("${this.size} != ${other.size}")
    else
        BooleanMatrix(this.size) { r, c -> this[r, c] && other[r, c] }

infix fun BooleanMatrix.or(other: BooleanMatrix): BooleanMatrix =
    if (this.size != other.size)
        throw IllegalArgumentException("${this.size} != ${other.size}")
    else
        BooleanMatrix(this.size) { r, c -> this[r, c] || other[r, c] }

infix fun BooleanMatrix.xor(other: BooleanMatrix): BooleanMatrix =
    if (this.size != other.size)
        throw IllegalArgumentException("${this.size} != ${other.size}")
    else
        BooleanMatrix(this.size) { r, c -> this[r, c] xor other[r, c] }
