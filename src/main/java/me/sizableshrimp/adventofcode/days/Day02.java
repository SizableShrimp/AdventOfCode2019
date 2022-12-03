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

public class Day02 extends SeparatedDay {
    List<Long> baseMemory = LineConvert.longs(lines.get(0));

    @Override
    protected Object part1() {
        List<Long> memory = new ArrayList<>(baseMemory);
        memory.set(1, 12L);
        memory.set(2, 2L);
        new Intcode(memory).runInstruction();
        return memory.get(0);
    }

    @Override
    protected Object part2() {
        for (long noun = 0; noun <= 99; noun++) {
            for (long verb = 0; verb <= 99; verb++) {
                List<Long> memory = new ArrayList<>(baseMemory);
                memory.set(1, noun);
                memory.set(2, verb);
                new Intcode(memory).runInstruction();
                if (memory.get(0) == 1969_07_20) {
                    return 100 * noun + verb;
                }
            }
        }
        return null;
    }
}
