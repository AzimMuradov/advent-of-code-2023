import kotlin.math.abs


typealias Space = List<List<Char>>

fun main() {
    fun Space.countEmptyLines() = map { line ->
        if (line.all { it == '.' }) 1 else 0
    }.runningReduce(Int::plus)

    fun Space.transpose(): List<List<Char>> {
        val newSpace = MutableList(first().size) { MutableList(size) { '.' } }
        for ((y, line) in withIndex()) {
            for ((x, c) in line.withIndex()) {
                newSpace[x][y] = c
            }
        }
        return newSpace
    }

    fun calcDistSum(space: Space, expansionK: Int): Long {
        val galaxies = buildList {
            for ((y, line) in space.withIndex()) {
                for ((x, c) in line.withIndex()) {
                    if (c == '#') add(x to y)
                }
            }
        }

        val emptyXs = space.transpose().countEmptyLines()
        val emptyYs = space.countEmptyLines()
        val addDistK = expansionK - 1

        return sequence {
            for ((i, g1) in galaxies.withIndex()) {
                val (x1, y1) = g1
                for ((x2, y2) in galaxies.drop(i + 1)) {
                    val xD = abs(x2 - x1)
                    val xExp = abs(emptyXs[x2] - emptyXs[x1]) * addDistK
                    val yD = abs(y2 - y1)
                    val yExp = abs(emptyYs[y2] - emptyYs[y1]) * addDistK

                    yield(xD + xExp + yD + yExp)
                }
            }
        }.sumOf(Int::toLong)
    }

    fun part1(input: List<String>): Long = calcDistSum(
        space = input.map { it.toList() },
        expansionK = 2
    )

    fun part2(input: List<String>): Long = calcDistSum(
        space = input.map { it.toList() },
        expansionK = 1_000_000
    )

    val input = readInputLines("day-11-input")

    part1(input).println()
    part2(input).println()
}
