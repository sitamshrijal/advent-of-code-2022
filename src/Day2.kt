fun main() {
    fun parse(input: List<String>): List<Game> = input.map {
        Game(Move.parse(it[0]), Move.parse(it[2]))
    }

    fun part1(input: List<String>): Int {
        val games = parse(input)
        return games.sumOf { it.score() }
    }

    fun part2(input: List<String>): Int {
        val games = parse(input)

        val mapped = games.map {
            val (opponent, you) = it
            var choice = Move.ROCK
            // Loss
            if (you == Move.ROCK) {
                choice = when (opponent) {
                    Move.ROCK -> Move.SCISSORS
                    Move.PAPER -> Move.ROCK
                    Move.SCISSORS -> Move.PAPER
                }
            }
            // Draw
            if (you == Move.PAPER) {
                choice = opponent
            }
            // Win
            if (you == Move.SCISSORS) {
                choice = when (opponent) {
                    Move.ROCK -> Move.PAPER
                    Move.PAPER -> Move.SCISSORS
                    Move.SCISSORS -> Move.ROCK
                }
            }
            Game(opponent, choice)
        }
        return mapped.sumOf { it.score() }
    }

    val input = readInput("input2")
    println(part1(input))
    println(part2(input))
}

enum class Move(val score: Int) {
    ROCK(1), PAPER(2), SCISSORS(3);

    companion object {
        fun parse(char: Char): Move = when (char) {
            'A', 'X' -> ROCK
            'B', 'Y' -> PAPER
            'C', 'Z' -> SCISSORS
            else -> error("Invalid char!")
        }
    }
}

data class Game(val opponent: Move, val you: Move) {
    private fun isDraw(): Boolean = opponent == you

    private fun isWin(): Boolean = when (opponent) {
        Move.ROCK -> you == Move.PAPER
        Move.PAPER -> you == Move.SCISSORS
        Move.SCISSORS -> you == Move.ROCK
    }

    fun score(): Int {
        var score = you.score
        if (isDraw()) {
            score += 3
        }
        if (isWin()) {
            score += 6
        }
        return score
    }
}
