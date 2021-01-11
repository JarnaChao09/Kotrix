package org.kotrix.vector

import org.kotrix.matrix.DoubleMatrix
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.reflect.KClass

class DoubleVector(length: Int = 10, initBlock: (Int) -> Double = { 0.0 }): Vector<Double>(length, initBlock), NumberVector<Double> {
    constructor(length: Int = 10, initValue: Double): this(length, initBlock = { initValue })

    constructor(copy: Vector<Double>): this(copy.size, initBlock = { i -> copy[i] })

    constructor(list: List<Double>): this(list.size, initBlock = { i -> list[i] })

    sealed class Scope {
        val actions: MutableList<Scope> = emptyList<Scope>().toMutableList()

        class Base: Scope() /** DO NOT MAKE SINGLETON, EACH MUST HAVE SEPARATE SCOPE TO ACCESS SEPARATE ACTIONS LIST **/

        class Error(msg: String = "Illegal Type was found"): Throwable(msg)

        data class Append(val value: Double, var times: Int = 1): Scope()

        data class Push(val value: Double, var times: Int = 1): Scope()

        data class Put(val value: Double, val at: Int, var times: Int = 1): Scope()

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
            this@Scope.also { this@Scope.actions.add(Append(this.toDouble())) }
    }

    companion object {
        @JvmStatic
        fun of(vararg elements: Number): DoubleVector =
            DoubleVector(elements.size) { i -> elements[i].toDouble() }

        val EMPTY: DoubleVector
            get() = DoubleVector(0)
    }

    override val type: KClass<out Double>
        get() = Double::class

    override val list: List<Double>
        get() = super.list

    val array: DoubleArray
        get() = this.toArray().toDoubleArray()

    override val magnitude: Double
        get() {
            var sum = 0.0
            for (i in this) {
                sum += i * i
            }
            return sqrt(sum)
        }

    override fun plus(other: NumberVector<Double>): DoubleVector {
        val ret = DoubleVector(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i] + other[i]
        }
        return ret
    }

    override fun minus(other: NumberVector<Double>): DoubleVector {
        val ret = DoubleVector(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i] - other[i]
        }
        return ret
    }

    override fun times(other: NumberVector<Double>): DoubleVector {
        val ret = DoubleVector(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i] * other[i]
        }
        return ret
    }

    override fun div(other: NumberVector<Double>): DoubleVector {
        val ret = DoubleVector(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i] / other[i]
        }
        return ret
    }

    override fun rem(other: NumberVector<Double>): DoubleVector {
        val ret = DoubleVector(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i] % other[i]
        }
        return ret
    }

    override fun pow(other: NumberVector<Double>): DoubleVector {
        val ret = DoubleVector(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i].pow(other[i])
        }
        return ret
    }

    override fun plusAssign(other: NumberVector<Double>) {
        for (i in 0 until this.size) {
            this[i] += other[i]
        }
    }

    override fun minusAssign(other: NumberVector<Double>) {
        for (i in 0 until this.size) {
            this[i] -= other[i]
        }
    }

    override fun timesAssign(other: NumberVector<Double>) {
        for (i in 0 until this.size) {
            this[i] *= other[i]
        }
    }

    override fun divAssign(other: NumberVector<Double>) {
        for (i in 0 until this.size) {
            this[i] /= other[i]
        }
    }

    override fun remAssign(other: NumberVector<Double>) {
        for (i in 0 until this.size) {
            this[i] %= other[i]
        }
    }

    override fun powAssign(other: NumberVector<Double>) {
        for (i in 0 until this.size) {
            this[i] = this[i].pow(other[i])
        }
    }

    override fun unaryPlus(): DoubleVector =
        DoubleVector(this.size) { i -> +(this[i]) }

    override fun unaryMinus(): DoubleVector =
        DoubleVector(this.size) { i -> -(this[i]) }

    override fun dot(other: NumberVector<Double>): Double {
        return DoubleVector(this.size).mapIndexed { index: Int, _ -> this[index] * other[index] }.sum()
    }

    fun dot(other: IntVector): Double {
        return DoubleVector(this.size).mapIndexed { index: Int, _ -> this[index] * other[index] }.sum()
    }

    override fun scalarProject(other: NumberVector<Double>): Double = (this.dot(other)) / other.toDoubleVector().magnitude

    fun scalarProject(other: IntVector): Double = (this.dot(other)) / other.magnitude

    override fun projectOnto(other: NumberVector<Double>): DoubleVector = other.toDoubleVector() * (this.dot(other) / other.dot(other))

    fun projectOnto(other: IntVector): DoubleVector = other.toDoubleVector() * (this.dot(other) / other.dot(other))

    override fun cross(other: NumberVector<Double>): DoubleVector {
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

    fun cross(other: IntVector): DoubleVector {
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

    operator fun plus(other: IntVector): DoubleVector =
        this + other.toDoubleVector()

    operator fun minus(other: IntVector): DoubleVector =
        this - other.toDoubleVector()

    operator fun times(other: IntVector): DoubleVector = this * other.toDoubleVector()

    operator fun div(other: IntVector): DoubleVector = this / other.toDoubleVector()

    operator fun rem(other: IntVector): DoubleVector = this % other.toDoubleVector()

    fun pow(other: IntVector): DoubleVector = this.pow(other.toDoubleVector())

    operator fun plus(other: Int): DoubleVector =
        DoubleVector(this.size).mapIndexed { index, _ -> this[index] + other } as DoubleVector

    operator fun plus(other: Double): DoubleVector =
        DoubleVector(this.size).mapIndexed { index, _ -> this[index] + other } as DoubleVector

    operator fun minus(other: Int): DoubleVector =
        DoubleVector(this.size) { i -> this[i] - other }

    operator fun minus(other: Double): DoubleVector =
        DoubleVector(this.size) { i -> this[i] - other }

    operator fun times(other: Int): DoubleVector =
        this * other.toDouble()

    operator fun times(other: Double): DoubleVector =
        DoubleVector(this.size) { i -> this[i] * other }

    operator fun div(other: Int): DoubleVector =
        DoubleVector(this.size) { i -> this[i] / other }

    operator fun div(other: Double): DoubleVector =
        DoubleVector(this.size) { i -> this[i] / other }

    operator fun rem(other: Int): DoubleVector =
        DoubleVector(this.size) { i -> this[i] % other }

    operator fun rem(other: Double): DoubleVector =
        DoubleVector(this.size) { i -> this[i] % other }

    override fun toMatrix(asCol: Boolean): DoubleMatrix = DoubleMatrix(this, asCol)

    override fun toArray(): Array<Double> = this.toList().toTypedArray()

    override fun toIntVector(): IntVector =
        IntVector(this.size) { i -> this[i].toInt() }

    override fun toDoubleVector(): DoubleVector = DoubleVector(this)
}