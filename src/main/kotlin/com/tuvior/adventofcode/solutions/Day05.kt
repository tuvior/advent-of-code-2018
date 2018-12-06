package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day
import java.util.*
import kotlin.math.abs

class Day05 : Day<String, Int>(5, "Alchemical Reduction") {

    override val inputTransform: (String) -> String = { it }

    override fun solutionPart1(inputData: Sequence<String>): Int {
        val polymers = reactPolymers(inputData.first().toList())
        return polymers.size
    }

    override fun solutionPart2(inputData: Sequence<String>): Int {
        val reactedPolymers = reactPolymers(inputData.first().toList())

        return ('a'..'z').asSequence()
            .map { c -> reactedPolymers.filterNot { it.equals(c, true) } }
            .map { reactPolymers(it.toList()).size }
            .min()!!
    }

    private fun reactPolymers(polymer: List<Char>): List<Char> {
        return polymer.foldRight(LinkedList()) { unit, poly ->
            if (poly.isEmpty()) poly.addFirst(unit)
            else {
                val head = poly.first()
                if (abs(head - unit) == 32) poly.pop()
                else poly.addFirst(unit)
            }
            poly
        }
    }
}