package org.kotrix.symbolic.parse

import org.kotrix.symbolic.funAST.*
import org.kotrix.vector.toVector

class Parser(private var tokensList: List<Tokens>) {
    private var tokensIterator = tokensList.listIterator()
    private var currentToken: Tokens = Tokens.Empty

    fun parse(): Fun {
        advanceCurrent()

        val ret = expr()

        if (currentToken != Tokens.Empty) {
            throw Exception("Invalid Syntax")
        }

        return ret
    }

    operator fun invoke(newTokensList: List<Tokens>): Parser = this.reset(newTokensList)

    fun reset(newTokensList: List<Tokens>): Parser {
        return this.apply {
            this.tokensList = newTokensList
            this.tokensIterator = newTokensList.listIterator()
            this.currentToken = Tokens.Empty
        }
    }

    private fun atom(): Fun {
        val current = currentToken

        return when {
            current is Tokens.Parentheses && current.type == Tokens.ParenthesesType.Left -> {
                advanceCurrent()

                val ret = expr()

                val current1 = currentToken
                when {
                    current1 !is Tokens.Parentheses || current1.type != Tokens.ParenthesesType.Right -> {
                        throw Exception("Invalid Syntax")
                    }
                    else -> {
                        advanceCurrent()

                        ret
                    }
                }
            }
//            current is Tokens.Diamond && current.type == Tokens.DiamondType.Left -> {
//                val expressions = mutableListOf<Fun>()
//                advanceCurrent()
//
//                val current1 = currentToken
//
//                if (current1 is Tokens.Diamond && current1.type == Tokens.DiamondType.Right) {
//                    throw Exception("Invalid Syntax, can not create empty vector")
//                }
//
//                expressions.add(expr())
//
//                var current2 = currentToken
//                while (current2 is Tokens.Comma) {
//                    advanceCurrent()
//
//                    expressions.add(expr())
//
//                    current2 = currentToken
//                }
//
//                if (current2 !is Tokens.Diamond || current2.type != Tokens.DiamondType.Right) {
//                    throw Exception("Invalid Syntax, expected , or ending >")
//                }
//
//                advanceCurrent()
//                return Vector(expressions.toVector())
//            }
            current is Tokens.Scalar -> {
                advanceCurrent()
                Scalar(current.value)
            }
            else -> {
                throw Exception("Invalid Syntax")
            }
        }
    }

    private fun power(): Fun {
        var left = atom()

        var current = currentToken

        while (current != Tokens.Empty && current is Tokens.Operator && current.type == Tokens.OperatorTypes.Power) {
            when (current.type) {
                Tokens.OperatorTypes.Power -> {
                    advanceCurrent()
                    left = Power(left, factor())
                }
                else -> {
                    throw Exception("Invalid Syntax")
                }
            }
            current = currentToken
        }
        return left
    }

    private fun factor(): Fun {
        val current = currentToken

        if (current == Tokens.Empty) {
            return power()
        }

        return when {
            current is Tokens.Operator && current.type == Tokens.OperatorTypes.Plus -> {
                advanceCurrent()
                UnaryPlus(factor())
            }
            current is Tokens.Operator && current.type == Tokens.OperatorTypes.Minus -> {
                advanceCurrent()
                UnaryMinus(factor())
            }
            else -> power()
        }
    }

    private fun term(): Fun {
        var result = factor()

        var current = currentToken

        while (current != Tokens.Empty
            && current is Tokens.Operator
            && (current.type == Tokens.OperatorTypes.Times || current.type == Tokens.OperatorTypes.Divide)
        ) {
            result = when (current.type) {
                Tokens.OperatorTypes.Times -> {
                    advanceCurrent()
                    Times(result, factor())
                }
                Tokens.OperatorTypes.Divide -> {
                    advanceCurrent()
                    Divide(result, factor())
                }
                else -> {
                    throw Exception("Invalid Syntax")
                }
            }
            current = currentToken
        }
        return result
    }

    private fun expr(): Fun {
        var result = term()

        var current = currentToken

        while (current != Tokens.Empty
            && current is Tokens.Operator
            && (current.type == Tokens.OperatorTypes.Plus || current.type == Tokens.OperatorTypes.Minus)
        ) {
            result = when (current.type) {
                Tokens.OperatorTypes.Plus -> {
                    advanceCurrent()
                    Add(result, term())
                }
                Tokens.OperatorTypes.Minus -> {
                    advanceCurrent()
                    Subtract(result, term())
                }
                else -> {
                    throw Exception("Invalid Syntax")
                }
            }
            current = currentToken
        }
        return result
    }

    private fun advanceCurrent() {
        currentToken = if (tokensIterator.hasNext()) tokensIterator.next() else Tokens.Empty
    }
}