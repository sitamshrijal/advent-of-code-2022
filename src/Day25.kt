fun main() {
    fun parse(input: List<String>): List<Long> = input.map { it.toDecimal() }

    fun part1(input: List<String>): String {
        val numbers = parse(input)
        return numbers.sum().toSnafu()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("input25")
    println(part1(input))
    println(part2(input))
}

/**
 * Convert a decimal number to a SNAFU number.
 */
fun Long.toSnafu(): String {
    var decimal = this // Input number
    var snafu = ""  // Output number
    while (decimal != 0L) {
        when (val digit = decimal.mod(5L)) {
            in 0..2 -> snafu += digit
            3L -> {
                snafu += '='
                decimal += 5L
            }

            4L -> {
                snafu += '-'
                decimal += 5L
            }
        }
        decimal /= 5L
    }
    return snafu.reversed()
}

/**
 * Convert a SNAFU number to decimal.
 */
fun String.toDecimal(): Long {
    var decimal = 0L
    indices.forEach {
        val place = if (it == 0) 1L else List(it) { 5L }.reduce { a, b -> a * b }
        val digit = this[lastIndex - it]

        decimal += when (digit) {
            '=' -> -2 * place
            '-' -> -place
            else -> digit.digitToInt() * place
        }
    }
    return decimal
}
