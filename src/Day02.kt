import java.io.IOException
import kotlin.math.abs

fun main() {
    // R, P, S
    // P, S, R
    val ref = mapOf("A" to listOf(3, 1), "B" to listOf(1, 2), "C" to listOf(2, 3))
    val ref2 = mapOf("X" to listOf(3, 1), "Y" to listOf(1, 2), "Z" to listOf(2, 3))
    val winLoseMap = mapOf("X" to -1, "Y" to 0, "Z" to 1)

    fun calcScore(input: List<String>): Int {

        val oindex = ref.getValue(input[0])
        val mindex = ref2.getValue(input[1])

        val players = listOf(oindex[0], mindex[0])

        val winnerNumber = abs(mindex[0] - oindex[0])

        return when (winnerNumber) {
            0 -> 3 + mindex[1]
            1 -> {
                if (players.max() == mindex[0]) {
                    6 + mindex[1]
                } else {
                    mindex[1]
                }
            }

            else -> if (players.min() == mindex[0]) {
                6 + mindex[1]
            } else {
                mindex[1]
            }
        }
    }

    fun findLosing(s: String): String {
        val index = ref.getValue(s)
        val f =
            ref2.filter { (_, v) -> (v[0] < index[0] && abs(v[0] - index[0]) == 1) || (v[0] > index[0] && abs(v[0] - index[0]) > 1) }
        return f.keys.first()
    }

    fun findWining(s: String): String {
        val index = ref.getValue(s)
        val f =
            ref2.filter { (_, v) -> (v[0] > index[0] && abs(v[0] - index[0]) == 1) || (v[0] < index[0] && abs(v[0] - index[0]) > 1) }
        return f.keys.first()
    }

    fun findDraw(s: String): String {
        val index = ref.getValue(s)
        val f =
            ref2.filter { (_, v) -> (v[0] == index[0]) }
        return f.keys.last()
    }

    fun winLoseDraw(input: List<String>): Int {
        return when (winLoseMap.getValue(input[1])) {
            0 -> calcScore(listOf(input[0], findDraw(input[0])))
            -1 -> calcScore(listOf(input[0], findLosing(input[0])))
            1 -> calcScore(listOf(input[0], findWining(input[0])))
            else -> throw IOException()
        }
    }

    fun part1(input: List<String>): Int {
        val t = input.fold(0) { total, v -> total + calcScore(v.split(" ")) }
        return t
    }

    fun part2(input: List<String>): Int {
        return input.fold(0) { total, v -> total + winLoseDraw(v.split(" ")) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
//    check(part2(testInput) == 3900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))

}
