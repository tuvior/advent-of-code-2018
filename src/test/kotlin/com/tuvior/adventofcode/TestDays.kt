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
        val inputFilePath1 = "/day${"%02d".format(n)}_1_result.txt"
        val inputFilePath2 = "/day${"%02d".format(n)}_2_result.txt"
        val solution = javaClass.getResource(inputFilePath)
            ?.readText()?.lines()?.dropLastWhile { it.isEmpty() }?.take(2)

        if (solution == null) {
            val (part1, part2) = javaClass.getResource(inputFilePath1)?.readText() to
                    javaClass.getResource(inputFilePath2)?.readText()

            if (part1 == null || part2 == null)  {
                throw IllegalStateException("Input solution for Day $n doesn't exist. ($inputFilePath)")
            }
            return part1 to part2
        }

        val (part1, part2) = solution

        return part1 to part2
    }

    @testFactory fun days_AreResultsCorrect(): Collection<DynamicTest> {
        val ignoreDays = listOf(14) // days that don't run really well on travis
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

        val isTravis = System.getenv("TRAVIS") == "true"

        return days.filterNot { isTravis && it.key in ignoreDays }
            .map { (n, day) ->
            val exec = Executable {
                val (solution1, solution2) = resultsForDay(n)

                val res = getResult.call(day) as Result<*>

                assertEquals(solution1, res.first.toString(), "Wrong solution for Part 1")
                assertEquals(solution2, res.second.toString(), "Wrong solution for Part 2")
            }
            val testName = "day${"%02d".format(n)}_isResultCorrect"
            DynamicTest.dynamicTest(testName, exec)
        }
    }
}