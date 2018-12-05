package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day

class Day01 : Day<Int>(1) {

    override fun part1(): Int {
        return inputLines.map(Integer::parseInt).sum()
    }

    override fun part2(): Int {
        val visited = mutableSetOf(0)
        val changes = inputLines.map(Integer::parseInt)
        var freq = 0
        while(true) {
            for (change in changes) {
                freq += change
                if (freq in visited) return freq
                else visited += freq
            }
        }
    }
}