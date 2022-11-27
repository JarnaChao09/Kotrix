package org.kotrix.symbolic.ast

import org.kotrix.symbolic.ast.extensions.scalar

//data class Sigma(val offsets: List<Int>, val variableExpr: String, val expOffset: Int, val start: Int, val end: Int) {
//    override fun toString(): String = "sum(${
//        offsets.map {
//            "($variableExpr${if (it != 0) " + $it" else ""})"
//        }.joinToString(prefix="(",postfix=")",separator="")
//    }, x ^ ($variableExpr${if (expOffset != 0) " - $expOffset" else ""}), $variableExpr = $start .. $end)"
//
//    fun rewrite(toN: Int): Sigma {
//        val dist = toN - this.start
//
//        return Sigma(this.offsets.map { it - dist }, this.variableExpr, this.expOffset + dist, toN, this.end + dist)
//    }
//
//    fun withRange(newStart: Int, newEnd: Int = this.end): Sigma = this.copy(start = newStart, end = newEnd)
//
//    fun firstNTerms(N: Int = this.end - this.start): List<String> = List(N + 1) {
//        val correctedN = this.start + it
//        val prod = offsets.fold(1) { acc, value -> acc * (correctedN + value) }
//        val exp = correctedN - expOffset
//        val x = if (exp > 1) "x^($exp)" else if (exp == 1) "x" else "1"
//        if (prod > 1) "($prod)$x" else if (prod == 1) x else "0"
//    }
//}

data class Series(val expression: Fun, val variable: Variable, val range: IntRange, val op: (Fun, Fun) -> Fun) : Fun() {
    override fun diff(by: Variable): Fun {
        return this.copy(expression = this.expression.diff(by), range = (range.first + 1)..range.last)
    }

    override fun partialEval(value: Map<Fun, Scalar>): Fun {
        val env = buildMap {
            putAll(value)
        }.toMutableMap()
        return range.map {
            env[variable] = it.scalar
            expression.eval(env)
        }.reduce(op).partialEval(value)
    }

    override fun fullEval(value: Map<Fun, Scalar>): Double {
        val env = buildMap {
            putAll(value)
        }.toMutableMap()
        return range.map {
            env[variable] = it.scalar
            expression.eval(env)
        }.reduce(op).evalAllAtZero()
    }

    override fun simplify(): Fun {
        return this.copy(expression = expression.simplify())
    }

    override fun sub(replace: Variable, with: Fun): Fun {
        return this.copy(expression = expression.sub(replace, with))
    }

    override fun stringify(): String {
        return "Sum[${this.expression.stringify()} | ${this.variable.stringify()} <- ${this.range}]"
    }
}

fun sum(expression: Fun, startInclusive: Int, endInclusive: Int, name: String = "n", op: (Fun, Fun) -> Fun = Fun::plus) =
    Series(expression, Variable(name), startInclusive..endInclusive, op)

fun prod(expression: Fun, startInclusive: Int, endInclusive: Int, name: String = "n", op: (Fun, Fun) -> Fun = Fun::times) =
    Series(expression, Variable(name), startInclusive..endInclusive, op)