fun main() {


    fun part1(input: List<String>): Int {
        val stack = ArrayDeque<List<Int>>()
        var i = 0
        input.forEach {
            stack.add(i, it.toList().map { c -> c.digitToInt() })
            i++
        }
        val trees = stack.mapIndexed { n, cur ->
            val nextRows = stack.filterIndexed { i, _ -> i > n }
            val prevRows = stack.filterIndexed { i, _ -> i < n }
            cur.filterIndexed { j, c ->
                // Find all visibles

                if (prevRows.isEmpty() || nextRows.isEmpty() || j == 0 || j == cur.size - 1) {
                    return@filterIndexed true
                }
                // look up to the end of edge
                if (prevRows.find { it[j] >= c } == null) {
                    return@filterIndexed true
                }

                // look down to the end of edge
                if (nextRows.find { it[j] >= c } == null) {
                    return@filterIndexed true
                }

                // look left to the end of the edge
                if (cur.subList(0, j).find { it >= c } == null) {
                    return@filterIndexed true
                }

                // look right to the end of the edge
                if (cur.subList(j + 1, cur.size).find { it >= c } == null) {
                    return@filterIndexed true
                }

                false
            }.count()
        }.sum()

        return trees
    }


    fun part2(input: List<String>): Int {
        val stack = ArrayDeque<List<Int>>()
        var i = 0
        input.forEach {
            stack.add(i, it.toList().map { c -> c.digitToInt() })
            i++
        }
        val trees = stack.mapIndexed { n, cur ->
            val nextRows = stack.filterIndexed { i, _ -> i > n }
            val prevRows = stack.filterIndexed { i, _ -> i < n }

            cur.mapIndexed { j, c ->
                // Find all visibles
                if (prevRows.isEmpty() || nextRows.isEmpty() || j == 0 || j == cur.size - 1) {
                    return@mapIndexed 0
                }

                // look up to the end of edge
                var up = 0
                prevRows.reversed().find { up++; it[j] >= c  }
                // look down to the end of edge
                var down = 0
                nextRows.find { down++; it[j] >= c }
                // look left to the end of the edge
                var left = 0
                cur.subList(0, j).reversed().find { left++; it >= c }
                // look right to the end of the edge
                var right = 0
                cur.subList(j + 1, cur.size).find { right++; it >= c }

                up * down * left * right
            }.max()
        }.max()

        return trees
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)


    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))


}
