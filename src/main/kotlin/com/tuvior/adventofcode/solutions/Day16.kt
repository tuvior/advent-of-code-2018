package com.tuvior.adventofcode.solutions

import com.tuvior.adventofcode.day.Day

class Day16 : Day<String, Int>(16, "Chronal Classification") {

    override val inputTransform: (String) -> String = { it }

    override fun solutionPart1(inputData: Sequence<String>): Int {
        val examples = parseExamples(inputData.joinToString("\n").substringBefore("\n\n\n\n"))

        return examples.map { (reg, instruction, regAfter) ->
            ops.count { it.execute(reg, instruction).contentEquals(regAfter) }
        }.count { it >= 3 }
    }

    override fun solutionPart2(inputData: Sequence<String>): Int {
        val (ex, prog) = inputData.joinToString("\n").split("\n\n\n\n")

        val examples = parseExamples(ex)

        val program = prog.split("\n")
            .map { line -> line.split(" ").map { it.toInt() }.let { Instruction(it[0], it[1], it[2], it[3]) } }

        val opCodes = examples.flatMap { (reg, instruction, regAfter) ->
            ops.filter { it.execute(reg, instruction).contentEquals(regAfter) }.map { instruction.opcode to it }
        }.groupBy { it.first }.mapValues { it.value.map { it.second }.distinct().toMutableList() }

        val assigned = mutableSetOf<Int>()

        while (opCodes.values.any { it.size > 1 }) {
            val next = opCodes.filterKeys { it !in assigned }.entries.first { it.value.size == 1 }
            assigned += next.key

            opCodes.filterKeys { it !in assigned }.values.forEach { it -= next.value[0] }
        }

        return program.fold(intArrayOf(0, 0, 0, 0)) { register, instruction ->
            opCodes[instruction.opcode]!![0].execute(register, instruction)
        }[0]
    }
}

private fun parseExamples(examples: String): List<Triple<Register, Instruction, Register>> {
    return examples.split("\n\n").map { example ->
        val (before, ins, after) = example.split("\n")

        val reg = before.removeSurrounding("Before: [", "]").split(", ").map { it.toInt() }.toIntArray()
        val instruction = ins.split(" ").map { it.toInt() }.let { Instruction(it[0], it[1], it[2], it[3]) }
        val regAfter = after.removeSurrounding("After:  [", "]").split(", ").map { it.toInt() }.toIntArray()
        Triple(reg, instruction, regAfter)
    }
}

sealed class Op(val op: (Register, Int, Int) -> Int) {
    fun execute(register: Register, instruction: Instruction): Register {
        val result = register.copyOf()
        result[instruction.c] = op(result, instruction.a, instruction.b)
        return result
    }

    override fun toString(): String = javaClass.simpleName
}

object addr : Op({ r, a, b -> r[a] + r[b] })
object addi : Op({ r, a, b -> r[a] + b })

object mulr : Op({ r, a, b -> r[a] * r[b] })
object muli : Op({ r, a, b -> r[a] * b })

object banr : Op({ r, a, b -> r[a] and r[b] })
object bani : Op({ r, a, b -> r[a] and b })

object borr : Op({ r, a, b -> r[a] or r[b] })
object bori : Op({ r, a, b -> r[a] or b })

object setr : Op({ r, a, _ -> r[a] })
object seti : Op({ _, a, _ -> a })

object gtir : Op({ r, a, b -> if (a > r[b]) 1 else 0 })
object gtri : Op({ r, a, b -> if (r[a] > b) 1 else 0 })
object gtrr : Op({ r, a, b -> if (r[a] > r[b]) 1 else 0 })

object eqir : Op({ r, a, b -> if (a == r[b]) 1 else 0 })
object eqri : Op({ r, a, b -> if (r[a] == b) 1 else 0 })
object eqrr : Op({ r, a, b -> if (r[a] == r[b]) 1 else 0 })

val ops = listOf(addr, addi, mulr, muli, banr, bani, borr, bori, setr, seti, gtir, gtri, gtrr, eqir, eqri, eqrr)

open class Instruction(val opcode: Int, val a: Int, val b: Int, val c: Int)

typealias Register = IntArray