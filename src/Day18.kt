fun main() {
    fun parse(input: List<String>): List<Position3D> = input.map { Position3D.parse(it) }

    fun part1(input: List<String>): Int {
        val cubes = parse(input)
        var count = 0
        cubes.forEach { cube ->
            count += 6 - cube.adjacentCubes().count { it in cubes }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("input18")
    println(part1(input))
    println(part2(input))
}

data class Position3D(val x: Int, val y: Int, val z: Int) {
    fun adjacentCubes(): List<Position3D> = listOf(
        copy(x = x - 1),
        copy(x = x + 1),
        copy(y = y - 1),
        copy(y = y + 1),
        copy(z = z - 1),
        copy(z = z + 1)
    )

    companion object {
        fun parse(input: String): Position3D {
            val (x, y, z) = input.split(",")
            return Position3D(x.toInt(), y.toInt(), z.toInt())
        }
    }
}
