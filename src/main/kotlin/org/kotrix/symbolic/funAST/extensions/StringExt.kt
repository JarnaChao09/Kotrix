package org.kotrix.symbolic.funAST.extensions

import org.kotrix.symbolic.funAST.Fun
import org.kotrix.symbolic.funAST.Variable

val String.asVar: Variable
    get() = Variable(this)

val String.parse: Fun
    get() = TODO()