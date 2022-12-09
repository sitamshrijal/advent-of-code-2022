import kotlin.math.abs
import kotlin.math.sign

fun main() {
    fun parse(input: List<String>): List<Pair<Direction, Int>> = input.map {
        val (direction, count) = it.split(" ")
        Direction.parse(direction) to count.toInt()
    }

    fun part1(input: List<String>): Int {
        val movements = parse(input)

        // Starting position
        val s = Position(0, 0)
        var head = s
        var tail = s

        val visited = mutableListOf(tail)
        movements.forEach { (direction, count) ->
            repeat(count) {
                head = head.move(direction)
                if (!tail.isNear(head)) {
                    tail = tail.moveNear(head)
                    visited += tail
                }
            }
        }
        return visited.distinct().size
    }

    fun part2(input: List<String>): Int {
        val movements = parse(input)

        // Starting position
        val s = Position(0, 0)
        val knots = MutableList(10) { s }

        val visited = mutableListOf(s)
        movements.forEach { (direction, count) ->
            repeat(count) {
                // Move the head
                knots[0] = knots[0].move(direction)

                (1..9).forEach { index ->
                    val position = knots[index]
                    val follow = knots[index - 1]
                    if (!position.isNear(follow)) {
                        knots[index] = position.moveNear(follow)
                        if (index == 9) {
                            visited += knots[9]
                        }
                    }
                }
            }
        }
        return visited.distinct().size
    }

    val input = readInput("input9")
    println(part1(input))
    println(part2(input))
}

enum class Direction(val dx: Int, val dy: Int) {
    LEFT(-1, 0), RIGHT(1, 0), UP(0, 1), DOWN(0, -1);

    companion object {
        fun parse(string: String): Direction = values().find { it.name.startsWith(string) }!!
    }
}

data class Position(val x: Int, val y: Int) {
    fun isNear(other: Position): Boolean = abs(x - other.x) <= 1 && abs(y - other.y) <= 1

    fun move(direction: Direction): Position = Position(x + direction.dx, y + direction.dy)

    fun moveNear(other: Position): Position {
        val dx = x - other.x
        val dy = y - other.y
        return Position(x - dx.sign, y - dy.sign)
    }
}
