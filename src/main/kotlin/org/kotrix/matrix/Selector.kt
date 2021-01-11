package org.kotrix.matrix

enum class Selector {
    ALL {
        override fun check(i: Int, j: Int): Boolean = true
    },
    DIAGONAL {
        override fun check(i: Int, j: Int): Boolean = i == j
    },
    OFF_DIAGONAL {
        override fun check(i: Int, j: Int): Boolean = i != j
    },
    LOWER {
        override fun check(i: Int, j: Int): Boolean = i <= j
    },
    STRICT_LOWER {
        override fun check(i: Int, j: Int): Boolean = i < j
    },
    STRICT_UPPER {
        override fun check(i: Int, j: Int): Boolean = i > j
    },
    UPPER {
        override fun check(i: Int, j: Int): Boolean = i >= j
    };

    abstract fun check(i: Int, j: Int): Boolean
}