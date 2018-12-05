package com.tuvior.adventofcode.util

fun <T> measureComputation(block: () -> T): Pair<T, Long> {
    val before = System.nanoTime()
    val result = block()
    val after = System.nanoTime()
    return result to after - before
}