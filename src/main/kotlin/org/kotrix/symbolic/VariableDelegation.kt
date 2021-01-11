package org.kotrix.symbolic

import kotlin.reflect.KProperty

class VariableDelegation(private val name: String? = null) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Variable = Variable(name ?: property.name)
}

typealias Var = VariableDelegation