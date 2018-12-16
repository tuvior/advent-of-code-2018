package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day
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

            if (simulation.any { it == ElfDeath }) power++
            else {
                val winningRound = simulation.size - 1
                val totalHp = units.filter { it.alive }.sumBy { it.hp }
                return winningRound * totalHp
            }
        }
    }

    private fun turn(map: Array<Array<Char>>, units: List<Unit>, stopOnDeath: Boolean = false): TurnResult {
        val sorted = units.filter { it.alive }.sortedWith(compareBy({ it.pos.second }, { it.pos.first }))

        for (unit in sorted) {
            if (!unit.alive) continue

            val status = unit.act(map, sorted)

            if (status == Victory) return status
            else if (status == ElfDeath && stopOnDeath) return status
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


sealed class Unit(var pos: Vector, var hp: Int = 200, var power: Int, val type: Char) {
    var alive = true
    val neighbors get() = pos.neighbors

    fun act(map: Array<Array<Char>>, units: List<Unit>): TurnResult {
        val enemies = units.filter { it.alive }.filter { it.type != type }
            .sortedWith(compareBy({ it.pos.second }, { it.pos.first }))
        val otherUnitPos = units.filter { it.alive && it != this }.map { it.pos }.toSet()

        if (enemies.isEmpty()) return Victory

        val attackPositions =
            enemies.flatMap { it.neighbors.filterNot { p -> p in otherUnitPos || map[p.second][p.first] == '#' } }

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
                    return when (it.type) {
                        'E' -> ElfDeath
                        'G' -> GoblinDeath
                        else -> EndTurn
                    }
                }
            }
        }

        return EndTurn
    }

    private fun move(map: Array<Array<Char>>, attackPositions: List<Vector>, otherUnitPos: Set<Vector>) {
        val Q = LinkedList<Pair<Vector, Int>>()
        val pred = mutableMapOf<Vector, Pair<Int, Vector>>()
        val visited = mutableSetOf<Vector>()

        Q += pos to 0

        while (Q.isNotEmpty()) {
            val (next, dist) = Q.pop()

            next.neighbors.forEach { n ->
                val nDist = dist + 1
                if (map[n.second][n.first] == '#' || n in otherUnitPos) {
                    return@forEach
                }
                if (n !in pred || pred[n]!!.let { nDist < it.first || nDist == it.first && next < it.second }) {
                    pred[n] = nDist to next
                }
                if (n !in visited) {
                    Q += n to nDist
                }
                visited += n
            }
        }

        val candidate = pred.filterKeys { it in attackPositions }
            .entries
            .sortedWith(compareBy({ it.value.first }, { it.key.second }, { it.key.first }))
            .firstOrNull() ?: return

        var t = candidate.key

        while (pred[t]!!.first > 1) {
            t = pred[t]!!.second
        }

        pos = t
    }
}

class Elf(pos: Vector, power: Int = 3, hp: Int = 200) : Unit(pos, hp, power, 'E')
class Goblin(pos: Vector, power: Int = 3, hp: Int = 200) : Unit(pos, hp, power, 'G')

sealed class TurnResult
object EndTurn : TurnResult()
object Victory : TurnResult()
object ElfDeath : TurnResult()
object GoblinDeath : TurnResult()

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