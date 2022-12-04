/*
 * AdventOfCode2019
 * Copyright (C) 2022 SizableShrimp
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.sizableshrimp.adventofcode.intcode;

import lombok.AccessLevel;
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
@Getter(AccessLevel.PACKAGE)
public class Intcode {
    private final List<Long> memory;
    private long pointer;
    @Setter(AccessLevel.PACKAGE)
    private long relativeBase;
    @Setter(AccessLevel.PACKAGE)
    @Getter
    private long lastCode;
    private boolean exit;
    public static final LongSupplier DEFAULT_IN = () -> 0;
    public static final LongConsumer DEFAULT_OUT = i -> {};
    @Setter
    private LongSupplier in;
    @Setter
    private LongConsumer out;

    public Intcode(List<Long> memory, LongSupplier in, LongConsumer out) {
        this.memory = memory;
        this.in = in;
        this.out = out;
    }

    public Intcode(List<Long> memory, long... in) {
        this(memory, new LongArraySupplier(in), DEFAULT_OUT);
    }

    public Intcode(List<Long> memory, List<String> asciiInput) {
        this(memory, new StringSupplier(String.join("\n", asciiInput) + "\n"), DEFAULT_OUT);
    }

    public long runInstruction() {
        exit = false;
        while (!exit) {
            Instruction instruction = parseInstruction((int) getMemory(pointer));
            if (instruction.opcode == OpCode.EXIT)
                break;
            pointer = calculate(instruction);
        }
        return lastCode;
    }

    long calculate(Instruction instruction) {
        long next = instruction.opcode.apply(this, instruction);
        if (next == -1)
            next = pointer + instruction.opcode.parameters + 1;
        return next;
    }

    Instruction parseInstruction(int code) {
        OpCode opcode = OpCode.getOpCode(code % 100);
        List<ParameterMode> modes = new ArrayList<>();
        for (int i = 1; i <= opcode.parameters; i++) {
            int digit = (code / (int) Math.pow(10, i + 1)) % 10;
            modes.add(ParameterMode.getMode(digit));
        }
        return new Instruction(opcode, List.copyOf(getParams(opcode, modes)));
    }

    List<Long> getParams(OpCode opcode, List<ParameterMode> modes) {
        long[] values = getValues(opcode);
        List<Long> result = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            ParameterMode mode = modes.get(i);
            long value = mode.getLongFromMode(values[i], this, opcode.lastSet && i == values.length - 1);
            result.add(value);
        }
        return result;
    }

    long[] getValues(OpCode opcode) {
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

    public void exit() {
        exit = true;
    }

    @Value
    static class Instruction {
        OpCode opcode;
        List<Long> params;
    }

    @AllArgsConstructor
    private enum ParameterMode {
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
            return switch (this) {
                case POSITION_MODE -> set ? input : ic.getMemory(input);
                case IMMEDIATE_MODE -> input;
                case RELATIVE_MODE -> set ? input + ic.getRelativeBase() : ic.getMemory(ic.getRelativeBase() + input);
            };
        }
    }

    @RequiredArgsConstructor
    private static class LongArraySupplier implements LongSupplier {
        private final long[] array;
        private int index;

        @Override
        public long getAsLong() {
            return array[index++];
        }
    }

    @RequiredArgsConstructor
    private static class StringSupplier implements LongSupplier {
        private final String string;
        private int index;

        @Override
        public long getAsLong() {
            return string.charAt(index++);
        }
    }
}
