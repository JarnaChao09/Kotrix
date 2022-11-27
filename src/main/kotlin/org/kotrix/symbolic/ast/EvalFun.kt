package org.kotrix.symbolic.ast

import org.kotrix.symbolic.ast.extensions.*

interface EvalFun<X, Y, R: Fun> {
    fun partialEval(value: Map<X, Y>): R
    fun fullEval(value: Map<X, Y>): Double
    @Suppress("UNCHECKED_CAST")
    fun eval(value: Map<X, Y>): R = try {
        this.fullEval(value).scalar as R
    } catch (e: Exception) {
        this.partialEval(value)
    }
}