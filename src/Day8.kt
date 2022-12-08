fun main() {
    fun parse(input: List<String>): List<List<Tree>> {
        val grid = mutableListOf<MutableList<Tree>>()
        input.forEach { row ->
            val list = mutableListOf<Tree>()
            row.forEach { column ->
                list += Tree(column.digitToInt())
            }
            grid += list
        }
        return grid
    }

    fun part1(input: List<String>): Int {
        val grid = parse(input)

        val rowSize = grid.size
        val columnSize = grid[0].size

        var count = 0
        for (i in 0 until rowSize) {
            for (j in 0 until columnSize) {
                // All trees on the edge are visible
                if (i == 0 || j == 0 || i == rowSize - 1 || j == columnSize - 1) {
                    count++
                } else {
                    val tree = grid[i][j]

                    val left = grid[i].subList(0, j)
                    val right = grid[i].subList(j + 1, columnSize)
                    val up = grid.map { it[j] }.subList(0, i)
                    val down = grid.map { it[j] }.subList(i + 1, rowSize)

                    if (tree.isVisible(left) || tree.isVisible(right) || tree.isVisible(up) || tree.isVisible(
                            down
                        )
                    ) {
                        count++
                    }
                }
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        val grid = parse(input)

        val rowSize = grid.size
        val columnSize = grid[0].size

        val scenicScores = mutableListOf<Int>()
        for (i in 0 until rowSize) {
            for (j in 0 until columnSize) {
                if (i == 0 || j == 0 || i == rowSize - 1 || j == columnSize - 1) {
                    // Do nothing
                } else {
                    val tree = grid[i][j]

                    val left = grid[i].subList(0, j).reversed()
                    val right = grid[i].subList(j + 1, columnSize)
                    val up = grid.map { it[j] }.subList(0, i).reversed()
                    val down = grid.map { it[j] }.subList(i + 1, rowSize)

                    scenicScores += tree.score(left) * tree.score(right) * tree.score(up) * tree.score(
                        down
                    )
                }
            }
        }
        return scenicScores.max()
    }

    val input = readInput("input8")
    println(part1(input))
    println(part2(input))
}

data class Tree(val height: Int) {
    fun isVisible(trees: List<Tree>): Boolean = trees.all { it.height < height }

    fun score(trees: List<Tree>): Int {
        var score = 0
        for (tree in trees) {
            score++
            if (tree.height >= height) {
                break
            }
        }
        return score
    }
}
