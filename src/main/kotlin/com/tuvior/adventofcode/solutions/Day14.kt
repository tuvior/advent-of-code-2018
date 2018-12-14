package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day

class Day14 : Day<Int, Any>(14, "Chocolate Charts") {

    override val inputTransform: (String) -> Int = String::toInt

    override fun solutionPart1(inputData: Sequence<Int>): String {
        val nRecipes = inputData.first()

        val recipeBoard = RecipeBoard(3, 7)

        while (recipeBoard.recipes <= nRecipes + 10) {
            val sumScores = recipeBoard.elfA.value + recipeBoard.elfB.value
            recipeBoard.insert(sumScores)
            recipeBoard.stepFirst(1 + recipeBoard.elfA.value)
            recipeBoard.stepSecond(1 + recipeBoard.elfB.value)
        }

        return recipeBoard.getTailSig(nRecipes)
    }

    override fun solutionPart2(inputData: Sequence<Int>): Int {
        val signature = inputData.first().toString()

        val recipeBoard = RecipeBoard(3, 7, signature.length + 1)

        while (signature !in recipeBoard.sig) {
            val sumScores = recipeBoard.elfA.value + recipeBoard.elfB.value
            recipeBoard.insert(sumScores)
            recipeBoard.stepFirst(1 + recipeBoard.elfA.value)
            recipeBoard.stepSecond(1 + recipeBoard.elfB.value)
        }

        return recipeBoard.recipes - signature.length - (1 - recipeBoard.sig.indexOf(signature))
    }
}

class RecipeBoard(recipe1: Int, recipe2: Int, private val sigLen: Int = 0) {
    var elfA: Recipe = Recipe(recipe1)
    var elfB: Recipe = Recipe(recipe2)
    var recipes = 2
    private var tail: Recipe = elfB
    var sig = "$recipe1$recipe2"

    init {
        elfA.succ = elfB
        elfA.prev = elfB
        elfB.succ = elfA
        elfB.prev = elfA
    }

    fun insert(value: Int) {
        val first = value / 10
        val second = value % 10

        if (first > 0) {
            tail = tail.append(first); recipes++
            if (sigLen > 0) sig += first
        }
        tail = tail.append(second); recipes++
        if (sigLen > 0) {
            sig += second
            sig = sig.takeLast(sigLen)
        }
    }

    fun getTailSig(n: Int): String {
        var r = tail
        repeat(recipes - 10 - n) { r = r.prev }
        return buildString {
            repeat(10) {
                insert(0, r.value)
                r = r.prev
            }
        }
    }

    fun stepFirst(n: Int) = repeat(n) { elfA = elfA.succ }

    fun stepSecond(n: Int) = repeat(n) { elfB = elfB.succ }


}

class Recipe(val value: Int, prev: Recipe? = null, succ: Recipe? = null) {
    var prev: Recipe = prev ?: this
    var succ: Recipe = succ ?: this

    fun append(value: Int): Recipe {
        val new = Recipe(value, this, succ)
        succ.prev = new
        succ = new
        return new
    }
}