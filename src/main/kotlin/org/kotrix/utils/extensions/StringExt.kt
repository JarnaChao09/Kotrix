package org.kotrix.utils.extensions

internal operator fun String.times(maxLength: Int): String {
    var dummy = ""
    for (i in 0 until maxLength) {
        dummy += this
    }
    return dummy
}