package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day

class Day09 : Day<Pair<Int, Int>, Long>(9, "Marble Mania") {

    override val inputTransform: (String) -> Pair<Int, Int> =
        { line -> line.split(" ").let { it[0].toInt() to it[6].toInt() } }

    override fun solutionPart1(inputData: Sequence<Pair<Int, Int>>): Long {
        val (players, lastMarble) = inputData.first()

        return getWinnerScore(players, lastMarble)
    }

    override fun solutionPart2(inputData: Sequence<Pair<Int, Int>>): Long {
        val (players, lastMarble) = inputData.first()

        return getWinnerScore(players, lastMarble * 100)
    }

    private fun getWinnerScore(numPlayers: Int, lastMarble: Int): Long {
        val points = LongArray(numPlayers) { 0 }
        val game = MarbleGame(0)

        for (i in 1..lastMarble) {
            if (i % 23 == 0) {
                game.rotateRight(7)
                points[i % numPlayers] += i + game.remove()
            } else {
                game.rotateLeft(2)
                game.insert(i.toLong())
            }
        }

        return points.max()!!
    }
}

class MarbleGame(root: Long) {
    private var current = Marble(root)

    fun insert(value: Long) {
        current = current.insert(value)
    }

    fun remove(): Long {
        val value = current.value
        current = current.remove()
        return value
    }

    fun rotateLeft(n: Int) = repeat(n) { current = current.succ }

    fun rotateRight(n: Int) = repeat(n) { current = current.prev }
}

class Marble(val value: Long, prev: Marble? = null, succ: Marble? = null) {
    var prev: Marble = prev ?: this
    var succ: Marble = succ ?: this

    fun insert(value: Long): Marble {
        val new = Marble(value, prev, this)
        prev.succ = new
        prev = new
        return new
    }

    fun remove(): Marble {
        prev.succ = succ
        succ.prev = prev
        return succ
    }
}