/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.intcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

@RequiredArgsConstructor
@Getter
public class Intcode {
    private final List<Integer> memory;
    private int pointer;
    @Setter
    private int lastCode;
    private final IntSupplier in;
    @Setter
    private IntConsumer out = i -> {};

    public Intcode(List<Integer> memory) {
        this(memory, () -> 0);
    }
    
    public Intcode(List<Integer> memory, int... in) {
        this(memory, new IntArraySupplier(in));
    }

    public int runInstruction() {
        Instruction instruction = parseInstruction(memory.get(pointer));
        OpCode opcode = instruction.opcode;
        if (opcode == OpCode.EXIT)
            return lastCode;

        pointer = calculate(instruction, getValues(opcode));
        return runInstruction();
    }

    public int[] getValues(OpCode opcode) {
        int[] values = new int[opcode.parameters];
        for (int i = 0; i < values.length; i++) {
            values[i] = memory.get(pointer + i + 1);
        }
        return values;
    }

    public int calculate(Instruction instruction, int... values) {
        OpCode opcode = instruction.opcode;
        List<Integer> parameters = getParams(instruction, values);
        int next = opcode.operation.calculate(this, opcode, parameters, in, out);
        if (next == -1)
            next = pointer + opcode.parameters + 1;
        return next;
    }

    public List<Integer> getParams(Instruction instruction, int[] values) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            ParameterMode mode = instruction.getModes().get(i);
            int value = mode.getIntFromMode(values[i], memory, instruction.opcode.lastSet && i == values.length - 1);
            result.add(value);
        }
        return result;
    }

    public Instruction parseInstruction(int code) {
        OpCode opcode = OpCode.getOpCode(code % 100);
        List<ParameterMode> modes = new ArrayList<>();
        for (int i = 1; i <= opcode.parameters; i++) {
            int digit = (code / (int) Math.pow(10, i + 1)) % 10;
            modes.add(digit == 1 ? ParameterMode.IMMEDIATE_MODE : ParameterMode.POSITION_MODE);
        }
        return new Instruction(opcode, List.copyOf(modes));
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
                return set ? input : memory.get(input);
            } else {
                return input;
            }
        }
    }
}
