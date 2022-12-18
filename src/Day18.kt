import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Cube(val x: Int, val y: Int, val z: Int, val size: Int)

fun main() {

    fun isNeighbor(cube1: Cube, cube2: Cube): Boolean {
        // Check if the cubes are adjacent in the x, y, or z direction
        return (abs(cube1.x - cube2.x) == 1 && cube1.y == cube2.y && cube1.z == cube2.z) ||
                (abs(cube1.y - cube2.y) == 1 && cube1.x == cube2.x && cube1.z == cube2.z) ||
                (abs(cube1.z - cube2.z) == 1 && cube1.x == cube2.x && cube1.y == cube2.y)
    }

    fun calculateSurfaceArea(cube: Cube, neighbor: Cube, exposedSides: Int): Int {
        var out = exposedSides
        if (neighbor.x == cube.x + 1) {
            out--
        }
        if (neighbor.x == cube.x - 1) {
            out--
        }

        // Check the top and bottom sides of the given cube
        if (neighbor.y == cube.y + 1) {
            out--
        }
        if (neighbor.y == cube.y - 1) {
            out--
        }

        // Check the left and right sides of the given cube
        if (neighbor.z == cube.z + 1) {
            out--
        }
        if (neighbor.z == cube.z - 1) {
            out--
        }

        return out
    }

    fun part1(input: List<String>): Int {
        val cubes = input.map {
            val (x, y, z) = it.split(",").map { it.toInt() }
            Cube(x, y, z, 1)
        }.toSet()

        return cubes.map {
            it.let { c -> cubes.filter { cube -> c != cube && isNeighbor(c, cube) } }
                .fold(6) { acc, neighbour ->
                    calculateSurfaceArea(neighbour, it, acc)
                }
        }.sum()
    }

    fun generateGrid(cubes: Set<Cube>): List<List<List<Cube>>> {
        // Find the minimum and maximum x, y, and z coordinates of the cubes
        val minX = cubes.minBy { it.x }.x
        val maxX = cubes.maxBy { it.x }.x
        val minY = cubes.minBy { it.y }.y
        val maxY = cubes.maxBy { it.y }.y
        val minZ = cubes.minBy { it.z }.z
        val maxZ = cubes.maxBy { it.z }.z

        // Initialize the grid with empty cubes
        val grid = mutableListOf<MutableList<MutableList<Cube>>>()
        for (i in minX..maxX) {
            val row = mutableListOf<MutableList<Cube>>()
            for (j in minY..maxY) {
                val column = mutableListOf<Cube>()
                for (k in minZ..maxZ) {
                    column.add(Cube(i, j, k, 0))
                }
                row.add(column)
            }
            grid.add(row)
        }

        // Add the cubes from the list to the grid
        for (cube in cubes) {
            grid[cube.x - minX][cube.y - minY][cube.z - minZ] = cube
        }

        return grid
    }

    fun findTrappedEmptyCubes(grid: List<List<List<Cube>>>): List<Cube> {
        // Initialize a list to store the empty cubes
        val trappedCubes = mutableListOf<Cube>()

        // Iterate over the rows in the grid
        for (i in grid.indices) {
            // Iterate over the columns in the current row
            for (j in grid[i].indices) {
                // Iterate over the cubes in the current column
                for (k in grid[i][j].indices) {
                    // Get the current cube
                    val cube = grid[i][j][k]

                    // Check if the current cube is empty
                    if (cube.size == 0) {
                        // Check if the current cube is completely surrounded by cubes with a size greater than 0
                        val isTrapped = (i == 0 || i == grid.size - 1 || grid[i - 1][j][k].size > 0 || grid[i + 1][j][k].size > 0) &&
                                (j == 0 || j == grid[i].size - 1 || grid[i][j - 1][k].size > 0 || grid[i][j + 1][k].size > 0) &&
                                (k == 0 || k == grid[i][j].size - 1 || grid[i][j][k - 1].size > 0 || grid[i][j][k + 1].size > 0)
                        if (isTrapped) {
                            trappedCubes.add(cube)
                        }
                    }
                }
            }
        }

        return trappedCubes
    }
    fun findTrappedCubes(cubes: Set<Cube>): List<Cube> {
        // Find the minimum and maximum x, y, and z coordinates of the cubes
        val minX = cubes.minBy { it.x }!!.x
        val maxX = cubes.maxBy { it.x }!!.x
        val minY = cubes.minBy { it.y }!!.y
        val maxY = cubes.maxBy { it.y }!!.y
        val minZ = cubes.minBy { it.z }!!.z
        val maxZ = cubes.maxBy { it.z }!!.z

        // Initialize a list to store the missing cubes
        val trappedCubes = mutableListOf<Cube>()

        // Iterate over the range of x, y, and z coordinates
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    // Check if the current coordinates are not present in the list of cubes
                    if (cubes.none { it.x == x && it.y == y && it.z == z }) {
                        // Check if the missing cube is completely surrounded by cubes with a size greater than 0
                        val isSurrounded = (x == minX || x == maxX || cubes.any { it.x == x - 1 || it.x == x + 1 }) &&
                                (y == minY || y == maxY || cubes.any { it.y == y - 1 || it.y == y + 1 }) &&
                                (z == minZ || z == maxZ || cubes.any { it.z == z - 1 || it.z == z + 1 })
                        if (isSurrounded) {
                            // Check if the missing cube is not immediately adjacent to any other missing cubes
                            val isTrapped = !trappedCubes.any { it.x in x-1..x+1 && it.y in y-1..y+1 && it.z in z-1..z+1 }
                            if (isTrapped) {
                                // Add the missing cube to the list
                                trappedCubes.add(Cube(x, y, z, 0))
                            }
                        }
                    }
                }
            }
        }

        return trappedCubes
    }
    fun part2(input: List<String>): Int {
        val cubes = input.map {
            val (x, y, z) = it.split(",").map { it.toInt() }
            Cube(x, y, z, 1)
        }.toSet()

        val grid = generateGrid(cubes)

        val surface = cubes.map {
            it.let { c -> cubes.filter { cube -> c != cube && isNeighbor(c, cube) } }
                .fold(6) { acc, neighbour ->
                    calculateSurfaceArea(neighbour, it, acc)
                }
        }.sum()
        val missingCubes = findTrappedCubes(cubes)

        return surface

    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
//    check(part1(testInput) == 64)
    check(part2(testInput) == 58)

    val input = readInput("Day18")
    println(part1(input))
//    println(part2(input))

}
