public class Lexer(val code : String, private val DEBUG : Boolean = false) {
    private var pos : Int = 0
    private var stringNumber = 0;
    private var stringPos = 0;
    private var tokenListAll = mutableListOf<Token>()

    private fun filterTokenList(tokenList : MutableList<Token>) : List<Token> {
        return tokenList.filter { token -> token.type.name !in listOf(tokensMap["SPACE"]!!.name, tokensMap["comment"]!!.name) }
    }

    fun lexicalAnalysis() : List<Token> {
        while (nextToken());
        return filterTokenList(tokenListAll)
    }

    fun getFullTokenList() : List<Token> = tokenListAll.toList()

    private fun nextToken() : Boolean {
        if (pos >= code.length) return false
        for (i in tokensMap.values) {
            val regex = Regex("^" + i.regex.pattern)
            val result = regex.find(code.substring(pos))
            if (result?.value != null) {
                val token:Token
                var tokenText = result.value
                if (result.value == "\n"){
                    tokenText = "\\n"
                    token = Token(i, tokenText, stringNumber, stringPos, pos)
                    stringNumber++
                    stringPos = 0
                } else{
                    token = Token(i, tokenText, stringNumber, stringPos, pos)
                }
                pos += result.value.length
                stringPos++
                tokenListAll.add(token)
                if (token.type.group == "spaces"){
                    stringPos--
                }
                if (DEBUG) {
                    println("Token = ${token.aboutMe()}".replace("\n", "\\n"))
                }
                return true
            }
        }
        throw Error("LEXER ERROR: Check position $pos! There is a lexical error")
    }
}
