import java.util.ArrayDeque

val OPERATORS = arrayOf("+", "-", "*", "/", "==", "!=", ">=", ">", "<=", "<", "||", "&&")
val OPERATORS_PRIORITY = mapOf(
        "||" to 1, "&&" to 1, "==" to 2, "!=" to 2, ">=" to 2, ">" to 2, "<=" to 2, "<" to 2, "+" to 3, "-" to 3, "*" to 4, "/" to 4,
)

class Parser(private val tokens : List<Token>, private val DEBUG : Boolean = false) {
    private var pos : Int = 0
    private val scope = mutableMapOf<String, Any>()
    private fun match(vararg expected : String) : Token? {
        // return Token if currentToken is one of expected else null
        if (pos >= tokens.size) {
            return null
        }

        val currentToken = tokens[pos]
        if (expected.find { type -> tokensMap[type]!!.name == currentToken.type.name } != null) {
            pos++

            return currentToken
        }

        return null
    }

    private fun require(vararg expected : String) : Token {
        // return Token if currentToken is one of expected else throw exception
        return match(*expected) ?: throw Exception("Check string ${tokens[pos].stringNumber}, position ${tokens[pos].stringPos}. Expected ${tokensMap[expected[0]]?.name} but found ${tokens[pos].text}")
    }

    fun run() {
        while (pos < tokens.size) {
            parsing()
        }
    }

    private fun parsing() {
        if (match("var") != null) {
            if (DEBUG) println("STARTED VAR INITIALIZING PARSING")
            parseVarInitializing()
        } else if (match("print") != null) {
            if (DEBUG) println("STARTED PRINT PARSING")
            parsePrint()
        } else if (match("identifier") != null) {
            if (DEBUG) println("STARTED ASSIGMENT PARSING")
            parseAssignment()
        } else if (match("if") != null) {
            if (DEBUG) println("STARTED PARSING CONDITIONAL")
            parseIf()
        } else if (match("while") != null) {
            if (DEBUG) println("STARTED LOOP PARSING")
            parseLoop()
        } else pos++

    }

    private fun parseVarInitializing() {
        val variableName = require("identifier").text
        require(":")
        val varType = require("Int", "Array").text
        if (varType == "Int") {
            require("=")
            val variableValue = parseExpression()!!
            scope[variableName] = variableValue
        } else {
            val size = parseExpression() as Int
            scope[variableName] = Array<Int>(size) { 0 }
        }
    }

    private fun parsePrint() {
        val expression = parseExpression()
        if (expression == null) {
            if (DEBUG) {
                println("Empty output")
            }
            println("")
        } else {
            println("$expression")
        }
    }

    private fun parseAssignment() {
        val variableName : String = tokens[pos - 1].text
        var index = -1
        if (match("[") != null) {
            index = parseExpression() as Int
        }
        require("=")
        val variableValue = parseExpression() as Int
        if (scope[variableName] != null) {
            if (scope[variableName] is Array<*>) {
                if (index != -1) {
                    (scope[variableName] as Array<Int>)[index] = variableValue
                }
            } else {
                scope[variableName] = variableValue
            }
        } else throw Exception("Variable $variableName is not declared!")
    }

    private fun parseExpression() : Any? {
        if (match("input") != null) {
            return readLine()?.toInt()
        }
        val operatorsStack = ArrayDeque<String>()
        val numberStack = ArrayDeque<Int>()
        var currentToken = require("number", "identifier", "(")
        while (currentToken.text !in listOf(";", "{", "]")) {
//            if (DEBUG) println(currentToken.aboutMe())
            if (currentToken.text == "(") {
                operatorsStack.push("(")
            } else if (currentToken.text == ")") {
                var currentOperator = operatorsStack.pop()
                while (currentOperator != "(") {
                    val result = calculateExpression(numberStack.pop(), numberStack.pop(), currentOperator)
                    numberStack.push(result)
                    currentOperator = operatorsStack.pop()
                }
            } else if (currentToken.type.group == "operators" || currentToken.type.group == "BooleanOperators") {
                if (operatorsStack.isEmpty() || operatorsStack.first == "(") operatorsStack.push(currentToken.text)
                else {
                    val currentOperatorPriority = OPERATORS_PRIORITY[currentToken.text]!!
                    val stackOperatorPriority = OPERATORS_PRIORITY[operatorsStack.first]!!
                    if (currentOperatorPriority > stackOperatorPriority) {
                        operatorsStack.push(currentToken.text)
                    } else {
                        val result = calculateExpression(numberStack.pop(), numberStack.pop(), operatorsStack.pop())
                        numberStack.push(result)
                        operatorsStack.push(currentToken.text)
                    }
                }
            } else if (currentToken.type.name == "NUMBER") {
                numberStack.push(currentToken.text.toInt())
            } else if (currentToken.type.name == "IDENT_NAME") {
                if (scope[currentToken.text] == null) {
                    throw Exception("Check ${currentToken.stringNumber} string, ${currentToken.stringPos} pos. Variable isn't declared")
                }
                if (match("[") != null) {
                    var index = parseExpression() as Int
                    numberStack.push((scope[currentToken.text] as Array<Int>)[index])
                } else {
                    numberStack.push(scope[currentToken.text] as Int)
                }
            }
            currentToken = require("number", "identifier", "[", "]", "(", ")", ";", *OPERATORS, "{")
        }
        while (!operatorsStack.isEmpty()) {
            val result = calculateExpression(numberStack.pop(), numberStack.pop(), operatorsStack.pop())
            numberStack.push(result)
        }
        return numberStack.pop()
    }



    private fun calculateExpression(number1 : Int, number2 : Int, operator : String) : Int {
        when (operator) {
            "+" -> {
                return number2 + number1
            }
            "-" -> {
                return number2 - number1
            }
            "*" -> {
                return number2 * number1
            }
            "/" -> {
                return number2 / number1
            }
            "%" -> {
                return number2 % number1
            }
            "!=" -> {
                return if (number2 != number1) 1 else 0
            }
            "==" -> {
                return if (number2 == number1) 1 else 0
            }
            ">=" -> {
                return if (number2 >= number1) 1 else 0
            }
            "<=" -> {
                return if (number2 <= number1) 1 else 0
            }
            ">" -> {
                return if (number2 > number1) 1 else 0
            }
            "<" -> {
                return if (number2 < number1) 1 else 0
            }
            "||" -> {
                return if ((number1 != 0) || (number2 != 0)) 1 else 0
            }
            "&&" -> {
                return if ((number1 != 0) && (number2 != 0)) 1 else 0
            }
            else -> throw Error("Operator '$operator' isn't supported")
        }
    }

    private fun skipBlock() {
        var openBraces = 1
        var closingBraces = 0
        while (openBraces != closingBraces) {
            if (match("}") != null) closingBraces++
            else if (match("{") != null) openBraces++
            else pos++
        }
    }

    private fun parseLoop() {
        val startConditionPos = pos
        var isTrue : Boolean = parseExpression() as Int != 0
        if (DEBUG) println("Loop condition = $isTrue")
        while (isTrue) {
            while (match("}") == null) {
                parsing()
            }
            pos = startConditionPos
            isTrue = parseExpression() as Int != 0

            if (DEBUG) println("Loop condition = $isTrue")
        }
        skipBlock()

    }

    private fun parseIf() {
        val isTrue : Boolean = parseExpression() as Int != 0
        if (DEBUG) println("`If` condition = $isTrue")
        if (isTrue) {
            while (match("}") == null) {
                parsing()
            }
            if (match("else") != null) {
                require("{")
                skipBlock()
            }
        } else {
            skipBlock()
            if (match("else") != null) {
                while (match("}") == null) {
                    parsing()
                }
            }
        }
    }
}