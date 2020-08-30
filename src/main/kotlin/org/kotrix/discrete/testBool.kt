package org.kotrix.discrete

fun main() {
    val p = Variable("p")
    val q = Variable("q")
    val r = Variable("r")

    val expression = (p OR q) AND ((p.NOT) OR r) arrow (q OR r)

    val truthTable = TruthTable(expression)

    println(truthTable)
}