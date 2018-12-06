package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day

class Day04 : Day<LogLine, Int>(4, "Repose Record") {

    override val inputTransform: (String) -> LogLine = LogLine.Companion::parse

    override fun solutionPart1(inputData: Sequence<LogLine>): Int {
        val sortedLogs = inputData.sorted()
        val guardsTable = makeGuardSleepingTable(sortedLogs)

        val worstGuard = guardsTable.maxBy { it.value.sum() }!!
        return worstGuard.key * worstGuard.value.indexOf(worstGuard.value.max()!!)
    }

    override fun solutionPart2(inputData: Sequence<LogLine>): Int {
        val sortedLogs = inputData.sorted()
        val guardsTable = makeGuardSleepingTable(sortedLogs)

        return guardsTable.maxBy { (_, table) ->
            table.max()!!
        }?.let { (guard, table) ->
            guard * table.indexOf(table.max()!!)
        } ?: -1
    }

    private fun makeGuardSleepingTable(logs: Sequence<LogLine>): Map<Int, IntArray> {
        val guardsTable = mutableMapOf<Int, IntArray>()

        var currentGuard = 0
        var startTime = -1
        for (log in logs) {
            when (log.logType) {
                LogLine.BEGIN_SHIFT -> currentGuard = log.guard
                LogLine.FALL_ASLEEP -> startTime = log.minutes
                LogLine.WAKE_UP -> {
                    val minutes = guardsTable.getOrPut(currentGuard) { IntArray(60) }
                    for (minute in (startTime until log.minutes)) {
                        minutes[minute] += 1
                    }
                }
            }
        }

        return guardsTable
    }
}

class LogLine(
    private val year: Int,
    private val month: Int,
    private val day: Int,
    private val hours: Int,
    val minutes: Int,
    content: String
) : Comparable<LogLine> {
    val logType = when {
        "Guard" in content -> BEGIN_SHIFT
        "falls asleep" == content -> FALL_ASLEEP
        "wakes up" == content -> WAKE_UP
        else -> OTHER
    }
    val guard = if (logType == BEGIN_SHIFT) content.split(" ")[1].drop(1).toInt() else -1

    override fun compareTo(other: LogLine): Int {
        if (this == other) return 0
        val yearC = year.compareTo(other.year)
        if (yearC != 0) return yearC
        val monthC = month.compareTo(other.month)
        if (monthC != 0) return monthC
        val dayC = day.compareTo(other.day)
        if (dayC != 0) return dayC
        val hourC = hours.compareTo(other.hours)
        if (hourC != 0) return hourC
        return minutes.compareTo(other.minutes)
    }

    companion object {
        const val BEGIN_SHIFT = 0
        const val FALL_ASLEEP = 1
        const val WAKE_UP = 2
        const val OTHER = 3

        private val pattern = Regex("\\[([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2})] (.*)")

        fun parse(log: String): LogLine {
            val result = pattern.matchEntire(log)!!
            return LogLine(
                result.groupValues[1].toInt(),
                result.groupValues[2].toInt(),
                result.groupValues[3].toInt(),
                result.groupValues[4].toInt(),
                result.groupValues[5].toInt(),
                result.groupValues[6]
            )
        }
    }
}