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

import me.sizableshrimp.adventofcode.templates.SeparatedDay;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class Day16 extends SeparatedDay {
    int[] baseInput = Arrays.stream(lines.get(0).split("")).mapToInt(Integer::parseInt).toArray();
    int[] pattern = {0, 1, 0, -1};

    @Override
    protected Object part1() {
        int[] signal = baseInput;
        for (int phase = 0; phase < 100; phase++) {
            int[] next = new int[signal.length];
            for (int i = 0; i < next.length; i++) {
                int result = 0;
                for (int j = 0; j < signal.length; j++) {
                    result += signal[j] * getPattern(j, i + 1);
                }
                next[i] = Math.abs(result % 10);
            }
            signal = next;
        }
        return section(signal, 8);
    }

    private int getPattern(int index, int size) {
        return pattern[((index + 1) / size) % 4];
    }

    @Override
    protected Object part2() {
        int offset = Integer.parseInt(section(baseInput, 7));
        if (offset < baseInput.length / 2)
            throw new IllegalArgumentException("Solution is invalid for input");
        int[] input = Collections.nCopies(10_000, baseInput).stream()
                .flatMapToInt(Arrays::stream).skip(offset).toArray();
        return section(run(input), 8);
    }

    int[] run(int[] input) {
        int[] prev = input;
        for (int phase = 0; phase < 100; phase++) {
            int[] signal = Arrays.copyOf(prev, prev.length);
            for (int i = signal.length - 2; i >= 0; i--) {
                signal[i] = (prev[i] + signal[i + 1]) % 10;
            }
            prev = signal;
        }
        return prev;
    }

    private String section(int[] output, int i) {
        return Arrays.stream(output).boxed().limit(i).map(Object::toString).collect(Collectors.joining());
    }
}
