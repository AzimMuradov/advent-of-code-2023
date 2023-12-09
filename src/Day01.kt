fun main() {
    fun part1(input: List<String>): Int = input
        .map { line ->
            val first = line.first(Char::isDigit)
            val last = line.last(Char::isDigit)
            "$first$last"
        }
        .sumOf(String::toInt)

    fun part2(input: List<String>): Int {
        val wordDigits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        val digits = buildMap {
            for (digit in 1..9) {
                put(key = digit.toString(), value = digit)
            }
            for ((index, wordDigit) in wordDigits.withIndex()) {
                put(key = wordDigit, value = index + 1)
            }
        }
        return input
            .map { line ->
                val first = digits.getValue(line.findAnyOf(digits.keys)!!.second)
                val last = digits.getValue(line.findLastAnyOf(digits.keys)!!.second)
                "$first$last"
            }
            .sumOf(String::toInt)
    }

    val input = readInputLines("day-01-input")

    part1(input).println()
    part2(input).println()
}
