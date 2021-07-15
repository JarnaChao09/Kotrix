package org.kotrix.symbolic.funAST.delegates

import org.kotrix.symbolic.funAST.Variable
import kotlin.reflect.KProperty

class VariableDelegation(private val name: String? = null) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Variable = Variable(name ?: property.name)
}

typealias Var = VariableDelegation