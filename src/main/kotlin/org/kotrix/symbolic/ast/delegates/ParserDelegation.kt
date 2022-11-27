package org.kotrix.symbolic.funAST.delegates

import org.kotrix.symbolic.funAST.Fun
import org.kotrix.symbolic.parse.Lexer
import org.kotrix.symbolic.parse.Parser
import kotlin.reflect.KProperty

class ParserDelegation(private val text: String, val lexer: Lexer = Lexer(text), val parser: Parser = Parser(emptyList())) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Fun = parser(lexer.generateTokens()).parse()
}

typealias Parse = ParserDelegation