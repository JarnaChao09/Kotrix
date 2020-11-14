package org.kotrix.vector

fun <T: Any> Vector<T>.forEach(action: (T) -> Unit) {
    for (i in this) {
        action(i)
    }
}

fun <T: Any> Vector<T>.forEachIndexed(action: (index: Int, T) -> Unit) {
    for ((index, i) in this.withIndex()) {
        action(index, i)
    }
}

fun <T: Any> Vector<T>.map(action: (T) -> T): Vector<T> {
    val ret = Vector.nulls<T>()
    for (i in this) {
        ret.append(action(i))
    }
    return ret
}

fun <T: Any> Vector<T>.mapIndexed(action: (index: Int, T) -> T): Vector<T> {
    val ret = Vector.nulls<T>()
    for ((index, i) in this.withIndex()) {
        ret.append(action(index, i))
    }
    return ret
}

fun <T: Any> Collection<T>.toVector() = Vector(this.toList())

fun Collection<Int>.toIntVector() = IntVector(this.toList())

fun Collection<Double>.toDoubleVector() = DoubleVector(this.toList())

inline fun <reified T: Any> vector(actions: Vector.Scope<T>.() -> Vector.Scope<T>): Vector<T> =
    Vector.Scope.Base<T>().actions().build()

fun intVector(actions: IntVector.Scope.() -> IntVector.Scope): IntVector =
    IntVector(IntVector.Scope.Base().actions().build())

fun doubleVector(actions: DoubleVector.Scope.() -> DoubleVector.Scope): DoubleVector =
    DoubleVector(DoubleVector.Scope.Base().actions().build())
