public class TokenType(val name: String, val regex: Regex) {
}

public val tokenTypeMap: Map<String, TokenType>
    get() = mapOf(
        "LOG" to TokenType("LOG", Regex("LOG")),
        "NUMBER" to TokenType("NUMBER", Regex("0|\\d+")),
        "VARIABLE" to TokenType("VARIABLE", Regex("[A-Za-z_][A-Za-z\\d_]*")),
        "SEMICOLON" to TokenType("SEMICOLON", Regex(";")),
        "SPACE" to TokenType("SPACE", Regex("[ \\n\\t\\r]")),
        "ASSIGN" to TokenType("ASSIGN", Regex("=")),
        "PLUS" to TokenType("PLUS", Regex("\\+")),
        "MINUS" to TokenType("MINUS", Regex("-")),
        "LBRACKET" to TokenType("LBRACKET", Regex("\\(")),
        "RBRACKET" to TokenType("RBRACKET", Regex("\\)")),
        "MULTIPLY" to TokenType("MULTIPLY", Regex("\\*")),
        "DIV" to TokenType("DIV", Regex("/")),
        "MOD" to TokenType("MOD", Regex("%")),
    )