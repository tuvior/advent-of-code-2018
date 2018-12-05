package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day

class Day01 : Day<Int, Int>(1) {

    override val inputTransform: (String) -> Int = Integer::parseInt

    override fun solutionPart1(inputData: Sequence<Int>): Int {
        return inputData.sum()
    }

    override fun solutionPart2(inputData: Sequence<Int>): Int {
        val visited = mutableSetOf(0)
        var freq = 0
        while(true) {
            for (change in inputData) {
                freq += change
                if (freq in visited) return freq
                else visited += freq
            }
        }
    }
}