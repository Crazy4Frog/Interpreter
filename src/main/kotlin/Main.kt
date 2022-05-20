import java.io.File

fun main() {
    val code = File("./src/main/kotlin/code.txt").readText(charset = Charsets.UTF_8)
    val lexer = Lexer(code, DEBUG = false)
    val tokens = lexer.lexicalAnalysis()
//    tokens.forEach { x -> println(x.aboutMe()) }

    val parser = Parser(tokens, DEBUG = true)
    parser.run()
    println("End of the program...")
}

