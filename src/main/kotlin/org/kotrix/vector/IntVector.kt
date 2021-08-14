package org.kotrix.vector

import org.kotrix.matrix.IntMatrix
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.reflect.KClass

class IntVector(length: Int = 10, initBlock: (Int) -> Int = { 0 }): VectorImpl<Int>(length, initBlock), NumberVectors<Int> {
    constructor(length: Int = 10, initValue: Int): this(length, initBlock = { initValue })

    constructor(copy: VectorImpl<Int>): this(copy.size, initBlock = { i -> copy[i] })

    constructor(list: List<Int>): this(list.size, initBlock = { i -> list[i] })

    sealed class Scope {
        val actions: MutableList<Scope> = emptyList<Scope>().toMutableList()

        class Base: Scope() /** DO NOT MAKE SINGLETON, EACH MUST HAVE SEPARATE SCOPE TO ACCESS SEPARATE ACTIONS LIST **/

        class Error(msg: String = "Illegal Type was found"): Throwable(msg)

        data class Append(val value: Int, var times: Int = 1): Scope()

        data class Push(val value: Int, var times: Int = 1): Scope()

        data class Put(val value: Int, val at: Int, var times: Int = 1): Scope()

        data class Repeat(val times: Int, val repeat: Scope.() -> Scope): Scope() {
            fun run(): Scope {
                val base = Base().repeat()
                for (i in base.actions) {
                    when(i) {
                        is Append -> i.times = this.times
                        is Push -> i.times = this.times
                        is Put -> i.times = this.times
                        else -> throw Error()
                    }
                }
                return base
            }
        }

        operator fun Number.not(): Scope =
            this@Scope.also { this@Scope.actions.add(Append(this.toInt())) }
    }

    companion object {
        @JvmStatic
        fun of(vararg elements: Number): IntVector =
            IntVector(elements.size) { i -> elements[i].toInt() }

        val EMPTY: IntVector
            get() = IntVector(0)
    }

    override val type: KClass<out Int>
        get() = Int::class

    override val list: List<Int>
        get() = super.list

    val array: IntArray
        get() = this.toArray().toIntArray()

    override val magnitude: Double
        get() {
            var sum = 0
            for (i in this) {
                sum += i * i
            }
            return sqrt(sum.toDouble())
        }

    override fun plus(other: NumberVectors<Int>): IntVector {
        val ret = IntVector(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i] + other[i]
        }
        return ret
    }

    override fun minus(other: NumberVectors<Int>): IntVector {
        val ret = IntVector(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i] - other[i]
        }
        return ret
    }

    override fun times(other: NumberVectors<Int>): IntVector {
        val ret = IntVector(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i] * other[i]
        }
        return ret
    }

    override fun div(other: NumberVectors<Int>): IntVector {
        val ret = IntVector(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i] / other[i]
        }
        return ret
    }

    override fun rem(other: NumberVectors<Int>): IntVector {
        val ret = IntVector(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i] % other[i]
        }
        return ret
    }

    override fun pow(other: NumberVectors<Int>): IntVector {
        val ret = IntVector(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i].toDouble().pow(other[i].toDouble()).toInt()
        }
        return ret
    }

    override fun plusAssign(other: NumberVectors<Int>) {
        for (i in 0 until this.size) {
            this[i] += other[i]
        }
    }

    override fun minusAssign(other: NumberVectors<Int>) {
        for (i in 0 until this.size) {
            this[i] -= other[i]
        }
    }

    override fun timesAssign(other: NumberVectors<Int>) {
        for (i in 0 until this.size) {
            this[i] *= other[i]
        }
    }

    override fun divAssign(other: NumberVectors<Int>) {
        for (i in 0 until this.size) {
            this[i] /= other[i]
        }
    }

    override fun remAssign(other: NumberVectors<Int>) {
        for (i in 0 until this.size) {
            this[i] %= other[i]
        }
    }

    override fun powAssign(other: NumberVectors<Int>) {
        for (i in 0 until this.size) {
            this[i] = this[i].toDouble().pow(other[i]).toInt()
        }
    }

    override fun unaryPlus(): IntVector =
        IntVector(this.size) { i -> +(this[i]) }

    override fun unaryMinus(): IntVector =
        IntVector(this.size) { i -> -(this[i]) }

    override fun dot(other: NumberVectors<Int>): Int {
        return IntVector(this.size).mapIndexed { index: Int, _ -> this[index] * other[index] }.sum()
    }

    fun dot(other: DoubleVector): Double {
        return DoubleVector(this.size).mapIndexed { index: Int, _ -> this[index] * other[index]  }.sum()
    }

    override fun scalarProject(other: NumberVectors<Int>): Double = (this.dot(other)) / other.toIntVector().magnitude

    fun scalarProject(other: DoubleVector): Double = (this.dot(other)) / other.magnitude

    override fun projectOnto(other: NumberVectors<Int>): IntVector = other.toIntVector() * (this.dot(other) / other.dot(other))

    fun projectOnto(other: DoubleVector): DoubleVector = other * (this.dot(other) / other.dot(other))

    override fun cross(other: NumberVectors<Int>): IntVector {
        if (this.size > 3 || other.size > 3 || this.size == 1 || other.size == 1) {
            throw IllegalArgumentException("CROSS PRODUCT ONLY DEFINED FOR 2-3D")
        }
        val ret = IntVector(3)
        if (this.size == 2 && other.size == 2) {
            ret[0] = this[0] * other[1] - this[1] * other[0]
        }
        if (this.size == 3 && other.size == 3) {
            ret[0] = this[1] * other[2] - this[2] * other[1]
            ret[1] = this[2] * other[0] - this[0] * other[2]
            ret[2] = this[0] * other[1] - this[1] * other[0]
        }
        return ret
    }

    fun cross(other: DoubleVector): DoubleVector {
        if (this.size > 3 || other.size > 3 || this.size == 1 || other.size == 1) {
            throw IllegalArgumentException("CROSS PRODUCT ONLY DEFINED FOR 2-3D")
        }
        val ret = DoubleVector(3)
        if (this.size == 2 && other.size == 2) {
            ret[0] = this[0] * other[1] - this[1] * other[0]
        }
        if (this.size == 3 && other.size == 3) {
            ret[0] = this[1] * other[2] - this[2] * other[1]
            ret[1] = this[2] * other[0] - this[0] * other[2]
            ret[2] = this[0] * other[1] - this[1] * other[0]
        }
        return ret
    }

    operator fun plus(other: DoubleVector): DoubleVector =
        this.toDoubleVector() + other

    operator fun minus(other: DoubleVector): DoubleVector =
        this.toDoubleVector() - other

    operator fun times(other: DoubleVector): DoubleVector = this.toDoubleVector() * other

    operator fun div(other: DoubleVector): DoubleVector = this.toDoubleVector() / other

    operator fun rem(other: DoubleVector): DoubleVector = this.toDoubleVector() % other

    fun pow(other: DoubleVector): DoubleVector = this.toDoubleVector().pow(other)

    operator fun plus(other: Int): IntVector =
        IntVector(this.size).mapIndexed { index, _ -> this[index] + other } as IntVector

    operator fun plus(other: Double): DoubleVector =
        DoubleVector(this.size).mapIndexed { index, _ -> this[index] + other } as DoubleVector

    operator fun minus(other: Int): IntVector =
        IntVector(this.size).mapIndexed { index, _ -> this[index] - other } as IntVector

    operator fun minus(other: Double): DoubleVector =
        DoubleVector(this.size).mapIndexed { index, _ -> this[index] - other } as DoubleVector

    operator fun times(other: Int): IntVector =
        IntVector(this.size) { i -> this[i] * other }

    operator fun times(other: Double): DoubleVector =
        DoubleVector(this.size) { i -> this[i] * other }

    operator fun div(other: Int): IntVector =
        IntVector(this.size) { i -> this[i] / other }

    operator fun div(other: Double): DoubleVector =
        DoubleVector(this.size) { i -> this[i] / other }

    operator fun rem(other: Int): IntVector =
        IntVector(this.size) { i -> this[i] % other }

    operator fun rem(other: Double): DoubleVector =
        DoubleVector(this.size) { i -> this[i] % other }

    override fun toMatrix(asCol: Boolean): IntMatrix = IntMatrix(this, asCol)

    override fun toArray(): Array<Int> = this.toList().toTypedArray()

    override fun toIntVector(): IntVector = IntVector(this)

    override fun toDoubleVector(): DoubleVector =
        DoubleVector(this.size) { i -> this[i].toDouble() }
}