fun main() {
    fun availableCsForS(c: Coordinate, maze: Maze): List<Coordinate> =
        Dir.entries.mapNotNull { dir -> dir.goIfCan(c, maze) }

    fun availableCs(c: Coordinate, maze: Maze): List<Coordinate> = when (maze[c]) {
        '|' -> listOf(Dir.U, Dir.D)
        '-' -> listOf(Dir.L, Dir.R)
        'L' -> listOf(Dir.U, Dir.R)
        'J' -> listOf(Dir.U, Dir.L)
        '7' -> listOf(Dir.D, Dir.L)
        'F' -> listOf(Dir.D, Dir.R)
        else -> error("impossible")
    }.mapNotNull { dir -> dir.goIfCan(c, maze) }

    fun part1(input: List<String>): Int {
        val maze = input.map { it.toList() }

        val sY = maze.indexOfFirst { 'S' in it }
        val sX = maze[sY].indexOf('S')
        val s = Coordinate(sX, sY)

        val loop = generateSequence(seed = s to availableCsForS(s, maze).first()) { (prev, curr) ->
            if (maze[curr] != 'S') {
                curr to availableCs(curr, maze).first { it != prev }
            } else {
                null
            }
        }.map {
            it.first
        }.toList()

        return loop.size / 2
    }

    fun part2(input: List<String>): Int {
        val maze = input.map { it.toList() }

        val sY = maze.indexOfFirst { 'S' in it }
        val sX = maze[sY].indexOf('S')
        val s = Coordinate(sX, sY)

        val loop = generateSequence(seed = s to availableCsForS(s, maze).first()) { (prev, curr) ->
            if (maze[curr] != 'S') {
                curr to availableCs(curr, maze).first { it != prev }
            } else {
                null
            }
        }.onEach {
            // println(it.second)
            // println(maze[it.second])
        }.map {
            it.first
        }.toSet()

        val newMaze = maze.mapIndexed { y, line ->
            line.mapIndexed { x, c ->
                if (Coordinate(x, y) in loop) c else ' '
            }
        }

        val enlargedMaze = MutableList(newMaze.size * 3) {
            MutableList(newMaze.first().size * 3) { '.' }
        }

        fun fill(c: Coordinate, maze: Maze) {
            for ((y, line) in maze.withIndex()) {
                for ((x, ch) in line.withIndex()) {
                    enlargedMaze[c.y * 3 + y][c.x * 3 + x] = ch
                }
            }
        }

        for ((y, line) in newMaze.withIndex()) {
            for ((x, c) in line.withIndex()) {
                val filling = when (c) {
                    '|' -> listOf(
                        listOf('.', '*', '.'),
                        listOf('.', '*', '.'),
                        listOf('.', '*', '.'),
                    )

                    '-' -> listOf(
                        listOf('.', '.', '.'),
                        listOf('*', '*', '*'),
                        listOf('.', '.', '.'),
                    )

                    'L' -> listOf(
                        listOf('.', '*', '.'),
                        listOf('.', '*', '*'),
                        listOf('.', '.', '.'),
                    )

                    'J' -> listOf(
                        listOf('.', '*', '.'),
                        listOf('*', '*', '.'),
                        listOf('.', '.', '.'),
                    )

                    '7' -> listOf(
                        listOf('.', '.', '.'),
                        listOf('*', '*', '.'),
                        listOf('.', '*', '.'),
                    )

                    'F' -> listOf(
                        listOf('.', '.', '.'),
                        listOf('.', '*', '*'),
                        listOf('.', '*', '.'),
                    )

                    'S' -> listOf(
                        listOf('.', '*', '.'),
                        listOf('*', '*', '*'),
                        listOf('.', '*', '.'),
                    )

                    else -> listOf(
                        listOf('.', '.', '.'),
                        listOf('.', '.', '.'),
                        listOf('.', '.', '.'),
                    )
                }

                fill(Coordinate(x, y), filling)
            }
        }

        floodFillUtil(enlargedMaze, Coordinate(x = 0, y = 0))

        var cnt = 0
        for ((y, line) in newMaze.withIndex()) {
            for ((x, _) in line.withIndex()) {
                val aaa = (0..2).fold(true) { acc, yy ->
                    acc && (0..2).fold(true) { accc, xx ->
                        accc && enlargedMaze[y * 3 + yy][x * 3 + xx] == '.'
                    }
                }
                if (aaa) cnt++
            }
        }
        return cnt
    }

    val input = readInputLines("day-10-input")

    part1(input).println()
    part2(input).println()
}

fun floodFillUtil(maze: MutableMaze, coordinate: Coordinate) {
    val recFloodFillUtil: DeepRecursiveFunction<Coordinate, Unit> = DeepRecursiveFunction { c ->
        if (c.x in maze.first().indices && c.y in maze.indices) {
            if (maze[c] == '.') {
                maze[c] = ' '

                callRecursive(c.copy(x = c.x - 1))
                callRecursive(c.copy(x = c.x + 1))
                callRecursive(c.copy(y = c.y - 1))
                callRecursive(c.copy(y = c.y + 1))
            }
        }
    }

    recFloodFillUtil(coordinate)
}


typealias Maze = List<List<Char>>
typealias MutableMaze = MutableList<MutableList<Char>>

operator fun Maze.get(c: Coordinate) = this.getOrNull(c.y)?.getOrNull(c.x)

operator fun MutableMaze.set(c: Coordinate, value: Char) {
    getOrNull(c.y)?.set(c.x, value)
}

data class Coordinate(val x: Int, val y: Int)

enum class Dir {
    L, R, U, D;

    fun goIfCan(c: Coordinate, maze: Maze): Coordinate? = when (this) {
        L -> c.copy(x = c.x - 1).takeIf { maze[it] in setOf('L', 'F', '-', 'S') }
        R -> c.copy(x = c.x + 1).takeIf { maze[it] in setOf('J', '7', '-', 'S') }
        U -> c.copy(y = c.y - 1).takeIf { maze[it] in setOf('7', 'F', '|', 'S') }
        D -> c.copy(y = c.y + 1).takeIf { maze[it] in setOf('J', 'L', '|', 'S') }
    }
}
