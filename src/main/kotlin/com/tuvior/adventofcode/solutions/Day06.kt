package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day
import kotlin.math.abs

class Day06 : Day<Point, Int>(6, "Chronal Coordinates") {

    override val inputTransform: (String) -> Point = Point.Companion::parse

    override fun solutionPart1(inputData: Sequence<Point>): Int {
        val maxX = inputData.maxBy { it.x }!!.x
        val maxY = inputData.maxBy { it.y }!!.y
        val minX = inputData.minBy { it.x }!!.x
        val minY = inputData.minBy { it.y }!!.y

        val points = (minX..maxX).flatMap { x -> (minY..maxY).map { y -> Point(x, y) } }

        return points.mapNotNull { p ->
            val distances = inputData.map { it to p.manhattanDistance(it) }
            val min = distances.minBy { it.second }!!.second
            distances.filter { it.second == min }.let { if (it.count() == 1) (it.first().first to p) else null }
        }.groupBy { it.first }.mapValues { it.value.map { it.second } }
            .filterNot { (_, closest) ->
                closest.any { it.x == minX || it.y == maxY || it.x == minY || it.y == maxY }
            }.map { it.value.count() }
            .max()!!
    }

    override fun solutionPart2(inputData: Sequence<Point>): Int {
        val inSize = inputData.count()
        val maxX = inputData.maxBy { it.x }!!.x + 10_000 / inSize + 1
        val maxY = inputData.maxBy { it.y }!!.y + 10_000 / inSize + 1
        val minX = inputData.minBy { it.x }!!.x - 10_000 / inSize - 1
        val minY = inputData.minBy { it.y }!!.y - 10_000 / inSize - 1

        val points = (minX..maxX).flatMap { x -> (minY..maxY).map { y -> Point(x, y) } }

        return points.map { p -> inputData.sumBy { it.manhattanDistance(p) } }.filter { it < 10_000 }.count()
    }
}

class Point(val x: Int, val y: Int) {
    fun manhattanDistance(p: Point): Int {
        return abs(x - p.x) + abs(y - p.y)
    }

    companion object {
        fun parse(coordinate: String): Point {
            val (x, y) = coordinate.split(", ").take(2).map { it.toInt() }
            return Point(x, y)
        }
    }
}