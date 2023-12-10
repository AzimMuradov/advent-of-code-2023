fun main() {
    val regOrder = listOf(
        'A', 'K', 'Q', 'J', 'T',
        '9', '8', '7', '6', '5', '4', '3', '2',
    )
    val advOrder = listOf(
        'A', 'K', 'Q', 'T',
        '9', '8', '7', '6', '5', '4', '3', '2',
        'J',
    )

    fun List<String>.parse() = map {
        val (cards, bid) = it.split(" ")
        cards to bid.toInt()
    }

    fun List<Pair<Hand, Int>>.totalWinnings() =
        sortedBy { (hand, _) -> hand }
            .mapIndexed { index, (_, bid) -> (index + 1) * bid }
            .sum()

    fun part1(input: List<String>): Int = input
        .parse()
        .map { (cards, bid) ->
            val cardCount: List<Int> = cards
                .groupingBy { it }
                .eachCount()
                .values
                .toList()
                .sorted()

            val type: HandType = HandType.entries.first { type -> type.check(cardCount) }

            Hand(cards, type, regOrder) to bid
        }
        .totalWinnings()

    fun part2(input: List<String>): Int = input
        .parse()
        .map { (cards, bid) ->

            val jokedCards = run {
                val (jokers, others) = cards.partition { it == 'J' }
                val maxCountLabel = others
                    .groupingBy { it }
                    .eachCount()
                    .maxByOrNull { it.value }
                    ?.key
                    ?: 'Q'
                maxCountLabel.toString().repeat(jokers.length) + others
            }

            jokedCards to bid

            val cardCount: List<Int> = jokedCards
                .groupingBy { it }
                .eachCount()
                .values
                .toList()
                .sorted()
            val type: HandType = HandType.entries.first { type -> type.check(cardCount) }

            Hand(cards, type, advOrder) to bid
        }
        .totalWinnings()

    val input = readInputLines("day-07-input")

    part1(input).println()
    part2(input).println()
}


private data class Hand(
    val cards: String,
    val type: HandType,
    val labelsOrder: List<Char>,
) : Comparable<Hand> {

    private val labelsValue = cards.map {
        labelsOrder.lastIndex - labelsOrder.indexOf(it)
    }.asReversed().fold(0 to 1) { (sum, power), labelValue ->
        sum + labelValue * power to power * labelsOrder.size
    }.first

    override fun compareTo(other: Hand): Int =
        if (type.ordinal != other.type.ordinal) {
            -(type.ordinal - other.type.ordinal)
        } else {
            labelsValue - other.labelsValue
        }
}

private enum class HandType {
    FiveOfAnyKind {
        override fun check(cardCount: List<Int>): Boolean = cardCount == listOf(5)
    },
    FourOfAnyKind {
        override fun check(cardCount: List<Int>): Boolean = cardCount == listOf(1, 4)
    },
    FullHouse {
        override fun check(cardCount: List<Int>): Boolean = cardCount == listOf(2, 3)
    },
    ThreeOfAnyKind {
        override fun check(cardCount: List<Int>): Boolean = cardCount == listOf(1, 1, 3)
    },
    TwoPair {
        override fun check(cardCount: List<Int>): Boolean = cardCount == listOf(1, 2, 2)
    },
    OnePair {
        override fun check(cardCount: List<Int>): Boolean = cardCount == listOf(1, 1, 1, 2)
    },
    HighCard {
        override fun check(cardCount: List<Int>): Boolean = cardCount == listOf(1, 1, 1, 1, 1)
    };

    abstract fun check(cardCount: List<Int>): Boolean
}
