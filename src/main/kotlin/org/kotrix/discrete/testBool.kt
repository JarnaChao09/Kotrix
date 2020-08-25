package org.kotrix.discrete

fun main() {
    val p = Variable("p")
    val q = Variable("q")
    val r = Variable("r")

    val expression = (p OR q) arrow (p XOR (r.NOT))

    println(expression)

    println(expression.stringify())

    println(expression(
            p to true.const,
            q to true.const,
            r to false.const,
    ).stringify())
}