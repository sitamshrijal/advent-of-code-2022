fun main() {
    fun parse(input: List<String>): List<Position> {
        val rocks = input.flatMap {
            val splits = it.split(" -> ")

            val list = mutableListOf<Position>()

            splits.zipWithNext().forEach { (start, end) ->
                val x1 = start.substringBefore(",").toInt()
                val y1 = start.substringAfter(",").toInt()

                val x2 = end.substringBefore(",").toInt()
                val y2 = end.substringAfter(",").toInt()

                // Vertical
                if (x1 == x2) {
                    val range = if (y1 < y2) {
                        y1..y2
                    } else {
                        y1 downTo y2
                    }
                    range.forEach {
                        list += Position(x1, it)
                    }
                }
                // Horizontal
                if (y1 == y2) {
                    val range = if (x1 < x2) {
                        x1..x2
                    } else {
                        x1 downTo x2
                    }
                    range.forEach {
                        list += Position(it, y1)
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
        var sand = Position(500, 0)
        val maxY = rocks.maxBy { it.y }.y

        var count = 0

        while (sand.y <= maxY) {
            val canMoveDown = rocks.none { it.x == sand.x && it.y == sand.y + 1 }
            val canMoveDownLeft = rocks.none { it.x == sand.x - 1 && it.y == sand.y + 1 }
            val canMoveDownRight = rocks.none { it.x == sand.x + 1 && it.y == sand.y + 1 }

            if (canMoveDown) {
                sand = sand.copy(y = sand.y + 1)
            } else if (canMoveDownLeft) {
                sand = sand.copy(x = sand.x - 1, y = sand.y + 1)
            } else if (canMoveDownRight) {
                sand = sand.copy(x = sand.x + 1, y = sand.y + 1)
            } else {
                rocks += sand
                count++

                // New unit of sand
                sand = Position(500, 0)
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
