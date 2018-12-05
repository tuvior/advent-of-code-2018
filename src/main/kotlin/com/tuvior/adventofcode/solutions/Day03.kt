package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day

class Day03 : Day<Int>(3) {

    override fun part1(): Int {
        return inputLines
            .map(Claim.Companion::parse)
            .flatMap { claim ->
                claim.xRange.asSequence()
                    .flatMap { x -> claim.yRange.asSequence().map { y -> x to y } }
            }.groupBy { it }
            .count { it.value.size > 1 }
    }

    override fun part2(): Int {
        val claims = inputLines.map(Claim.Companion::parse)

        return claims.first { claim -> claims.none { claim.id != it.id && claim overlaps it } }.id
    }

    class Claim(val id: Int, val x: Int, val y: Int, val width: Int, val height: Int) {
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