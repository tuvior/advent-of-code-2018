package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day

class Day02 : Day<String, Any>(2, "Inventory Management System") {

    override val inputTransform: (String) -> String = { it }

    override fun solutionPart1(inputData: Sequence<String>): Any {
        return inputData
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

    override fun solutionPart2(inputData: Sequence<String>): Any {
        return inputData
            .mapIndexed { i, line -> inputData.drop(i + 1).map { line.zip(it) } }
            .flatten()
            .first { zipped -> zipped.count { it.first != it.second } == 1 }
            .let { zipped -> zipped.filter { it.first == it.second }.joinToString("") { it.first.toString() } }
    }
}