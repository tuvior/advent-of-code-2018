package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day

class Day13 : Day<String, Vector>(13, "Mine Cart Madness") {

    override val inputTransform: (String) -> String = { it }

    override fun solutionPart1(inputData: Sequence<String>): Vector {
        val (tracks, carts) = parseMap(inputData)

        while (true) {
            carts.forEach { c ->
                c.advance()
                val track = tracks[c.position.second]!![c.position.first]!!
                when (track) {
                    '/', '\\' -> c.turn(track)
                    '+' -> c.turnIntersection()
                }
            }

            carts.groupingBy { it.position }
                .eachCount()
                .entries
                .firstOrNull { (_, c) -> c > 1 }
                ?.let { p -> return p.key }
        }
    }

    override fun solutionPart2(inputData: Sequence<String>): Vector {
        val (tracks, carts) = parseMap(inputData)

        while (true) {
            carts.filterNot { it.crashed }
                .sortedWith(compareBy({ it.position.first }, { it.position.second }))
                .forEach { c ->
                    if (!c.crashed) {
                        c.advance()

                        carts.filterNot { it.crashed }.firstOrNull { it != c && it.position == c.position }
                            ?.let {
                                it.crashed = true
                                c.crashed = true
                                return@forEach
                            }

                        val track = tracks[c.position.second]!![c.position.first]!!
                        when (track) {
                            '/', '\\' -> c.turn(track)
                            '+' -> c.turnIntersection()
                        }
                    }
                }

            carts.filterNot { it.crashed }.let { alive ->
                if (alive.size == 1) return alive.first().position
            }
        }
    }

    private fun parseMap(input: Sequence<String>): Pair<Map<Int, Map<Int, Char>>, List<Cart>> {
        val carts = mutableListOf<Cart>()
        return input.mapIndexed { y, l ->
            y to l.mapIndexed { x, c ->
                x to when (c) {
                    '^' -> '|'.also { carts += Cart(x to y, 0 to -1) }
                    '<' -> '-'.also { carts += Cart(x to y, -1 to 0) }
                    '>' -> '-'.also { carts += Cart(x to y, 1 to 0) }
                    'v' -> '|'.also { carts += Cart(x to y, 0 to 1) }
                    else -> c
                }
            }.toMap()
        }.toMap() to carts
    }
}

data class Cart(var position: Vector, private var velocity: Vector) {
    private var turn = 0
    var crashed = false

    fun turn(slope: Char) {
        when (slope) {
            '/' -> velocity = -velocity.second to -velocity.first
            '\\' -> velocity = velocity.second to velocity.first
        }
    }

    fun turnIntersection() {
        when (turn) {
            0 -> velocity = velocity.second to -velocity.first
            2 -> velocity = -velocity.second to velocity.first
        }
        turn = (turn + 1) % 3
    }

    fun advance() {
        position = position.first + velocity.first to position.second + velocity.second
    }
}