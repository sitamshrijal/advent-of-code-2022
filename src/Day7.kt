fun main() {
    fun parse(input: List<String>): Directory {
        var current = Directory("/")
        input.forEach {
            when {
                it.startsWith("$ cd /") -> {}
                it.startsWith("$ ls") -> {}
                it.startsWith("$ cd ..") -> {
                    current = current.parent ?: current
                }

                it.startsWith("$ cd") -> {
                    val name = it.substringAfter("$ cd ")

                    val directory = current.directories.find { it.name == name }
                    if (directory == null) {
                        val new = Directory(name = name, parent = current)
                        current = new
                        current.directories += new
                    } else {
                        current = directory
                    }
                }

                it.startsWith("dir") -> {
                    current.directories += Directory(
                        name = it.substringAfter("dir "), parent = current
                    )
                }

                else -> {
                    val (size, name) = it.split(" ")
                    current.files += File(name, size.toInt())
                }
            }
        }
        return current.parent!!.parent!!
    }

    fun part1(input: List<String>): Int {
        val directory = parse(input)

        return directory.getAllChildren().map { it.size() }.filter { it <= 100_000 }.sum()
    }

    fun part2(input: List<String>): Int {
        val directory = parse(input)

        val unused = 70_000_000 - directory.size()
        val needed = 30_000_000 - unused

        return directory.getAllChildren().filter { it.size() >= needed }
            .sortedBy { it.size() }[0].size()
    }

    val input = readInput("input7")
    println(part1(input))
    println(part2(input))
}

data class File(val name: String, val size: Int)

data class Directory(
    val name: String = "",
    val files: MutableList<File> = mutableListOf(),
    val directories: MutableList<Directory> = mutableListOf(),
    val parent: Directory? = null
) {
    fun size(): Int = files.sumOf { it.size } + directories.sumOf { it.size() }

    fun getAllChildren(): List<Directory> =
        directories + directories.flatMap { it.getAllChildren() }

    override fun toString(): String = name
}
