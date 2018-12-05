package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day

class Day03 : Day<Day03.Claim, Int>(3, "No Matter How You Slice It") {

    override val inputTransform: (String) -> Claim = Claim.Companion::parse

    override fun solutionPart1(inputData: Sequence<Claim>): Int {
        return inputData
            .flatMap { claim ->
                claim.xRange.asSequence()
                    .flatMap { x -> claim.yRange.asSequence().map { y -> x to y } }
            }.groupBy { it }
            .count { it.value.size > 1 }
    }

    override fun solutionPart2(inputData: Sequence<Claim>): Int {
        return inputData.first { claim -> inputData.none { claim.id != it.id && claim overlaps it } }.id
    }

    class Claim(val id: Int, x: Int, y: Int, width: Int, height: Int) {
        val xRange = x until x + width
        val yRange = y until y + height

        infix fun overlaps(otherClaim: Claim): Boolean {
            if (xRange.first > otherClaim.xRange.last || otherClaim.xRange.first > xRange.last) {
                return false
            }

            if (yRange.first > otherClaim.yRange.last || otherClaim.yRange.first > yRange.last) {
                return false
            }

            return true
        }

        companion object {
            private val pattern = Regex("#(\\d+)\\s@\\s(\\d+),(\\d+):\\s(\\d+)x(\\d+)")

            fun parse(claim: String): Claim {
                val result = pattern.matchEntire(claim)!!
                return Claim(
                    result.groupValues[1].toInt(),
                    result.groupValues[2].toInt(),
                    result.groupValues[3].toInt(),
                    result.groupValues[4].toInt(),
                    result.groupValues[5].toInt()
                )
            }
        }
    }
}