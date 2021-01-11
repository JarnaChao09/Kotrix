package org.kotrix.utils

import org.kotrix.vector.Vector
import java.io.Serializable

data class Location<out A, out B>(val value: A, val location: B): Serializable {
    override fun toString(): String = "$value at $location"
}

infix fun <A, B> A.at(location: B) = Location(this, location)

val <T> Location<T, T>.list: List<T>
    get() = listOf(this.value, this.location)

val <T: Any> Location<T, T>.vector: Vector<T>
    get() = Vector.of(this.value, this.location)
