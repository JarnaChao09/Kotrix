package org.kotrix.vector

import org.kotrix.matrix.DoubleMatrix
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.reflect.KClass

class DoubleVectorOld(length: Int = 10, initBlock: (Int) -> Double = { 0.0 }): VectorImplOld<Double>(length, initBlock), NumberVectors<Double> {
    constructor(length: Int = 10, initValue: Double): this(length, initBlock = { initValue })

    constructor(copy: VectorImplOld<Double>): this(copy.size, initBlock = { i -> copy[i] })

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
        fun of(vararg elements: Number): DoubleVectorOld =
            DoubleVectorOld(elements.size) { i -> elements[i].toDouble() }

        val EMPTY: DoubleVectorOld
            get() = DoubleVectorOld(0)
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

    override fun plus(other: NumberVectors<Double>): DoubleVectorOld {
        val ret = DoubleVectorOld(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i] + other[i]
        }
        return ret
    }

    override fun minus(other: NumberVectors<Double>): DoubleVectorOld {
        val ret = DoubleVectorOld(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i] - other[i]
        }
        return ret
    }

    override fun times(other: NumberVectors<Double>): DoubleVectorOld {
        val ret = DoubleVectorOld(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i] * other[i]
        }
        return ret
    }

    override fun div(other: NumberVectors<Double>): DoubleVectorOld {
        val ret = DoubleVectorOld(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i] / other[i]
        }
        return ret
    }

    override fun rem(other: NumberVectors<Double>): DoubleVectorOld {
        val ret = DoubleVectorOld(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i] % other[i]
        }
        return ret
    }

    override fun pow(other: NumberVectors<Double>): DoubleVectorOld {
        val ret = DoubleVectorOld(this.size)
        for (i in 0 until ret.size) {
            ret[i] = this[i].pow(other[i])
        }
        return ret
    }

    override fun plusAssign(other: NumberVectors<Double>) {
        for (i in 0 until this.size) {
            this[i] += other[i]
        }
    }

    override fun minusAssign(other: NumberVectors<Double>) {
        for (i in 0 until this.size) {
            this[i] -= other[i]
        }
    }

    override fun timesAssign(other: NumberVectors<Double>) {
        for (i in 0 until this.size) {
            this[i] *= other[i]
        }
    }

    override fun divAssign(other: NumberVectors<Double>) {
        for (i in 0 until this.size) {
            this[i] /= other[i]
        }
    }

    override fun remAssign(other: NumberVectors<Double>) {
        for (i in 0 until this.size) {
            this[i] %= other[i]
        }
    }

    override fun powAssign(other: NumberVectors<Double>) {
        for (i in 0 until this.size) {
            this[i] = this[i].pow(other[i])
        }
    }

    override fun unaryPlus(): DoubleVectorOld =
        DoubleVectorOld(this.size) { i -> +(this[i]) }

    override fun unaryMinus(): DoubleVectorOld =
        DoubleVectorOld(this.size) { i -> -(this[i]) }

    override fun dot(other: NumberVectors<Double>): Double {
        return DoubleVectorOld(this.size).mapIndexed { index: Int, _ -> this[index] * other[index] }.sum()
    }

    fun dot(other: IntVectorOld): Double {
        return DoubleVectorOld(this.size).mapIndexed { index: Int, _ -> this[index] * other[index] }.sum()
    }

    override fun scalarProject(other: NumberVectors<Double>): Double = (this.dot(other)) / other.toDoubleVector().magnitude

    fun scalarProject(other: IntVectorOld): Double = (this.dot(other)) / other.magnitude

    override fun projectOnto(other: NumberVectors<Double>): DoubleVectorOld = other.toDoubleVector() * (this.dot(other) / other.dot(other))

    fun projectOnto(other: IntVectorOld): DoubleVectorOld = other.toDoubleVector() * (this.dot(other) / other.dot(other))

    override fun cross(other: NumberVectors<Double>): DoubleVectorOld {
        if (this.size > 3 || other.size > 3 || this.size == 1 || other.size == 1) {
            throw IllegalArgumentException("CROSS PRODUCT ONLY DEFINED FOR 2-3D")
        }
        val ret = DoubleVectorOld(3)
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

    fun cross(other: IntVectorOld): DoubleVectorOld {
        if (this.size > 3 || other.size > 3 || this.size == 1 || other.size == 1) {
            throw IllegalArgumentException("CROSS PRODUCT ONLY DEFINED FOR 2-3D")
        }
        val ret = DoubleVectorOld(3)
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

    operator fun plus(other: IntVectorOld): DoubleVectorOld =
        this + other.toDoubleVector()

    operator fun minus(other: IntVectorOld): DoubleVectorOld =
        this - other.toDoubleVector()

    operator fun times(other: IntVectorOld): DoubleVectorOld = this * other.toDoubleVector()

    operator fun div(other: IntVectorOld): DoubleVectorOld = this / other.toDoubleVector()

    operator fun rem(other: IntVectorOld): DoubleVectorOld = this % other.toDoubleVector()

    fun pow(other: IntVectorOld): DoubleVectorOld = this.pow(other.toDoubleVector())

    operator fun plus(other: Int): DoubleVectorOld =
        DoubleVectorOld(this.size).mapIndexed { index, _ -> this[index] + other } as DoubleVectorOld

    operator fun plus(other: Double): DoubleVectorOld =
        DoubleVectorOld(this.size).mapIndexed { index, _ -> this[index] + other } as DoubleVectorOld

    operator fun minus(other: Int): DoubleVectorOld =
        DoubleVectorOld(this.size) { i -> this[i] - other }

    operator fun minus(other: Double): DoubleVectorOld =
        DoubleVectorOld(this.size) { i -> this[i] - other }

    operator fun times(other: Int): DoubleVectorOld =
        this * other.toDouble()

    operator fun times(other: Double): DoubleVectorOld =
        DoubleVectorOld(this.size) { i -> this[i] * other }

    operator fun div(other: Int): DoubleVectorOld =
        DoubleVectorOld(this.size) { i -> this[i] / other }

    operator fun div(other: Double): DoubleVectorOld =
        DoubleVectorOld(this.size) { i -> this[i] / other }

    operator fun rem(other: Int): DoubleVectorOld =
        DoubleVectorOld(this.size) { i -> this[i] % other }

    operator fun rem(other: Double): DoubleVectorOld =
        DoubleVectorOld(this.size) { i -> this[i] % other }

    override fun toMatrix(asCol: Boolean): DoubleMatrix = DoubleMatrix(this, asCol)

    override fun toArray(): Array<Double> = this.toList().toTypedArray()

    override fun toIntVector(): IntVectorOld =
        IntVectorOld(this.size) { i -> this[i].toInt() }

    override fun toDoubleVector(): DoubleVectorOld = DoubleVectorOld(this)
}