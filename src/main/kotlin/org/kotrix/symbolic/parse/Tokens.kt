package org.kotrix.symbolic.parse

sealed class Tokens {
    enum class OperatorTypes {
        Plus,
        Minus,
        Times,
        Divide,
        Power,
    }

    enum class ParenthesesType {
        Left,
        Right,
    }

    enum class BracketType {
        Left,
        Right,
    }

    // todo figure out how to differentiate between bool ops and <> for vectors
    enum class DiamondType {
        Left,
        Right,
    }

    object Empty: Tokens()

    object Comma: Tokens() {
        override fun toString(): String = "Comma()"
    }

    data class Scalar(val value: Double): Tokens()

    data class Variable(val name: String): Tokens()

    data class Operator(val type: OperatorTypes): Tokens()

    data class Parentheses(val type: ParenthesesType): Tokens()

    data class Bracket(val type: BracketType): Tokens()

    data class Diamond(val type: DiamondType): Tokens()
}
