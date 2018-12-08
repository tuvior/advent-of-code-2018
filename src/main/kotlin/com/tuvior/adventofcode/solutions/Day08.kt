package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day
import java.util.*

class Day08 : Day<List<Int>, Int>(8, "Memory Maneuver") {

    override val inputTransform: (String) -> List<Int> = { line -> line.split(" ").map(String::toInt) }

    override fun solutionPart1(inputData: Sequence<List<Int>>): Int {
        val licenseTree = LinkedList(inputData.first())

        fun nodeChecksum(list: LinkedList<Int>): Int {
            val children = list.remove()
            val metadata = list.remove()
            val childrenChecksum = (1..children).map { nodeChecksum(list) }.sum()
            val nodeChecksum = (1..metadata).map { list.remove() }.sum()
            return childrenChecksum + nodeChecksum
        }

        return nodeChecksum(licenseTree)
    }

    override fun solutionPart2(inputData: Sequence<List<Int>>): Int {
        val licenseTree = LinkedList(inputData.first())

        fun nodeChecksum(list: LinkedList<Int>): Int {
            val children = list.remove()
            val metadata = list.remove()
            val childrenChecksum = (1..children).map { nodeChecksum(list) }
            val nodeChecksum = (1..metadata).map { list.remove() }
            return if (children == 0) {
                nodeChecksum.sum()
            } else {
                nodeChecksum.filter { it in 1..children }
                    .map { childrenChecksum[it - 1] }
                    .sum()
            }
        }

        return nodeChecksum(licenseTree)
    }
}