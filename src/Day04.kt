
fun main() {

    fun part1(input: List<String>): Int {
        var c = 0
        input.forEach {
            val row = it.split(',')
            val input1 = row[0].split('-').map { s -> s.toInt() }
            val input2 = row[1].split('-').map { s -> s.toInt() }

            val r1 = input1[0]..input1[1]
            val r2 = input2[0]..input2[1]

            val diff1 = r1 - r2
            val diff2 = r2 - r1
            if (diff1.isEmpty() || diff2.isEmpty()) {
                c++
            }
        }
        return c
    }


    fun part2(input: List<String>): Int {
        var c = 0
        input.forEach {
            val row = it.split(',')
            val input1 = row[0].split('-').map { s -> s.toInt() }
            val input2 = row[1].split('-').map { s -> s.toInt() }

            val r1 = input1[0]..input1[1]
            val r2 = input2[0]..input2[1]

            if (r1.toSet().intersect(r2.toSet()).isNotEmpty()) {
                c++
            }
        }
        return c
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))


}
