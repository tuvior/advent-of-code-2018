package com.tuvior.adventofcode.util

inline fun <T> measureComputation(crossinline block: () -> T): Pair<T, Long> {
    val before = System.nanoTime()
    val result = block()
    val after = System.nanoTime()
    return result to after - before
}