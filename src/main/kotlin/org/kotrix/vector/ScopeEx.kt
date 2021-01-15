package org.kotrix.vector

import org.kotrix.utils.Location

/** Vector.Scope<T> **/
infix fun <T: Any> Vector.Scope<T>.append(other: T): Vector.Scope<T> =
    this.also { this.actions.add(Vector.Scope.Append(other)) }

infix fun <T: Any> Vector.Scope<T>.push(other: T): Vector.Scope<T> =
    this.also { this.actions.add(Vector.Scope.Push(other)) }

infix fun <T: Any> Vector.Scope<T>.put(other: Location<T, Int>): Vector.Scope<T> =
    this.also { this.actions.add(Vector.Scope.Put(other.value, other.location)) }

infix fun <T: Any> Vector.Scope<T>.times(t: Int): Vector.Scope<T> =
    this.also {
        when (val last = this.actions.last()) {
            is Vector.Scope.Append -> last.times = t
            is Vector.Scope.Push -> last.times = t
            is Vector.Scope.Put -> last.times = t
        }
    }

fun <T: Any> Vector.Scope<T>.repeat(t: Int, block: Vector.Scope<T>.() -> Vector.Scope<T>): Vector.Scope<T> =
    this.also { this.actions.addAll(Vector.Scope.Repeat(t, block).run().actions) }

fun <T : Any> Vector.Scope<T>.build(): Vector<T> {
    val ret = Vector.nulls<T>(0)
    for(i in this.actions) {
        when(i) {
            is Vector.Scope.Append -> for (u in 0 until i.times) ret.append(i.value)
            is Vector.Scope.Push -> for (u in 0 until i.times) ret.push(i.value)
            is Vector.Scope.Put -> for (u in 0 until i.times) ret.put(i.value, i.at)
            else -> throw Vector.Scope.Error()
        }
    }
    this.actions.clear()
    return ret
}

/** IntVector.Scope **/
infix fun IntVector.Scope.put(loc: Location<Int, Int>): IntVector.Scope =
    this.also { this.actions.add(IntVector.Scope.Put(loc.value, loc.location)) }

infix fun IntVector.Scope.append(other: Int): IntVector.Scope =
    this.also { this.actions.add(IntVector.Scope.Append(other)) }

infix fun IntVector.Scope.push(other: Int): IntVector.Scope =
    this.also { this.actions.add(IntVector.Scope.Push(other)) }

infix fun IntVector.Scope.times(t: Int): IntVector.Scope =
    this.also {
        when (val last = this.actions.last()) {
            is IntVector.Scope.Append -> last.times = t
            is IntVector.Scope.Push -> last.times = t
            is IntVector.Scope.Put -> last.times = t
        }
    }

fun IntVector.Scope.repeat(t: Int, block: IntVector.Scope.() -> IntVector.Scope): IntVector.Scope =
    this.also { this.actions.addAll(IntVector.Scope.Repeat(t, block).run().actions) }

fun IntVector.Scope.build(): IntVector {
    val ret = IntVector(0)
    for(i in this.actions) {
        when(i) {
            is IntVector.Scope.Append -> for (u in 0 until i.times) ret.append(i.value)
            is IntVector.Scope.Push -> for (u in 0 until i.times) ret.push(i.value)
            is IntVector.Scope.Put -> for (u in 0 until i.times) ret.put(i.value, i.at)
            else -> throw Vector.Scope.Error()
        }
    }
    this.actions.clear()
    return ret
}

/** DoubleVector.Scope **/
infix fun DoubleVector.Scope.put(loc: Location<Double, Int>): DoubleVector.Scope =
    this.also { this.actions.add(DoubleVector.Scope.Put(loc.value, loc.location)) }

infix fun DoubleVector.Scope.append(other: Double): DoubleVector.Scope =
    this.also { this.actions.add(DoubleVector.Scope.Append(other)) }

infix fun DoubleVector.Scope.push(other: Double): DoubleVector.Scope =
    this.also { this.actions.add(DoubleVector.Scope.Push(other)) }

infix fun DoubleVector.Scope.times(t: Int): DoubleVector.Scope =
    this.also {
        when (val last = this.actions.last()) {
            is DoubleVector.Scope.Append -> last.times = t
            is DoubleVector.Scope.Push -> last.times = t
            is DoubleVector.Scope.Put -> last.times = t
        }
    }

fun DoubleVector.Scope.repeat(t: Int, block: DoubleVector.Scope.() -> DoubleVector.Scope): DoubleVector.Scope =
    this.also { this.actions.addAll(DoubleVector.Scope.Repeat(t, block).run().actions) }

fun DoubleVector.Scope.build(): DoubleVector {
    val ret = DoubleVector(0)
    for(i in this.actions) {
        when(i) {
            is DoubleVector.Scope.Append -> for (u in 0 until i.times) ret.append(i.value)
            is DoubleVector.Scope.Push -> for (u in 0 until i.times) ret.push(i.value)
            is DoubleVector.Scope.Put -> for (u in 0 until i.times) ret.put(i.value, i.at)
            else -> throw Vector.Scope.Error()
        }
    }
    this.actions.clear()
    return ret
}