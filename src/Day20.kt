fun main() {
    fun parse(input: List<String>, decryptionKey: Long = 1L): MutableList<Number> =
        input.mapIndexed { index, s -> Number(s.toLong() * decryptionKey, index) }.toMutableList()

    fun part1(input: List<String>): Long {
        val numbers = parse(input)

        numbers.indices.forEach { originalIndex ->
            val index = numbers.indexOfFirst { it.index == originalIndex }
            val toMove = numbers.removeAt(index)

            numbers.addWrapped(index + toMove.value, toMove)
        }
        val index = numbers.indexOfFirst { it.value == 0L }
        return listOf(1000, 2000, 3000).sumOf { numbers.getWrapped(index + it).value }
    }

    fun part2(input: List<String>): Long {
        val numbers = parse(input, 811_589_153L)

        repeat(10) {
            numbers.indices.forEach { originalIndex ->
                val index = numbers.indexOfFirst { it.index == originalIndex }
                val toMove = numbers.removeAt(index)

                numbers.addWrapped(index + toMove.value, toMove)
            }
        }
        val index = numbers.indexOfFirst { it.value == 0L }
        return listOf(1000, 2000, 3000).sumOf { numbers.getWrapped(index + it).value }
    }

    val input = readInput("input20")
    println(part1(input))
    println(part2(input))
}

data class Number(val value: Long, val index: Int)

fun <T> MutableList<T>.getWrapped(index: Int): T = this[index % size]

fun <T> MutableList<T>.addWrapped(index: Long, value: T) {
    add(index.mod(size), value)
}
