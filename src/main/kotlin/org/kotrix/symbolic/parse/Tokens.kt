package org.kotrix.symbolic.parse

// todo rewrite fun to match with lexer tokens
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

    object Empty: Tokens()

    data class Scalar(val value: Double): Tokens()

    data class Variable(val name: String): Tokens()

    data class Operator(val type: Tokens.OperatorTypes): Tokens()

    data class Parentheses(val type: Tokens.ParenthesesType): Tokens()
}
