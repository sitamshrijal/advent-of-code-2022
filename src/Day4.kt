fun main() {
    fun parse(input: List<String>): List<Pair<IntRange, IntRange>> {
        return input.map {
            val (section1, section2) = it.split(",")
            section1.toRange() to section2.toRange()
        }
    }

    fun part1(input: List<String>): Int {
        val mapped = parse(input)

        return mapped.count { (section1, section2) ->
            val firstInSecond = section1.first in section2 && section1.last in section2
            val secondInFirst = section2.first in section1 && section2.last in section1
            firstInSecond || secondInFirst
        }
    }

    fun part2(input: List<String>): Int {
        val mapped = parse(input)

        return mapped.count { (section1, section2) ->
            section1.last in section2 || section2.last in section1
        }
    }

    val input = readInput("input4")
    println(part1(input))
    println(part2(input))
}

fun String.toRange(): IntRange {
    val splits = split("-").map { it.toInt() }
    return splits[0]..splits[1]
}
