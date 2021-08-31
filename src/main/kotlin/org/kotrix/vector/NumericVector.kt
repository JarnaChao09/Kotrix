package org.kotrix.vector

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Number> emptyNumericVector(size: Int): NumericVector<T> = NumericVector(size) {
    0 as T
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Number> emptyMutableNumericVector(size: Int): MutableNumericVector<T> =
    MutableNumericVector(size) {
        0 as T
    }

@Suppress("FunctionName")
inline fun <reified T : Number> NumericVector(size: Int, init: (index: Int) -> T): NumericVector<T> =
    MutableNumericVector(size, init)

@Suppress("FunctionName", "UNCHECKED_CAST")
inline fun <reified T : Number> MutableNumericVector(size: Int, init: (index: Int) -> T): MutableNumericVector<T> {
    val vector = when (T::class) {
        Int::class -> IntVector(size)
        else -> TODO("${T::class.simpleName} Numeric Vector type has not been implemented yet")
    } as MutableNumericVector<T>
    repeat(size) {
        vector[it] = init(it)
    }
    return vector
}