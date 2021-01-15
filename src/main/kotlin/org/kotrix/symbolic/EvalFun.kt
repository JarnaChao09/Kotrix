package org.kotrix.symbolic

interface EvalFun<X, Y, R: Fun> {
    fun partialEval(value: Map<X, Y>): R
    fun fullEval(value: Map<X, Y>): Double
    @Suppress("UNCHECKED_CAST")
    fun eval(value: Map<X, Y>): R = try {
        this.fullEval(value).const as R
    } catch (e: Exception) {
        this.partialEval(value)
    }
}