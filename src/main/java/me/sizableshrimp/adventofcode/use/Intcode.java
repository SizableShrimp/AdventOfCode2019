/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.use;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Intcode {
    /**
     * Runs an instruction at the given instruction pointer. This will continue to run instructions until an invalid
     * opcode is reached, an index goes out of bounds, or opcode 99 is reached. Internally, this uses
     * {@link #calculate} to run instructions and find the next instruction pointer.
     *
     * @param memory The list of integers stored in memory.
     * @param pointer The instruction pointer in memory of which to run an instruction.
     * @return True if the instruction exited from a opcode 99 call, false otherwise.
     */
    public static boolean runInstruction(List<Integer> memory, int pointer) {
        int opcode = memory.get(pointer);
        if (!opcodes.containsKey(opcode))
            return opcode == 99;
        int[] values = new int[opcodes.get(opcode)];
        for (int i = 0; i < values.length; i++) {
            values[i] = memory.get(pointer + i + 1);
        }
        int calculate = calculate(memory, pointer, opcode, values);
        if (calculate == -1)
            return false;
        return runInstruction(memory, calculate);
    }

    private static Map<Integer, Integer> opcodes = new HashMap<>(); //a map of opcodes to their parameters

    static {
        opcodes.put(1, 3);
        opcodes.put(2, 3);
    }

    /**
     * Calculate the result of a given instruction using the opcode and values from memory.
     *
     * @param memory The list of integers stored in memory.
     * @param index The current index in memory.
     * @param opcode The opcode to determine which function is applied.
     * @param values The array of integer values to be used in the calculation.
     * @return The number for the next instruction pointer, or -1 if a value is out of bounds as an index in memory.
     * This new pointer is based on adding the number of values in the instruction to the current index.
     */
    public static int calculate(List<Integer> memory, int index, int opcode, int... values) {
        if (!inBounds(memory, values))
            return -1;

        if (opcode == 1) {
            memory.set(values[2], memory.get(values[0]) + memory.get(values[1]));
        } else if (opcode == 2) { //manually create else if in preparation for more opcodes
            memory.set(values[2], memory.get(values[0]) * memory.get(values[1]));
        }

        //increase by the number of values in the instruction (1 opcode + parameters)
        return index + 1 + opcodes.get(opcode);
    }

    /**
     * Returns true if the array of integers are <u>all</u> in bounds as indices for memory, false otherwise.
     *
     * @param memory The list of integers stored in memory.
     * @param values The array of integers to check as indices in memory.
     * @return True if the array of integers are all in bounds as indices for memory, false otherwise.
     */
    public static boolean inBounds(List<Integer> memory, int... values) {
        for (int i : values) {
            if (i < 0 || i >= memory.size())
                return false;
        }
        return true;
    }
}
