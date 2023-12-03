fun main() {
    fun part1(input: List<String>): Int = input
        .map {
            val first = it.first(Char::isDigit)
            val last = it.last(Char::isDigit)
            "$first$last"
        }
        .sumOf(String::toInt)

    fun part2(input: List<String>): Int {
        val digits = (1..9).map { it.toString() to it } +
                listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
                    .mapIndexed { index, s -> s to index + 1 }
        return input
            .map {
                val first = digits.map { (str, digit) ->
                    it.indexOf(str) to digit
                }.filter { it.first != -1 }.minByOrNull { it.first }!!.second
                val last = digits.map { (str, digit) ->
                    it.lastIndexOf(str) to digit
                }.filter { it.first != -1 }.maxByOrNull { it.first }!!.second
                "$first$last"
            }
            .sumOf(String::toInt)
    }

    val input = readInput("day-01-input")

    part1(input).println()
    part2(input).println()
}
