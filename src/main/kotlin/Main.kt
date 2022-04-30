fun main(args: Array<String>) {
    println("Hello World!")

    tokenTypeMap.values.forEach { x -> x.regex.pattern = "^" + x.regex.pattern }
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
}
fun sum(a:Int, b:Int):Int{
    return a + b
}
