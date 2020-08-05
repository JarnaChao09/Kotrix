package org.kotrix.symbolic

import org.kotrix.symbolic.Fun
import org.kotrix.symbolic.Variable

val String.asVar: Variable
    get() = Variable(this)

val String.parse: Fun
    get() = TODO()