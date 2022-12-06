fun main() {
    fun parse(input: List<String>): String {
        return input.first()
    }

    fun part1(input: List<String>): Int {
        val data = parse(input)
        return data.windowed(4).indexOfFirst { it.toSet().size == 4 } + 4
    }

    fun part2(input: List<String>): Int {
        val data = parse(input)
        return data.windowed(14).indexOfFirst { it.toSet().size == 14 } + 14
    }

    val input = readInput("input6")
    println(part1(input))
    println(part2(input))
}
