/*
 * Copyright (c) 2019 SizableShriminst.getParameters().
 * This file is provided under version 3 of the GNU Lesser General Public License.
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
