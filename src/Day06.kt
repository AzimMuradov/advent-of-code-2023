import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {
    fun calc(time: Long, minDist: Long): Long {
        val d = (time * time - 4 * minDist).toDouble()
        val x1 = (time + sqrt(d)) / 2
        val x2 = (time - sqrt(d)) / 2
        return ceil(x1).toLong() - floor(x2).toLong() - 1
    }

    fun part1(input: List<String>): Long {
        val (times, dists) = input.map { line ->
            line
                .split(":")
                .last()
                .split(" ")
                .filterNot(String::isBlank)
                .map(String::toLong)
        }

        val races = times zip dists

        return races
            .map { (time, dist) -> calc(time, dist) }
            .reduce(Long::times)
    }

    fun part2(input: List<String>): Long {
        val (time, dist) = input.map { line ->
            line
                .split(":")
                .last()
                .split(" ")
                .filterNot(String::isBlank)
                .joinToString(separator = "")
                .toLong()
        }

        return calc(time, dist)
    }

    val input = readInput("day-06-input")

    part1(input).println()
    part2(input).println()
}
