fun main() {
    data class Card(val winning: Set<Int>, val my: Set<Int>)

    fun Card(line: String): Card {
        val info = line.split(":").last().trim()
        val (w, m) = info.split("|").map { it.trim() }

        fun parseNums(setOfNums: String) = setOfNums
            .split(" ")
            .mapNotNullTo(mutableSetOf(), String::toIntOrNull)

        return Card(winning = parseNums(w), my = parseNums(m))
    }

    fun List<String>.matchesPerCard() = map(::Card).map { (winning, my) ->
        (winning intersect my).size
    }

    fun part1(input: List<String>): Int = input
        .matchesPerCard()
        .sumOf { matches -> if (matches != 0) 1 shl (matches - 1) else 0 }

    fun part2(input: List<String>): Int {
        val matchesPerCard = input.matchesPerCard()

        val wonAmountPerCard = IntArray(input.size)
        for (card in input.indices.reversed()) {
            val cardsWonRange = (card + 1..card + matchesPerCard[card])
                .filter { it in input.indices }

            wonAmountPerCard[card] = cardsWonRange.count() + cardsWonRange.sumOf(wonAmountPerCard::get)
        }

        return wonAmountPerCard.sum() + wonAmountPerCard.size
    }

    val input = readInput("day-04-input")

    part1(input).println()
    part2(input).println()
}
