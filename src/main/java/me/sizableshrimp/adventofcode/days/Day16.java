/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
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
