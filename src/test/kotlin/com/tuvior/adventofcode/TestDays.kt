package com.tuvior.adventofcode

import com.tuvior.adventofcode.day.Day
import com.tuvior.adventofcode.day.Result
import com.tuvior.adventofcode.solutions.*
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible
import kotlin.test.assertEquals
import org.junit.Test as test

class TestDays {
    private val getResult = Day::class.declaredFunctions
        .first { it.name == "getResult" }
        .also { it.isAccessible = true }

    private fun resultsForDay(n: Int): Pair<String, String> {
        val inputFilePath = "/day${"%02d".format(n)}_result.txt"
        val (first, second) = javaClass.getResource(inputFilePath)
            ?.readText()?.lines()?.dropLastWhile { it.isEmpty() }?.take(2)
            ?: throw IllegalStateException("Input solution for Day $n doesn't exist. ($inputFilePath)")
        return first to second
    }

    @test fun day1_IsResultCorrect() {
        val (solution1, solution2) = resultsForDay(1)

        val res = getResult.call(Day01()) as Result<*>

        assertEquals(res.first.toString(), solution1, "Wrong solution for Part 1")
        assertEquals(res.second.toString(), solution2, "Wrong solution for Part 2")
    }

    @test fun day2_IsResultCorrect() {
        val (solution1, solution2) = resultsForDay(2)

        val res = getResult.call(Day02()) as Result<*>

        assertEquals(res.first.toString(), solution1, "Wrong solution for Part 1")
        assertEquals(res.second.toString(), solution2, "Wrong solution for Part 2")
    }

    @test fun day3_IsResultCorrect() {
        val (solution1, solution2) = resultsForDay(3)

        val res = getResult.call(Day03()) as Result<*>

        assertEquals(res.first.toString(), solution1, "Wrong solution for Part 1")
        assertEquals(res.second.toString(), solution2, "Wrong solution for Part 2")
    }

    @test fun day4_IsResultCorrect() {
        val (solution1, solution2) = resultsForDay(4)

        val res = getResult.call(Day04()) as Result<*>

        assertEquals(res.first.toString(), solution1, "Wrong solution for Part 1")
        assertEquals(res.second.toString(), solution2, "Wrong solution for Part 2")
    }

    @test fun day5_IsResultCorrect() {
        val (solution1, solution2) = resultsForDay(5)

        val res = getResult.call(Day05()) as Result<*>

        assertEquals(res.first.toString(), solution1, "Wrong solution for Part 1")
        assertEquals(res.second.toString(), solution2, "Wrong solution for Part 2")
    }
}