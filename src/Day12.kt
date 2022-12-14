import kotlin.math.abs

typealias GridPosition = Triple<Int, Int, Char>
typealias Barrier = Set<GridPosition>

const val MAX_SCORE = 99999999

abstract class Grid(private val barriers: List<Barrier>) {

    open fun heuristicDistance(start: GridPosition, finish: GridPosition): Int {
        val dx = abs(start.first - finish.first)
        val dy = abs(start.second - finish.second)
        return (dx + dy) + (-2) * minOf(dx, dy)
    }

    fun inBarrier(position: GridPosition) = barriers.any { it.contains(position) }

    abstract fun getNeighbours(position: GridPosition): List<GridPosition>

    open fun moveCost(from: GridPosition, to: GridPosition) = if (inBarrier(to)) MAX_SCORE else 1
}

class SquareGrid(private val grid: Set<GridPosition>, barriers: List<Barrier>) : Grid(barriers) {

    private val costRange = 'a'..'z'

    private val validMoves = listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))

    override fun getNeighbours(position: GridPosition): List<GridPosition> = validMoves.map {
        grid.find { p -> position.first + it.first == p.first && position.second + it.second == p.second }
    }.filterNotNull().filter { inGrid(it) }.filter {
        when (it.third) {
            'S' -> {
                true
            }

            'E' -> {
                position.third == 'z'
            }

            else -> {
                (costRange.indexOf(position.third) - costRange.indexOf(it.third)) >= -1
            }
        }
    }

    private fun inGrid(it: GridPosition) = grid.find { p -> it.first == p.first && it.second == p.second } != null

    override fun moveCost(from: GridPosition, to: GridPosition): Int {

        return 1
//        return if ((costRange.indexOf(from.third) - costRange.indexOf(to.third)) < 0) {
//            abs(costRange.indexOf(from.third) - costRange.indexOf(to.third))
//        } else {
//            1
//        }
    }
}


/**
 * Implementation of the A* Search Algorithm to find the optimum path between 2 points on a grid.
 *
 * The Grid contains the details of the barriers and methods which supply the neighboring vertices and the
 * cost of movement between 2 cells.  Examples use a standard Grid which allows movement in 8 directions
 * (i.e. includes diagonals) but alternative implementation of Grid can be supplied.
 *
 */
fun aStarSearch(start: GridPosition, finish: GridPosition, grid: Grid): Pair<List<GridPosition>, Int> {

    /**
     * Use the cameFrom values to Backtrack to the start position to generate the path
     */
    fun generatePath(currentPos: GridPosition, cameFrom: Map<GridPosition, GridPosition>): List<GridPosition> {
        val path = mutableListOf(currentPos)
        var current = currentPos
        while (cameFrom.containsKey(current)) {
            current = cameFrom.getValue(current)
            path.add(0, current)
        }
        return path.toList()
    }

    val openVertices = mutableSetOf(start)
    val closedVertices = mutableSetOf<GridPosition>()
    val costFromStart = mutableMapOf(start to 0)
    val estimatedTotalCost = mutableMapOf(start to grid.heuristicDistance(start, finish))

    val cameFrom = mutableMapOf<GridPosition, GridPosition>()  // Used to generate path by back tracking

    while (openVertices.size > 0) {

        val currentPos = openVertices.minBy { estimatedTotalCost.getValue(it) }!!

        // Check if we have reached the finish
        if (currentPos.first == finish.first && currentPos.second == finish.second) {
            // Backtrack to generate the most efficient path
            val path = generatePath(currentPos, cameFrom)
            return Pair(path, estimatedTotalCost.getValue(currentPos)) // First Route to finish will be optimum route
        }

        // Mark the current vertex as closed
        openVertices.remove(currentPos)
        closedVertices.add(currentPos)
        val neighbours = grid.getNeighbours(currentPos);

        grid.getNeighbours(currentPos).filterNot { closedVertices.contains(it) }  // Exclude previous visited vertices
            .forEach { neighbour ->
                val score = costFromStart.getValue(currentPos) + grid.moveCost(currentPos, neighbour)
                if (score < costFromStart.getOrDefault(neighbour, MAX_SCORE)) {
                    if (openVertices.find { it.first == neighbour.first && it.second == neighbour.second } == null) {
                        openVertices.add(neighbour)
                    }
                    cameFrom.put(neighbour, currentPos)
                    costFromStart.put(neighbour, score)
                    estimatedTotalCost.put(neighbour, score + grid.heuristicDistance(neighbour, finish))
                }
            }

    }

    throw IllegalArgumentException("No Path from Start $start to Finish $finish")
}

fun main() {

    fun part1(input: List<String>): Int {
        val start = input.mapIndexed { i, l ->
            if (l.toList().indexOf('S') != -1) {
                GridPosition(l.toList().indexOf('S'), i, 'a')
            } else {
                null
            }
        }.filterNotNull().first()
        val end = input.mapIndexed { i, l ->
            if (l.toList().indexOf('E') != -1) {
                GridPosition(l.toList().indexOf('E'), i, 'a')
            } else {
                null
            }
        }.filterNotNull().first()


        val gridData = input.mapIndexed { y, l ->
            l.toList().mapIndexed { x, c ->
                GridPosition(x, y, c)
            }
        }.flatten().toSet()


        val result = aStarSearch(start, end, SquareGrid(gridData, listOf()))

        return result.second
    }


    fun part2(input: List<String>): Int {
        val starts = input.mapIndexed { y, l ->
            l.toList().mapIndexed { x, c ->
                if (c == 'S' || c == 'a') {
                    GridPosition(x, y, 'a')
                } else {
                    null
                }
            }.filterNotNull()
        }.flatten()
        val end = input.mapIndexed { i, l ->
            if (l.toList().indexOf('E') != -1) {
                GridPosition(l.toList().indexOf('E'), i, 'a')
            } else {
                null
            }
        }.filterNotNull().first()


        val gridData = input.mapIndexed { y, l ->
            l.toList().mapIndexed { x, c ->
                GridPosition(x, y, c)
            }
        }.flatten().toSet()

        val min = starts.minOfOrNull { aStarSearch(it, end, SquareGrid(gridData, listOf())).second }
        return min ?: 0
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
//check(part1(testInput) == 31)
    check(part2(testInput) == 29)


    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))

}

