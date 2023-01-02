import java.util.Stack

fun main() {
    fun parse(input: List<String>): Pair<List<Stack<Char>>, List<CrateMove>> {
        val stacks = input.filterNot {
            it.startsWith("move") || it.isEmpty()
        }

        val map = mutableMapOf<Int, Stack<Char>>()

        (stacks.lastIndex - 1 downTo 0).forEach {
            val crates = stacks[it]
            val splits = crates.split(" ")

            var stackNumber = 1
            var spaceCount = 0
            splits.forEach { split ->
                if (split.isEmpty()) {
                    spaceCount++
                } else {
                    val value = map.getOrDefault(stackNumber, Stack<Char>())
                    value.push(split[1])
                    map[stackNumber++] = value
                }
                if (spaceCount == 4) {
                    stackNumber++
                    // Reset
                    spaceCount = 0
                }
            }
        }

        val regex = """move (\d+) from (\d) to (\d)""".toRegex()
        val moves = input.filter { it.startsWith("move") }.map {
            val (match1, match2, match3) = regex.matchEntire(it)!!.destructured
            val count = match1.toInt()
            val start = match2.toInt()
            val end = match3.toInt()
            CrateMove(map[start]!!, map[end]!!, count)
        }
        return map.values.toList() to moves
    }

    fun part1(input: List<String>): String {
        val (stacks, moves) = parse(input)

        moves.forEach { (start, end, count) ->
            repeat(count) {
                end.push(start.pop())
            }
        }
        return stacks.map { it.peek() }.joinToString(separator = "")
    }

    fun part2(input: List<String>): String {
        val (stacks, moves) = parse(input)

        moves.forEach { (start, end, count) ->
            val toAdd = buildList {
                repeat(count) {
                    add(start.pop())
                }
            }

            toAdd.reversed().forEach {
                end.push(it)
            }
        }
        return stacks.map { it.peek() }.joinToString(separator = "")
    }

    val input = readInput("input5")
    println(part1(input))
    println(part2(input))
}

/**
 * Move [count] crates from the [start] stack to the [end] stack.
 */
data class CrateMove(val start: Stack<Char>, val end: Stack<Char>, val count: Int)
