package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day

class Day10 : Day<Light, Any>(10, "The Stars Align") {

    override val inputTransform: (String) -> Light = Light.Companion::parse

    override fun solutionPart1(inputData: Sequence<Light>): String {
        val t = getTimeAtHeightMinima(inputData)

        val alignedPoints = inputData.map { it.positionAt(t) }

        val (minX, maxX) = alignedPoints.map { it.first }.let { it.min()!! to it.max()!! }

        return alignedPoints
            .groupBy { it.second }
            .toSortedMap()
            .mapValues { pos -> pos.value.map { it.first } }.values
            .joinToString("\n", "\n", "\n") { xs ->
                (minX..maxX).map { it in xs }.joinToString(" ") { if (it) "#" else " " }
            }
    }

    override fun solutionPart2(inputData: Sequence<Light>): Int {
        return getTimeAtHeightMinima(inputData)
    }

    private fun getTimeAtHeightMinima(lights: Sequence<Light>): Int {
        return generateSequence(0, Int::inc)
            .map { t -> lights.map { it.positionAt(t).second } }
            .map { it.max()!! - it.min()!! }
            .zipWithNext()
            .indexOfFirst { (h1, h2) -> h2 > h1 }
    }

}

typealias Vector = Pair<Int, Int>

class Light(private val position: Vector, private val velocity: Vector) {

    fun positionAt(t: Int): Vector {
        return position.first + velocity.first * t to position.second + velocity.second * t
    }

    companion object {
        private val pattern =
            Regex("position=<[\\s]*([-]?[\\d]+),[\\s]*([-]?[\\d]+)> velocity=<[\\s]*([-]?[\\d]+),[\\s]*([-]?[\\d]+)>")

        fun parse(entry: String): Light {
            val result = pattern.matchEntire(entry)!!
            return result.groupValues.let {
                Light(
                    it[1].toInt() to it[2].toInt(),
                    it[3].toInt() to it[4].toInt()
                )
            }
        }
    }
}