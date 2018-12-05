package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day

class Day02 : Day<Any>(2) {

    override fun part1(): Int {
        return inputLines
            .map { line -> line.groupBy { it }.map { it.value.size } }
            .fold(0 to 0) { acc, occurrences ->
                val dub = occurrences.any { it == 2 }
                val trip = occurrences.any { it == 3 }
                when {
                    trip && dub -> acc.first + 1 to acc.second + 1
                    trip -> acc.first to acc.second + 1
                    dub -> acc.first + 1 to acc.second
                    else -> acc
                }
            }.let { it.first * it.second }
    }

    override fun part2(): String {
        return inputLines
            .mapIndexed { i, line -> inputLines.drop(i + 1).map { line to it } }
            .flatten()
            .first { (a, b) -> a.zip(b).count { it.first != it.second } == 1 }
            .let { (a, b) -> a.zip(b).filter { it.first == it.second }.joinToString("") { it.first.toString() } }
    }
}