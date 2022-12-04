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

import me.sizableshrimp.adventofcode.helper.LineConvert;
import me.sizableshrimp.adventofcode.intcode.Intcode;
import me.sizableshrimp.adventofcode.templates.SeparatedDay;

import java.util.ArrayList;
import java.util.List;

public class Day21 extends SeparatedDay {
    List<Long> baseMemory = LineConvert.longs(lines.get(0));
    long result;

    @Override
    protected Object part1() {
        return runInstructions(List.of("NOT B J", "NOT C T", "OR T J", "AND D J", "NOT A T", "OR T J", "WALK"));
    }

    @Override
    protected Object part2() {
        return runInstructions(List.of("NOT B J", "NOT C T", "OR T J", "AND D J", "AND H J", "NOT A T", "OR T J", "RUN"));
    }

    private long runInstructions(List<String> ascii) {
        Intcode ic = new Intcode(new ArrayList<>(baseMemory), ascii);
        ic.setOut(this::in);
        ic.runInstruction();

        return result;
    }

    void in(long in) {
        char c = (char) in;
        if (Character.toString(c).matches("\\p{ASCII}*")) {
            // System.out.print(c);
        } else {
            result = in;
        }
    }
}
