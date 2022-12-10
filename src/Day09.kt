import java.lang.Integer.min
import java.lang.Integer.max
import kotlin.math.abs

fun main() {


    var hPos = Pair(0, 0)
    var tPos = Pair(0, 0)

    fun calcTailMove(h: Pair<Int, Int>, t: Pair<Int, Int>, dir: String): Pair<Int, Int> {
        val (x, y) = h
        val (tx, ty) = t

        if (x == tx && y == ty) {
            return t
        }
        val xDiv = max(tx, x) - min(tx, x)
        val yDiv = max(ty, y) - min(ty, y)


        if (xDiv > 1) {
            // they don't touch on x-axis
            if (yDiv == 1) {
                return if (tx > x) {
                    Pair(tx - 1, y)
                } else {
                    Pair(tx + 1, y)
                }
            }
            return if (tx > x) {
                Pair(tx - 1, ty)
            } else {
                Pair(tx + 1, ty)
            }
        }
        if (yDiv > 1) {
            // they don't touch on y-axis
            if (xDiv == 1) {
                return if (ty > y) {
                    Pair(x, ty - 1)
                } else {
                    Pair(x, ty + 1)
                }
            }
            return if (ty > y) {
                Pair(tx, ty - 1)
            } else {
                Pair(tx, ty + 1)
            }

        }

        return t
    }

    fun markTailOnMatrix(t: Pair<Int, Int>, matrix: ArrayList<Triple<Int, Int, Int>>) {
        val (x, y) = t
        val position = matrix.find { it.first == x && it.second == y }
        if (position == null) {
            matrix.add(Triple(x, y, 1))
        } else {
            matrix.remove(position)
            matrix.add(Triple(x, y, position.third + 1))
        }

    }

    fun move(dir: String, steps: Int, matrix: ArrayList<Triple<Int, Int, Int>>) {
        when (dir) {
            "R" -> {
                for (i in 1..steps) {
                    hPos = Pair(hPos.first + 1, hPos.second)
                    tPos = calcTailMove(hPos, tPos, dir)
                    markTailOnMatrix(tPos, matrix)
                }
            }

            "L" -> {
                for (i in 1..steps) {
                    hPos = Pair(hPos.first - 1, hPos.second)
                    tPos = calcTailMove(hPos, tPos, dir)
                    markTailOnMatrix(tPos, matrix)
                }
            }

            "U" -> {
                for (i in 1..steps) {
                    hPos = Pair(hPos.first, hPos.second + 1)
                    tPos = calcTailMove(hPos, tPos, dir)
                    markTailOnMatrix(tPos, matrix)
                }
            }

            "D" -> {
                for (i in 1..steps) {
                    hPos = Pair(hPos.first, hPos.second - 1)
                    tPos = calcTailMove(hPos, tPos, dir)
                    markTailOnMatrix(tPos, matrix)
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val matrix = arrayListOf<Triple<Int, Int, Int>>()
        matrix.add(Triple(0, 0, 1))
        input.forEach { line ->
            val (dir, steps) = line.split(" ")
            move(dir, steps.toInt(), matrix)

        }

        return matrix.size
    }

    fun moveTail(dir: String, steps: Int, matrix: ArrayList<Triple<Int, Int, Int>>, tails: Array<Pair<Int, Int>>) {
        when (dir) {
            "R" -> {
                for (i in 1..steps) {
                    hPos = Pair(hPos.first + 1, hPos.second)
                    tails.forEachIndexed { index, pair ->
                        val prev = tails.getOrElse(index - 1) { hPos }
                        tails[index] = calcTailMove(prev, pair, dir)
                        if (index == tails.lastIndex) {
                            markTailOnMatrix(tails[index], matrix)
                        }
                    }
                }
            }

            "L" -> {
                for (i in 1..steps) {
                    hPos = Pair(hPos.first - 1, hPos.second)
                    tails.forEachIndexed { index, pair ->
                        val prev = tails.getOrElse(index - 1) { hPos }
                        tails[index] = calcTailMove(prev, pair, dir)
                        if (index == tails.lastIndex) {
                            markTailOnMatrix(tails[index], matrix)
                        }
                    }
                }
            }

            "U" -> {
                for (i in 1..steps) {
                    hPos = Pair(hPos.first, hPos.second + 1)
                    tails.forEachIndexed { index, pair ->
                        val prev = tails.getOrElse(index - 1) { hPos }
                        tails[index] = calcTailMove(prev, pair, dir)
                        if (index == tails.lastIndex) {
                            markTailOnMatrix(tails[index], matrix)
                        }
                    }
                }
            }

            "D" -> {
                for (i in 1..steps) {
                    hPos = Pair(hPos.first, hPos.second - 1)
                    tails.forEachIndexed { index, pair ->
                        val prev = tails.getOrElse(index - 1) { hPos }
                        tails[index] = calcTailMove(prev, pair, dir)
                        if (index == tails.lastIndex) {
                            markTailOnMatrix(tails[index], matrix)
                        }
                    }
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        val matrix = arrayListOf<Triple<Int, Int, Int>>()
        matrix.add(Triple(0, 0, 1))
        val tails = (1..9).map { Pair(0, 0) }.toTypedArray()
        input.forEach { line ->
            val (dir, steps) = line.split(" ")
            moveTail(dir, steps.toInt(), matrix, tails)

        }

        return matrix.size
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
//   check(part1(testInput) == 13)
   // check(part2(testInput) == 36)


    val input = readInput("Day09")
  //  println(part1(input))
    println(part2(input))


}
