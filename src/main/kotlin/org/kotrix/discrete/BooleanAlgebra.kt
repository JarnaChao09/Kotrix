package org.kotrix.discrete

import org.kotrix.utils.Stringify

sealed class BooleanAlgebra(
        open val variables: Set<Variable> = emptySet()
): Stringify, EvalBool<BooleanAlgebra, Constant, BooleanAlgebra> {
    operator fun invoke(value: Map<BooleanAlgebra, Constant>) = this.eval(value)

    operator fun invoke(vararg value: Pair<BooleanAlgebra, Constant>) = this(mapOf(*value))
}

data class Constant(val value: Boolean): BooleanAlgebra() {
    override fun stringify(): String =
            this.value.toString().toUpperCase()

    override fun fullEval(value: Map<BooleanAlgebra, Constant>): Boolean = this.value
}

data class Variable(val name: String): BooleanAlgebra() {
    override val variables: Set<Variable>
        get() = setOf(this)

    inner class NoValueException: Exception("No Value Given for $this")

    override fun stringify(): String =
            this.name

    override fun fullEval(value: Map<BooleanAlgebra, Constant>): Boolean {
        for ((i, j) in value) {
            when(i) {
                this -> return j.value
                else -> continue
            }
        }
        throw NoValueException()
    }

}

data class To(val sufficient: BooleanAlgebra, val necessary: BooleanAlgebra):
        BooleanAlgebra() {
    override val variables: Set<Variable>
        get() = sufficient.variables union sufficient.variables

    override fun stringify(): String =
            "(${sufficient.stringify()} ==> ${necessary.stringify()})"

    override fun fullEval(value: Map<BooleanAlgebra, Constant>): Boolean {
        val left = sufficient.fullEval(value)
        val right = necessary.fullEval(value)

//        println("TO $left $right result: ${!left || right}")

        return !left || right
    }
}

infix fun BooleanAlgebra.arrow(other: BooleanAlgebra): BooleanAlgebra =
        To(this, other)

data class Iff(val leftexpr: BooleanAlgebra, val rightexpr: BooleanAlgebra):
        BooleanAlgebra(leftexpr.variables union rightexpr.variables) {
    override fun stringify(): String =
            "(${leftexpr.stringify()} <==> ${rightexpr.stringify()})"

    override fun fullEval(value: Map<BooleanAlgebra, Constant>): Boolean {
        val left = leftexpr.fullEval(value)
        val right = rightexpr.fullEval(value)

//        println("IFF $left $right result: ${left == right}")

        return left == right
    }
}

infix fun BooleanAlgebra.iff(other: BooleanAlgebra): BooleanAlgebra =
        Iff(this, other)

data class And(val leftop: BooleanAlgebra, val rightop: BooleanAlgebra):
        BooleanAlgebra(leftop.variables union rightop.variables) {
    override fun stringify(): String =
            "(${leftop.stringify()} && ${rightop.stringify()})"

    override fun fullEval(value: Map<BooleanAlgebra, Constant>): Boolean {
        val left = leftop.fullEval(value)
        val right = rightop.fullEval(value)

//        println("AND $left $right result: ${left && right}")

        return left && right
    }
}

infix fun BooleanAlgebra.AND(other: BooleanAlgebra): BooleanAlgebra =
        And(this, other)

data class Or(val leftop: BooleanAlgebra, val rightop: BooleanAlgebra):
        BooleanAlgebra(leftop.variables union rightop.variables) {
    override fun stringify(): String =
            "(${leftop.stringify()} || ${rightop.stringify()})"

    override fun fullEval(value: Map<BooleanAlgebra, Constant>): Boolean {
        val left = leftop.fullEval(value)
        val right = rightop.fullEval(value)

//        println("OR $left $right result: ${left || right}")

        return left || right
    }
}

infix fun BooleanAlgebra.OR(other: BooleanAlgebra): BooleanAlgebra =
        Or(this, other)

data class Xor(val leftop: BooleanAlgebra, val rightop: BooleanAlgebra):
        BooleanAlgebra(leftop.variables union rightop.variables) {
    override fun stringify(): String =
            "(${leftop.stringify()} ^ ${rightop.stringify()})"

    override fun fullEval(value: Map<BooleanAlgebra, Constant>): Boolean {
        val left = leftop.fullEval(value)
        val right = rightop.fullEval(value)

//        println("XOR $left $right result: ${left xor right}")

        return left xor right
    }
}

infix fun BooleanAlgebra.XOR(other: BooleanAlgebra): BooleanAlgebra =
        Xor(this, other)

data class Not(val expr: BooleanAlgebra): BooleanAlgebra(expr.variables) {
    override fun stringify(): String =
            "!(${expr.stringify()})"

    override fun fullEval(value: Map<BooleanAlgebra, Constant>): Boolean {
        val res = expr.fullEval(value)

//        println("NOT $res result: ${!res}")

        return !res
    }
}

val BooleanAlgebra.NOT: BooleanAlgebra
    get() = Not(this)
