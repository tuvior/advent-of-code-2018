package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day

class Day12 : Day<String, Number>(12, "Subterranean Sustainability") {

    override val inputTransform: (String) -> String = { it }

    override fun solutionPart1(inputData: Sequence<String>): Int {
        val initialState = parseInitialState(inputData.first())
        val genStates = parseGeneratingStates(inputData.drop(2))

        return generateSequence(initialState) { getNextGenerationState(it, genStates) }.take(21).last().sum()
    }

    override fun solutionPart2(inputData: Sequence<String>): Long {
        val initialState = parseInitialState(inputData.first())
        val genStates = parseGeneratingStates(inputData.drop(2))

        val (divergenceStart, nextState) = generateSequence(initialState) { getNextGenerationState(it, genStates) }
            .mapIndexed { i, state ->
                val min = state.min()!!
                val max = state.max()!!
                (i to state.sum()) to (min..max).map { if (it in state) '#' else '.' }.joinToString("")
            }.zipWithNext()
            .first { (s1, s2) -> s1.second == s2.second }

        val (baseState, baseValue) = divergenceStart.first
        val constantInc = nextState.first.second - baseValue

        return baseValue + (50_000_000_000 - baseState) * constantInc
    }

    private fun parseInitialState(state: String): Set<Int> {
        return state.removePrefix("initial state: ")
            .mapIndexed { i, c -> i to c }
            .filter { (_, c) -> c == '#' }
            .map { (i, _) -> i }
            .toSet()
    }

    private fun parseGeneratingStates(rules: Sequence<String>): Set<String> {
        return rules.map { line -> line.split(" => ").let { it[0] to it[1] } }
            .filter { it.second == "#" }
            .map { it.first }
            .toSet()
    }

    private fun getNextGenerationState(state: Set<Int>, genStates: Set<String>): Set<Int> {
        val min = state.min()!! - 2
        val max = state.max()!! + 2
        return (min..max).map { x -> x to ((x - 2)..(x + 2)).map { if (it in state) '#' else '.' }.joinToString("") }
            .filter { (_, s) -> s in genStates }
            .map { (x, _) -> x }
            .toSet()
    }
}