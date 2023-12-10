import java.math.BigInteger


fun main() {
    fun parse(input: List<String>): Pair<Sequence<Char>, Map<String, Pair<String, String>>> {
        val infPath = sequence {
            while (true) yieldAll(input.first().toList())
        }
        val map = input.drop(n = 2).associate {
            val (from, lr) = it.dropLast(n = 1).split(" = (")
            val (l, r) = lr.split(", ")
            from to (l to r)
        }
        return infPath to map
    }

    fun pathLen(
        start: String,
        stopProperty: (String) -> Boolean,
        infPath: Sequence<Char>,
        map: Map<String, Pair<String, String>>,
    ) = infPath
        .runningFold(initial = start) { loc, dir ->
            val (left, right) = map.getValue(loc)
            if (dir == 'L') left else right
        }
        .takeWhile { !stopProperty(it) }
        .count()

    fun part1(input: List<String>): Int {
        val (infPath, map) = parse(input)

        return pathLen(
            start = "AAA",
            stopProperty = { it == "ZZZ" },
            infPath, map,
        )
    }

    fun part2(input: List<String>): BigInteger {
        val (infPath, map) = parse(input)

        val starts = map.keys.filter { it.last() == 'A' }

        val pathLens = starts.map { start ->
            pathLen(
                start,
                stopProperty = { it.last() == 'Z' },
                infPath, map,
            ).toBigInteger()
        }
        val gcd = pathLens.reduce(BigInteger::gcd)
        val lcm = pathLens.map { it / gcd }.reduce(BigInteger::times) * gcd

        return lcm
    }

    val input = readInputLines("day-08-input")

    part1(input).println()
    part2(input).println()
}
