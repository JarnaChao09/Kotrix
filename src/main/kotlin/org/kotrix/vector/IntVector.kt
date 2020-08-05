package org.kotrix.vector

import org.kotrix.matrix.IntMatrix
import kotlin.math.pow
import kotlin.reflect.KClass

class IntVector(length: Int = 10, initBlock: (Int) -> Int = { 0 }): NumberVector<Int>(length, initBlock) {
    constructor(length: Int = 10, initValue: Int): this(length, initBlock = { initValue })

    constructor(copy: Vector<Int>): this(copy.length, initBlock = { i -> copy[i] })

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
        fun of(vararg elements: Int): IntVector =
            IntVector(elements.size) { i -> elements[i] }

        val EMPTY: IntVector
            get() = IntVector(0)
    }

    override val type: KClass<out Int>
        get() = super.type

    override val list: List<Int>
        get() = super.list

    val array: IntArray
        get() = this.toArray().toIntArray()

    override fun plus(other: NumberVector<Int>): IntVector {
        val ret = IntVector(this.length)
        for (i in 0 until ret.length) {
            ret[i] = this[i] + other[i]
        }
        return ret
    }

    override fun minus(other: NumberVector<Int>): IntVector {
        val ret = IntVector(this.length)
        for (i in 0 until ret.length) {
            ret[i] = this[i] - other[i]
        }
        return ret
    }

    override fun times(other: NumberVector<Int>): IntVector {
        val ret = IntVector(this.length)
        for (i in 0 until ret.length) {
            ret[i] = this[i] * other[i]
        }
        return ret
    }

    override fun div(other: NumberVector<Int>): IntVector {
        val ret = IntVector(this.length)
        for (i in 0 until ret.length) {
            ret[i] = this[i] / other[i]
        }
        return ret
    }

    override fun rem(other: NumberVector<Int>): IntVector {
        val ret = IntVector(this.length)
        for (i in 0 until ret.length) {
            ret[i] = this[i] % other[i]
        }
        return ret
    }

    override infix fun pow(other: NumberVector<Int>): IntVector {
        val ret = IntVector(this.length)
        for (i in 0 until ret.length) {
            ret[i] = this[i].toDouble().pow(other[i].toDouble()).toInt()
        }
        return ret
    }

    override fun plusAssign(other: NumberVector<Int>) {
        for (i in 0 until this.length) {
            this[i] += other[i]
        }
    }

    override fun minusAssign(other: NumberVector<Int>) {
        for (i in 0 until this.length) {
            this[i] -= other[i]
        }
    }

    override fun timesAssign(other: NumberVector<Int>) {
        for (i in 0 until this.length) {
            this[i] *= other[i]
        }
    }

    override fun divAssign(other: NumberVector<Int>) {
        for (i in 0 until this.length) {
            this[i] /= other[i]
        }
    }

    override fun remAssign(other: NumberVector<Int>) {
        for (i in 0 until this.length) {
            this[i] %= other[i]
        }
    }

    override infix fun powAssign(other: NumberVector<Int>) {
        for (i in 0 until this.length) {
            this[i] = this[i].toDouble().pow(other[i]).toInt()
        }
    }

    override fun unaryPlus(): IntVector =
        IntVector(this.length) { i -> +(this[i]) }

    override fun unaryMinus(): IntVector =
        IntVector(this.length) { i -> -(this[i]) }

    override infix fun dot(other: NumberVector<Int>): Int {
        return IntVector(this.length).mapIndexed { _, index: Int -> this[index] * other[index] }.sum()
    }

    override infix fun cross(other: NumberVector<Int>): IntVector {
        if (this.length > 3 || other.length > 3 || this.length == 1 || other.length == 1) {
            throw IllegalArgumentException("CROSS PRODUCT ONLY DEFINED FOR 2-3D")
        }
        val ret = IntVector(3)
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

    operator fun plus(other: DoubleVector): DoubleVector =
        this.toDoubleVector() + other

    operator fun minus(other: DoubleVector): DoubleVector =
        this.toDoubleVector() - other

    operator fun times(other: DoubleVector): DoubleVector = this.toDoubleVector() * other

    operator fun div(other: DoubleVector): DoubleVector = this.toDoubleVector() / other

    operator fun rem(other: DoubleVector): DoubleVector = this.toDoubleVector() % other

    infix fun pow(other: DoubleVector): DoubleVector = this.toDoubleVector() pow other

    operator fun plus(other: Int): IntVector =
        IntVector(this.length).mapIndexed { index, _ -> this[index] + other }

    operator fun plus(other: Double): DoubleVector =
        DoubleVector(this.length).mapIndexed { _, index -> this[index] + other }

    operator fun minus(other: Int): IntVector =
        IntVector(this.length).mapIndexed { index, _ -> this[index] - other }

    operator fun minus(other: Double): DoubleVector =
        DoubleVector(this.length).mapIndexed { _, index -> this[index] - other }

    operator fun times(other: Int): IntVector =
        IntVector(this.length) { i -> this[i] * other }

    operator fun times(other: Double): DoubleVector =
        DoubleVector(this.length) { i -> this[i] * other }

    operator fun div(other: Int): IntVector =
        IntVector(this.length) { i -> this[i] / other }

    operator fun div(other: Double): DoubleVector =
        DoubleVector(this.length) { i -> this[i] / other }

    operator fun rem(other: Int): IntVector =
        IntVector(this.length) { i -> this[i] % other }

    operator fun rem(other: Double): DoubleVector =
        DoubleVector(this.length) { i -> this[i] % other }

    override fun forEach(action: (Int) -> Unit) {
        for (i in this) {
            action(i)
        }
    }

    override fun forEachIndexed(action: (Int, index: Int) -> Unit) {
        var index = 0
        for (i in this) {
            action(i, index++)
        }
    }

    override fun map(action: (Int) -> Int): IntVector {
        val ret = IntVector(this.length)
        var index = 0
        for (i in this) {
            ret[index++] = action(i)
        }
        return ret
    }

    override fun mapIndexed(action: (Int, index: Int) -> Int): IntVector {
        val ret = IntVector(this.length)
        var index = 0
        for (i in this) {
            ret[index] = action(i, index++)
        }
        return ret
    }

    override fun toMatrix(asCol: Boolean): IntMatrix = IntMatrix(this, asCol)

    override fun toArray(): Array<Int> = this.toList().toTypedArray()

    override fun toIntVector(): IntVector = IntVector(this)

    override fun toDoubleVector(): DoubleVector =
        DoubleVector(this.length) { i -> this[i].toDouble() }
}