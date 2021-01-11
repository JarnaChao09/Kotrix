package org.kotrix.discrete

import kotlin.math.pow
import kotlin.math.max
import org.kotrix.matrix.BooleanMatrix
import org.kotrix.utils.by
import org.kotrix.utils.sliceTo
import org.kotrix.vector.BooleanVector
import org.kotrix.vector.Vector
import org.kotrix.vector.forEachIndexed
import org.kotrix.vector.toVector

class TruthTable(private val expression: BooleanAlgebra) {
    private val variables: Set<Variable> by lazy { expression.variables.toSortedSet { l, r -> l.name.compareTo(r.name) } }

    private val varIndex: Map<Variable, Int> by lazy {
        mapOf(*Array<Pair<Variable, Int>>(expression.variables.size) { expression.variables.toList()[it] to it })
    }

    private fun getAllOperations(
            from: BooleanAlgebra = expression,
            allOps: Vector<BooleanAlgebra> = Vector<BooleanAlgebra>(0) { expression },
    ): Vector<BooleanAlgebra> {
        return when(from) {
            is Constant -> allOps.also { it.remove(from) }
            is Variable -> allOps.also { it.remove(from) }
            is To -> getAllOperations(from.sufficient, getAllOperations(from.necessary, allOps.also {
                it.append(from)
            }))
            is Iff -> getAllOperations(from.leftexpr, getAllOperations(from.rightexpr, allOps.also {
                it.append(from)
            }))
            is And -> getAllOperations(from.leftop, getAllOperations(from.rightop, allOps.also {
                it.append(from)
            }))
            is Or -> getAllOperations(from.leftop, getAllOperations(from.rightop, allOps.also {
                it.append(from)
            }))
            is Xor -> getAllOperations(from.leftop, getAllOperations(from.rightop, allOps.also {
                it.append(from)
            }))
            is Not -> getAllOperations(from.expr, allOps.also { it.append(from) })
        }
    }

    private val varValues: BooleanMatrix by lazy {
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

    operator fun String.times(other: Int): String =
            this.run { var ret = ""; for(i in 0 until other) { ret += this }; ret }

    override fun toString(): String {
        val titleString = MutableList(0) { "" }
        val tableString = MutableList(0) { MutableList(0) { "" } }

        val sortedOperations = this.getAllOperations()
                .sortedBy { it.stringify().length }
                .toVector()

        val maxLengthList = MutableList(this.variables.size + sortedOperations.size) { 5 }

        variables.forEachIndexed { index, variable ->
            maxLengthList[index] = max(maxLengthList[index], variable.stringify().length)
        }

        val offset = this.variables.size

        sortedOperations.forEachIndexed { index, booleanAlgebra ->
            maxLengthList[index + offset] = max(maxLengthList[index + offset], booleanAlgebra.stringify().length)
        }

        var index = 0
        for (i in this.variables) {
            titleString.add(i.stringify() + (" " * (maxLengthList[index++] - i.stringify().length)))
        }

        for (i in sortedOperations) {
            titleString.add(i.stringify() + (" " * (maxLengthList[index++] - i.stringify().length)))
        }

        val variableList = this.variables.toList()
        for (r in 0 until this.varValues.rowLength) {
            index = 0
            val dummy = Vector(0) { "" }
            dummy appendAll this.varValues[r].toList().map { "$it${" " * (maxLengthList[index++] - it.toString().length)}" }.toVector()
            for (op in sortedOperations) {
                val values = Array<Pair<Variable, Constant>>(expression.variables.size) {
                    variableList[it] to this.varValues[r, this.varIndex.getValue(variableList[it])].const
                }
                val temp = (op(*values) as Constant).value.toString()
                dummy append (temp + (" " * (maxLengthList[index++] - temp.length)))
            }
            tableString += dummy.toList().toMutableList()
        }

        return "${
            titleString.joinToString(prefix = "|", postfix = "|", separator = "|",)
        }\n${
            tableString.joinToString(separator = "\n") { 
                it.joinToString(prefix = "|", postfix = "|", separator = "|") 
            }
        }"
    }
}