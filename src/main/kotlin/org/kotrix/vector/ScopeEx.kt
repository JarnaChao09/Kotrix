package org.kotrix.vector

import org.kotrix.utils.Location

/** Vector.Scope<T> **/
infix fun <T: Any> VectorImpl.Scope<T>.append(other: T): VectorImpl.Scope<T> =
    this.also { this.actions.add(VectorImpl.Scope.Append(other)) }

infix fun <T: Any> VectorImpl.Scope<T>.push(other: T): VectorImpl.Scope<T> =
    this.also { this.actions.add(VectorImpl.Scope.Push(other)) }

infix fun <T: Any> VectorImpl.Scope<T>.put(other: Location<T, Int>): VectorImpl.Scope<T> =
    this.also { this.actions.add(VectorImpl.Scope.Put(other.value, other.location)) }

infix fun <T: Any> VectorImpl.Scope<T>.times(t: Int): VectorImpl.Scope<T> =
    this.also {
        when (val last = this.actions.last()) {
            is VectorImpl.Scope.Append -> last.times = t
            is VectorImpl.Scope.Push -> last.times = t
            is VectorImpl.Scope.Put -> last.times = t
        }
    }

fun <T: Any> VectorImpl.Scope<T>.repeat(t: Int, block: VectorImpl.Scope<T>.() -> VectorImpl.Scope<T>): VectorImpl.Scope<T> =
    this.also { this.actions.addAll(VectorImpl.Scope.Repeat(t, block).run().actions) }

fun <T : Any> VectorImpl.Scope<T>.build(): VectorImpl<T> {
    val ret = VectorImpl.nulls<T>(0)
    for(i in this.actions) {
        when(i) {
            is VectorImpl.Scope.Append -> for (u in 0 until i.times) ret.append(i.value)
            is VectorImpl.Scope.Push -> for (u in 0 until i.times) ret.push(i.value)
            is VectorImpl.Scope.Put -> for (u in 0 until i.times) ret.put(i.value, i.at)
            else -> throw VectorImpl.Scope.Error()
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
            else -> throw VectorImpl.Scope.Error()
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
            else -> throw VectorImpl.Scope.Error()
        }
    }
    this.actions.clear()
    return ret
}