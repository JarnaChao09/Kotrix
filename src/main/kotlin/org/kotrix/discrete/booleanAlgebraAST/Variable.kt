package org.kotrix.discrete.booleanAlgebraAST

data class Variable(val name: String) : BooleanAlgebra() {
    override val variables: Set<Variable>
        get() = setOf(this)

    inner class NoValueException : Exception("No Value Given for $this")

    override fun stringify(): String =
        this.name

    override fun toLaTeX(): String =
        this.name

    override fun fullEval(value: Map<BooleanAlgebra, Constant>): Boolean {
        for ((i, j) in value) {
            when (i) {
                this -> return j.value
                else -> continue
            }
        }
        throw NoValueException()
    }
}