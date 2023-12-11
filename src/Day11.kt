import kotlin.math.abs


typealias Space = List<List<Char>>

fun main() {
    fun Space.countEmptyLines() = map {
        if (it.all { it == '.' }) 1 else 0
    }.runningReduce(Int::plus)

    fun Space.transpose(): List<List<Char>> {
        val newSpace = MutableList(first().size) { MutableList(size) { '.' } }

        forEachIndexed { i, line ->
            line.forEachIndexed { j, x ->
                newSpace[j][i] = x
            }
        }

        return newSpace
    }

    fun calcDistSum(space: Space, expansionK: Int): Long {
        val emptyXs = space.transpose().countEmptyLines()
        val emptyYs = space.countEmptyLines()

        val gs = buildList {
            space.forEachIndexed { y, line ->
                line.forEachIndexed { x, c ->
                    if (c == '#') add(x to y)
                }
            }
        }

        val addDistK = (expansionK - 1).toLong()

        return gs.flatMap { (x1, y1) ->
            gs.map { (x2, y2) ->
                val xD = abs(x2 - x1)
                val xExp = abs(emptyXs[x2] - emptyXs[x1]) * addDistK
                val yD = abs(y2 - y1)
                val yExp = abs(emptyYs[y2] - emptyYs[y1]) * addDistK

                xD + xExp + yD + yExp
            }
        }.reduce(Long::plus) / 2
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
