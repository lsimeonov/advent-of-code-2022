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
    fun prepareDwarfs(input: List<String>): List<Dwarf> {
        val dwarfs = mutableListOf<Dwarf>()
        var dwarf = Dwarf()
        input.iterator().forEach {
            if (it.equals("")) {
                dwarfs.add(dwarf)
                dwarf = Dwarf()
            } else {
                dwarf.addCalorie(Calorie(it.toInt()))
            }
        }

        dwarfs.sortWith<Dwarf>(object : Comparator<Dwarf> {
            override fun compare(d0: Dwarf, di: Dwarf): Int {
                val c0 = d0.totalCalories()
                val c1 = di.totalCalories()
                if (c0 > c1) {
                    return -1
                }
                if (c0 == c1) {
                    return 0
                }
                return 1
            }
        })

        return dwarfs
    }

    fun part1(input: List<String>): Int {
        val dwarfs = prepareDwarfs(input)
        return dwarfs.first().totalCalories()
    }

    fun part2(input: List<String>): Int {
        val dwarfs = prepareDwarfs(input)
        return dwarfs.subList(0, 3).sumOf { dwarf -> dwarf.totalCalories() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3000)
    check(part2(testInput) == 3900)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))

}
