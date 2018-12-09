package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day
import java.util.*

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
        val circle = LinkedList(listOf(0L))

        for (i in 1..lastMarble) {
            if (i % 23 == 0) {
                repeat(7) { circle.push(circle.removeLast()) }
                points[i % numPlayers] += i + circle.pop()
            } else {
                repeat(2) { circle.addLast(circle.pop()) }
                circle.push(i.toLong())
            }
        }

        return points.max()!!
    }
}