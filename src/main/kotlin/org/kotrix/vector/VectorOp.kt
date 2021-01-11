package org.kotrix.vector

import kotlin.math.pow

/** Addition **/
operator fun Int.plus(other: IntVector): IntVector =
    other + this

operator fun Int.plus(other: DoubleVector): DoubleVector =
    other + this

operator fun Double.plus(other: DoubleVector): DoubleVector =
    other + this

operator fun Double.plus(other: IntVector): DoubleVector =
    other + this

/** Subtraction **/
operator fun Int.minus(other: IntVector): IntVector =
    -other + this

operator fun Int.minus(other: DoubleVector): DoubleVector =
    -other + this

operator fun Double.minus(other: IntVector): DoubleVector =
    this - other.toDoubleVector()

operator fun Double.minus(other: DoubleVector): DoubleVector =
    -other + this

/** Multiplication **/
operator fun Int.times(other: IntVector): IntVector =
    other * this

operator fun Int.times(other: DoubleVector): DoubleVector =
    other * this

operator fun Double.times(other: IntVector): DoubleVector =
    other * this

operator fun Double.times(other: DoubleVector): DoubleVector =
    other * this

/** Division **/
operator fun Int.div(other: IntVector): IntVector =
    (this / other.toDoubleVector()).toIntVector()

operator fun Int.div(other: DoubleVector): DoubleVector =
    DoubleVector(other.size) { i -> this / other[i] }

operator fun Double.div(other: IntVector): DoubleVector =
    this / other.toDoubleVector()

operator fun Double.div(other: DoubleVector): DoubleVector =
    DoubleVector(other.size).mapIndexed { index, _ -> this / other[index] } as DoubleVector

/** Remainder **/
operator fun Int.rem(other: IntVector): IntVector =
    IntVector(other.size).mapIndexed { index, _ -> this % other[index] } as IntVector

operator fun Int.rem(other: DoubleVector): DoubleVector =
    DoubleVector(other.size).mapIndexed { index, _ -> this % other[index] } as DoubleVector

operator fun Double.rem(other: IntVector): DoubleVector =
    DoubleVector(other.size).mapIndexed { index, _ -> this % other[index] } as DoubleVector

operator fun Double.rem(other: DoubleVector): DoubleVector =
    DoubleVector(other.size).mapIndexed { index, _ -> this % other[index] } as DoubleVector

/** Power **/
infix fun Int.pow(other: IntVector): IntVector =
    IntVector(other.size) { this } pow other

infix fun Int.pow(other: DoubleVector): DoubleVector =
    DoubleVector(other.size) { this.toDouble() } pow other

infix fun Double.pow(other: IntVector): DoubleVector =
    DoubleVector(other.size) { this } pow other.toDoubleVector()

infix fun Double.pow(other: DoubleVector): DoubleVector =
    DoubleVector(other.size) { this } pow other

infix fun IntVector.pow(other: Int): IntVector =
    IntVector(this.size) { i -> (this[i].toDouble()).pow(other).toInt() }

infix fun IntVector.pow(other: Double): DoubleVector =
    DoubleVector(this.size) { i -> (this[i].toDouble()).pow(other) }

infix fun DoubleVector.pow(other: Int): DoubleVector =
    DoubleVector(this.size) { i -> this[i].pow(other) }

infix fun DoubleVector.pow(other: Double): DoubleVector =
    DoubleVector(this.size) { i -> this[i].pow(other) }

/** List Conversion **/
inline fun <reified T> List<T>.toVector(): Vector<T> where T: Any {
    val ret = Vector.nulls<T>(this.size)
    for (i in 0 until ret.size) {
        ret[i] = this[i]
    }
    return ret
}

object VectorOp {
    /** Addition **/
    @JvmStatic
    @JvmName(name = "intVecAddInt")
    fun ivai(vec: IntVector, int: Int): IntVector = vec + int

    @JvmStatic
    @JvmName(name = "intVecAddDouble")
    fun ivad(vec: IntVector, double: Double): DoubleVector = vec + double

    @JvmStatic
    @JvmName(name = "doubleVecAddInt")
    fun dvai(vec: DoubleVector, int: Int): DoubleVector = vec + int

    @JvmStatic
    @JvmName(name = "doubleVecAddDouble")
    fun dvad(vec: DoubleVector, double: Double): DoubleVector = vec + double

    /** Subtraction **/
    @JvmStatic
    @JvmName(name = "intVecSubInt")
    fun ivsi(vec: IntVector, int: Int): IntVector = vec - int

    @JvmStatic
    @JvmName(name = "intSubIntVec")
    fun isiv(int: Int, vec: IntVector): IntVector = int - vec

    @JvmStatic
    @JvmName(name = "intVecSubDouble")
    fun ivsd(vec: IntVector, double: Double): DoubleVector = vec - double

    @JvmStatic
    @JvmName(name = "doubleSubIntVec")
    fun dsiv(double: Double, vec: IntVector): DoubleVector = double - vec

    @JvmStatic
    @JvmName(name = "doubleVecSubInt")
    fun dvsi(vec: DoubleVector, int: Int): DoubleVector = vec - int

    @JvmStatic
    @JvmName(name = "intSubDoubleVec")
    fun isdv(int: Int, vec: DoubleVector): DoubleVector = int - vec

    @JvmStatic
    @JvmName(name = "doubleVecSubDouble")
    fun dvsd(vec: DoubleVector, double: Double): DoubleVector = vec - double

    @JvmStatic
    @JvmName(name = "doubleSubDoubleVec")
    fun dsdv(double: Double, vec: DoubleVector): DoubleVector = double - vec

    /** Multiplication **/
    @JvmStatic
    @JvmName(name = "intVecMultInt")
    fun ivmi(vec: IntVector, int: Int): IntVector = vec * int

    @JvmStatic
    @JvmName(name = "intVecMultDouble")
    fun ivmd(vec: IntVector, double: Double): DoubleVector = vec * double

    @JvmStatic
    @JvmName(name = "doubleVecMultInt")
    fun dvmi(vec: DoubleVector, int: Int): DoubleVector = vec * int

    @JvmStatic
    @JvmName(name = "doubleVecMultDouble")
    fun dvmd(vec: DoubleVector, double: Double): DoubleVector = vec * double

    /** Division **/
    @JvmStatic
    @JvmName(name = "intVecDivInt")
    fun ivdi(vec: IntVector, int: Int): IntVector = vec / int

    @JvmStatic
    @JvmName(name = "intDivIntVec")
    fun idiv(int: Int, vec: IntVector): IntVector = int / vec

    @JvmStatic
    @JvmName(name = "intVecDivDouble")
    fun ivdd(vec: IntVector, double: Double): DoubleVector = vec / double

    @JvmStatic
    @JvmName(name = "doubleDivIntVec")
    fun ddiv(double: Double, vec: IntVector): DoubleVector = double / vec

    @JvmStatic
    @JvmName(name = "doubleVecDivInt")
    fun dvdi(vec: DoubleVector, int: Int): DoubleVector = vec / int

    @JvmStatic
    @JvmName(name = "intDivDoubleVec")
    fun iddv(int: Int, vec: DoubleVector): DoubleVector = int / vec

    @JvmStatic
    @JvmName(name = "doubleVecDivDouble")
    fun dvdd(vec: DoubleVector, double: Double): DoubleVector = vec / double

    @JvmStatic
    @JvmName(name = "doubleDivDoubleVec")
    fun dddv(double: Double, vec: DoubleVector): DoubleVector = double / vec

    /** Remainder **/
    @JvmStatic
    @JvmName(name = "intVecRemInt")
    fun ivri(vec: IntVector, int: Int): IntVector = vec % int

    @JvmStatic
    @JvmName(name = "intRemIntVec")
    fun iriv(int: Int, vec: IntVector): IntVector = int % vec

    @JvmStatic
    @JvmName(name = "intVecRemDouble")
    fun ivrd(vec: IntVector, double: Double): DoubleVector = vec % double

    @JvmStatic
    @JvmName(name = "doubleRemIntVec")
    fun driv(double: Double, vec: IntVector): DoubleVector = double % vec

    @JvmStatic
    @JvmName(name = "doubleVecRemInt")
    fun dvri(vec: DoubleVector, int: Int): DoubleVector = vec % int

    @JvmStatic
    @JvmName(name = "intRemDoubleVec")
    fun irdv(int: Int, vec: DoubleVector): DoubleVector = int % vec

    @JvmStatic
    @JvmName(name = "doubleVecRemDouble")
    fun dvrd(vec: DoubleVector, double: Double): DoubleVector = vec % double

    @JvmStatic
    @JvmName(name = "doubleRemDoubleVec")
    fun drdv(double: Double, vec: DoubleVector): DoubleVector = double % vec

    /** Power **/
    @JvmStatic
    @JvmName(name = "intVecPowInt")
    fun ivpi(vec: IntVector, int: Int): IntVector = vec pow int

    @JvmStatic
    @JvmName(name = "intPowIntVec")
    fun ipiv(int: Int, vec: IntVector): IntVector = int pow vec

    @JvmStatic
    @JvmName(name = "intVecPowDouble")
    fun ivpd(vec: IntVector, double: Double): DoubleVector = vec pow double

    @JvmStatic
    @JvmName(name = "doublePowIntVec")
    fun dpiv(double: Double, vec: IntVector): DoubleVector = double pow vec

    @JvmStatic
    @JvmName(name = "doubleVecPowInt")
    fun dvpi(vec: DoubleVector, int: Int): DoubleVector = vec pow int

    @JvmStatic
    @JvmName(name = "intPowDoubleVec")
    fun ipdv(int: Int, vec: DoubleVector): DoubleVector = int pow vec

    @JvmStatic
    @JvmName(name = "doubleVecPowDouble")
    fun dvpd(vec: DoubleVector, double: Double): DoubleVector = vec pow double

    @JvmStatic
    @JvmName(name = "doublePowDoubleVec")
    fun dpdv(double: Double, vec: DoubleVector): DoubleVector = double pow vec
}