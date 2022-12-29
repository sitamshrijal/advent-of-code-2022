fun main() {
    fun parse(input: List<String>): List<Packet> =
        input.filter { it.isNotEmpty() }.map { Packet.parse(it) }

    fun part1(input: List<String>): Int {
        val packets = parse(input)
        var sum = 0
        packets.chunked(2).forEachIndexed { index, pair ->
            if (pair[0] < pair[1]) {
                sum += index + 1
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val packets = parse(input)

        val divider1 = Packet.parse("[[2]]")
        val divider2 = Packet.parse("[[6]]")

        val sorted = (packets + divider1 + divider2).sorted()

        val i = sorted.indexOf(divider1) + 1
        val j = sorted.indexOf(divider2) + 1
        return i * j
    }

    val input = readInput("input13")
    println(part1(input))
    println(part2(input))
}

sealed interface Packet : Comparable<Packet> {
    data class IntPacket(val value: Int) : Packet {
        override fun compareTo(other: Packet): Int = when (other) {
            is IntPacket -> value compareTo other.value
            is ListPacket -> ListPacket(listOf(this)) compareTo other
        }
    }

    data class ListPacket(val packets: List<Packet>) : Packet {
        override fun compareTo(other: Packet): Int = when (other) {
            is IntPacket -> compareTo(ListPacket(listOf(other)))
            is ListPacket -> {
                val zipped = packets zip other.packets
                val comparisons = zipped.map { it.first compareTo it.second }

                comparisons.firstOrNull { it != 0 } ?: (packets.size compareTo other.packets.size)
            }
        }
    }

    companion object {
        fun parse(input: String): Packet {
            if (input.all { it.isDigit() }) {
                return IntPacket(input.toInt())
            }
            if (input == "[]") {
                return ListPacket(emptyList())
            }
            val cleanInput = input.removeSurrounding("[", "]")
            val packets = mutableListOf<Packet>()

            var current = 0
            var level = 0
            cleanInput.forEachIndexed { index, c ->
                when (c) {
                    '[' -> level++
                    ']' -> level--
                    ',' -> if (level == 0) {
                        packets += parse(cleanInput.substring(current, index))
                        current = index + 1
                    }
                }
            }
            packets += parse(cleanInput.substring(current))
            return ListPacket(packets)
        }
    }
}
