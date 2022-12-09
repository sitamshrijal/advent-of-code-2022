import kotlin.math.abs
import kotlin.math.sign

fun main() {
    fun parse(input: List<String>): List<Pair<String, Int>> = input.map {
        val (direction, count) = it.split(" ")
        direction to count.toInt()
    }

    fun part1(input: List<String>): Int {
        val movements = parse(input)

        // Starting position
        val s = Position(0, 0)
        var head = s
        var tail = s

        val visited = mutableListOf(tail)
        movements.forEach { (direction, count) ->
            when (direction) {
                "R" -> {
                    repeat(count) {
                        val x = head.x + 1
                        val y = head.y
                        head = Position(x, y)
                        if (!tail.isNear(head)) {
                            tail = tail.moveNear(head)
                            visited += tail
                        }
                    }
                }

                "U" -> {
                    repeat(count) {
                        val x = head.x
                        val y = head.y + 1
                        head = Position(x, y)
                        if (!tail.isNear(head)) {
                            tail = tail.moveNear(head)
                            visited += tail
                        }
                    }
                }

                "L" -> {
                    repeat(count) {
                        val x = head.x - 1
                        val y = head.y
                        head = Position(x, y)
                        if (!tail.isNear(head)) {
                            tail = tail.moveNear(head)
                            visited += tail
                        }
                    }
                }

                "D" -> {
                    repeat(count) {
                        val x = head.x
                        val y = head.y - 1
                        head = Position(x, y)
                        if (!tail.isNear(head)) {
                            tail = tail.moveNear(head)
                            visited += tail
                        }
                    }
                }
            }
        }
        return visited.distinct().size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("input9")
    println(part1(input))
    println(part2(input))
}

data class Position(val x: Int, val y: Int) {
    fun isNear(other: Position): Boolean = abs(x - other.x) <= 1 && abs(y - other.y) <= 1

    fun moveNear(other: Position): Position {
        val dx = x - other.x
        val dy = y - other.y
        return Position(x - dx.sign, y - dy.sign)
    }
}
