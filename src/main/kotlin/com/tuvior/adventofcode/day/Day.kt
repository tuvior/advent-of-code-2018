package com.tuvior.adventofcode.day

import com.tuvior.adventofcode.util.measureComputation

abstract class Day<T>(val n: Int) : Runnable {
    protected val inputLines: Sequence<String>

    init {
        val inputFilePath = "/day${"%02d".format(n)}.txt"
        inputLines = javaClass.getResource(inputFilePath).readText().lines().dropLastWhile { it.isEmpty() }.asSequence()
    }

    override fun run() {
        println("Solution of Day $n:")
        val result = getResult()
        println("Part 1: ${result.first} - Part 2: ${result.second}")
        println("Runtime 1: ${result.runtimeFirst / 1_000_000_000f}s - Runtime 2: ${result.runtimeSecond / 1_000_000_000f}s")
        println()
    }

    protected open fun getResult(): Result<T> {
        val (part1, timing1) = measureComputation(::part1)
        val (part2, timing2) = measureComputation(::part2)

        return Result(part1, timing1, part2, timing2)
    }

    protected open fun part1(): T = throw UnsupportedOperationException()
    protected open fun part2(): T = throw UnsupportedOperationException()
}

data class Result<T>(val first: T, val runtimeFirst: Long, val second: T, val runtimeSecond: Long)