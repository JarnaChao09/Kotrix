package org.kotrix.symbolic.funAST

import org.kotrix.symbolic.funAST.extensions.scalar

data class Variable(val name: String): Fun() {
    override val variables: Set<Variable>
        get() = setOf(this)

    class NoValueException(x: Variable): Exception("No Value Given for ${x.stringify()}")

    override fun stringify(): String = name

    override fun diff(by: Variable): Fun =
        if (this == by) 1.scalar else 0.scalar

    override fun fullEval(value: Map<Fun, Scalar>): Double {
        loop@for ((i, j) in value) {
            when(i) {
                this -> return j.value
                else -> continue@loop
            }
        }
        throw NoValueException(this)
    }

    override fun partialEval(value: Map<Fun, Scalar>): Fun {
        loop@for ((i, j) in value) {
            when(i) {
                this -> return j
                else -> continue@loop
            }
        }
        return this
    }

    override fun toString(): String = this.name

    override fun sub(replace: Variable, with: Fun): Fun =
        if (this == replace) with else this

    override fun copy(): Fun = Variable(this.name)
}