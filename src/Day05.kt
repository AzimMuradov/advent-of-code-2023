fun main() {
    data class Mapping(val d: Long, val s: Long, val r: Long)

    fun calc(parts: List<String>, seeds: Sequence<Long>): Long {
        val maps = parts.drop(n = 1).map { part ->
            part
                .lines()
                .drop(n = 1)
                .filterNot(String::isBlank)
                .map { line ->
                    val (d, s, r) = line.split(" ").map(String::toLong)
                    Mapping(d, s, r)
                }
                .sortedBy(Mapping::s)
        }

        return seeds.map {
            maps.fold(it) { acc, maps ->
                maps.firstOrNull { (_, s, r) -> acc in s..<s + r }?.let { it.d + (acc - it.s) } ?: acc
            }
        }.minOf { it }
    }

    fun part1(input: String): Long {
        val parts = input.split("\n\n")

        val seeds = parts
            .first()
            .removePrefix("seeds: ")
            .split(" ")
            .map(String::toLong)
            .asSequence()

        return calc(parts, seeds)
    }

    fun part2(input: String): Long {
        val parts = input.split("\n\n")

        val seeds = parts
            .first()
            .removePrefix("seeds: ")
            .split(" ")
            .map(String::toLong)
            .chunked(2)
            .map { it.first()..<it.first() + it.last() }
            .asSequence()
            .flatten()

        return calc(parts, seeds)
    }

    val input = readInputText("day-05-input")

    part1(input).println()
    part2(input).println()
}
