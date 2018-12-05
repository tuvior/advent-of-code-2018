package com.tuvior.adventofcode.day

abstract class Day<T>(val n: Int) : Runnable {
    protected val inputLines: Sequence<String>

    init {
        val inputFilePath = "/day${"%02d".format(n)}.txt"
        inputLines = javaClass.getResource(inputFilePath).readText().lines().dropLastWhile { it.isEmpty() }.asSequence()
    }

    override fun run() {
        println("Solution of Day $n")
        val before = System.nanoTime()
        val result = getResult()
        val after = System.nanoTime()
        println("Part 1: ${result.first} - Part 2: ${result.second}")
        println("Runtime: ${(after - before) / 1_000_000_000f}s")
    }

    protected open fun getResult(): Result<T> = Result(part1(), part2())
    protected open fun part1(): T = throw UnsupportedOperationException()
    protected open fun part2(): T = throw UnsupportedOperationException()
}

data class Result<T>(val first: T, val second: T)