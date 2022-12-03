fun main() {


    val az = ('a'..'z').toList() + ('A'..'Z').toList()

    fun part1(input: List<String>): Int {
        return input.map {
            it.chunked(it.length / 2)
                .reduce { acc, string -> string.toList().intersect(acc.toList().toSet()).first().toString() }.first()
        }.fold(0) { acc: Int, c: Char -> acc + az.indexOf(c) + 1 }


    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf {
            az.indexOf(it[0].toList().filter { ch ->
                it.slice(1 until it.lastIndex + 1).filter { s -> s.toList().contains(ch) }.size == 2
            }.distinct().first()) + 1
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))


}
