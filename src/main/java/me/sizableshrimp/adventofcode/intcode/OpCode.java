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

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum OpCode {
    EXIT(99, 0, false, null),
    ADD(1, 3, true, (ic, inst) -> {
        ic.setMemory(inst.getParams().get(2), inst.getParams().get(0) + inst.getParams().get(1));
        return -1L;
    }), MULTIPLY(2, 3, true, (ic, inst) -> {
        ic.setMemory(inst.getParams().get(2), inst.getParams().get(0) * inst.getParams().get(1));
        return -1L;
    }), INPUT(3, 1, true, (ic, inst) -> {
        ic.setMemory(inst.getParams().get(0).intValue(), ic.getIn().getAsLong());
        return -1L;
    }), OUTPUT(4, 1, false, (ic, inst) -> {
        ic.getOut().accept(inst.getParams().get(0));
        ic.setLastCode(inst.getParams().get(0));
        return -1L;
    }), JIT(5, 2, false, (ic, inst) -> {
        if (inst.getParams().get(0) != 0)
            return inst.getParams().get(1);
        return -1L;
    }), JIF(6, 2, false, (ic, inst) -> {
        if (inst.getParams().get(0) == 0)
            return inst.getParams().get(1);
        return -1L;
    }), LESS(7, 3, true, (ic, inst) -> {
        ic.setMemory(inst.getParams().get(2), inst.getParams().get(0) < inst.getParams().get(1) ? 1 : 0);
        return -1L;
    }), EQUALS(8, 3, true, (ic, inst) -> {
        ic.setMemory(inst.getParams().get(2), inst.getParams().get(0).intValue() == inst.getParams().get(1) ? 1 : 0);
        return -1L;
    }), ADJUST(9, 1, false, (ic, inst) -> {
        ic.setRelativeBase(ic.getRelativeBase() + inst.getParams().get(0));
        return -1L;
    });

    final int code;
    final int parameters;
    final boolean lastSet;
    @Delegate
    BiFunction<Intcode, Intcode.Instruction, Long> operation;

    private static final Map<Integer, OpCode> codes = Arrays.stream(values()).collect(Collectors.toMap(oc -> oc.code,
            Function.identity()));

    public static OpCode getOpCode(int code) {
        OpCode opcode = codes.get(code);
        if (opcode != null)
            return opcode;
        throw new IllegalArgumentException();
    }
}
