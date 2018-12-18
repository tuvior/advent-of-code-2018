package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day

class Day18 : Day<String, Int>(18, "Settlers of The North Pole") {

    override val inputTransform: (String) -> String = { it }

    override fun solutionPart1(inputData: Sequence<String>): Int {
        val map = parseMap(inputData)
        val acres = map.flatten()

        repeat(10) { update(map, acres) }

        return acres.count { it.state == '|' } * acres.count { it.state == '#' }
    }

    override fun solutionPart2(inputData: Sequence<String>): Int {
        val map = parseMap(inputData)
        val acres = map.flatten()

        val oldStates = mutableListOf(acres.joinToString("") { it.state.toString() })

        while (true) {
            update(map, acres)

            val state = acres.joinToString("") { it.state.toString() }

            if (state in oldStates) {
                val start = oldStates.indexOf(state)
                val period = oldStates.size - start

                val relative = ((1_000_000_000 - start) % period) + start

                val endState = oldStates[relative]

                return endState.count { it == '|' } * endState.count { it == '#' }
            }

            oldStates += state
        }
    }

    private fun update(map: Array<Array<Acre>>, acres: List<Acre>) {
        val h = map.size
        val w = map[0].size
        for (y in 0 until h) {
            for (x in 0 until w) {
                val acre = map[y][x]
                val r = (x + 1).coerceAtMost(w - 1)
                val d = (y + 1).coerceAtMost(h - 1)
                val l = (x - 1).coerceAtLeast(0)
                val u = (y - 1).coerceAtLeast(0)
                val (trees, yards) = (l..r)
                    .flatMap { x -> (u..d).map { y -> map[y][x] } }
                    .fold(intArrayOf(0, 0)) { c, a ->
                        when {
                            a == acre -> c
                            a.state == '#' -> c.also { it[1]++ }
                            a.state == '|' ->  c.also { it[0]++ }
                            else -> c
                        }
                    }

                when (acre.state) {
                    '.' -> acre.nextState = if (trees >= 3) '|' else '.'
                    '|' -> acre.nextState = if (yards >= 3) '#' else '|'
                    '#' -> acre.nextState = if (yards >= 1 && trees >= 1) '#' else '.'
                }
            }
        }
        acres.forEach { it.state = it.nextState }
    }

    private fun parseMap(acres: Sequence<String>): Array<Array<Acre>> {
        return acres.map { line ->
            line.map { Acre(it, it) }.toTypedArray()
        }.toList().toTypedArray()
    }
}

class Acre(var state: Char, var nextState: Char)