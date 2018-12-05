package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day
import kotlin.math.abs

class Day01 : Day<Int, Int>(1, "Chronal Calibration") {

    override val inputTransform: (String) -> Int = Integer::parseInt

    override fun solutionPart1(inputData: Sequence<Int>): Int {
        return inputData.sum()
    }

    override fun solutionPart2(inputData: Sequence<Int>): Int {
        val sum = inputData.sum()
        val sums = inputData.fold(listOf(0)) { l, n -> l + (l.last() + n) }

        val set = mutableSetOf<Int>()
        sums.forEach { if (it in set) return it else set += it }

        return sums.dropLast(1)
            .groupBy { ((it % sum) + sum) % sum }
            .map { it.value.distinct() }
            .filter { it.size > 1 }
            .minBy { (a, b) -> abs(a - b) }!!
            .last()
    }
}