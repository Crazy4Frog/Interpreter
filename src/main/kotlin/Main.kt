import java.io.File

fun main(args : Array<String>) {
    val code = File("./src/main/kotlin/code.txt").readText(charset = Charsets.UTF_8)
    val lexer = Lexer(code, DEBUG = false)
    val tokens = lexer.lexicalAnalysis()
//    tokens.forEach { x -> println(x.aboutMe()) }

    val parser = Parser(tokens, DEBUG = true)
    parser.run()
//    val parser = Parser(tokens, true)
//    val rootNode = parser.parseCode()
//    println("\n ${((rootNode as StatementsNode).codeStrings[0] as BinaryOperatorNode).rightNode}")
//    parser.run(rootNode)
}

//    val regex = Regex("^" + tokenTypeMap["NUMBER"]?.regex?.pattern)
// Try adding program arguments via Run/Debug configuration.
// Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
