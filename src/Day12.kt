fun main() {
    fun parse(input: List<String>): List<List<HillPosition>> = buildList {
        input.forEach { row ->
            val list = mutableListOf<HillPosition>()
            row.forEach { char ->
                val elevation = char.toElevation()
                list += HillPosition(elevation, Type.parse(char))
            }
            add(list)
        }
    }

    fun part1(input: List<String>): Int {
        val heightmap = parse(input)

        val xRange = input.indices
        val yRange = input[0].indices
        val directions = listOf(-1 to 0, 1 to 0, 0 to 1, 0 to -1)

        heightmap.forEachIndexed { x, row ->
            row.forEachIndexed { y, position ->
                val adjacentList = mutableListOf<HillPosition>()

                directions.forEach { (dx, dy) ->
                    if (x + dx in xRange && y + dy in yRange) {
                        adjacentList += heightmap[x + dx][y + dy]
                    }
                }
                position.adjacentPositions = adjacentList
            }
        }

        val start = heightmap.flatten().first { it.type == Type.START }
        val end = heightmap.flatten().first { it.type == Type.END }

        val queue = mutableListOf<HillPosition>()
        queue.add(start)

        while (queue.isNotEmpty()) {
            val vertex = queue.removeFirst()
            vertex.adjacentPositions.forEach {
                if (it.color == Color.WHITE && it.elevation - vertex.elevation <= 1) {
                    it.color = Color.GRAY
                    it.distance = vertex.distance + 1
                    queue.add(it)
                }
            }
            vertex.color = Color.BLACK
        }
        return end.distance
    }

    fun part2(input: List<String>): Int {
        val heightmap = parse(input)

        val xRange = input.indices
        val yRange = input[0].indices
        val directions = listOf(-1 to 0, 1 to 0, 0 to 1, 0 to -1)

        heightmap.forEachIndexed { x, row ->
            row.forEachIndexed { y, position ->
                val adjacentList = mutableListOf<HillPosition>()

                directions.forEach { (dx, dy) ->
                    if (x + dx in xRange && y + dy in yRange) {
                        adjacentList += heightmap[x + dx][y + dy]
                    }
                }
                position.adjacentPositions = adjacentList
            }
        }

        // Start at the end
        val start = heightmap.flatten().first { it.type == Type.END }

        val queue = mutableListOf<HillPosition>()
        queue.add(start)

        while (queue.isNotEmpty()) {
            val vertex = queue.removeFirst()
            if (vertex.elevation == 0) {
                break
            }
            vertex.adjacentPositions.forEach {
                if (it.color == Color.WHITE && vertex.elevation - it.elevation <= 1) {
                    it.color = Color.GRAY
                    it.distance = vertex.distance + 1
                    queue.add(it)
                }
            }
            vertex.color = Color.BLACK
        }
        return heightmap
            .flatten()
            .filter { it.elevation == 0 && it.distance != 0 }
            .minOf { it.distance }
    }

    val input = readInput("input12")
    println(part1(input))
    println(part2(input))
}

enum class Type {
    START, END, OTHER;

    companion object {
        fun parse(char: Char): Type = when (char) {
            'S' -> START
            'E' -> END
            else -> OTHER
        }
    }
}

enum class Color { WHITE, GRAY, BLACK }

data class HillPosition(
    val elevation: Int,
    val type: Type,
    var color: Color = Color.WHITE,
    var distance: Int = 0
) {
    var adjacentPositions = mutableListOf<HillPosition>()
}

fun Char.toElevation(): Int = when (this) {
    'S' -> 0
    'E' -> 25
    else -> this - 'a'
}
