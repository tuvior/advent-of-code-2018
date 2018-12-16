package com.tuvior.adventofcode.util

inline fun <T> measureComputation(crossinline block: () -> T): Pair<T, Long> {
    val before = System.nanoTime()
    val result = block()
    val after = System.nanoTime()
    return result to after - before
}

fun <T> Sequence<T>.takeWhileInclusive(pred: (T) -> Boolean): Sequence<T> {
    var shouldContinue = true
    return takeWhile {
        val result = shouldContinue
        shouldContinue = pred(it)
        result
    }
}

typealias Vector = Pair<Int, Int>

operator fun Vector.compareTo(other: Vector): Int {
    return if (second.compareTo(other.second) == 0) first.compareTo(other.first)
    else second.compareTo(other.second)
}

val Vector.neighbors
    get() = listOf(
        first to second - 1,
        first + 1 to second,
        first to second + 1,
        first - 1 to second
    )