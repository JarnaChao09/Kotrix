package org.kotrix.discrete

import org.kotrix.discrete.booleanAlgebraAST.*

fun main() {
    val p = Variable("p")
    val q = Variable("q")
    val r = Variable("r")

    val expression = q.NOT iff (p OR r)
    
    val truthTable = TruthTable(expression)

    println(truthTable)
}