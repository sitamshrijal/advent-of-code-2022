fun main() {
    fun parse(input: List<String>): Map<Int, Int> {
        var elfNumber = 1
        val map = mutableMapOf<Int, Int>()

        input.forEach {
            if (it.isEmpty()) {
                elfNumber++
            } else {
                val value = map.getOrDefault(elfNumber, 0)
                map[elfNumber] = value + it.toInt()
            }
        }
        return map
    }

    fun part1(input: List<String>): Int {
        val map = parse(input)
        return map.values.max()
    }

    fun part2(input: List<String>): Int {
        val map = parse(input)
        return map.values.sortedDescending().take(3).sum()
    }

    val input = readInput("input1")
    println(part1(input))
    println(part2(input))
}
