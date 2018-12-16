package com.tuvior.adventofcode.day

import com.tuvior.adventofcode.util.measureComputation

abstract class Day<IN, out R>(val n: Int, val title: String) : Runnable {
    private val inputLines: List<String> by lazy {
        val inputFilePath = "/input/day${"%02d".format(n)}.txt"
        javaClass.getResource(inputFilePath)
            ?.readText()?.lines()?.dropLastWhile { it.isEmpty() }
            ?: throw IllegalStateException("Input file for Day $n doesn't exist. ($inputFilePath)")
    }

    protected abstract val inputTransform: (String) -> IN

    override fun run() {
        println("Solution of Day $n [$title]:")
        val result = getResult()
        println(" Part 1: ${result.first} {t = ${"%.4f".format(result.runtimeFirst / 1_000_000f)} ms}")
        println(" Part 2: ${result.second} {t = ${"%.4f".format(result.runtimeSecond / 1_000_000f)} ms}")
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

class Result<T>(val first: T, val runtimeFirst: Long, val second: T, val runtimeSecond: Long)