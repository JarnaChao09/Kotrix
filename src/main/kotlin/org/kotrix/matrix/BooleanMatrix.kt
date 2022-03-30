package org.kotrix.matrix

import org.kotrix.utils.Shape
import org.kotrix.utils.by
import org.kotrix.vector.BooleanVectorOld
import org.kotrix.vector.VectorImplOld
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class BooleanMatrix(dim: Shape, initBlock: (r: Int, c: Int) -> Boolean): Matrix<Boolean>(dim, initBlock) {
    constructor(x: Int, y: Int, initBlock: (Int) -> Boolean = { false }): this(dim = Shape(
        x,
        y
    ), initBlock = { _, _ -> initBlock(0)})

    constructor(vectorOfVector: VectorImplOld<BooleanVectorOld>, asColVectors: Boolean = false): this(
        dim = if (asColVectors) vectorOfVector[0].size by vectorOfVector.size else vectorOfVector.size by vectorOfVector[0].size,
        initBlock = if (asColVectors) { r, c -> vectorOfVector[c][r] } else { r, c -> vectorOfVector[r][c] }
    )

    constructor(vector: BooleanVectorOld, asCol: Boolean = false):
            this(
                dim = if (asCol) Shape(vector.size, 1) else Shape(
                    1,
                    vector.size
                ),
                initBlock = if (asCol) { i, _ -> vector[i]} else { _, i -> vector[i]  }
            )

    constructor(matrix: Matrix<Boolean>): this(dim = matrix.dim, initBlock = { r, c -> matrix[r, c] })

    constructor(dim1: Shape, asRows: Boolean, initBlock: (Int) -> Boolean): this(dim = dim1, initBlock = if (asRows) { _, i -> initBlock(i) } else { r, _ -> initBlock(r) })

    constructor(dim1: Shape): this(dim1, initBlock = { _, _ -> false })

    constructor(): this(Shape(3, 3), initBlock = { _, _ -> false })

    override val type: KClass<out Boolean> by lazy { Boolean::class }

    operator fun not(): BooleanMatrix =
        BooleanMatrix(this.shape) { r, c -> !this[r, c] }
}

infix fun BooleanMatrix.and(other: BooleanMatrix): BooleanMatrix =
    if (this.shape != other.shape)
        throw IllegalArgumentException("${this.shape} != ${other.shape}")
    else
        BooleanMatrix(this.shape) { r, c -> this[r, c] && other[r, c] }

infix fun BooleanMatrix.or(other: BooleanMatrix): BooleanMatrix =
    if (this.shape != other.shape)
        throw IllegalArgumentException("${this.shape} != ${other.shape}")
    else
        BooleanMatrix(this.shape) { r, c -> this[r, c] || other[r, c] }

infix fun BooleanMatrix.xor(other: BooleanMatrix): BooleanMatrix =
    if (this.shape != other.shape)
        throw IllegalArgumentException("${this.shape} != ${other.shape}")
    else
        BooleanMatrix(this.shape) { r, c -> this[r, c] xor other[r, c] }
