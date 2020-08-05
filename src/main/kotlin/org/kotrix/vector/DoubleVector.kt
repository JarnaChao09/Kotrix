package org.kotrix.vector

import org.kotrix.matrix.DoubleMatrix
import kotlin.math.pow
import kotlin.reflect.KClass

class DoubleVector(length: Int = 10, initBlock: (Int) -> Double = { 0.0 }): NumberVector<Double>(length, initBlock) {
    constructor(length: Int = 10, initValue: Double): this(length, initBlock = { initValue })

    constructor(copy: Vector<Double>): this(copy.length, initBlock = { i -> copy[i] })

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
        fun of(vararg elements: Double): DoubleVector =
            DoubleVector(elements.size) { i -> elements[i] }

        val EMPTY: DoubleVector
            get() = DoubleVector(0)
    }

    override val type: KClass<out Double>
        get() = Double::class

    override val list: List<Double>
        get() = super.list

    val array: DoubleArray
        get() = this.toArray().toDoubleArray()

    override fun plus(other: NumberVector<Double>): DoubleVector {
        val ret = DoubleVector(this.length)
        for (i in 0 until ret.length) {
            ret[i] = this[i] + other[i]
        }
        return ret
    }

    override fun minus(other: NumberVector<Double>): DoubleVector {
        val ret = DoubleVector(this.length)
        for (i in 0 until ret.length) {
            ret[i] = this[i] - other[i]
        }
        return ret
    }

    override fun times(other: NumberVector<Double>): DoubleVector {
        val ret = DoubleVector(this.length)
        for (i in 0 until ret.length) {
            ret[i] = this[i] * other[i]
        }
        return ret
    }

    override fun div(other: NumberVector<Double>): DoubleVector {
        val ret = DoubleVector(this.length)
        for (i in 0 until ret.length) {
            ret[i] = this[i] / other[i]
        }
        return ret
    }

    override fun rem(other: NumberVector<Double>): DoubleVector {
        val ret = DoubleVector(this.length)
        for (i in 0 until ret.length) {
            ret[i] = this[i] % other[i]
        }
        return ret
    }

    override infix fun pow(other: NumberVector<Double>): DoubleVector {
        val ret = DoubleVector(this.length)
        for (i in 0 until ret.length) {
            ret[i] = this[i].pow(other[i])
        }
        return ret
    }

    override fun plusAssign(other: NumberVector<Double>) {
        for (i in 0 until this.length) {
            this[i] += other[i]
        }
    }

    override fun minusAssign(other: NumberVector<Double>) {
        for (i in 0 until this.length) {
            this[i] -= other[i]
        }
    }

    override fun timesAssign(other: NumberVector<Double>) {
        for (i in 0 until this.length) {
            this[i] *= other[i]
        }
    }

    override fun divAssign(other: NumberVector<Double>) {
        for (i in 0 until this.length) {
            this[i] /= other[i]
        }
    }

    override fun remAssign(other: NumberVector<Double>) {
        for (i in 0 until this.length) {
            this[i] %= other[i]
        }
    }

    override fun powAssign(other: NumberVector<Double>) {
        for (i in 0 until this.length) {
            this[i] = this[i].pow(other[i])
        }
    }

    override fun unaryPlus(): DoubleVector =
        DoubleVector(this.length) { i -> +(this[i]) }

    override fun unaryMinus(): DoubleVector =
        DoubleVector(this.length) { i -> -(this[i]) }

    override fun dot(other: NumberVector<Double>): Double {
        return DoubleVector(this.length).mapIndexed { _, index: Int -> this[index] * other[index] }.sum()
    }

    override fun cross(other: NumberVector<Double>): DoubleVector {
        if (this.length > 3 || other.length > 3 || this.length == 1 || other.length == 1) {
            throw IllegalArgumentException("CROSS PRODUCT ONLY DEFINED FOR 2-3D")
        }
        val ret = DoubleVector(3)
        if (this.length == 2 && other.length == 2) {
            ret[0] = this[0] * other[1] - this[1] * other[0]
        }
        if (this.length == 3 && other.length == 3) {
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

    infix fun pow(other: IntVector): DoubleVector = this pow other.toDoubleVector()

    operator fun plus(other: Int): DoubleVector =
        DoubleVector(this.length).mapIndexed { _, index -> this[index] + other }

    operator fun plus(other: Double): DoubleVector =
        DoubleVector(this.length).mapIndexed { _, index -> this[index] + other }

    operator fun minus(other: Int): DoubleVector =
        DoubleVector(this.length) { i -> this[i] - other }

    operator fun minus(other: Double): DoubleVector =
        DoubleVector(this.length) { i -> this[i] - other }

    operator fun times(other: Int): DoubleVector =
        this * other.toDouble()

    operator fun times(other: Double): DoubleVector =
        DoubleVector(this.length) { i -> this[i] * other }

    operator fun div(other: Int): DoubleVector =
        DoubleVector(this.length) { i -> this[i] / other }

    operator fun div(other: Double): DoubleVector =
        DoubleVector(this.length) { i -> this[i] / other }

    operator fun rem(other: Int): DoubleVector =
        DoubleVector(this.length) { i -> this[i] % other }

    operator fun rem(other: Double): DoubleVector =
        DoubleVector(this.length) { i -> this[i] % other }

    override fun forEach(action: (Double) -> Unit) {
        for (i in this) {
            action(i)
        }
    }

    override fun forEachIndexed(action: (Double, index: Int) -> Unit) {
        var index = 0
        for (i in this) {
            action(i, index++)
        }
    }

    override fun map(action: (Double) -> Double): DoubleVector {
        val ret = DoubleVector(this.length)
        var index = 0
        for (i in this) {
            ret[index++] = action(i)
        }
        return ret
    }

    override fun mapIndexed(action: (Double, index: Int) -> Double): DoubleVector {
        val ret = DoubleVector(this.length)
        var index = 0
        for (i in this) {
            ret[index] = action(i, index++)
        }
        return ret
    }

    override fun toMatrix(asCol: Boolean): DoubleMatrix = DoubleMatrix(this, asCol)

    override fun toArray(): Array<Double> = this.toList().toTypedArray()

    override fun toIntVector(): IntVector =
        IntVector(this.length) { i -> this[i].toInt() }

    override fun toDoubleVector(): DoubleVector = DoubleVector(this)
}