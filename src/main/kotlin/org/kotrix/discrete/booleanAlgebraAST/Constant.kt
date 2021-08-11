package org.kotrix.discrete.booleanAlgebraAST

import java.util.*

data class Constant(val value: Boolean) : BooleanAlgebra() {
    override fun stringify(): String =
        this.value.toString().uppercase(Locale.getDefault())

    override fun fullEval(value: Map<BooleanAlgebra, Constant>): Boolean = this.value
}