fun main() {


    fun part1(input: List<String>): Int {
        var clock = 0
        var registry = 1
        var total = 0
        input.forEach { l ->
            var calculated = false
            if (l == "noop") {
                clock++
            } else {
                for (i in 1..2) {
                    clock++
                    if ((clock == 20 || ((clock - 20) % 40) == 0) && clock <= 220) {
                        total += clock * registry
                        calculated = true
                    }
                }
                registry += l.split(" ").last().toInt()
            }

            if (!calculated && ((clock == 20 || ((clock - 20) % 40) == 0) && clock <= 220)) {
                total += clock * registry
            }
        }

        return total
    }

    fun draw(sprite: IntRange, clock: Int): Int {
        if (sprite.contains(clock)) {
            print('#')
        } else {
            print(' ')
        }
        if (clock % 40 == 0) {
            println()
            // Reset clock
            return 0
        }
        return clock
    }

    fun part2(input: List<String>): Int {
        var clock = 0
        var crtClock = 0
        var registry = 1
        var sprite = 0..3
        input.forEach { l ->
            var drawn = false
            if (l == "noop") {
                clock++
                crtClock++

            } else {
                for (i in 1..2) {
                    clock++
                    crtClock++
                    crtClock = draw(sprite, crtClock)
                    drawn = true
                }
                registry += l.split(" ").last().toInt()
                sprite = registry..registry + 2
            }
            if (!drawn) {
                draw(sprite, crtClock)
            }
        }

        return 0
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    part2(testInput)


    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))


}
