package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.Fun
import org.kotrix.symbolic.funAST.Variable

data class Derivative(val function: Fun? = null, val by: Variable? = null) {
    class NoVariableGiven: Throwable(message = "No Variable given")

    operator fun div(other: Derivative): Fun =
        this.function?.diff(other.by ?: throw NoVariableGiven()) ?: throw NoVariableGiven()
}

fun d(func: Fun): Derivative = Derivative(function = func)

fun d(by: Variable): Derivative = Derivative(by = by)