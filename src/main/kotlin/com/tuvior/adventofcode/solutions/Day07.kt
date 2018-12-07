package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day
import java.util.*

class Day07 : Day<Dependency, Any>(7, "The Sum of Its Parts") {

    override val inputTransform: (String) -> Dependency = Dependency.Companion::parse

    override fun solutionPart1(inputData: Sequence<Dependency>): String {
        val stepNames = ('A'..'Z').toList()

        val nodeDeps = stepNames.associateWith { s ->
            inputData.filter { it.self == s }.map { it.dependsOn }.toMutableList()
        }.toMutableMap()

        val free = PriorityQueue(nodeDeps.filterValues { it.isEmpty() }.keys)
            .apply { forEach { nodeDeps -= it } }

        var order = ""

        while (free.isNotEmpty()) {
            val n = free.remove()
            order += n

            nodeDeps.values.forEach { it -= n }
            val newCandidates = nodeDeps.filterValues { it.isEmpty() }.keys
                .apply { forEach { nodeDeps -= it } }
            free += newCandidates
        }

        return order
    }

    override fun solutionPart2(inputData: Sequence<Dependency>): Int {
        val stepNames = ('A'..'Z').toList()

        val nodeDeps = stepNames.associateWith { s ->
            inputData.filter { it.self == s }.map { it.dependsOn }.toMutableList()
        }.toMutableMap()

        val free = PriorityQueue(nodeDeps.filterValues { it.isEmpty() }.keys)
            .apply { forEach { nodeDeps -= it } }

        var t = 0
        val workers = arrayOf<Pair<Char, Int>?>(null, null, null, null, null)

        while (nodeDeps.isNotEmpty()) {
            workers.filterNotNull().filter { it.second <= t }.forEach { w ->
                val i = workers.indexOf(w)
                nodeDeps.values.forEach { it -= w.first }
                workers[i] = null
                val newCandidates = nodeDeps.filterValues { it.isEmpty() }.keys
                    .apply { forEach { nodeDeps -= it } }
                free += newCandidates
            }

            workers.forEachIndexed { i, w ->
                if (w == null && free.isNotEmpty()) {
                    val n = free.remove()
                    val duration = n - 'A' + 61
                    workers[i] = n to t + duration
                }
            }

            t = workers.filterNotNull().map { it.second }.min() ?: t + 1
        }

        return t
    }
}

class Dependency(val self: Char, val dependsOn: Char) {
    companion object {
        private val pattern = Regex("Step ([A-Z]) must be finished before step ([A-Z]) can begin.")

        fun parse(dependency: String): Dependency {
            val result = pattern.matchEntire(dependency)!!
            return Dependency(result.groupValues[2][0], result.groupValues[1][0])
        }
    }
}