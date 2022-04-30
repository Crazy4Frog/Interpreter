import kotlin.collections.ArrayDeque
class ReversePolishNotation(val input:String) {
    var output: String = ""
    val stackOperator = ArrayDeque<Operator>()

}

class Operator(val priority: Int, val symbol: String) {}
class ContainerOfOperators() {
    private val operators = mutableListOf<Operator>()
    private fun fillOperators(){
        addOperator(Operator(3, "*"))
        addOperator(Operator(3, "/"))
        addOperator(Operator(2, "+"))
        addOperator(Operator(2, "-"))
        addOperator(Operator(1, "1"))
    }

    fun addOperator(operator: Operator) {
        operators.add(operator)
    }

    fun findOperator(symbol: String): Operator? {
        for (item in operators) {
            if (item.symbol == symbol) {
                return item
            }
        }
        return null
    }
    init {
        fillOperators()
    }
}
