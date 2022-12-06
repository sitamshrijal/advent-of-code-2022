fun main() {
    fun parse(input: List<String>): String {
        return input.first()
    }

    fun part1(input: List<String>): Int {
        val data = parse(input)

        data.forEachIndexed { index, _ ->
            if (index > 3) {
                val previousValues =
                    setOf(data[index - 1], data[index - 2], data[index - 3], data[index - 4])

                if (previousValues.size == 4) {
                    return index
                }
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val data = parse(input)

        data.forEachIndexed { index, _ ->
            if (index > 13) {
                val previousValues = buildSet {
                    (index - 1 downTo index - 14).forEach {
                        add(data[it])
                    }
                }
                if (previousValues.size == 14) {
                    return index
                }
            }
        }
        return 0
    }

    val input = readInput("input6")
    println(part1(input))
    println(part2(input))
}
