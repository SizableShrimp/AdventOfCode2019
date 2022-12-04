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

package me.sizableshrimp.adventofcode.days;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Delegate;
import me.sizableshrimp.adventofcode.helper.LineConvert;
import me.sizableshrimp.adventofcode.templates.SeparatedDay;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class Day22 extends SeparatedDay {
    private static final LinearFunc IDENTITY = new LinearFunc(ONE, ZERO);
    List<Instruction> instructions = new ArrayList<>();

    @Override
    protected Object part1() {
        return runShuffling(2019, 10_007, 1, false);
    }

    @Override
    protected Object part2() {
        return runShuffling(2020, 119_315_717_514_047L, 101_741_582_076_661L, true);
    }

    long runShuffling(long pos, long cs, long sh, boolean inverse) {
        BigInteger position = BigInteger.valueOf(pos);
        BigInteger cardSize = BigInteger.valueOf(cs);
        BigInteger shuffles = BigInteger.valueOf(sh);

        LinearFunc base = IDENTITY;
        Collections.reverse(instructions);
        for (Instruction inst : instructions) {
            base = base.join(inst.transform(cardSize, inverse), cardSize);
        }

        LinearFunc func = base;
        if (inverse) {
            // newM = (m ^ shuffles % cardSize)
            // newB = (b * (newM - 1) * inv(m - 1, cardSize)) % cardSize
            BigInteger newM = base.m.modPow(shuffles, cardSize);
            BigInteger newB = base.b.multiply(newM.subtract(ONE)).multiply(base.m.subtract(ONE).modInverse(cardSize)).mod(cardSize);
            func = new LinearFunc(newM, newB);
        }

        // System.out.println(func);
        return func.apply(position).mod(cardSize).longValue();
    }

    @Override
    protected void parse() {
        instructions = new ArrayList<>();

        for (String line : lines) {
            List<Integer> ints = LineConvert.ints(line);
            if (line.startsWith("cut")) {
                instructions.add(new Instruction(Operation.CUT_N, BigInteger.valueOf(ints.get(0))));
            } else if (line.startsWith("deal with increment")) {
                instructions.add(new Instruction(Operation.DEAL_INCREMENT, BigInteger.valueOf(ints.get(0))));
            } else if (line.equals("deal into new stack")) {
                instructions.add(new Instruction(Operation.DEAL_NEW, ZERO));
            }
        }
    }

    @Value
    private static class Instruction {
        Operation op;
        BigInteger n;

        private LinearFunc transform(BigInteger size, boolean inverse) {
            return op.transform(size, n, inverse);
        }
    }

    @AllArgsConstructor
    private enum Operation {
        // -i - 1
        DEAL_NEW((cardSize, n, opposite) -> new LinearFunc(ONE.negate(), ONE.negate())),
        // opposite i + n    normal i - n
        CUT_N((cardSize, n, opposite) -> opposite ? new LinearFunc(ONE, n.mod(cardSize)) : new LinearFunc(ONE, n.negate().mod(cardSize))),
        // opposite i * (this^-1 mod s)    normal i * n
        DEAL_INCREMENT((cardSize, n, opposite) -> opposite ? new LinearFunc(n.modInverse(cardSize), ZERO) : new LinearFunc(n, ZERO));

        @Delegate
        Transformer func;
    }

    @Value
    private static class LinearFunc {
        BigInteger m, b;

        private BigInteger apply(BigInteger x) {
            return m.multiply(x).add(b);
        }

        private LinearFunc join(LinearFunc other, BigInteger cardSize) {
            // f(x) = mx + b    g(x) = nx + c    f(g(x) = m(nx + c) + b = mnx + mc + b where A = mn and B = mc + b
            return new LinearFunc(this.m.multiply(other.m).mod(cardSize), this.m.multiply(other.b).add(this.b).mod(cardSize));
        }
    }

    @FunctionalInterface
    private interface Transformer {
        LinearFunc transform(BigInteger cardSize, BigInteger n, boolean inverse);
    }
}
