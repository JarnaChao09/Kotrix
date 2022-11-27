package org.kotrix.symbolic.ast.extensions

import org.kotrix.symbolic.ast.Fun
import org.kotrix.symbolic.ast.Variable

val String.asVar: Variable
    get() = Variable(this)

val String.parse: Fun
    get() = TODO()