package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day
import com.tuvior.adventofcode.util.Vector
import com.tuvior.adventofcode.util.neighbors
import java.util.*

class Day17 : Day<ClayLine, Int>(17, "Reservoir Research") {

    override val inputTransform: (String) -> ClayLine = ClayLine.Companion::parse

    override fun solutionPart1(inputData: Sequence<ClayLine>): Int {
        val clay = inputData.toList().flatMap { it.tiles }

        val tiles = flow(500 to 0, clay)

        return tiles.count { it.state >= WET }
    }

    override fun solutionPart2(inputData: Sequence<ClayLine>): Int {
        val clay = inputData.toList().flatMap { it.tiles }

        val tiles = flow(500 to 0, clay)

        return tiles.count { it.state == WATER }
    }

    private fun flow(spring: Vector, clay: List<Tile>): List<Tile> {
        val minY = clay.minBy { it.pos.second }!!.pos.second
        val maxY = clay.maxBy { it.pos.second }!!.pos.second

        val waterStack = LinkedList<Tile>()
        val tiles = clay.groupBy { it.pos.second }
            .mapValues { (_, xs) -> xs.map { it.pos.first to it }.toMap().toMutableMap() }
            .toMutableMap()

        var currTile = Tile(spring, WET)

        do {
            val (r, d, l) = currTile.pos.neighbors.drop(1)
            val rTile = tiles.getOrPut(r.second) { mutableMapOf() }.getOrPut(r.first) { Tile(r) }
            val dTile = tiles.getOrPut(d.second) { mutableMapOf() }.getOrPut(d.first) { Tile(d) }
            val lTile = tiles.getOrPut(l.second) { mutableMapOf() }.getOrPut(l.first) { Tile(l) }

            if (rTile.rContain) currTile.r = true
            if (lTile.lContain) currTile.l = true
            if (currTile.r && currTile.l) currTile.state = WATER

            when {
                dTile.state == SAND && d.second <= maxY -> {
                    waterStack.push(currTile)
                    currTile = dTile
                    dTile.state = WET
                }
                rTile.state == SAND && dTile.dContain -> {
                    waterStack.push(currTile)
                    currTile = rTile
                    rTile.state = WET
                }
                lTile.state == SAND && dTile.dContain -> {
                    waterStack.push(currTile)
                    currTile = lTile
                    lTile.state = WET
                }
                else -> {
                    val parent = waterStack.pop()

                    if (parent.pos.second < currTile.pos.second) {
                        // assign water state on this line before going up
                        tiles[currTile.pos.second]!!.values
                            .filter { it.state == WET }
                            .forEach {
                                val (r1, _, l1) = it.pos.neighbors.drop(1)
                                val r1Tile = tiles.getOrPut(r1.second) { mutableMapOf() }.getOrPut(r1.first) { Tile(r1) }
                                val l1Tile = tiles.getOrPut(l1.second) { mutableMapOf() }.getOrPut(l1.first) { Tile(l1) }

                                if (r1Tile.r) it.r = true
                                if (l1Tile.l) it.l = true
                                if (it.l && it.r) it.state = WATER
                            }
                    }

                    currTile = parent
                }
            }
        } while (currTile.pos != spring)

        return tiles.filterKeys { it >= minY }.values.flatMap { it.values }
    }
}

private const val CLAY = -1
private const val SAND = 0
private const val WET = 1
private const val WATER = 2

class Tile(val pos: Vector, var state: Int = SAND, var r: Boolean = false, var l: Boolean = false) {
    val dContain get() = state == WATER || state == CLAY
    val lContain get() = l || state == CLAY
    val rContain get() = r || state == CLAY
}

sealed class ClayLine {
    val tiles: List<Tile>
        get() {
            return when (this) {
                is VerticalClayLine -> y.map { Tile(x to it, CLAY) }
                is HorizontalClayLine -> x.map { Tile(it to y, CLAY) }
            }
        }

    companion object {
        private val vPattern = Regex("x=([0-9]+), y=([0-9]+)\\.\\.([0-9]+)")
        private val hPattern = Regex("y=([0-9]+), x=([0-9]+)\\.\\.([0-9]+)")

        fun parse(clayLine: String): ClayLine {
            return if (clayLine.matches(hPattern)) {
                val result = hPattern.matchEntire(clayLine)!!.groupValues
                HorizontalClayLine(result[2].toInt()..result[3].toInt(), result[1].toInt())
            } else {
                val result = vPattern.matchEntire(clayLine)!!.groupValues
                VerticalClayLine(result[1].toInt(), result[2].toInt()..result[3].toInt())
            }
        }
    }
}

class VerticalClayLine(val x: Int, val y: IntRange) : ClayLine()
class HorizontalClayLine(val x: IntRange, val y: Int) : ClayLine()

