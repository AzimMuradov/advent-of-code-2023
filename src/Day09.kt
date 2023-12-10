fun main() {
    fun parse(line: String) = line.split(" ").map(String::toInt)

    fun calcSequences(nums: List<Int>) = generateSequence(nums) { currNums ->
        if (currNums.any { it != 0 }) {
            currNums.zipWithNext { a, b -> b - a }
        } else {
            null
        }
    }

    fun part1(input: List<String>): Int = input.sumOf { line ->
        calcSequences(parse(line))
            .map(List<Int>::last)
            .sum()
    }

    fun part2(input: List<String>): Int = input.sumOf { line ->
        calcSequences(parse(line))
            .map(List<Int>::first)
            .fold(0 to 1) { (sum, sign), x ->
                sum + sign * x to -sign
            }.first
    }

    val input = readInputLines("day-09-input")

    part1(input).println()
    part2(input).println()
}
