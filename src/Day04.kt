fun main() {
    data class Card(val winning: Set<Int>, val my: Set<Int>)

    fun Card(line: String): Card {
        val info = line.split(":").last().trim()
        val (w, m) = info.split("|").map { it.trim() }
        return Card(
            winning = w.split(" ").mapNotNullTo(mutableSetOf()) { it.toIntOrNull() },
            my = m.split(" ").mapNotNullTo(mutableSetOf()) { it.toIntOrNull() },
        )
    }

    fun part1(input: List<String>): Int = input
        .map(::Card)
        .sumOf { (winning, my) ->
            val matches = (winning intersect my).size
            if (matches != 0) 1 shl (matches - 1) else 0
        }

    fun part2(input: List<String>): Int {
        val cardToWonCardsAmount = input
            .map(::Card)
            .mapIndexed { index, (winning, my) ->
                val matches = (winning intersect my).size
                index to matches
            }
            .toMap()

        val cardToTotalWonCardsAmount = mutableMapOf<Int, Int>()
        for (cardNum in input.indices.reversed()) {
            val cardsWon = (cardNum + 1)..(cardNum + cardToWonCardsAmount[cardNum]!!).coerceAtMost(input.lastIndex)
            cardToTotalWonCardsAmount[cardNum] = cardsWon.count() + cardsWon.sumOf(cardToTotalWonCardsAmount::getValue)
        }

        return cardToTotalWonCardsAmount.values.sum() + cardToTotalWonCardsAmount.size
    }

    val input = readInput("day-04-input")

    part1(input).println()
    part2(input).println()
}
