fun main() {
    fun parse(input: List<String>): List<List<Tree>> {
        val grid = buildList {
            input.forEach { row ->
                add(row.map { Tree(it.digitToInt()) })
            }
        }

        for (i in grid.indices) {
            for (j in grid[0].indices) {
                val tree = grid[i][j]

                tree.left = grid[i].take(j)
                tree.right = grid[i].drop(j + 1)
                tree.up = grid.map { it[j] }.take(i)
                tree.down = grid.map { it[j] }.drop(i + 1)
            }
        }
        return grid
    }

    fun part1(input: List<String>): Int {
        val grid = parse(input)
        return grid.flatten().count { it.isVisible() }
    }

    fun part2(input: List<String>): Int {
        val grid = parse(input)
        return grid.flatten().maxOf { it.scenicScore() }
    }

    val input = readInput("input8")
    println(part1(input))
    println(part2(input))
}

data class Tree(val height: Int) {
    var left = listOf<Tree>()
    var right = listOf<Tree>()
    var up = listOf<Tree>()
    var down = listOf<Tree>()

    fun isVisible(): Boolean =
        isVisible(left) || isVisible(right) || isVisible(up) || isVisible(down)

    private fun isVisible(trees: List<Tree>): Boolean = trees.all { it.height < height }

    private fun score(trees: List<Tree>): Int {
        var score = 0
        for (tree in trees) {
            score++
            if (tree.height >= height) {
                break
            }
        }
        return score
    }

    fun scenicScore(): Int =
        score(left.reversed()) * score(right) * score(up.reversed()) * score(down)
}
