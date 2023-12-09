fun main() {
    fun numbers(line: String) = buildList {
        var isFindingDigit = line.first().isDigit()
        var beg = 0
        for ((i, c) in line.withIndex()) {
            if (!isFindingDigit && c.isDigit()) {
                isFindingDigit = true
                beg = i
            } else if (isFindingDigit && !c.isDigit()) {
                isFindingDigit = false
                add(beg..<i to line.substring(beg..<i).toInt())
            }
        }
        if (isFindingDigit && line.last().isDigit()) {
            add(beg..<line.length to line.substring(beg..<line.length).toInt())
        }
    }

    fun getSymbols(map: List<String>, region: Pair<Int, IntRange>): List<Char> {
        val (line, range) = region
        val (xs, ys) = range.let { it.first - 1..it.last + 1 } to line - 1..line + 1
        val chars = buildSet {
            for (x in xs) {
                for (y in ys) {
                    add(map.getOrNull(y)?.getOrNull(x))
                }
            }
        }

        return chars
            .filterNotNull()
            .filterNot { it.isDigit() }
            .filterNot { it == '.' }
    }

    fun getGears(map: List<String>, region: Pair<Int, IntRange>): List<Pair<Int, Int>> {
        val (line, range) = region
        val (xs, ys) = range.let { it.first - 1..it.last + 1 } to line - 1..line + 1
        val chars = buildList {
            for (x in xs) {
                for (y in ys) {
                    add(map.getOrNull(y)?.getOrNull(x)?.let { it to (x to y) })
                }
            }
        }

        return chars.filterNotNull().filter { it.first == '*' }.map { it.second }
    }

    fun part1(input: List<String>): Int = input
        .mapIndexed { index, line ->
            numbers(line).mapNotNull { (range, num) ->
                if (getSymbols(input, index to range).isNotEmpty()) {
                    num
                } else {
                    null
                }
            }
        }
        .flatten()
        .sum()

    fun part2(input: List<String>): Int = input
        .asSequence()
        .mapIndexed { index, line ->
            numbers(line).mapNotNull { (range, num) ->
                val gears = getGears(input, index to range)
                if (gears.isNotEmpty()) {
                    gears.map { it to num }
                } else {
                    null
                }
            }.flatten()
        }
        .flatten()
        .groupBy({ it.first }, { it.second })
        .filter { it.value.size == 2 }
        .map { it.value.reduce(Int::times) }
        .sum()

    val input = readInputLines("day-03-input")

    part1(input).println()
    part2(input).println()
}
