package org.kotrix.discrete.automata

enum class TokenType {
    ID,
    EQ,
    OR,
    AC,
    AS,
    NL,
}

data class Token(val value: String, val type: TokenType)

sealed interface Grammar {
    var next: Grammar?
}

data class Ident(val name: String, override var next: Grammar? = null) : Grammar {
    override fun toString(): String = "$name${next?.toString() ?: ""}"
}

data class AlphabetCharacter(val character: String, override var next: Grammar? = null) : Grammar {
    override fun toString(): String = "$character${next?.toString() ?: ""}"
}

data class AcceptingState(override var next: Grammar? = null) : Grammar {
    override fun toString(): String = "AcceptingState${next?.toString() ?: ""}"
}

data class Rule(val identifier: Grammar, val rule: List<Grammar>, override var next: Grammar? = null) : Grammar {
    override fun toString(): String = "$identifier = ${rule.joinToString(separator=" | ")}${if (next != null) {
        "\n${next.toString()}"
    } else {
        ""
    }}"
}

fun lex(input: String, alphabet: Set<Char>, acceptingState: String): List<Token> {
    return buildList {
        var i = 0
        while (i < input.length) {
            val c = input[i]
            when(c) {
                ' ' -> {}
                in alphabet -> add(Token("$c", TokenType.AC))
                '\n' -> add(Token("\\n", TokenType.NL))
                '|' -> add(Token("$c", TokenType.OR))
                '=' -> add(Token("$c", TokenType.EQ))
                else -> {
                    val s = buildString {
                        append(c)
                        i++
                        while (i < input.length && (
                                    input[i] !in alphabet && input[i] != '\n' && input[i] != '|' && input[i] != '=' && input[i] != ' '
                                    )) {
                            append(input[i++])
                        }
                        i--
                    }

                    add(Token(s, if (s == acceptingState) {
                        TokenType.AS
                    } else {
                        TokenType.ID
                    }))
                }
            }
            i++
        }
    }
}

data class ParserState(val tokens: List<Token>, val index: Int) {
    val value: Token?
        get() = if (index < tokens.size) { tokens[index] } else { null }
    val nextState: ParserState
        get() = ParserState(tokens, index + 1)
}

fun atom(state: ParserState): Pair<Grammar, ParserState> {
    var currentState = state
    var ret: Grammar? = null
    var curr: Grammar? = null

    while ((currentState.value?.type?.equals(TokenType.ID) == true) ||
        (currentState.value?.type?.equals(TokenType.AC) == true) ||
        (currentState.value?.type?.equals(TokenType.AS) == true)) {
        val value = currentState.value!!
        when (value.type) {
            TokenType.ID -> {
                if (ret == null) {
                    ret = Ident(value.value)
                    curr = ret
                } else {
                    curr!!.next = Ident(value.value)
                    curr = curr.next
                }
            }
            TokenType.AC -> {
                if (ret == null) {
                    ret = AlphabetCharacter(value.value)
                    curr = ret
                } else {
                    curr!!.next = AlphabetCharacter(value.value)
                    curr = curr.next
                }
            }
            TokenType.AS -> {
                if (ret == null) {
                    ret = AcceptingState()
                    curr = ret
                } else {
                    curr!!.next = AcceptingState()
                    curr = curr.next
                }
            }
            else -> {
                error("error state 1")
            }
        }
        currentState = currentState.nextState
    }

    return (ret ?: error("error state 2")) to currentState
}

fun rule(state: ParserState): Pair<Grammar, ParserState> {
    var (expr: Grammar, currentState) = atom(state)

    if (currentState.value?.type?.equals(TokenType.EQ) == true) {
        val rules = buildList {
            do {
                currentState = currentState.nextState
                val (c, s) = atom(currentState)
                add(c)
                currentState = s
            } while (currentState.value?.type?.equals(TokenType.OR) == true)
        }
        expr = Rule(expr, rule=rules)
    }

    return expr to currentState
}

fun parse(input: List<Token>): Grammar {
    val state = ParserState(input, -1)
    var ret: Grammar? = null
    var curr: Grammar? = null
    var currentState = state

    do {
        currentState = currentState.nextState
        val (tr, ts) = rule(currentState)
        currentState = ts
        if (ret == null) {
            ret = tr
            curr = ret
        } else {
            curr!!.next = tr
            curr = curr.next
        }
    } while (currentState.value?.type?.equals(TokenType.NL) == true)

    return ret!!
}

data class NFANode(val stateID: String, val transitions: Map<String, String>, val lambdaTransitions: Set<String> = setOf())

class NFA(val startState: String, val acceptStates: Set<String>, val nfaNodes: Map<String, NFANode>) {
    fun accepts(value: String): Boolean {
        var currentStates = setOf(startState to 0)
        val finishedStates = mutableSetOf<Pair<String, Int>>()

        while (currentStates.isNotEmpty()) {
            currentStates = buildSet {
                for ((currentState, currentIndex) in currentStates) {
                    if (currentIndex < value.length) {
                        val node = nfaNodes[currentState]!!
                        for ((transitionString, destinationNode) in node.transitions) {
                            if (currentIndex + transitionString.length <= value.length && value.substring(currentIndex..<currentIndex + transitionString.length) == transitionString) {
                                add(destinationNode to currentIndex + transitionString.length)
                            }
                        }
                        addAll(node.lambdaTransitions.map { it to currentIndex })
                    } else {
                        finishedStates.add(currentState to currentIndex)
                    }
                }
            }
        }

        return finishedStates.any { (s, _) -> s in acceptStates }
    }

    override fun toString(): String {
        return buildString {
            append("Start State: ${this@NFA.startState} | Accept States: ${this@NFA.acceptStates}")
            append('\n')
            for ((lhs, node) in this@NFA.nfaNodes) {
                append("    $lhs -> $node")
                append('\n')
            }
        }
    }
}

fun grammar2NFA(grammar: Grammar, startState: String, defaultAcceptState: String="X"): NFA {
    var current: Grammar? = grammar

    val acceptingStateSet = mutableSetOf(defaultAcceptState)
    val nfaMap = buildMap {
        put(defaultAcceptState, NFANode(defaultAcceptState, emptyMap(), emptySet()))
        while (current != null) {
            val currentLambdaTransitions = mutableSetOf<String>()
            val currentTransitions = mutableMapOf<String, String>()
            val c = current
            current = current?.next
            when (c) {
                is Rule -> {
                    val currentState = when (val id = c.identifier) {
                        is Ident -> id.name
                        else -> error("lhs of a = must be an identifier")
                    }
                    for (g in c.rule) {
                        when (g) {
                            is AcceptingState -> acceptingStateSet.add(currentState)
                            is Ident -> currentLambdaTransitions.add(g.name)
                            is AlphabetCharacter -> {
                                var currentChar: Grammar? = g
                                var sum = ""
                                while (currentChar != null) {
                                    when (currentChar) {
                                        is AlphabetCharacter -> {
                                            sum += currentChar.character
                                            currentChar = currentChar.next
                                            if (currentChar == null) {
                                                currentTransitions[sum] = defaultAcceptState
                                            }
                                        }
                                        is Ident -> {
                                            currentTransitions[sum] = currentChar.name
                                            currentChar = currentChar.next
                                        }
                                        else -> error("cannot nest rule")
                                    }
                                }
                            }
                            is Rule -> error("cannot nest rule")
                        }
                        put(currentState, NFANode(currentState, currentTransitions, currentLambdaTransitions))
                    }
                }
                else -> error("top level should be rule")
            }
        }
    }

    return NFA(startState, acceptingStateSet, nfaMap)
}

fun enumerate(size: Int, alphabet: Set<Char>): Sequence<String> {
    return sequence {
        var prev = listOf("")

        repeat(size) {
            prev = buildList {
                for (str in prev) {
                    for (alphabetCharacter in alphabet) {
                        val tmp = str + alphabetCharacter
                        add(tmp)
                        yield(tmp)
                    }
                }
            }
        }
    }
}

fun main() {
    val grammar = """
    	S = 0S | 1A
        A = 1S | 0B
        B = x | C
        C = 0000 | S
    """.trimIndent()

    val alphabet = setOf('0', '1')

    val tokens = lex(grammar, alphabet, "x")
    val tree = parse(tokens)
    println(tree)
    val nfa = grammar2NFA(tree, "S")

    for (enumeratedString in enumerate(6, alphabet)) {
        if (nfa.accepts(enumeratedString)) {
            println("$enumeratedString is accepted")
        }
    }
}