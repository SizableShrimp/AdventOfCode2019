/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.use;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

public class Intcode {
    @Getter
    private static String lastCode = "";
    private static int input;

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
        Instruction instruction = parseInstruction(memory.get(pointer));
        if (instruction == null)
            return false;
        OpCode opcode = instruction.opcode;

        if (opcode == OpCode.EXIT)
            return true;
        int[] values = new int[opcode.parameters];
        for (int i = 0; i < values.length; i++) {
            values[i] = memory.get(pointer + i + 1);
        }
        int calculate = calculate(memory, pointer, instruction, values);
        if (calculate == -1)
            return false;
        return runInstruction(memory, calculate);
    }

    public static boolean runInstruction(List<Integer> memory, int input, int pointer) {
        Intcode.input = input;
        return runInstruction(memory, pointer);
    }

    public static Instruction parseInstruction(int code) {
        OpCode opcode = OpCode.getOpCode(code % 100);
        if (opcode == null)
            return null;
        List<ParameterMode> modes = new ArrayList<>();
        for (int i = 1; i <= opcode.parameters; i++) {
            int digit = (code / (int) Math.pow(10, i + 1)) % 10;
            modes.add(digit == 1 ? ParameterMode.IMMEDIATE_MODE : ParameterMode.POSITION_MODE);
        }
        return new Instruction(opcode, List.copyOf(modes));
    }

    /**
     * Calculate the result of a given instruction using the opcode and values from memory.
     *
     * @param memory The list of integers stored in memory.
     * @param index The current index in memory.
     * @param instruction The instruction to run.
     * @param values The array of integer values to be used in the calculation.
     * @return The number for the next instruction pointer, or -1 if a value is out of bounds as an index in memory.
     */
    public static int calculate(List<Integer> memory, int index, Instruction instruction, int... values) {
        OpCode code = instruction.opcode;
        List<Integer> parameters = getParams(memory, instruction, values, code.lastSet);
        if (parameters == null)
            return -1;
        return code.operation.calculate(memory, index, instruction, parameters);
    }

    public static List<Integer> getParams(List<Integer> memory, Instruction instruction, int[] values, boolean set) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            int in = values[i];
            int out = instruction.getModes().get(i).getIntFromMode(in, memory, set && i == values.length - 1);
            if (out == Integer.MAX_VALUE)
                return null;
            result.add(out);
        }
        return result;
    }

    public static boolean inBounds(List<Integer> memory, int value) {
        return value >= 0 && value < memory.size();
    }

    @Value
    public static class Instruction {
        OpCode opcode;
        List<ParameterMode> modes;
    }

    @AllArgsConstructor
    public enum ParameterMode {
        POSITION_MODE, IMMEDIATE_MODE;

        public int getIntFromMode(int input, List<Integer> memory, boolean set) {
            if (this == POSITION_MODE) {
                if (inBounds(memory, input)) {
                    return set ? input : memory.get(input);
                } else {
                    return Integer.MAX_VALUE;
                }
            } else {
                return input;
            }
        }
    }

    @AllArgsConstructor
    @RequiredArgsConstructor
    public enum OpCode {
        EXIT(99, 0, false),
        ADD(1, 3, true, (m, i, inst, p) -> {
            m.set(p.get(2), p.get(0) + p.get(1));
            return getDefaultPointer(i, inst);
        }), MULTIPLY(2, 3, true, (m, i, inst, p) -> {
            m.set(p.get(2), p.get(0) * p.get(1));
            return getDefaultPointer(i, inst);
        }), INPUT(3, 1, true, (m, i, inst, p) -> {
            m.set(p.get(0), input);
            return getDefaultPointer(i, inst);
        }), OUTPUT(4, 1, false, (m, i, inst, p) -> {
            lastCode = Integer.toString(p.get(0));
            return getDefaultPointer(i, inst);
        }), JIT(5, 2, false, (m, i, inst, p) -> {
            if (p.get(0) != 0)
                return p.get(1);
            return getDefaultPointer(i, inst);
        }), JIF(6, 2, false, (m, i, inst, p) -> {
            if (p.get(0) == 0)
                return p.get(1);
            return getDefaultPointer(i, inst);
        }), LESS(7, 3, true, (m, i, inst, p) -> {
            m.set(p.get(2), p.get(0) < p.get(1) ? 1 : 0);
            return getDefaultPointer(i, inst);
        }), EQUALS(8, 3, true, (m, i, inst, p) -> {
            m.set(p.get(2), (int) p.get(0) == p.get(1) ? 1 : 0);
            return getDefaultPointer(i, inst);
        });

        public static OpCode getOpCode(int code) {
            for (OpCode opcode : values()) {
                if (opcode.code == code)
                    return opcode;
            }
            return null;
        }

        private static int getDefaultPointer(int index, Instruction inst) {
            return index + inst.opcode.parameters + 1;
        }

        final int code;
        final int parameters;
        final boolean lastSet;
        Operation operation;
    }

    @FunctionalInterface
    private interface Operation {
        int calculate(List<Integer> memory, int index, Instruction instruction, List<Integer> parameters);
    }
}
