package org.kotrix.vector

inline fun <reified T: Any> vector(actions: Vector.Scope<T>.() -> Vector.Scope<T>): Vector<T> =
    Vector.Scope.Base<T>().actions().build()

fun intVector(actions: IntVector.Scope.() -> IntVector.Scope): IntVector =
    IntVector(IntVector.Scope.Base().actions().build())

fun doubleVector(actions: DoubleVector.Scope.() -> DoubleVector.Scope): DoubleVector =
    DoubleVector(DoubleVector.Scope.Base().actions().build())
