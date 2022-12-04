fun main() {
    fun parse(input: List<String>): List<List<String>> {
        return input.map { it.chunked(it.length / 2) }
    }

    fun part1(input: List<String>): Int {
        val rucksacks = parse(input)

        return rucksacks.map { rucksack ->
            val (compartment1, compartment2) = rucksack
            compartment1.first { it in compartment2 }
        }.sumOf { it.priority() }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).map { group ->
            val (first, second, third) = group
            first.first { it in second && it in third }
        }.sumOf { it.priority() }
    }

    val input = readInput("input3")
    println(part1(input))
    println(part2(input))
}

fun Char.priority(): Int = if (isLowerCase()) {
    code - 96
} else {
    code - 38
}