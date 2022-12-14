fun main() {
    fun parse(input: List<String>): List<Monkey> = input.chunked(7).map { Monkey.parse(it) }

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

        val value = monkeys.map { it.test }.reduce(Long::times)
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

data class Monkey(
    val items: MutableList<Item>,
    val operation: (Item) -> Item,
    val test: Long,
    val ifTrue: Int,
    val ifFalse: Int,
    var inspectionCount: Long = 0L
) {
    fun inspectItem(item: Item, worryDecrease: (Item) -> Item): Pair<Item, Int> {
        inspectionCount++
        val itemAfterOperation = operation(item)
        val itemAfterWorryDecrease = worryDecrease(itemAfterOperation)
        val isDivisible = itemAfterWorryDecrease.worryLevel % test == 0L
        return itemAfterWorryDecrease to if (isDivisible) ifTrue else ifFalse
    }

    fun receiveItem(item: Item) {
        items += item
    }

    companion object {
        fun parse(input: List<String>): Monkey {
            val items = mutableListOf<Item>()

            val worryLevels = input[1].substringAfter("Starting items: ")
            worryLevels.split(", ").forEach {
                items += Item(it.toLong())
            }

            val operationText = input[2].substringAfter("Operation: new = old ")
            val (sign, value) = operationText.split(" ")
            val isOld = value == "old"
            val operation: (Item) -> Item = when (sign) {
                "*" -> {
                    if (isOld) {
                        { Item(it.worryLevel * it.worryLevel) }
                    } else {
                        { Item(it.worryLevel * value.toLong()) }
                    }
                }

                "+" -> {
                    { Item(it.worryLevel + value.toLong()) }
                }

                else -> error("Not supported")
            }

            val test = input[3].substringAfter("Test: divisible by ").toLong()
            val ifTrue = input[4].substringAfter("If true: throw to monkey ").toInt()
            val ifFalse = input[5].substringAfter("If false: throw to monkey ").toInt()

            return Monkey(items, operation, test, ifTrue, ifFalse)
        }
    }
}
