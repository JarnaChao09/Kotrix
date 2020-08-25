package org.kotrix.discrete

import kotlin.math.pow
import org.kotrix.matrix.BooleanMatrix
import org.kotrix.utils.by
import org.kotrix.utils.sliceTo
import org.kotrix.vector.BooleanVector
import org.kotrix.vector.Vector

class TruthTable(val expression: BooleanAlgebra) {
    val variables: Set<Variable> by lazy { expression.variables }

    val varIndex: Map<Variable, Int> by lazy {
        mapOf(*Array<Pair<Variable, Int>>(expression.variables.size) { expression.variables.toList()[it] to it })
    }

    fun getAllOperations(
            from: BooleanAlgebra = expression,
            allOps: Vector<BooleanAlgebra> = Vector<BooleanAlgebra>(0) { Constant(false) },
    ): Vector<BooleanAlgebra> {
        return when(from) {
            is Constant -> allOps
            is Variable -> allOps
            is To -> getAllOperations(from.sufficient, getAllOperations(from.necessary, allOps))
            is Iff -> TODO()
            is And -> TODO()
            is Or -> TODO()
            is Xor -> TODO()
            is Not -> TODO()
        }
    }

    val varValues: BooleanMatrix by lazy {
        val rowSize = 2.0.pow(expression.variables.size).toInt()
        val colSize = expression.variables.size
        val ret = BooleanMatrix(rowSize by colSize)

        var targetSize = rowSize
        for (i in expression.variables.indices) {
            targetSize /= 2
            var fillValue = true
            val insertVector = BooleanVector(0)
            while (insertVector.size != rowSize) {
                insertVector.appendAll(BooleanVector(targetSize) { fillValue })
                fillValue = !fillValue
            }
            ret[0 sliceTo rowSize, i] = insertVector
        }
        ret
    }
}