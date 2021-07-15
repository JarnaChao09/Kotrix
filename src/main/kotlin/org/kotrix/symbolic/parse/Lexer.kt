package org.kotrix.symbolic.parse

class Lexer(private var text: String) {
    private var tokens = mutableListOf<Tokens>()
    private var textIterator = text.iterator()
    private var currentCharacter = ""

    operator fun invoke(newText: String): Lexer {
        return this.reset(newText)
    }

    fun reset(newText: String): Lexer {
        return this.apply {
            this.text = newText
            this.textIterator = newText.iterator()
            this.currentCharacter = ""
            this.tokens.clear()
        }
    }

    fun generateTokens(): List<Tokens> {
        advance()

        while (currentCharacter != "") {
            when (currentCharacter) {
                " ", "\n", "\t" -> advance()
                in "0".."9", "." -> generateNumber()
                "+" -> advance {
                    tokens.add(Tokens.Operator(Tokens.OperatorTypes.Plus))
                }
                "-" -> advance {
                    tokens.add(Tokens.Operator(Tokens.OperatorTypes.Minus))
                }
                "*" -> advance {
                    tokens.add(Tokens.Operator(Tokens.OperatorTypes.Times))
                }
                "/" -> advance {
                    tokens.add(Tokens.Operator(Tokens.OperatorTypes.Divide))
                }
                "^" -> advance {
                    tokens.add(Tokens.Operator(Tokens.OperatorTypes.Power))
                }
                "(" -> advance {
                    tokens.add(Tokens.Parentheses(Tokens.ParenthesesType.Left))
                }
                ")" -> advance {
                    tokens.add(Tokens.Parentheses(Tokens.ParenthesesType.Right))
                }
            }
        }
        return tokens.toList()
    }

    private fun generateNumber() {
        var decimalCount = 0
        var numberString = currentCharacter

        advance()

        while (currentCharacter != "" && (currentCharacter == "." || currentCharacter in "0".."9")) {
            if (currentCharacter == ".") {
                decimalCount++
                if (decimalCount > 1) {
                    throw IllegalArgumentException("Too many decimals")
                }
            }

            numberString += currentCharacter
            advance()
        }

        numberString = when {
            numberString.startsWith(".") -> "0$numberString"
            numberString.endsWith(".") -> "${numberString}0"
            else -> numberString
        }

        tokens.add(Tokens.Scalar(numberString.toDouble()))
    }

    private fun advance() {
        currentCharacter = "${if (textIterator.hasNext()) textIterator.next() else ""}"
    }

    private fun advance(block: () -> Unit) {
        advance()
        block()
    }
}