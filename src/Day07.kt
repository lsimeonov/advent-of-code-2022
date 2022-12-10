fun main() {

    data class Object(var size: Int, val name: String, val isDir: Boolean, var current: Boolean, val parent: Object?)


    fun executeCommand(cmd: String, fs: MutableList<Object>) {
        if (cmd.startsWith("$ cd")) {
            val dir = cmd.split(" ")[2]
            val cur = fs.first { it.current }
            cur.current = false
            if (dir == "..") {
                if (cur.name != "/") {
                    cur.parent?.current = true
                }
            } else if (dir == "/") {
                fs.first { it.name == dir }.current = true
            } else {
                val newDir = fs.firstOrNull { it.name == dir && it.parent == cur }
                if (newDir != null) {
                    newDir.current = true
                }
            }
        }

    }

    // calculate size of all directories
    fun calculateSize(fs: MutableList<Object>) {
        fs.asReversed().forEach {
            if (it.isDir) {
                it.size = fs.filter { o -> o.parent == it }.sumOf { s -> s.size }
            }
        }
    }

    fun processInput(input: List<String>, fs: MutableList<Object>) {
        var lsmode = false
        input.forEach { out ->
            if (out.startsWith("$ ls")) {
                lsmode = true
                return@forEach
            }

            if (out.startsWith("$")) {
                lsmode = false
                executeCommand(out, fs)

                return@forEach
            }

            if (lsmode) {
                val parent = fs.first { it.current }
                if (out.startsWith("dir")) {
                    val dir = out.split(" ")
                    val size = 0
                    val name = dir[1]
                    fs.add(Object(size, name, isDir = true, current = false, parent = parent))
                } else {
                    val (size, name) = out.split(" ")
                    fs.add(Object(size.toInt(), name, isDir = false, current = false, parent = parent))
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val fs = mutableListOf<Object>()

        fs.add(0, Object(0, "/", isDir = true, current = true, parent = null))
        processInput(input, fs)
        calculateSize(fs)
        val size = fs.filter { it.size < 100000 && it.isDir }.sumOf { it.size }
        return size
    }


    fun part2(input: List<String>): Int {
        val fs = mutableListOf<Object>()

        fs.add(0, Object(0, "/", isDir = true, current = true, parent = null))
        processInput(input, fs)
        calculateSize(fs)

        val free = 70000000 - fs.first{ it.name == "/" }.size
        val delete = 30000000 - free

        return fs.filter { it.isDir && it.size > delete }.minOf { it.size }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)


    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))


}
