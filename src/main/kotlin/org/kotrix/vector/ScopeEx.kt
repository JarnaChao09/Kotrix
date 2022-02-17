package org.kotrix.vector

import org.kotrix.utils.Location

/** Vector.Scope<T> **/
infix fun <T: Any> VectorImplOld.Scope<T>.append(other: T): VectorImplOld.Scope<T> =
    this.also { this.actions.add(VectorImplOld.Scope.Append(other)) }

infix fun <T: Any> VectorImplOld.Scope<T>.push(other: T): VectorImplOld.Scope<T> =
    this.also { this.actions.add(VectorImplOld.Scope.Push(other)) }

infix fun <T: Any> VectorImplOld.Scope<T>.put(other: Location<T, Int>): VectorImplOld.Scope<T> =
    this.also { this.actions.add(VectorImplOld.Scope.Put(other.value, other.location)) }

infix fun <T: Any> VectorImplOld.Scope<T>.times(t: Int): VectorImplOld.Scope<T> =
    this.also {
        when (val last = this.actions.last()) {
            is VectorImplOld.Scope.Append -> last.times = t
            is VectorImplOld.Scope.Push -> last.times = t
            is VectorImplOld.Scope.Put -> last.times = t
            else -> {}
        }
    }

fun <T: Any> VectorImplOld.Scope<T>.repeat(t: Int, block: VectorImplOld.Scope<T>.() -> VectorImplOld.Scope<T>): VectorImplOld.Scope<T> =
    this.also { this.actions.addAll(VectorImplOld.Scope.Repeat(t, block).run().actions) }

fun <T : Any> VectorImplOld.Scope<T>.build(): VectorImplOld<T> {
    val ret = VectorImplOld.nulls<T>(0)
    for(i in this.actions) {
        when(i) {
            is VectorImplOld.Scope.Append -> for (u in 0 until i.times) ret.append(i.value)
            is VectorImplOld.Scope.Push -> for (u in 0 until i.times) ret.push(i.value)
            is VectorImplOld.Scope.Put -> for (u in 0 until i.times) ret.put(i.value, i.at)
            else -> throw VectorImplOld.Scope.Error()
        }
    }
    this.actions.clear()
    return ret
}

/** IntVector.Scope **/
infix fun IntVectorOld.Scope.put(loc: Location<Int, Int>): IntVectorOld.Scope =
    this.also { this.actions.add(IntVectorOld.Scope.Put(loc.value, loc.location)) }

infix fun IntVectorOld.Scope.append(other: Int): IntVectorOld.Scope =
    this.also { this.actions.add(IntVectorOld.Scope.Append(other)) }

infix fun IntVectorOld.Scope.push(other: Int): IntVectorOld.Scope =
    this.also { this.actions.add(IntVectorOld.Scope.Push(other)) }

infix fun IntVectorOld.Scope.times(t: Int): IntVectorOld.Scope =
    this.also {
        when (val last = this.actions.last()) {
            is IntVectorOld.Scope.Append -> last.times = t
            is IntVectorOld.Scope.Push -> last.times = t
            is IntVectorOld.Scope.Put -> last.times = t
            else -> {}
        }
    }

fun IntVectorOld.Scope.repeat(t: Int, block: IntVectorOld.Scope.() -> IntVectorOld.Scope): IntVectorOld.Scope =
    this.also { this.actions.addAll(IntVectorOld.Scope.Repeat(t, block).run().actions) }

fun IntVectorOld.Scope.build(): IntVectorOld {
    val ret = IntVectorOld(0)
    for(i in this.actions) {
        when(i) {
            is IntVectorOld.Scope.Append -> for (u in 0 until i.times) ret.append(i.value)
            is IntVectorOld.Scope.Push -> for (u in 0 until i.times) ret.push(i.value)
            is IntVectorOld.Scope.Put -> for (u in 0 until i.times) ret.put(i.value, i.at)
            else -> throw VectorImplOld.Scope.Error()
        }
    }
    this.actions.clear()
    return ret
}

/** DoubleVector.Scope **/
infix fun DoubleVectorOld.Scope.put(loc: Location<Double, Int>): DoubleVectorOld.Scope =
    this.also { this.actions.add(DoubleVectorOld.Scope.Put(loc.value, loc.location)) }

infix fun DoubleVectorOld.Scope.append(other: Double): DoubleVectorOld.Scope =
    this.also { this.actions.add(DoubleVectorOld.Scope.Append(other)) }

infix fun DoubleVectorOld.Scope.push(other: Double): DoubleVectorOld.Scope =
    this.also { this.actions.add(DoubleVectorOld.Scope.Push(other)) }

infix fun DoubleVectorOld.Scope.times(t: Int): DoubleVectorOld.Scope =
    this.also {
        when (val last = this.actions.last()) {
            is DoubleVectorOld.Scope.Append -> last.times = t
            is DoubleVectorOld.Scope.Push -> last.times = t
            is DoubleVectorOld.Scope.Put -> last.times = t
            else -> {}
        }
    }

fun DoubleVectorOld.Scope.repeat(t: Int, block: DoubleVectorOld.Scope.() -> DoubleVectorOld.Scope): DoubleVectorOld.Scope =
    this.also { this.actions.addAll(DoubleVectorOld.Scope.Repeat(t, block).run().actions) }

fun DoubleVectorOld.Scope.build(): DoubleVectorOld {
    val ret = DoubleVectorOld(0)
    for(i in this.actions) {
        when(i) {
            is DoubleVectorOld.Scope.Append -> for (u in 0 until i.times) ret.append(i.value)
            is DoubleVectorOld.Scope.Push -> for (u in 0 until i.times) ret.push(i.value)
            is DoubleVectorOld.Scope.Put -> for (u in 0 until i.times) ret.put(i.value, i.at)
            else -> throw VectorImplOld.Scope.Error()
        }
    }
    this.actions.clear()
    return ret
}