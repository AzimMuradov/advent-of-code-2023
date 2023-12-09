fun main() {
    data class GameInfo(val num: UInt, val red: UInt, val green: UInt, val blue: UInt)
    data class ShowcaseInfo(val red: UInt, val green: UInt, val blue: UInt)

    fun getGameInfo(line: String): GameInfo {
        val (gameHeader, cubesInfo) = line.split(":").map(String::trim)

        val gameNum = gameHeader.split(" ").last().toUInt()

        val showcases = cubesInfo.split(";").map(String::trim).map { showcase ->
            val cubes = showcase.split(",").map {
                val (cnt, clr) = it.trim().split(" ")
                clr to cnt.toUInt()
            }
            val red = cubes.find { it.first == "red" }?.second ?: 0u
            val green = cubes.find { it.first == "green" }?.second ?: 0u
            val blue = cubes.find { it.first == "blue" }?.second ?: 0u

            ShowcaseInfo(red, green, blue)
        }

        return GameInfo(
            num = gameNum,
            red = showcases.maxOf(ShowcaseInfo::red),
            green = showcases.maxOf(ShowcaseInfo::green),
            blue = showcases.maxOf(ShowcaseInfo::blue),
        )
    }

    fun part1(input: List<String>): Int = input
        .map(::getGameInfo)
        .filter { (_, r, g, b) -> r <= 12u && g <= 13u && b <= 14u }
        .sumOf(GameInfo::num)
        .toInt()

    fun part2(input: List<String>): Int = input
        .map(::getGameInfo)
        .sumOf { (_, r, g, b) -> r * g * b }
        .toInt()

    val input = readInputLines("day-02-input")

    part1(input).println()
    part2(input).println()
}
