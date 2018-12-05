package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day
import kotlin.math.abs

class Day05 : Day<String, Int>(5, "Alchemical Reduction") {

    override val inputTransform: (String) -> String = { it }

    override fun solutionPart1(inputData: Sequence<String>): Int {
        val polymers = reactPolymers(inputData.first().toMutableList())
        return polymers.size
    }

    override fun solutionPart2(inputData: Sequence<String>): Int {
        val polymers = inputData.first()

        return ('a'..'z').asSequence()
            .map { c -> polymers.filterNot { it.equals(c, true) } }
            .map { reactPolymers(it.toMutableList()).size }
            .min()!!
    }

    private fun reactPolymers(polymer: MutableList<Char>): List<Char> {
        val iterator = polymer.listIterator()

        var current = iterator.next()
        while (iterator.hasNext()) {
            val next = iterator.next()

            if (abs(current - next) == 32) {
                iterator.remove()
                iterator.previous()
                iterator.remove()
                if (iterator.hasPrevious()) current = iterator.previous()
                else if (iterator.hasNext()) current = iterator.next()
            } else {
                current = next
            }
        }

        return polymer
    }

    // this runs bad on Kotlin
    private fun reactPolymers2(polymer: String): List<Char> {
        return polymer.foldRight(emptyList()) { unit, poly ->
            if (poly.isEmpty()) listOf(unit)
            else {
                val head = poly.first()
                val tail = poly.drop(1)
                if (abs(head - unit) == 32) tail
                else listOf(unit) + poly
            }
        }
    }
}