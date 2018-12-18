package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day
import com.tuvior.adventofcode.util.Vector
import com.tuvior.adventofcode.util.compareTo
import com.tuvior.adventofcode.util.neighbors
import com.tuvior.adventofcode.util.takeWhileInclusive
import java.util.*

class Day15 : Day<String, Int>(15, "Beverage Bandits") {

    override val inputTransform: (String) -> String = { it }

    override fun solutionPart1(inputData: Sequence<String>): Int {
        val (map, units) = parseMap(inputData)
        val victoryRound = generateSequence { turn(map, units) }.indexOfFirst { it == Victory }
        val totalHp = units.filter { it.alive }.sumBy { it.hp }

        return victoryRound * totalHp
    }

    override fun solutionPart2(inputData: Sequence<String>): Int {
        var power = 4
        while (true) {
            val (map, units) = parseMap(inputData, power)
            val simulation = generateSequence { turn(map, units, stopOnDeath = true) }
                .takeWhileInclusive { it != Victory && it != ElfDeath }
                .toList()

            if (simulation.last() == ElfDeath) power++
            else {
                val winningRound = simulation.size - 1
                val totalHp = units.filter { it.alive }.sumBy { it.hp }
                return winningRound * totalHp
            }
        }
    }

    private fun turn(map: Array<Array<Char>>, units: List<Unit>, stopOnDeath: Boolean = false): TurnResult {
        val sorted = units.filter { it.alive }.sorted()

        for (unit in sorted) {
            if (!unit.alive) continue

            when (unit.act(map, sorted)) {
                Victory -> return Victory
                ElfDeath -> if (stopOnDeath) return ElfDeath
            }
        }

        return EndTurn
    }

    private fun parseMap(input: Sequence<String>, power: Int = 3): Pair<Array<Array<Char>>, List<Unit>> {
        val units = mutableListOf<Unit>()
        return input.mapIndexed { y, l ->
            l.mapIndexed { x, c ->
                when (c) {
                    'G' -> '.'.also { units += Goblin(x to y) }
                    'E' -> '.'.also { units += Elf(x to y, power) }
                    else -> c
                }
            }.toTypedArray()
        }.toList().toTypedArray() to units
    }
}


private sealed class Unit(var pos: Vector, var power: Int, var hp: Int = 200, val type: Char) : Comparable<Unit> {
    var alive = true
    val neighbors get() = pos.neighbors

    override fun compareTo(other: Unit): Int = pos.compareTo(other.pos)

    fun act(map: Array<Array<Char>>, units: List<Unit>): TurnResult {
        val enemies = units.filter { it.alive }.filter { it.type != type }.sorted()
        if (enemies.isEmpty()) return Victory

        val otherUnitPos = units.filter { it.alive && it != this }.map { it.pos }.toSet()

        val attackPositions = enemies
            .flatMap { it.neighbors.filterNot { p -> p in otherUnitPos || map[p.second][p.first] == '#' } }

        if (pos !in attackPositions) {
            move(map, attackPositions, otherUnitPos)
        }

        val targets = enemies.filter { it.pos in neighbors }

        if (targets.isNotEmpty()) {
            val target = targets.sortedBy { it.hp }.first()

            target.let {
                it.hp -= power
                if (it.hp <= 0) {
                    it.alive = false
                    return when (it) {
                        is Elf -> ElfDeath
                        is Goblin -> GoblinDeath
                    }
                }
            }
        }

        return EndTurn
    }

    private fun move(map: Array<Array<Char>>, attackPositions: List<Vector>, otherUnitPos: Set<Vector>) {
        val pending = LinkedList<Pair<Vector, Int>>()
        val pred = mutableMapOf<Vector, Pair<Int, Vector>>()
        val visited = mutableSetOf<Vector>()

        pending += pos to 0

        while (pending.isNotEmpty()) {
            val (next, dist) = pending.pop()

            next.neighbors.forEach { n ->
                val nDist = dist + 1
                if (map[n.second][n.first] == '#' || n in otherUnitPos) {
                    return@forEach
                }
                if (n !in pred || pred[n]!!.let { nDist < it.first || nDist == it.first && next < it.second }) {
                    pred[n] = nDist to next
                }
                if (n !in visited) {
                    pending += n to nDist
                }
                visited += n
            }
        }

        val candidate = pred.filterKeys { it in attackPositions }
            .entries.sortedWith(compareBy({ it.value.first }, { it.key.second }, { it.key.first }))
            .firstOrNull() ?: return

        var t = candidate.key

        while (pred[t]!!.first > 1) {
            t = pred[t]!!.second
        }

        pos = t
    }
}

private class Elf(pos: Vector, power: Int = 3, hp: Int = 200) : Unit(pos, power, hp, 'E')
private class Goblin(pos: Vector, power: Int = 3, hp: Int = 200) : Unit(pos, power, hp, 'G')

sealed class TurnResult
object EndTurn : TurnResult()
object Victory : TurnResult()
object ElfDeath : TurnResult()
object GoblinDeath : TurnResult()