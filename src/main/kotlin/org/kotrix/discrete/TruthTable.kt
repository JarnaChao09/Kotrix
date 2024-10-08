package org.kotrix.discrete

import org.kotrix.discrete.booleanAlgebraAST.*
import kotlin.math.pow
import kotlin.math.max
import org.kotrix.matrix.BooleanMatrix
import org.kotrix.utils.by
import org.kotrix.utils.sliceTo
import org.kotrix.vector.*

class TruthTable(private val expression: BooleanAlgebra, private val order: Array<Variable>) {
    private val variables: Set<Variable> by lazy { expression.variables.toSortedSet { l, r -> l.name.compareTo(r.name) } }

    private val varIndex: Map<Variable, Int> by lazy {
        mapOf(*Array(expression.variables.size) { order[it] to it })
    }

    private fun getAllOperations(
        from: BooleanAlgebra = expression,
        allOps: MutableVector<BooleanAlgebra> = mutableVectorOf(),
    ): MutableVector<BooleanAlgebra> {
        return when(from) {
            is Constant -> allOps.also { it.remove(from) }
            is Variable -> allOps.also { it.remove(from) }
            is To -> getAllOperations(from.sufficient, getAllOperations(from.necessary, allOps.also {
                it.add(from)
            }))
            is Iff -> getAllOperations(from.leftexpr, getAllOperations(from.rightexpr, allOps.also {
                it.add(from)
            }))
            is And -> getAllOperations(from.leftop, getAllOperations(from.rightop, allOps.also {
                it.add(from)
            }))
            is Or -> getAllOperations(from.leftop, getAllOperations(from.rightop, allOps.also {
                it.add(from)
            }))
            is Xor -> getAllOperations(from.leftop, getAllOperations(from.rightop, allOps.also {
                it.add(from)
            }))
            is Not -> getAllOperations(from.expr, allOps.also { it.add(from) })
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
            val insertVector = BooleanVectorOld(0)
            while (insertVector.size != rowSize) {
                insertVector.appendAll(BooleanVectorOld(targetSize) { fillValue })
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
            val dummy = MutableList(0) { "" }
            dummy.addAll(this.varValues[r].toList().map { "$it${" " * (maxLengthList[index++] - it.toString().length)}" })
            for (op in sortedOperations) {
                val values = Array(expression.variables.size) {
                    variableList[it] to this.varValues[r, this.varIndex.getValue(variableList[it])].const
                }

                val temp = (op(*values) as Constant).value.toString()
                dummy.add(temp + (" " * (maxLengthList[index++] - temp.length)))
            }
            tableString += dummy
        }

        return "${
            titleString.joinToString(prefix = "|", postfix = "|", separator = "|",)
        }\n${
            tableString.joinToString(separator = "\n") { 
                it.joinToString(prefix = "|", postfix = "|", separator = "|") 
            }
        }"
    }

    fun toLaTeX(): String {
        var ret = "\\begin{array}{|"

        val numVariables = order.size

        for(i in 0 until numVariables) {
            ret += "c${if (i != numVariables - 1) " " else ""}"
        }
        ret += "|"

        val sortedOperations = this.getAllOperations()
            .sortedBy { it.stringify().length }

        for(i in sortedOperations.indices) {
            ret += "c|"
        }
        ret += "} \\hline "

        for(i in order) {
            ret += "${i.name} & "
        }

        for((i, v) in sortedOperations.withIndex()) {
            ret += "${v.toLaTeX()}${if(i != sortedOperations.size - 1) " & " else "\\\\ "}"
        }
        ret += "\\hline "

        val variableList = this.variables.toList()
        for (r in 0 until this.varValues.rowLength) {
            for(i in this.varValues[r].toList()) {
                ret += "${i.const.toLaTeX()} & "
            }
            for ((i, op) in sortedOperations.withIndex()) {
                val values = Array(expression.variables.size) {
                    variableList[it] to this.varValues[r, this.varIndex.getValue(variableList[it])].const
                }

                val temp = (op(*values) as Constant).value.const.toLaTeX()
                ret += "$temp${if(i != sortedOperations.size - 1) " & " else ""}"
            }
            ret += "\\\\ "
        }

        ret += "\\hline \\end{array}"

        return ret
    }
}