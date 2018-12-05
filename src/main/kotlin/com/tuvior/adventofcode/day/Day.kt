package com.tuvior.adventofcode.day

import com.tuvior.adventofcode.util.measureComputation

abstract class Day<IN, R>(val n: Int) : Runnable {
    private val inputLines: List<String> by lazy {
        val inputFilePath = "/day${"%02d".format(n)}.txt"
        javaClass.getResource(inputFilePath).readText().lines().dropLastWhile { it.isEmpty() }
    }

    protected abstract val inputTransform: (String) -> IN

    override fun run() {
        println("Solution of Day $n:")
        val result = getResult()
        println("Part 1: ${result.first} - Part 2: ${result.second}")
        println("Runtime 1: ${result.runtimeFirst / 1_000_000_000f}s - Runtime 2: ${result.runtimeSecond / 1_000_000_000f}s")
        println()
    }

    private fun getResult(): Result<R> {
        val inputData = inputLines.map(inputTransform).asSequence()
        val (part1, timing1) = measureComputation { solutionPart1(inputData) }
        val (part2, timing2) = measureComputation { solutionPart2(inputData) }

        return Result(part1, timing1, part2, timing2)
    }

    protected abstract fun solutionPart1(inputData: Sequence<IN>): R
    protected abstract fun solutionPart2(inputData: Sequence<IN>): R
}

data class Result<T>(val first: T, val runtimeFirst: Long, val second: T, val runtimeSecond: Long)