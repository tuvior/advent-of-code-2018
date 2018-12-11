package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day

class Day11 : Day<Int, Any>(11, "Chronal Charge") {

    override val inputTransform: (String) -> Int = String::toInt

    override fun solutionPart1(inputData: Sequence<Int>): Pair<Int, Int> {
        val serial = inputData.first()

        return (1..298).flatMap { x -> (1..298).map { y -> x to y } }
            .map { (x, y) -> (x to y) to squarePowerAt(x, y, serial) }
            .maxBy { it.second }!!.first
    }

    override fun solutionPart2(inputData: Sequence<Int>): Triple<Int, Int, Int> {
        val serial = inputData.first()

        val summedAreas = summedAreaTableOf(300, 300) { x, y -> powerAt(x, y, serial) }

        var bestSquare = Triple(-1, -1, -1)
        var bestSum = Int.MIN_VALUE

        for (s in 1..300) {
            for (x in s..300) {
                for (y in s..300) {
                    val sum = summedAreas[x][y] -
                            summedAreas[x][y - s] -
                            summedAreas[x - s][y] +
                            summedAreas[x - s][y - s]
                    if (sum > bestSum) {
                        bestSquare = Triple(x - s + 1, y - s + 1, s)
                        bestSum = sum
                    }
                }
            }
        }

        return bestSquare
    }

    private fun summedAreaTableOf(width: Int, height: Int, valueFunction: (Int, Int) -> Int): Array<IntArray> {
        val areaTable = Array(width + 1) { IntArray(height + 1) }

        for (x in 1..width) {
            for (y in 1..height) {
                areaTable[x][y] = valueFunction(x, y) +
                        areaTable[x][y - 1] +
                        areaTable[x - 1][y] -
                        areaTable[x - 1][y - 1]
            }
        }
        return areaTable
    }

    private fun powerAt(x: Int, y: Int, serial: Int) = (((x + 10) * y + serial) * (x + 10) / 100 % 10) - 5

    private fun squarePowerAt(xC: Int, yC: Int, serial: Int): Int {
        return (xC until xC + 3).sumBy { x -> (yC until yC + 3).sumBy { y -> powerAt(x, y, serial) } }
    }
}