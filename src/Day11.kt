
fun main() {

    class Monkey(
        val id: Int,
        val operation: (Long) -> Long,
        val test: (Long) -> Int,
        var Items: ArrayDeque<Long>,
        var div: Long,
        var activity: Long
    )


    fun part1(input: List<String>): Long {

        return 0.toLong()
    }


    fun part2(input: List<String>): Long {
        val MonkeyList = mutableListOf<Monkey>()

        input.chunked(7).forEach { mi ->
            val id = mi[0].filter { it.isDigit() }.toInt()
            val items = mi[1].substringAfter(":").split(",").map { it.trim().toLong() }.toCollection(ArrayDeque())
            var operationParts = mi[2].substringAfter("= ").split(" ")
            val operationFunction = fun(w: Long): Long {
                val rightValue = if (operationParts[2] == "old") {
                    w
                } else {
                    operationParts[2].toLong()
                }
                return when (operationParts[1]) {
                    "+" -> w + rightValue
                    "*" -> w * rightValue
                    else -> w
                }
            }

            var mod = mi[3].substringAfter(": ").filter { it.isDigit() }.toLong()
            var t = mi[4].filter { it.isDigit() }.toInt()
            val f = mi[5].filter { it.isDigit() }.toInt()
            val testFunction = fun(w: Long): Int {
                return if (w % mod == 0.toLong()) {
                    t
                } else {
                    f
                }
            }
            MonkeyList.add(Monkey(id, operationFunction, testFunction, items, mod, 0.toLong()))
        }

        val cmod = MonkeyList.fold(1.toLong()) { acc, it ->  acc * it.div }
        for (i in 1..10000) {
            MonkeyList.forEach { monkey ->
                for (j in 0 until monkey.Items.size) {
                    monkey.activity++
                    val item = monkey.Items.removeFirst()
                    var newWorryLevel = monkey.operation(item).mod(cmod)
                    val toMonkey = monkey.test(newWorryLevel)
                    MonkeyList[toMonkey].Items.add(newWorryLevel)
                }

            }
        }

        var sort = MonkeyList.sortedByDescending { it.activity }

        return sort[0].activity * sort[1].activity
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
//    check(part1(testInput) == (10605).toLong())
   check(part2(testInput) == (2713310158).toLong())


    val input = readInput("Day11")
//    println(part1(input))
    println(part2(input))


}
