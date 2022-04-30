public class Lexer(val code: String) {
    var pos: Int = 0
    val TokenList = mutableListOf<Token>()
    fun lexicalAnalysis(): List<Token> {
        while (nextToken()) {
            println("Токен $pos")
        }
        return TokenList
    }

    private fun nextToken(): Boolean {
        if (pos >= code.length) {
            return false
        }
        for (i in tokenTypeMap.values) {
            val regex = Regex("^" + i.regex.pattern)

        }
        return true
    }
}
