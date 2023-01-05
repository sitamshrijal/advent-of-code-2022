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
        val cubes = parse(input)

        val xRange = cubes.minOf { it.x } - 1..cubes.maxOf { it.x } + 1
        val yRange = cubes.minOf { it.y } - 1..cubes.maxOf { it.y } + 1
        val zRange = cubes.minOf { it.y } - 1..cubes.maxOf { it.z } + 1

        val queue = mutableListOf(Position3D(xRange.first, yRange.first, zRange.first))
        val visited = mutableSetOf<Position3D>()
        var count = 0

        while (queue.isNotEmpty()) {
            val cube = queue.removeAt(0)
            if (cube in visited) continue

            visited += cube

            cube.adjacentCubes().forEach {
                if (it in cubes) {
                    count++
                } else if (it.x in xRange && it.y in yRange && it.z in zRange) {
                    queue += it
                }
            }
        }
        return count
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
            val (x, y, z) = input.split(",").map { it.toInt() }
            return Position3D(x, y, z)
        }
    }
}
