package org.kotrix.matrix

inline fun <reified T: Any> matrix(actions: Matrix.Scope<T>.() -> Matrix.Scope<T>): Matrix<T> =
    Matrix(Matrix.Scope.Base<T>().actions().matrix)

fun <T: Any> Matrix<T>.forEach(which: Selector = Selector.ALL, action: (T) -> Unit) {
    for ((v, r, c) in this.withIndices) {
        if (which.check(r, c)) action(v)
    }
}

fun <T: Any> Matrix<T>.forEachIndexed(which: Selector = Selector.ALL, action: (T, ri: Int, ci: Int) -> Unit) {
    when(which) {
        Selector.ALL -> {
            for ((v, i, j) in this.withIndices) {
                action(v, i, j)
            }
        }
        Selector.DIAGONAL -> {
            for (i in 0 until this.rowLength) {
                action(this[i, i], i, i)
            }
        }
        Selector.OFF_DIAGONAL -> {
            for ((v, i, j) in this.withIndices) {
                if (i != j) action(v, i, j)
            }
        }
        Selector.LOWER -> {
            for ((v, i, j) in this.withIndices) {
                if (j <= i) action(v, i, j)
            }
        }
        Selector.STRICT_LOWER -> {
            for ((v, i, j) in this.withIndices) {
                if (j < i) action(v, i, j)
            }
        }
        Selector.STRICT_UPPER -> {
            for ((v, i, j) in this.withIndices) {
                if (j > i) action(v,i ,j)
            }
        }
        Selector.UPPER -> {
            for ((v, i, j) in this.withIndices) {
                if (j >= i) action(v, i, j)
            }
        }
    }
}

fun <T: Any> Matrix<T>.map(which: Selector = Selector.ALL, action: (T) -> T): Matrix<T> {
    val mat = Matrix.nulls<T>(this.shape)
    for ((v, r, c) in this.withIndices) {
        mat[r, c] = if (which.check(r, c)) action(v) else v
    }
    return mat
}

fun <T: Any> Matrix<T>.mapIndexed(which: Selector = Selector.ALL, action: (T, ri: Int, ci: Int) -> T): Matrix<T> {
    val mat = Matrix.nulls<T>(this.shape)
    for ((v, r, c) in this.withIndices) {
        mat[r, c] = if (which.check(r, c)) action(v, r, c) else v
    }
    return mat
}

fun <T: Any> Matrix<T>.all(only: Selector = Selector.ALL, check: (T) -> Boolean): Boolean {
    var ret = true
    for ((v, r, c) in this.withIndices) {
        ret = ret && (if (only.check(r, c)) check(v) else true)
    }
    return ret
}

fun <T: Any> Matrix<T>.any(only: Selector = Selector.ALL, check: (T) -> Boolean): Boolean {
    var ret = false
    for ((v, r, c) in this.withIndices) {
        ret = ret || (if (only.check(r, c)) check(v) else true)
    }
    return ret
}

fun <T: Any> Matrix<T>.replace(only: Selector = Selector.ALL, replacement: T, check: (T) -> Boolean): Matrix<T> {
    val mat = Matrix.nulls<T>(this.shape)
    for ((v, r, c) in this.withIndices) {
        mat[r, c] = if (only.check(r, c) && check(v)) replacement else v
    }
    return mat
}

fun <T: Any> Matrix<T>.replaceNot(only: Selector = Selector.ALL, replacement: T, check: (T) -> Boolean): Matrix<T> {
    val mat = Matrix.nulls<T>(this.shape)
    for ((v, r, c) in this.withIndices) {
        mat[r, c] = if (only.check(r, c) && !check(v)) replacement else v
    }
    return mat
}