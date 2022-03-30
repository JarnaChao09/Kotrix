package org.kotrix.utils

data class Shape(val x: Int, val y: Int) {
    val reversed: Shape
        get() = this.y by this.x

    val size: Int
        get() = this.x * this.y

    operator fun plus(other: Shape): Shape =
        Shape(this.x + other.x, this.y + other.y)

    operator fun minus(other: Shape): Shape =
        Shape(this.x - other.x, this.y - other.y)

    operator fun times(other: Int): Shape =
        Shape(this.x * other, this.y * other)

    fun scale(other: Int) = this * other

    companion object {
        @JvmStatic
        fun sizeOf(x: Int, y: Int) = x by y

        @JvmStatic
        @JvmName("add")
        fun sps(src: Shape, other: Shape) = src + other

        @JvmStatic
        @JvmName("subtract")
        fun sms(src: Shape, other: Shape) = src - other
    }
}

infix operator fun Int.times(other: Shape): Shape =
    Shape(this * other.x, this * other.y)

infix fun Int.by(other: Int): Shape = Shape(this, other)
