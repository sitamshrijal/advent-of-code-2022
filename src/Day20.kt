fun main() {
    fun parse(input: List<String>, decryptionKey: Int = 1): MutableList<Number> =
        input.mapIndexed { index, s -> Number(s.toInt() * decryptionKey, index) }.toMutableList()

    fun part1(input: List<String>): Int {
        val numbers = parse(input)

        val indices = numbers.indices

        indices.forEach { originalIndex ->
            val index = numbers.indexOfFirst { it.index == originalIndex }
            val toMove = numbers.removeAt(index)

            numbers.addWrapped(index + toMove.value, toMove)
        }
        val index = numbers.indexOfFirst { it.value == 0 }
        return listOf(1000, 2000, 3000).sumOf { numbers.getWrapped(index + it).value }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("input20")
    println(part1(input))
    println(part2(input))
}

data class Number(val value: Int, val index: Int)

fun <T> MutableList<T>.getWrapped(index: Int): T = this[index % size]

fun <T> MutableList<T>.addWrapped(index: Int, value: T) {
    add(index.mod(size), value)
}
