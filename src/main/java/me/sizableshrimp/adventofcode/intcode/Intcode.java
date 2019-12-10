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
import java.util.function.LongConsumer;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RequiredArgsConstructor
@Getter
public class Intcode {
    private final List<Long> memory;
    private long pointer;
    @Setter
    private long relativeBase;
    @Setter
    private long lastCode;
    private final LongSupplier in;
    @Setter
    private LongConsumer out = i -> {};

    public Intcode(List<Long> memory) {
        this(memory, () -> 0);
    }

    public Intcode(List<Long> memory, long... in) {
        this(memory, new LongArraySupplier(in));
    }

    public long runInstruction() {
        while (true) {
            Instruction instruction = parseInstruction((int) getMemory(pointer));
            if (instruction.opcode == OpCode.EXIT)
                return lastCode;
            pointer = calculate(instruction);
        }
    }

    public long calculate(Instruction instruction) {
        long next = instruction.opcode.apply(this, instruction);
        if (next == -1)
            next = pointer + instruction.opcode.parameters + 1;
        return next;
    }

    public Instruction parseInstruction(int code) {
        OpCode opcode = OpCode.getOpCode(code % 100);
        List<ParameterMode> modes = new ArrayList<>();
        for (int i = 1; i <= opcode.parameters; i++) {
            int digit = (code / (int) Math.pow(10, i + 1)) % 10;
            modes.add(ParameterMode.getMode(digit));
        }
        return new Instruction(opcode, List.copyOf(getParams(opcode, modes)));
    }

    public List<Long> getParams(OpCode opcode, List<ParameterMode> modes) {
        long[] values = getValues(opcode);
        List<Long> result = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            ParameterMode mode = modes.get(i);
            long value = mode.getLongFromMode(values[i], this, opcode.lastSet && i == values.length - 1);
            result.add(value);
        }
        return result;
    }

    public long[] getValues(OpCode opcode) {
        long[] values = new long[opcode.parameters];
        for (int i = 0; i < values.length; i++) {
            values[i] = getMemory(pointer + i + 1);
        }
        return values;
    }

    public long getMemory(long index) {
        memory.addAll(LongStream.rangeClosed(0, index - memory.size()).mapToObj(l -> 0L).collect(Collectors.toList()));
        return memory.get((int) index);
    }

    public void setMemory(long index, long value) {
        memory.addAll(LongStream.rangeClosed(0, index - memory.size()).mapToObj(l -> 0L).collect(Collectors.toList()));
        memory.set((int) index, value);
    }

    @Value
    public static class Instruction {
        OpCode opcode;
        List<Long> params;
    }

    @AllArgsConstructor
    public enum ParameterMode {
        POSITION_MODE(0), IMMEDIATE_MODE(1), RELATIVE_MODE(2);

        int id;

        public static ParameterMode getMode(int id) {
            for (ParameterMode value : values()) {
                if (value.id == id)
                    return value;
            }
            return POSITION_MODE;
        }

        public long getLongFromMode(long input, Intcode ic, boolean set) {
            switch (this) {
                case POSITION_MODE:
                    return set ? input : ic.getMemory(input);
                case IMMEDIATE_MODE:
                    return input;
                case RELATIVE_MODE:
                    return set ? input + ic.getRelativeBase() : ic.getMemory(ic.getRelativeBase() + input);
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
