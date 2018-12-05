package com.tuvior.adventofcode

import com.tuvior.adventofcode.day.Day

val days = (1 until 31)
    .map { "%02d".format(it) }
    .map { "com.tuvior.adventofcode.solutions.Day$it" }
    .mapIndexedNotNull { index, className ->
        try {
            (index + 1) to Class.forName(className).newInstance() as Day<*>
        } catch (cnf: ClassNotFoundException) {
            null
        }
    }.toMap()

fun main(args: Array<String>) {
    if (args.size == 1) {
        val day = args[0].toInt()
        if (day in days) {
            days[day]?.run()
        } else {
            error("No solution for Day $day was found.")
        }
    } else {
        days.values.forEach(Day<*>::run)
    }
}