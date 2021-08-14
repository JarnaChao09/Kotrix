package org.kotrix.discrete.booleanAlgebraAST

import org.kotrix.utils.Stringify

sealed class BooleanAlgebra(
        open val variables: Set<Variable> = emptySet()
): Stringify, EvalBool<BooleanAlgebra, Constant, BooleanAlgebra> {
    operator fun invoke(value: Map<BooleanAlgebra, Constant>) = this.eval(value)

    operator fun invoke(vararg value: Pair<BooleanAlgebra, Constant>) = this(mapOf(*value))
}

infix fun BooleanAlgebra.arrow(other: BooleanAlgebra): BooleanAlgebra =
    To(this, other)

infix fun BooleanAlgebra.iff(other: BooleanAlgebra): BooleanAlgebra =
        Iff(this, other)

infix fun BooleanAlgebra.AND(other: BooleanAlgebra): BooleanAlgebra =
        And(this, other)

infix fun BooleanAlgebra.OR(other: BooleanAlgebra): BooleanAlgebra =
        Or(this, other)

infix fun BooleanAlgebra.XOR(other: BooleanAlgebra): BooleanAlgebra =
        Xor(this, other)

val BooleanAlgebra.NOT: BooleanAlgebra
    get() = Not(this)
