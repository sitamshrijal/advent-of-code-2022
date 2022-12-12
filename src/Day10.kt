fun main() {
    fun parse(input: List<String>): List<Instruction> = input.map { it.parse() }

    fun signalStrength(instructions: List<Instruction>, cycle: Int): Int {
        var remaining = cycle
        var X = 1
        for (instruction in instructions) {
            if (remaining <= instruction.cycles) {
                break
            }
            if (instruction is Instruction.AddX) {
                X += instruction.value
            }
            remaining -= instruction.cycles
        }
        return X * cycle
    }

    fun part1(input: List<String>): Int {
        val instructions = parse(input)
        return listOf(20, 60, 100, 140, 180, 220).sumOf { signalStrength(instructions, it) }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("input10")
    println(part1(input))
    println(part2(input))
}

sealed class Instruction(val cycles: Int) {
    object NoOp : Instruction(1)
    data class AddX(val value: Int) : Instruction(2)
}

fun String.parse(): Instruction = if (this == "noop") {
    Instruction.NoOp
} else {
    Instruction.AddX(substringAfter(" ").toInt())
}
