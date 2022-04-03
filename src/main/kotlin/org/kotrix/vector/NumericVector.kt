package org.kotrix.vector

inline fun <reified T : Number> numericVectorOf(vararg elements: T): NumericVector<T> {
    require(elements.isNotEmpty()) {
        "cannot create a vector on an empty set of varargs"
    }
    return NumericVector(elements.size) {
        elements[it]
    }
}

inline fun <reified T : Number> mutableNumericVectorOf(vararg elements: T): MutableNumericVector<T> {
    require(elements.isNotEmpty()) {
        "cannot create a vector on an empty set of varargs"
    }
    return MutableNumericVector(elements.size) {
        elements[it]
    }
}

@Suppress("UNCHECKED_CAST", "FunctionName")
inline fun <reified T : Number> NumericVector(size: Int): NumericVector<T> = MutableNumericVector(size)

@Suppress("UNCHECKED_CAST", "FunctionName")
inline fun <reified T : Number> MutableNumericVector(size: Int): MutableNumericVector<T> = (when (T::class) {
    Byte::class -> ByteVector(size)
    Short::class -> ShortVector(size)
    Int::class -> IntVector(size)
    Long::class -> LongVector(size)
    Float::class -> FloatVector(size)
    Double::class -> DoubleVector(size)
    else -> TODO("${T::class.simpleName} Numeric Vector type has not been implemented yet")
} as MutableNumericVector<T>).also {
    require(size > 0) {
        "size must be greater than 0"
    }
}

@Suppress("FunctionName")
inline fun <reified T : Number> NumericVector(size: Int, crossinline init: (index: Int) -> T): NumericVector<T> =
    MutableNumericVector(size, init)

@Suppress("FunctionName", "UNCHECKED_CAST")
inline fun <reified T : Number> MutableNumericVector(
    size: Int,
    crossinline init: (index: Int) -> T
): MutableNumericVector<T> {
    val vector = when (T::class) {
        Byte::class -> ByteVector(size)
        Short::class -> ShortVector(size)
        Int::class -> IntVector(size)
        Long::class -> LongVector(size)
        Float::class -> FloatVector(size)
        Double::class -> DoubleVector(size)
        else -> TODO("${T::class.simpleName} Numeric Vector type has not been implemented yet")
    } as MutableNumericVector<T>
    repeat(size) {
        vector[it] = init(it)
    }
    return vector
}

@Suppress("FunctionName")
inline fun ByteVector(size: Int, crossinline init: (index: Int) -> Byte): NumericVector<Byte> =
    MutableByteVector(size, init)

@Suppress("FunctionName")
inline fun ShortVector(size: Int, crossinline init: (index: Int) -> Short): NumericVector<Short> =
    MutableShortVector(size, init)

@Suppress("FunctionName")
inline fun IntVector(size: Int, crossinline init: (index: Int) -> Int): NumericVector<Int> =
    MutableIntVector(size, init)

@Suppress("FunctionName")
inline fun LongVector(size: Int, crossinline init: (index: Int) -> Long): NumericVector<Long> =
    MutableLongVector(size, init)

@Suppress("FunctionName")
inline fun FloatVector(size: Int, crossinline init: (index: Int) -> Float): NumericVector<Float> =
    MutableFloatVector(size, init)

@Suppress("FunctionName")
inline fun DoubleVector(size: Int, crossinline init: (index: Int) -> Double): NumericVector<Double> =
    MutableDoubleVector(size, init)

@Suppress("FunctionName")
inline fun MutableByteVector(size: Int, crossinline init: (index: Int) -> Byte): MutableNumericVector<Byte> {
    val ret = ByteVector(size)
    repeat(size) {
        ret[it] = init(it)
    }
    return ret
}

@Suppress("FunctionName")
inline fun MutableShortVector(size: Int, crossinline init: (index: Int) -> Short): MutableNumericVector<Short> {
    val ret = ShortVector(size)
    repeat(size) {
        ret[it] = init(it)
    }
    return ret
}

@Suppress("FunctionName")
inline fun MutableIntVector(size: Int, crossinline init: (index: Int) -> Int): MutableNumericVector<Int> {
    val ret = IntVector(size)
    repeat(size) {
        ret[it] = init(it)
    }
    return ret
}

@Suppress("FunctionName")
inline fun MutableLongVector(size: Int, crossinline init: (index: Int) -> Long): MutableNumericVector<Long> {
    val ret = LongVector(size)
    repeat(size) {
        ret[it] = init(it)
    }
    return ret
}

@Suppress("FunctionName")
inline fun MutableFloatVector(size: Int, crossinline init: (index: Int) -> Float): MutableNumericVector<Float> {
    val ret = FloatVector(size)
    repeat(size) {
        ret[it] = init(it)
    }
    return ret
}

@Suppress("FunctionName")
inline fun MutableDoubleVector(size: Int, crossinline init: (index: Int) -> Double): MutableNumericVector<Double> {
    val ret = DoubleVector(size)
    repeat(size) {
        ret[it] = init(it)
    }
    return ret
}

fun ByteArray.asVector(): NumericVector<Byte> = ByteVector(this)
fun ShortArray.asVector(): NumericVector<Short> = ShortVector(this)
fun IntArray.asVector(): NumericVector<Int> = IntVector(this)
fun LongArray.asVector(): NumericVector<Long> = LongVector(this)
fun FloatArray.asVector(): NumericVector<Float> = FloatVector(this)
fun DoubleArray.asVector(): NumericVector<Double> = DoubleVector(this)

fun ByteArray.toVector(): NumericVector<Byte> = ByteVector(this.copyOf())
fun ShortArray.toVector(): NumericVector<Short> = ShortVector(this.copyOf())
fun IntArray.toVector(): NumericVector<Int> = IntVector(this.copyOf())
fun LongArray.toVector(): NumericVector<Long> = LongVector(this.copyOf())
fun FloatArray.toVector(): NumericVector<Float> = FloatVector(this.copyOf())
fun DoubleArray.toVector(): NumericVector<Double> = DoubleVector(this.copyOf())

inline fun <reified T : Number> Collection<T>.toNumericVector(): NumericVector<T> = this.toMutableNumericVector()

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Number> Collection<T>.toMutableNumericVector(): MutableNumericVector<T> = when (T::class) {
    Byte::class -> ByteVector(this as Collection<Byte>)
    Short::class -> ShortVector(this as Collection<Short>)
    Int::class -> IntVector(this as Collection<Int>)
    Long::class -> LongVector(this as Collection<Long>)
    Float::class -> FloatVector(this as Collection<Float>)
    Double::class -> DoubleVector(this as Collection<Double>)
    else -> TODO("${T::class.simpleName} Numeric Vector type has not been implemented yet")
} as MutableNumericVector<T>