fun main() {

    fun part1(input: List<String>): String {
        val stacks = mutableListOf<ArrayDeque<Char>>()
        var totalIndex = 0
        run collectStack@{
            input.toList().forEach {
                totalIndex++
                var stackIndex = 0
                it.chunked(4).forEach { stackInput ->
                    val d = stacks.getOrElse(stackIndex) { ArrayDeque() }
                    if (stackInput[1].toString().isNotBlank()) {
                        if (stackInput[1].toString().toIntOrNull() != null) {
                            // WE collected the stack now work with it
                            return@collectStack
                        }
                        d.add(stackInput[1])
                    }
                    if (stacks.indices.contains(stackIndex)) {
                        stacks[stackIndex] = d
                    } else {
                        stacks.add(stackIndex, d)
                    }
                    stackIndex++
                }
            }
        }

        val r = Regex("move\\s(\\d+)\\sfrom\\s(\\d+)\\sto\\s(\\d+)")
        input.toList().subList(totalIndex, input.size).forEach {
            val result = r.find(it)
            if (result != null) {
                val (amount, from, to) = result.destructured
                var total = amount.toInt()
                while (total > 0) {
                    stacks[to.toInt() - 1].addFirst(stacks[from.toInt() - 1].removeFirst())
                    total--
                }
            }

        }

        return stacks.fold("".toCharArray()) { acc, s -> acc.plus(s.first()) }.joinToString("")
    }


    fun part2(input: List<String>): String {

        val stacks = mutableListOf<ArrayDeque<Char>>()
        var totalIndex = 0
        run collectStack@{
            input.toList().forEach {
                totalIndex++
                var stackIndex = 0
                it.chunked(4).forEach { stackInput ->
                    val d = stacks.getOrElse(stackIndex) { ArrayDeque() }
                    if (stackInput[1].toString().isNotBlank()) {
                        if (stackInput[1].toString().toIntOrNull() != null) {
                            // WE collected the stack now work with it
                            return@collectStack
                        }
                        d.add(stackInput[1])
                    }
                    if (stacks.indices.contains(stackIndex)) {
                        stacks[stackIndex] = d
                    } else {
                        stacks.add(stackIndex, d)
                    }
                    stackIndex++
                }
            }
        }

        val r = Regex("move\\s(\\d+)\\sfrom\\s(\\d+)\\sto\\s(\\d+)")
        input.toList().subList(totalIndex, input.size).forEach { it ->
            val result = r.find(it)
            if (result != null) {
                val (amount, from, to) = result.destructured
                var total = amount.toInt()
                val extracted = mutableListOf<Char>()
                while (total > 0) {
                    extracted.add(stacks[from.toInt() - 1].removeFirst())
                    total--
                }
              extracted.reversed().forEach { ch -> stacks[to.toInt() - 1].addFirst(ch) }

            }

        }

        return stacks.fold("".toCharArray()) { acc, s -> acc.plus(s.first()) }.joinToString("")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")


    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))


}
