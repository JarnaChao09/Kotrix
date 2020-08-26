package org.kotrix.discrete

fun main() {
    val p = Variable("p")
    val q = Variable("q")
    val r = Variable("r")

    val expression = (p OR q) arrow (p XOR (r.NOT))

    val truthTable = TruthTable(expression)

    println(truthTable)
}