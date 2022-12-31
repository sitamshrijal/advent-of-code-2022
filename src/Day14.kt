fun main() {
    fun parse(input: List<String>): List<CavePosition> {
        val rocks = input.flatMap {
            val splits = it.split(" -> ")

            val list = mutableListOf<CavePosition>()

            splits.zipWithNext().forEach { (start, end) ->
                val x1 = start.substringBefore(",").toInt()
                val y1 = start.substringAfter(",").toInt()

                val x2 = end.substringBefore(",").toInt()
                val y2 = end.substringAfter(",").toInt()

                val xRange = minOf(x1, x2)..maxOf(x1, x2)
                val yRange = minOf(y1, y2)..maxOf(y1, y2)

                xRange.forEach { x ->
                    yRange.forEach { y ->
                        list += CavePosition(x, y)
                    }
                }
            }
            list
        }
        return rocks.distinct()
    }

    fun part1(input: List<String>): Int {
        val rocks = parse(input).toMutableList()

        // Starting position
        var sand = CavePosition(500, 0)
        val maxY = rocks.maxOf { it.y }

        var count = 0

        while (sand.y <= maxY) {
            // The first position the sand can move to; null otherwise
            val next =
                listOf(sand.down(), sand.downLeft(), sand.downRight()).firstOrNull { it !in rocks }

            if (next != null) {
                sand = next
            } else {
                rocks += sand
                count++

                // New unit of sand
                sand = CavePosition(500, 0)
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("input14")
    println(part1(input))
    println(part2(input))
}

data class CavePosition(val x: Int, val y: Int) {
    fun down(): CavePosition = CavePosition(x, y + 1)

    fun downLeft(): CavePosition = CavePosition(x - 1, y + 1)

    fun downRight(): CavePosition = CavePosition(x + 1, y + 1)
}
