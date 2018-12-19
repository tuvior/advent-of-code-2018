package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day

class Day19 : Day<String, Int>(19, "Go With The Flow") {

    override val inputTransform: (String) -> String = { it }

    override fun solutionPart1(inputData: Sequence<String>): Int {
        val bind = inputData.first().substringAfter(" ").toInt()
        val program = parseProgram(inputData.drop(1)).toList()

        return runProgram(program, intArrayOf(0, 0, 0, 0, 0, 0), bind)[0]
    }

    /**
     * The program calculates the sum of dividends of the number generated in r5
     * in the case where r0 = 1 the number generated is significantly larger
     */
    override fun solutionPart2(inputData: Sequence<String>): Int {
        val bind = inputData.first().substringAfter(" ").toInt()
        val program = parseProgram(inputData.drop(1)).toList()

        val r5 = runUntilBackJump(program, intArrayOf(1, 0, 0, 0, 0, 0), bind)[5]

        return generateSequence(1, Int::inc).take(r5).filter { r5 % it == 0 }.sum()
    }

    private fun runProgram(program: List<NamedInstruction>, state: Register, bind: Int): Register {
        var ip = 0
        var register = state
        while (ip in 0 until program.size) {
            register[bind] = ip
            register = program[ip].let { ins -> ins.op.execute(register, ins) }
            ip = register[bind] + 1
        }
        return register
    }

    private fun runUntilBackJump(program: List<NamedInstruction>, state: Register, bind: Int): Register {
        var ip = 0
        var register = state
        while (ip in 0 until program.size) {
            register[bind] = ip
            register = program[ip].let { ins -> ins.op.execute(register, ins) }
            if (register[bind] + 1 < ip) break
            ip = register[bind] + 1
        }
        return register
    }

    private fun parseProgram(instructions: Sequence<String>): Sequence<NamedInstruction> {
        return instructions.map { it.split(" ") }
            .map { (opName, a, b, c) ->
                val op = ops.first { it.toString() == opName }
                NamedInstruction(op, a.toInt(), b.toInt(), c.toInt())
            }
    }
}

class NamedInstruction(val op: Op, a: Int, b: Int, c: Int) : Instruction(-1, a, b, c)