package org.kotrix.matrix

inline fun intMatrix(actions: IntMatrix.Scope.() -> IntMatrix.Scope): IntMatrix =
    IntMatrix(IntMatrix.Scope.Base().actions().matrix)

inline fun doubleMatrix(actions: DoubleMatrix.Scope.() -> DoubleMatrix.Scope): DoubleMatrix =
    DoubleMatrix(DoubleMatrix.Scope.Base().actions().matrix)

fun IntMatrix.map(which: Selector, action: (Int) -> Int): IntMatrix {
    val mat = IntMatrix(this.shape)
    for ((v, r, c) in this.withIndices) {
        mat[r, c] = if (which.check(r, c)) action(v) else v
    }
    return mat
}

fun IntMatrix.mapIndexed(which: Selector, action: (Int, ri: Int, ci: Int) -> Int): IntMatrix {
    val mat = IntMatrix(this.shape)
    for ((v, r, c) in this.withIndices) {
        mat[r, c] = if (which.check(r, c)) action(v, r, c) else v
    }
    return mat
}

fun IntMatrix.replace(only: Selector, replacement: Int, check: (Int) -> Boolean): IntMatrix {
    val mat = IntMatrix(this.shape)
    for ((v, r, c) in this.withIndices) {
        mat[r, c] = if (only.check(r, c) && check(v)) replacement else v
    }
    return mat
}

fun IntMatrix.replaceNot(only: Selector, replacement: Int, check: (Int) -> Boolean): IntMatrix {
    val mat = IntMatrix(this.shape)
    for ((v, r, c) in this.withIndices) {
        mat[r, c] = if (only.check(r, c) && !check(v)) replacement else v
    }
    return mat
}

fun DoubleMatrix.map(which: Selector, action: (Double) -> Double): DoubleMatrix {
    val mat = DoubleMatrix(this.shape)
    for ((v, r, c) in this.withIndices) {
        mat[r, c] = if (which.check(r, c)) action(v) else v
    }
    return mat
}

fun DoubleMatrix.mapIndexed(which: Selector, action: (Double, ri: Int, ci: Int) -> Double): DoubleMatrix {
    val mat = DoubleMatrix(this.shape)
    for ((v, r, c) in this.withIndices) {
        mat[r, c] = if (which.check(r, c)) action(v, r, c) else v
    }
    return mat
}

fun DoubleMatrix.replace(only: Selector, replacement: Double, check: (Double) -> Boolean): DoubleMatrix {
    val mat = DoubleMatrix(this.shape)
    for ((v, r, c) in this.withIndices) {
        mat[r, c] = if (only.check(r, c) && check(v)) replacement else v
    }
    return mat
}

fun DoubleMatrix.replaceNot(only: Selector, replacement: Double, check: (Double) -> Boolean): DoubleMatrix {
    val mat = DoubleMatrix(this.shape)
    for ((v, r, c) in this.withIndices) {
        mat[r, c] = if (only.check(r, c) && !check(v)) replacement else v
    }
    return mat
}