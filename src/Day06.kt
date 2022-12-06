fun main() {

    fun findAnswer(s: String, l: Int): Int? {
        val b = ArrayDeque<Char>()
        s.toList().forEachIndexed { i, c ->
            b.addFirst(c)
            if (b.toSet().count() == l) {
                return i+1
            }
            if (b.count() == l) {
                b.removeLast()
            }
        }
        return null
    }

    fun part1(input: List<String>): Int {

        val results = input.map {
            findAnswer(it,4)
        }.filterNotNull()

        return results[0]
    }


    fun part2(input: List<String>): Int {
        val results = input.map {
            findAnswer(it,14)
        }.filterNotNull()

        return results[0]
    }

// test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day06_test")
//    check(part1(testInput) == 7)
//    check(part2(testInput) == 2)


    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))


}
