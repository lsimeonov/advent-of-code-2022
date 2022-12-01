class Calorie(val calorie: Int) {
}

class Dwarf {
    private var calories: MutableList<Calorie> = mutableListOf()

    fun addCalorie(calorie: Calorie) {
        calories.add(calorie)
    }

    fun totalCalories(): Int {
        return calories.sumOf { it.calorie }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val dwarfs = mutableListOf<Dwarf>()
        var dwarf = Dwarf()
        input.iterator().forEach {
            if (it.equals("")) {
                dwarfs.add(dwarf)
                dwarf = Dwarf()
            }else {
                dwarf.addCalorie(Calorie(it.toInt()))
            }
        }

        var max = 0
        dwarfs.iterator().forEach {
            val tmp = it.totalCalories()
            if (tmp > max) {
                max = tmp
            }
        }

        return max
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3000)

    val input = readInput("Day01")
    println(part1(input))

}
