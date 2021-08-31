package org.kotrix.vector

import kotlin.math.pow

/** Addition **/
operator fun Int.plus(other: IntVectorOld): IntVectorOld =
    other + this

operator fun Int.plus(other: DoubleVectorOld): DoubleVectorOld =
    other + this

operator fun Double.plus(other: DoubleVectorOld): DoubleVectorOld =
    other + this

operator fun Double.plus(other: IntVectorOld): DoubleVectorOld =
    other + this

/** Subtraction **/
operator fun Int.minus(other: IntVectorOld): IntVectorOld =
    -other + this

operator fun Int.minus(other: DoubleVectorOld): DoubleVectorOld =
    -other + this

operator fun Double.minus(other: IntVectorOld): DoubleVectorOld =
    this - other.toDoubleVector()

operator fun Double.minus(other: DoubleVectorOld): DoubleVectorOld =
    -other + this

/** Multiplication **/
operator fun Int.times(other: IntVectorOld): IntVectorOld =
    other * this

operator fun Int.times(other: DoubleVectorOld): DoubleVectorOld =
    other * this

operator fun Double.times(other: IntVectorOld): DoubleVectorOld =
    other * this

operator fun Double.times(other: DoubleVectorOld): DoubleVectorOld =
    other * this

/** Division **/
operator fun Int.div(other: IntVectorOld): IntVectorOld =
    (this / other.toDoubleVector()).toIntVector()

operator fun Int.div(other: DoubleVectorOld): DoubleVectorOld =
    DoubleVectorOld(other.size) { i -> this / other[i] }

operator fun Double.div(other: IntVectorOld): DoubleVectorOld =
    this / other.toDoubleVector()

operator fun Double.div(other: DoubleVectorOld): DoubleVectorOld =
    DoubleVectorOld(other.size).mapIndexed { index, _ -> this / other[index] } as DoubleVectorOld

/** Remainder **/
operator fun Int.rem(other: IntVectorOld): IntVectorOld =
    IntVectorOld(other.size).mapIndexed { index, _ -> this % other[index] } as IntVectorOld

operator fun Int.rem(other: DoubleVectorOld): DoubleVectorOld =
    DoubleVectorOld(other.size).mapIndexed { index, _ -> this % other[index] } as DoubleVectorOld

operator fun Double.rem(other: IntVectorOld): DoubleVectorOld =
    DoubleVectorOld(other.size).mapIndexed { index, _ -> this % other[index] } as DoubleVectorOld

operator fun Double.rem(other: DoubleVectorOld): DoubleVectorOld =
    DoubleVectorOld(other.size).mapIndexed { index, _ -> this % other[index] } as DoubleVectorOld

/** Power **/
infix fun Int.pow(other: IntVectorOld): IntVectorOld =
    IntVectorOld(other.size) { this } pow other

infix fun Int.pow(other: DoubleVectorOld): DoubleVectorOld =
    DoubleVectorOld(other.size) { this.toDouble() } pow other

infix fun Double.pow(other: IntVectorOld): DoubleVectorOld =
    DoubleVectorOld(other.size) { this } pow other.toDoubleVector()

infix fun Double.pow(other: DoubleVectorOld): DoubleVectorOld =
    DoubleVectorOld(other.size) { this } pow other

infix fun IntVectorOld.pow(other: Int): IntVectorOld =
    IntVectorOld(this.size) { i -> (this[i].toDouble()).pow(other).toInt() }

infix fun IntVectorOld.pow(other: Double): DoubleVectorOld =
    DoubleVectorOld(this.size) { i -> (this[i].toDouble()).pow(other) }

infix fun DoubleVectorOld.pow(other: Int): DoubleVectorOld =
    DoubleVectorOld(this.size) { i -> this[i].pow(other) }

infix fun DoubleVectorOld.pow(other: Double): DoubleVectorOld =
    DoubleVectorOld(this.size) { i -> this[i].pow(other) }

/** List Conversion **/
inline fun <reified T> List<T>.toVectorOld(): VectorImplOld<T> where T: Any {
    val ret = VectorImplOld.nulls<T>(this.size)
    for (i in 0 until ret.size) {
        ret[i] = this[i]
    }
    return ret
}

object VectorOp {
    /** Addition **/
    @JvmStatic
    @JvmName(name = "intVecAddInt")
    fun ivai(vec: IntVectorOld, int: Int): IntVectorOld = vec + int

    @JvmStatic
    @JvmName(name = "intVecAddDouble")
    fun ivad(vec: IntVectorOld, double: Double): DoubleVectorOld = vec + double

    @JvmStatic
    @JvmName(name = "doubleVecAddInt")
    fun dvai(vec: DoubleVectorOld, int: Int): DoubleVectorOld = vec + int

    @JvmStatic
    @JvmName(name = "doubleVecAddDouble")
    fun dvad(vec: DoubleVectorOld, double: Double): DoubleVectorOld = vec + double

    /** Subtraction **/
    @JvmStatic
    @JvmName(name = "intVecSubInt")
    fun ivsi(vec: IntVectorOld, int: Int): IntVectorOld = vec - int

    @JvmStatic
    @JvmName(name = "intSubIntVec")
    fun isiv(int: Int, vec: IntVectorOld): IntVectorOld = int - vec

    @JvmStatic
    @JvmName(name = "intVecSubDouble")
    fun ivsd(vec: IntVectorOld, double: Double): DoubleVectorOld = vec - double

    @JvmStatic
    @JvmName(name = "doubleSubIntVec")
    fun dsiv(double: Double, vec: IntVectorOld): DoubleVectorOld = double - vec

    @JvmStatic
    @JvmName(name = "doubleVecSubInt")
    fun dvsi(vec: DoubleVectorOld, int: Int): DoubleVectorOld = vec - int

    @JvmStatic
    @JvmName(name = "intSubDoubleVec")
    fun isdv(int: Int, vec: DoubleVectorOld): DoubleVectorOld = int - vec

    @JvmStatic
    @JvmName(name = "doubleVecSubDouble")
    fun dvsd(vec: DoubleVectorOld, double: Double): DoubleVectorOld = vec - double

    @JvmStatic
    @JvmName(name = "doubleSubDoubleVec")
    fun dsdv(double: Double, vec: DoubleVectorOld): DoubleVectorOld = double - vec

    /** Multiplication **/
    @JvmStatic
    @JvmName(name = "intVecMultInt")
    fun ivmi(vec: IntVectorOld, int: Int): IntVectorOld = vec * int

    @JvmStatic
    @JvmName(name = "intVecMultDouble")
    fun ivmd(vec: IntVectorOld, double: Double): DoubleVectorOld = vec * double

    @JvmStatic
    @JvmName(name = "doubleVecMultInt")
    fun dvmi(vec: DoubleVectorOld, int: Int): DoubleVectorOld = vec * int

    @JvmStatic
    @JvmName(name = "doubleVecMultDouble")
    fun dvmd(vec: DoubleVectorOld, double: Double): DoubleVectorOld = vec * double

    /** Division **/
    @JvmStatic
    @JvmName(name = "intVecDivInt")
    fun ivdi(vec: IntVectorOld, int: Int): IntVectorOld = vec / int

    @JvmStatic
    @JvmName(name = "intDivIntVec")
    fun idiv(int: Int, vec: IntVectorOld): IntVectorOld = int / vec

    @JvmStatic
    @JvmName(name = "intVecDivDouble")
    fun ivdd(vec: IntVectorOld, double: Double): DoubleVectorOld = vec / double

    @JvmStatic
    @JvmName(name = "doubleDivIntVec")
    fun ddiv(double: Double, vec: IntVectorOld): DoubleVectorOld = double / vec

    @JvmStatic
    @JvmName(name = "doubleVecDivInt")
    fun dvdi(vec: DoubleVectorOld, int: Int): DoubleVectorOld = vec / int

    @JvmStatic
    @JvmName(name = "intDivDoubleVec")
    fun iddv(int: Int, vec: DoubleVectorOld): DoubleVectorOld = int / vec

    @JvmStatic
    @JvmName(name = "doubleVecDivDouble")
    fun dvdd(vec: DoubleVectorOld, double: Double): DoubleVectorOld = vec / double

    @JvmStatic
    @JvmName(name = "doubleDivDoubleVec")
    fun dddv(double: Double, vec: DoubleVectorOld): DoubleVectorOld = double / vec

    /** Remainder **/
    @JvmStatic
    @JvmName(name = "intVecRemInt")
    fun ivri(vec: IntVectorOld, int: Int): IntVectorOld = vec % int

    @JvmStatic
    @JvmName(name = "intRemIntVec")
    fun iriv(int: Int, vec: IntVectorOld): IntVectorOld = int % vec

    @JvmStatic
    @JvmName(name = "intVecRemDouble")
    fun ivrd(vec: IntVectorOld, double: Double): DoubleVectorOld = vec % double

    @JvmStatic
    @JvmName(name = "doubleRemIntVec")
    fun driv(double: Double, vec: IntVectorOld): DoubleVectorOld = double % vec

    @JvmStatic
    @JvmName(name = "doubleVecRemInt")
    fun dvri(vec: DoubleVectorOld, int: Int): DoubleVectorOld = vec % int

    @JvmStatic
    @JvmName(name = "intRemDoubleVec")
    fun irdv(int: Int, vec: DoubleVectorOld): DoubleVectorOld = int % vec

    @JvmStatic
    @JvmName(name = "doubleVecRemDouble")
    fun dvrd(vec: DoubleVectorOld, double: Double): DoubleVectorOld = vec % double

    @JvmStatic
    @JvmName(name = "doubleRemDoubleVec")
    fun drdv(double: Double, vec: DoubleVectorOld): DoubleVectorOld = double % vec

    /** Power **/
    @JvmStatic
    @JvmName(name = "intVecPowInt")
    fun ivpi(vec: IntVectorOld, int: Int): IntVectorOld = vec pow int

    @JvmStatic
    @JvmName(name = "intPowIntVec")
    fun ipiv(int: Int, vec: IntVectorOld): IntVectorOld = int pow vec

    @JvmStatic
    @JvmName(name = "intVecPowDouble")
    fun ivpd(vec: IntVectorOld, double: Double): DoubleVectorOld = vec pow double

    @JvmStatic
    @JvmName(name = "doublePowIntVec")
    fun dpiv(double: Double, vec: IntVectorOld): DoubleVectorOld = double pow vec

    @JvmStatic
    @JvmName(name = "doubleVecPowInt")
    fun dvpi(vec: DoubleVectorOld, int: Int): DoubleVectorOld = vec pow int

    @JvmStatic
    @JvmName(name = "intPowDoubleVec")
    fun ipdv(int: Int, vec: DoubleVectorOld): DoubleVectorOld = int pow vec

    @JvmStatic
    @JvmName(name = "doubleVecPowDouble")
    fun dvpd(vec: DoubleVectorOld, double: Double): DoubleVectorOld = vec pow double

    @JvmStatic
    @JvmName(name = "doublePowDoubleVec")
    fun dpdv(double: Double, vec: DoubleVectorOld): DoubleVectorOld = double pow vec
}