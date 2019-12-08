/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.use;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
public enum OpCode {
    EXIT(99, 0, false),
    ADD(1, 3, true, (ic, oc, p, in, out) -> {
        ic.getMemory().set(p.get(2), p.get(0) + p.get(1));
        return -1;
    }), MULTIPLY(2, 3, true, (ic, oc, p, in, out) -> {
        ic.getMemory().set(p.get(2), p.get(0) * p.get(1));
        return -1;
    }), INPUT(3, 1, true, (ic, oc, p, in, out) -> {
        ic.getMemory().set(p.get(0), in.getAsInt());
        return -1;
    }), OUTPUT(4, 1, false, (ic, oc, p, in, out) -> {
        out.accept(p.get(0));
        ic.setLastCode(p.get(0));
        return -1;
    }), JIT(5, 2, false, (ic, oc, p, in, out) -> {
        if (p.get(0) != 0)
            return p.get(1);
        return -1;
    }), JIF(6, 2, false, (ic, oc, p, in, out) -> {
        if (p.get(0) == 0)
            return p.get(1);
        return -1;
    }), LESS(7, 3, true, (ic, oc, p, in, out) -> {
        ic.getMemory().set(p.get(2), p.get(0) < p.get(1) ? 1 : 0);
        return -1;
    }), EQUALS(8, 3, true, (ic, oc, p, in, out) -> {
        ic.getMemory().set(p.get(2), (int) p.get(0) == p.get(1) ? 1 : 0);
        return -1;
    });

    final int code;
    final int parameters;
    final boolean lastSet;
    Operation operation;

    private static final Map<Integer, OpCode> codes = Arrays.stream(values())
            .collect(Collectors.toMap(oc -> oc.code, Function.identity()));
    
    public static OpCode getOpCode(int code) {
        OpCode opcode = codes.get(code);
        if (opcode != null)
            return opcode;
        throw new IllegalArgumentException();
    }

    @FunctionalInterface
    public interface Operation {
        public int calculate(Intcode ic, OpCode opCode, List<Integer> params, IntSupplier in, IntConsumer out);
    }
}
