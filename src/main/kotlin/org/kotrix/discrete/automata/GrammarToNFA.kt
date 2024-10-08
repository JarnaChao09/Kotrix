package org.kotrix.discrete.automata

/**
 * Helper function to wrap output in visual line breaks
 *
 * @param title title of the specific section
 * @param separator string to use as the line break string
 * @param n length of the line break
 * @param block block of code to run in between the line breaks
 */
fun wrap(title: String, separator: String = "-", n: Int = 20, block: () -> Unit): Unit {
    println(title)
    repeat(n) { print(separator) }
    println()
    block()
    repeat(n) { print(separator) }
    println()
}

/**
 * Enum for differentiating the token type
 *
 * ID: identifiers
 * EQ: equal size
 * OR: pipe
 * AC: character that is inside the alphabet
 * AS: accepting state
 * NL: newline
 */
enum class TokenType {
    ID,
    EQ,
    OR,
    AC,
    AS,
    NL,
}

/**
 * Class for storing the tokens in the lexer
 *
 * @property value the lexeme of the token
 * @property type the type of the token, see [TokenType]
 */
data class Token(val value: String, val type: TokenType)

/**
 * Sealed interface for the Grammar Abstract Syntax Tree (AST) Structure
 *
 * @property next the next node in the AST (more of a vine instead of a tree)
 */
sealed interface Grammar {
    var next: Grammar?
}

/**
 * AST node for an identifier
 *
 * @property name the name of the identifier
 */
data class Ident(val name: String, override var next: Grammar? = null) : Grammar {
    override fun toString(): String = "$name${next?.toString() ?: ""}"
}

/**
 * AST node for an alphabetic character
 *
 * @property character the character this node represents, stored as a string for easier conversions
 */
data class AlphabetCharacter(val character: String, override var next: Grammar? = null) : Grammar {
    override fun toString(): String = "$character${next?.toString() ?: ""}"
}

/**
 * AST node for marking a state as an accepting state
 */
data class AcceptingState(override var next: Grammar? = null) : Grammar {
    override fun toString(): String = "AcceptingState${next?.toString() ?: ""}"
}

/**
 * AST node for a production rule
 *
 * @property identifier the left hand side of the =, the recursive variable
 * @property rule a list of all grammar strings to use as the production rule
 */
data class Rule(val identifier: Grammar, val rule: List<Grammar>, override var next: Grammar? = null) : Grammar {
    override fun toString(): String = "$identifier = ${rule.joinToString(separator = " | ")}${
        if (next != null) {
            "\n${next.toString()}"
        } else {
            ""
        }
    }"
}

/**
 * Function to lex the inputted grammar string based on an alphabet and accepting state string into a list of tokens
 * used for parsing. This function will get the string into a more unified structure (ignoring whitespaces)
 *
 * @param input the inputted grammar string
 * @param alphabet a set of characters considered the alphabet
 * @param acceptingState the string which will be used to denote states as accepting states
 * @return list of tokens
 */
fun lex(input: String, alphabet: Set<Char>, acceptingState: String): List<Token> {
    return buildList {
        var i = 0
        while (i < input.length) {
            val c = input[i]
            when (c) {
                ' ' -> {}
                in alphabet -> add(Token("$c", TokenType.AC))
                '\n' -> add(Token("\\n", TokenType.NL))
                '|' -> add(Token("$c", TokenType.OR))
                '=' -> add(Token("$c", TokenType.EQ))
                else -> {
                    val s = buildString {
                        append(c)
                        i++
                        while (i < input.length && (input[i] !in alphabet && input[i] != '\n' && input[i] != '|' && input[i] != '=' && input[i] != ' ')) {
                            append(input[i++])
                        }
                        i--
                    }

                    add(
                        Token(
                            s, if (s == acceptingState) {
                                TokenType.AS
                            } else {
                                TokenType.ID
                            }
                        )
                    )
                }
            }
            i++
        }
    }
}

/**
 * Class to hold the current parsing state
 *
 * @param tokens list of tokens to be parsed
 * @param index current token being parsed
 */
data class ParserState(val tokens: List<Token>, val index: Int) {
    val value: Token?
        get() = if (index < tokens.size) {
            tokens[index]
        } else {
            null
        }
    val nextState: ParserState
        get() = ParserState(tokens, index + 1)
}

// Recursive Descent Parser

/**
 * Handles identifiers, alphabet characters, and the accepting state marker
 *
 * atom -> identifier | alphabet_character | accepting_state
 * identifier -> non_alphabet_character
 *
 * @param state current state of the parser
 * @return the partially constructed grammar AST and updated parser state
 */
fun atom(state: ParserState): Pair<Grammar, ParserState> {
    var currentState = state
    var ret: Grammar? = null
    var curr: Grammar? = null

    while ((currentState.value?.type?.equals(TokenType.ID) == true) ||
        (currentState.value?.type?.equals(TokenType.AC) == true) ||
        (currentState.value?.type?.equals(TokenType.AS) == true)
    ) {
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

/**
 * Handles production rules
 *
 * Rule -> identifier '=' atom ('|' atom)*
 *
 * @param state current state of the parser
 * @return the partially constructed grammar AST and updated parser state
 */
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
        expr = Rule(expr, rule = rules)
    }

    return expr to currentState
}

/**
 * Runner function to perform the recursive descent parsing and to build the rule's vine (linked list)
 *
 * @param input list of tokens to be parsed
 * @return fully constructed grammar AST
 */
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

/**
 * Class to hold NFA nodes
 *
 * @param stateID id of the specific state
 * @param transitions labelled transitions from state to state
 * @param lambdaTransitions labelled lambda transitions from state to state
 */
data class NFANode(
    val stateID: String,
    val transitions: Map<String, Set<String>>,
    val lambdaTransitions: Set<String> = setOf(),
)

/**
 * NFA structure class
 *
 * @param startState state designated as start state
 * @param acceptStates set of states designated as final states
 * @param nfaNodes map of state id to node object
 */
class NFA(val startState: String, val acceptStates: Set<String>, val nfaNodes: Map<String, NFANode>) {
    /**
     * Tests if the NFA accepts the inputted string
     *
     * @param value the inputted string to test
     * @return true if accepted, false if not
     */
    fun accepts(value: String): Boolean {
        // set of all states to check with a cursor
        // the cursor states where in the value string the current "state" is at
        var currentStates = setOf(startState to 0)
        // once a state's cursor exceeds the length of the inputted string, it is in a finished state
        val finishedStates = mutableSetOf<String>()

        // run the automaton until there are no states to run for
        while (currentStates.isNotEmpty()) {
            currentStates = buildSet {
                for ((currentState, currentIndex) in currentStates) {
                    // if the current cursor location is still within the string, determine the next transition
                    // otherwise, add the string to the finish states, no more symbols can be inputted into the machine
                    if (currentIndex < value.length) {
                        val node = nfaNodes[currentState]!!
                        for ((transitionString, destinationNode) in node.transitions) {
                            if (currentIndex + transitionString.length <= value.length && value.substring(currentIndex..<currentIndex + transitionString.length) == transitionString) {
                                destinationNode.forEach {
                                    add(it to currentIndex + transitionString.length)
                                }
                            }
                        }
                        addAll(node.lambdaTransitions.map { it to currentIndex })
                    } else {
                        finishedStates.add(currentState)
                    }
                }
            }
        }

        // check if any of the finished states are in the accept states
        return finishedStates.any { it in acceptStates }
    }

    /**
     * @return string representation of the NFA
     */
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

/**
 * Converts from Grammar AST to NFA object
 *
 * @param grammar grammar to convert
 * @param startState state ID of the start state
 * @param defaultAcceptState state ID of the accept state
 * @return fully constructed NFA
 */
fun grammar2NFA(grammar: Grammar, startState: String, defaultAcceptState: String = "X"): NFA {
    // head of the grammar vine
    var current: Grammar? = grammar

    // set of all accepting states
    val acceptingStateSet = mutableSetOf(defaultAcceptState)
    // creation of map of state id to nfa object
    val nfaMap = buildMap {
        // add a default accept state to be used in certain cases [covered later]
        put(defaultAcceptState, NFANode(defaultAcceptState, emptyMap(), emptySet()))

        while (current != null) {
            // current set of lambda transitions
            val currentLambdaTransitions = mutableSetOf<String>()
            // current set of labelled transitions
            // set of nodes a transition goes to as this is an NFA
            val currentTransitions = mutableMapOf<String, Set<String>>()
            // current node in the vine
            val c = current
            // traverse to the next node in the vine
            current = current?.next
            when (c) {
                // if the current node is a rule
                is Rule -> {
                    // checking if the left hand side of the '=' is an identifier (resolver phase)
                    val currentState = when (val id = c.identifier) {
                        is Ident -> id.name
                        else -> error("lhs of a = must be an identifier")
                    }
                    // for every grammar node in the rule
                    for (g in c.rule) {
                        when (g) {
                            // set this current state as an accepting state
                            is AcceptingState -> acceptingStateSet.add(currentState)
                            // set this current state to have a lambda transition to the identifier
                            is Ident -> currentLambdaTransitions.add(g.name)
                            // construct the full alphabet multi-character transition
                            is AlphabetCharacter -> {
                                var currentChar: Grammar? = g
                                var sum = ""
                                while (currentChar != null) {
                                    when (currentChar) {
                                        // if the current character is an alphabet character, add it to the running sum
                                        is AlphabetCharacter -> {
                                            sum += currentChar.character
                                            currentChar = currentChar.next
                                            // if the next character is null, this is a terminal branch, add a transition to the default accept state to follow the right linear grammar syntax
                                            if (currentChar == null) {
                                                currentTransitions[sum] = currentTransitions.getOrDefault(sum, emptySet()) union setOf(defaultAcceptState)
                                            }
                                        }

                                        // current character is an identifier
                                        // assume this is the end of the string as the grammar parser can only handle right linear grammars
                                        // add a transition to the identifier
                                        is Ident -> {
                                            currentTransitions[sum] = currentTransitions.getOrDefault(sum, emptySet()) union setOf(currentChar.name)
                                            currentChar = currentChar.next
                                        }

                                        // rules cannot be nested
                                        else -> error("cannot nest rule")
                                    }
                                }
                            }

                            // rules cannot be nested
                            is Rule -> error("cannot nest rule")
                        }
                        // tie the state id to the constructed NFA object
                        put(currentState, NFANode(currentState, currentTransitions, currentLambdaTransitions))
                    }
                }

                // at top level, the grammar should only be consisting of rules
                else -> error("top level should be rule")
            }
        }
    }

    // given the start state, set of accepting states, and nfa map, construct the NFA object
    return NFA(startState, acceptingStateSet, nfaMap)
}

/**
 * Helper function to enumerate over all strings in the alphabet up to a certain length
 *
 * \forall w : w \in L \and |w| \le n
 *
 * @param size max length of the string
 * @param alphabet set of all alphabet characters
 * @return sequence of all enumerated strings
 */
fun enumerate(size: Int, alphabet: Set<Char>): Sequence<String> {
    return sequence {
        // memory of previous strings yielded in the enumeration
        var prev = listOf("")

        // repeat until the string is the desired length
        repeat(size) {
            // yield the next permutation of the string with the alphabet and memoize the result to be use in the next loop
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
    var i = 0
    val currentGrammar = mutableListOf<String>()
    var currentAlphabet = setOf('0', '1')
    var currentStartState = "S"
    var currentAcceptingState = "X"
    var currentAcceptedStrings = listOf<String>()
    var currentRejectedStrings = listOf<String>()
    while (true) {
        print("[${i++ + 1}]>>> ")
        val line = readln()
        when (val l = line.trim()) {
            ":f", ":F", ":fail" -> wrap("Rejected Strings") { currentRejectedStrings.forEach(::println) }
            ":h", ":H", ":help" -> println(
                """
                Grammar to NFA automation program v0.0.1
                    Commands:
                        :a / :A / :alpha c1 c2 ... cn - sets the alphabet to the set of c1 c2 ... cn (must be space separated)
                        :d / :D / :delete index       - removes the production rule at the specified index (0-based indexing)
                        :e / :E / :exec n             - executes the current grammar with an enumeration of all strings consisting of strings of length n
                        :f / :F / :fail               - lists all strings that failed the last exeuction of the grammar
                        :h / :H / :help               - prints out this help message
                        :l / :L / :list               - prints out the current grammar
                        :p / :P / :pass               - lists all strings that passed the last execution of the grammar
                        :q / :Q / :quit               - quits the REPL
                        :r / :R / :reset              - clears the current grammar of all production rules
                        :s / :S / :start str          - sets the start string to the value of str
                        :u / :U / :undo               - removes the last production rule
                        :x / :X / :accept str         - sets the accepting string to the value of str
                    
                    All other strings that do not start with : are considered production rules in the grammar
                    
                    Current Limitations:
                        Very sparse syntax error checking in grammar strings
                        Overriding of production rules is not possible
                        No error checking when using commands, may hard crash
                """.trimIndent()
            )

            ":l", ":L", ":list" -> currentGrammar.forEachIndexed { index, s -> println("$index: $s") }
            ":p", ":P", ":pass" -> wrap("Accepted Strings") { currentAcceptedStrings.forEach(::println) }
            ":q", ":Q", ":quit" -> kotlin.system.exitProcess(0)
            else -> l.split(" ").run {
                when (this[0]) {
                    ":a", ":A", ":alpha" -> currentAlphabet = this.drop(1).map { it[0] }.toSet()
                    ":d", ":D", ":delete" -> currentGrammar.removeAt(this[1].toInt())
                    ":e", ":E", ":exec" -> {
                        val n = this.getOrElse(1) {
                            "5"
                        }.toInt()
                        val tokens = lex(
                            currentGrammar.joinToString(separator = "\n", prefix = "", postfix = ""),
                            currentAlphabet,
                            currentAcceptingState
                        )
                        val tree = parse(tokens).also { wrap("Grammar") { println(it) } }
                        val nfa = grammar2NFA(tree, currentStartState).also { wrap("NFA") { println(it) } }
                        enumerate(n, currentAlphabet).partition {
                            nfa.accepts(it)
                        }.run {
                            currentAcceptedStrings = first
                            currentRejectedStrings = second
                        }
                    }

                    ":r", ":R", ":reset" -> currentGrammar.clear()
                    ":s", ":S", ":start" -> currentStartState = this[1]
                    ":u", ":U", ":undo" -> currentGrammar.removeLast()
                    ":x", ":X", ":accept" -> currentAcceptingState = this[1]
                    else -> if (l.startsWith(":")) {
                        println("Invalid Command \"$l\"")
                    } else {
                        currentGrammar += l
                    }
                }
            }
        }
    }
}