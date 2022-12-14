import kotlin.math.abs
import kotlin.math.max

typealias Dot = Pair<Int, Int>

fun main() {
    fun moveDot(a: Dot, b: Dot): Dot {
        return Dot(a.first + b.first, a.second + b.second)
    }

    fun dotEquals(a: Dot, b: Dot): Boolean {
        return a.first == b.first && a.second == b.second
    }

    fun part1(input: List<String>): Int {
        val walls = input.map {
            it.split(" -> ").map {
                val coord = it.split(",")
                Dot(coord[0].toInt(), coord[1].toInt())

            }
        }.let { r ->
            r.map { d ->
                d.mapIndexed { idx, cur ->
                    val line = mutableSetOf(cur)
                    val next = d.getOrNull(idx + 1)
                    if (next != null) {
                        val xDiff = next.first - cur.first
                        val yDiff = next.second - cur.second
                        val xDir = if (xDiff > 0) 1 else if (xDiff < 0) -1 else 0
                        val yDir = if (yDiff > 0) 1 else if (yDiff < 0) -1 else 0
                        val xSteps = abs(xDiff)
                        val ySteps = abs(yDiff)
                        val xStep = if (xSteps > 0) xDir else 0
                        val yStep = if (ySteps > 0) yDir else 0
                        for (i in 1..max(xSteps, ySteps)) {
                            line.add(moveDot(line.last(), Dot(xStep, yStep)))
                        }
                    }
                    line
                }.flatten().toSet()
            }.flatten().toSet()
        }

        val maxY = walls.maxOf { it.second }

        // Begin loop
        var movement = true
        val startSand = Dot(500, 0)
        val sands = mutableSetOf<Pair<Dot, Boolean>>(Pair(startSand, true))
        val movements = listOf(Pair(0, 1), Pair(-1, 1), Pair(1, 1))
        while (movement) {
            var s = sands.find { it.second }
            if (s == null) {
                s = Pair(startSand, true)
                sands.add(s)
            }
            // move it
            var moved = false
            run breaking@{
                movements.forEach {
                    val next = moveDot(s.first, it)
                    if (!walls.contains(next) && !sands.any { sand -> dotEquals(sand.first, next) }) {
                        // We can moveit
                        sands.remove(s)
                        sands.add(Pair(next, true))
                        moved = true
                        return@breaking
                    }
                }
            }
            if (!moved) {
                // We can't move it so stop it
                sands.remove(s)
                sands.add(Pair(s.first, false))
            } else {
                // check if we are out of bounds
                if (sands.last().first.second > maxY) {
                    // we are out of bounds
                    movement = false
                }
            }
        }

        return sands.size - 1

    }


    fun part2(input: List<String>): Int {
        val walls = input.map {
            it.split(" -> ").map {
                val coord = it.split(",")
                Dot(coord[0].toInt(), coord[1].toInt())

            }
        }.let { r ->
            r.map { d ->
                d.mapIndexed { idx, cur ->
                    val line = mutableSetOf(cur)
                    val next = d.getOrNull(idx + 1)
                    if (next != null) {
                        val xDiff = next.first - cur.first
                        val yDiff = next.second - cur.second
                        val xDir = if (xDiff > 0) 1 else if (xDiff < 0) -1 else 0
                        val yDir = if (yDiff > 0) 1 else if (yDiff < 0) -1 else 0
                        val xSteps = abs(xDiff)
                        val ySteps = abs(yDiff)
                        val xStep = if (xSteps > 0) xDir else 0
                        val yStep = if (ySteps > 0) yDir else 0
                        for (i in 1..max(xSteps, ySteps)) {
                            line.add(moveDot(line.last(), Dot(xStep, yStep)))
                        }
                    }
                    line
                }.flatten().toSet()
            }.flatten().toSet()
        }

        val maxY = walls.maxOf { it.second }

        // Begin loop
        var movement = true
        val startSand = Dot(500, 0)
        var movingSand = startSand
        val restedSands = mutableSetOf<Dot>()
        val movements = listOf(Pair(0, 1), Pair(-1, 1), Pair(1, 1))
        while (movement) {
            // move it
            var moved = false
            run breaking@{
                movements.forEach {
                    val next = moveDot(movingSand, it)
                    if (next.second < (maxY + 2) && !walls.contains(next) && !restedSands.any { sand ->
                            dotEquals(
                                sand,
                                next
                            )
                        }) {
                        // We can moveit
                        movingSand = next
                        moved = true
                        return@breaking
                    }
                }
            }
            if (!moved) {
                if (movingSand == startSand) {
                    movement = false
                }
                restedSands.add(movingSand)
                movingSand = startSand
            }


        }

        return restedSands.size

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    //   check(part1(testInput) == 24)
   check(part2(testInput) == 93)

    val input = readInput("Day14")
    //   println(part1(input))
    println(part2(input))

}
