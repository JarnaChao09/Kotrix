package org.kotrix.discrete

val Boolean.const: Constant
    get() = Constant(this)

infix fun Boolean.arrow(other: Boolean): BooleanAlgebra =
        To(this.const, other.const)

infix fun Boolean.iff(other: Boolean): BooleanAlgebra =
        Iff(this.const, other.const)

infix fun Boolean.AND(other: Boolean): BooleanAlgebra =
        And(this.const, other.const)

infix fun Boolean.OR(other: Boolean): BooleanAlgebra =
        Or(this.const, other.const)

infix fun Boolean.XOR(other: Boolean): BooleanAlgebra =
        Xor(this.const, other.const)

val Boolean.NOT: BooleanAlgebra
    get() = Not(this.const)
