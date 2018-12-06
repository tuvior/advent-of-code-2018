package com.tuvior.adventofcode

import com.tuvior.adventofcode.day.Day
import com.tuvior.adventofcode.day.Result
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.function.Executable
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible
import kotlin.test.assertEquals
import org.junit.jupiter.api.TestFactory as testFactory

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

    @testFactory fun days_AreResultsCorrect(): Collection<DynamicTest> {
        val days = (1..25)
            .map { "%02d".format(it) }
            .map { "com.tuvior.adventofcode.solutions.Day$it" }
            .mapIndexedNotNull { index, className ->
                try {
                    (index + 1) to Class.forName(className).newInstance() as Day<*, *>
                } catch (cnf: ClassNotFoundException) {
                    null
                }
            }.toMap()

        return days.map { (n, day) ->
            val exec = Executable {
                val (solution1, solution2) = resultsForDay(n)

                val res = getResult.call(day) as Result<*>

                assertEquals(res.first.toString(), solution1, "Wrong solution for Part 1")
                assertEquals(res.second.toString(), solution2, "Wrong solution for Part 2")
            }
            val testName = "day${"%02d".format(n)}_isResultCorrect"
            DynamicTest.dynamicTest(testName, exec)
        }
    }
}