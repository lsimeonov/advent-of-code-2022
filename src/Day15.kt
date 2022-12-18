import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

enum class Direction(var x: Int, var y: Int) {
    RIGHT(1, 0),
    DOWN(0, -1),
    LEFT(-1, 0),
    UP(0, 1);

    fun turnCw() =
        when (this) {
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
            UP -> RIGHT
        }

    fun turnCcw() =
        when (this) {
            RIGHT -> UP
            DOWN -> RIGHT
            LEFT -> DOWN
            UP -> LEFT
        }

    fun flip() =
        when (this) {
            RIGHT -> LEFT
            LEFT -> RIGHT
            UP -> DOWN
            DOWN -> UP
        }

    fun toVec() = IntVec(x, y)

    companion object {
        fun fromNSEW(ch: Char) =
            when (ch) {
                'N' -> UP
                'S' -> DOWN
                'E' -> RIGHT
                'W' -> LEFT
                else -> throw Exception("unkown direction ${ch}")
            }


        fun fromUDLR(ch: Char) =
            when (ch) {
                'U' -> UP
                'D' -> DOWN
                'R', 'F' -> RIGHT
                'L' -> LEFT
                else -> throw Exception("unkown direction ${ch}")

            }


        fun setYDown() {
            DOWN.y = 1
            UP.y = -1
        }
    }
}

data class IntVec(val x: Int, val y: Int) {
    operator fun plus(other: IntVec) = IntVec(x + other.x, y + other.y)
    operator fun plus(dir: Direction) = IntVec(x + dir.x, y + dir.y)

    operator fun minus(other: IntVec) = IntVec(x - other.x, y - other.y)
    operator fun unaryMinus() = IntVec(-x, -y)

    operator fun times(factor: Int) = IntVec(x * factor, y * factor)

    fun manhattan(other: IntVec) = abs(x - other.x) + abs(y - other.y)
    fun manhattan() = manhattan(zero)
    fun chebyshev(other: IntVec) = max(abs(x - other.x), abs(y - other.y))
    fun chebyshev() = chebyshev(zero)

    fun mirrorX(axis: Int = 0) = IntVec(axis - (x - axis), y)
    fun mirrorY(axis: Int = 0) = IntVec(x, axis - (y - axis))

    // Note, it is inclusive, so for indexing reduce with 1
    fun withinBounds(bounds: IntVec) = withinBounds(0, bounds.x, 0, bounds.y)
    fun withinBounds(minX: Int, maxX: Int, minY: Int, maxY: Int) =
        x in minX..maxX && y in minY..maxY

    fun neighbors() = Direction.values().map { this + it }
    fun neighbors(bounds: IntVec) = Direction.values().map { this + it }
        .filter { it.withinBounds(bounds) }

    fun neighbors9() = (-1..1)
        .flatMap { x ->
            (-1..1)
                .map { y -> IntVec(x, y) }
        }
        .filter { it.x != 0 || it.y != 0 }
        .map { this + it }

    companion object {
        val zero = IntVec(0, 0)

        fun fromStr(str: String, delim: String = ","): IntVec {
            val split = str.split(delim)
            return IntVec(split[0].toInt(), split[1].toInt())
        }

        fun String.toIntVec(delim: String = ",") = fromStr(this, delim)

        /**
         * Calculates the min and max of x and y for all the vectors
         */
        fun Iterable<IntVec>.bounds() = this.fold(
            listOf(
                Int.MAX_VALUE,
                Int.MIN_VALUE,
                Int.MAX_VALUE,
                Int.MIN_VALUE
            )
        ) { acc, vec ->
            listOf(
                min(acc[0], vec.x),
                max(acc[1], vec.x),
                min(acc[2], vec.y),
                max(acc[3], vec.y)
            )
        }

        fun Iterable<IntVec>.showAsGrid(char: Char = fullBlock): String {
            val (minX, maxX, minY, maxY) = this.bounds()
            return this.showAsGrid(minX..maxX, minY..maxY, char)
        }

        fun Iterable<IntVec>.showAsGrid(xRange: IntRange, yRange: IntRange, char: Char = fullBlock): String {
            return yRange.joinToString("\n") { y ->
                xRange.joinToString("") { x ->
                    if (IntVec(x, y) in this) {
                        char.toString()
                    } else {
                        " "
                    }
                }
            }
        }

    }
}


class Parse {
    companion object {
        fun allIntsInString(line: String): List<Int> {
            return """-?\d+""".toRegex().findAll(line)
                .map { it.value.toInt() }
                .toList()
        }

    }
}

fun String.allInts() = Parse.allIntsInString(this)
const val fullBlock = '█'

inline fun Int.big() = this.toBigInteger()
fun main() {

    fun part1(input: List<String>, goal: Int): Int {
        val sensors = input.map { it.allInts() }
            .map { (i1, i2, i3, i4) ->
                IntVec(i1, i2) to IntVec(i3, i4)
            }

        val safePointsRow10 = mutableSetOf<Int>()

        sensors.forEach { (sensor, beacon) ->
            val dst = sensor.manhattan(beacon)
            val dstTo10 = abs(sensor.y - goal)
            // Then can go dstTo10-dst to both sides
            val width = dst - dstTo10

            (sensor.x - width..sensor.x + width).forEach { x ->
                val pos = IntVec(x, goal)
                if (pos != beacon) {
                    safePointsRow10.add(x)
                }
            }

        }

        return safePointsRow10.size
    }


    fun part2(input: List<String>): BigInteger {
        val max = 4000000

        val sensors = input.map { it.allInts() }
            .map { (i1, i2, i3, i4) ->
                IntVec(i1, i2) to IntVec(i3, i4)
            }


        val safeRangesPerLine = Array<MutableList<IntRange>>(max + 1) { mutableListOf() }


        sensors.forEach { (sensor, beacon) ->
            for (y in 0..max) {
                val dst = sensor.manhattan(beacon)
                val dstToY = abs(sensor.y - y)
                val width = dst - dstToY

                if (width > 0) {
                    // Add a start/end of safe spot indexes
                    safeRangesPerLine[y].add(sensor.x - width..sensor.x + width)
                }

            }

        }

        safeRangesPerLine.forEachIndexed { y, ranges ->
            val sortedRanges = ranges.sortedBy { it.first }
            var highest = sortedRanges.first().last

            // Find first set of ranges with a gap
            sortedRanges.drop(1).map {
                if (it.first > highest) {
                    return (it.first - 1).big() * 4000000.big() + y.big()
                }
                if (it.last > highest) {
                    highest = it.last
                }
            }
        }


        return 0.big()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)
//    check(part2(testInput) == 93)

    val input = readInput("Day15")
      println(part1(input, 2000000))
    println(part2(input))

}
