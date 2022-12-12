fun main() {
    fun parse(input: List<String>): List<Monkey> {
        var monkeyNumber = 0
        val map = mutableMapOf<Int, Monkey>()

        val monkeyRegex = """Monkey (\d):""".toRegex()
        val operationRegex = """Operation: new = old (.) (\d+)""".toRegex()
        val testRegex = """Test: divisible by (\d+)""".toRegex()
        val ifTrueRegex = """If true: throw to monkey (\d)""".toRegex()
        val ifFalseRegex = """If false: throw to monkey (\d)""".toRegex()

        val items = mutableListOf<Item>()
        var operation: (Item) -> Item = { it }
        var test = Test(0) { false }
        var ifTrue = 0
        var ifFalse = 0

        input.forEach { line ->
            val cleanLine = line.trim()
            // Monkey
            if (line matches monkeyRegex) {
                val (n) = monkeyRegex.matchEntire(line)!!.destructured
                monkeyNumber = n.toInt()
            } else {
                // Starting items
                if (cleanLine.startsWith("Starting")) {
                    items.clear()
                    val worryLevels = cleanLine.substringAfter("Starting items: ")
                    worryLevels.split(", ").forEach {
                        items += Item(it.toLong())
                    }
                }
                // Operation
                if (cleanLine.trim() matches operationRegex) {
                    val (s, d) = operationRegex.matchEntire(cleanLine)!!.destructured

                    if (s == "*") {
                        operation = { Item(it.worryLevel * d.toInt()) }
                    }
                    if (s == "+") {
                        operation = { Item(it.worryLevel + d.toInt()) }
                    }
                }
                // Alt operation
                if (cleanLine == "Operation: new = old * old") {
                    operation = { Item(it.worryLevel * it.worryLevel) }
                }
                // Test
                if (cleanLine matches testRegex) {
                    val (d) = testRegex.matchEntire(cleanLine)!!.destructured
                    test = Test(d.toLong()) { it.worryLevel % d.toLong() == 0L }
                }
                // If true
                if (cleanLine matches ifTrueRegex) {
                    val (d) = ifTrueRegex.matchEntire(cleanLine)!!.destructured
                    ifTrue = d.toInt()
                }
                // If false
                if (cleanLine matches ifFalseRegex) {
                    val (d) = ifFalseRegex.matchEntire(cleanLine)!!.destructured
                    ifFalse = d.toInt()
                    val toAdd: List<Item> = items
                    map[monkeyNumber] =
                        Monkey(toAdd.toMutableList(), operation, test, ifTrue, ifFalse)
                }
            }
        }
        return map.values.toList()
    }

    fun part1(input: List<String>): Long {
        val monkeys = parse(input)

        val worryDecrease: (Item) -> (Item) = { Item(it.worryLevel / 3) }
        repeat(20) {
            monkeys.forEach { monkey ->
                monkey.items.forEach { item ->
                    val (newItem, throwTo) = monkey.inspectItem(item, worryDecrease)
                    monkeys[throwTo].items += newItem
                }
                // Clear items
                monkey.items.clear()
            }
        }
        val (first, second) = monkeys.map { it.inspectionCount }.sortedDescending().take(2)
        return first * second
    }

    fun part2(input: List<String>): Long {
        val monkeys = parse(input)

        val value = monkeys.map { it.test.divisibleBy }.reduce(Long::times)
        val worryDecrease: (Item) -> (Item) = { Item(it.worryLevel % value) }

        repeat(10_000) {
            monkeys.forEach { monkey ->
                monkey.items.forEach { item ->
                    val (newItem, throwTo) = monkey.inspectItem(item, worryDecrease)
                    monkeys[throwTo].receiveItem(newItem)
                }
                // Clear items
                monkey.items.clear()
            }
        }
        val (first, second) = monkeys.map { it.inspectionCount }.sortedDescending().take(2)
        return first * second
    }

    val input = readInput("input11")
    println(part1(input))
    println(part2(input))
}

@JvmInline
value class Item(val worryLevel: Long)

data class Test(val divisibleBy: Long, val testFunction: (Item) -> Boolean)

data class Monkey(
    val items: MutableList<Item>,
    val operation: (Item) -> Item,
    val test: Test,
    val ifTrue: Int,
    val ifFalse: Int,
    var inspectionCount: Long = 0L
) {
    fun inspectItem(item: Item, worryDecrease: (Item) -> Item): Pair<Item, Int> {
        inspectionCount++
        val itemAfterOperation = operation(item)
        val itemAfterWorryDecrease = worryDecrease(itemAfterOperation)
        val isDivisible = test.testFunction(itemAfterWorryDecrease)
        return itemAfterWorryDecrease to if (isDivisible) ifTrue else ifFalse
    }

    fun receiveItem(item: Item) {
        items += item
    }
}
