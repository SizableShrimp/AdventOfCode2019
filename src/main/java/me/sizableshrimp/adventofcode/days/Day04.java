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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Day04 extends SeparatedDay {
    int start;
    int end;
    boolean remove = false;

    @Override
    protected Object part1() {
        return IntStream.rangeClosed(start, end).filter(this::passwordValid).count();
    }

    @Override
    protected Object part2() {
        remove = true;
        return IntStream.rangeClosed(start, end).filter(this::passwordValid).count();
    }

    private boolean passwordValid(int input) {
        String s = String.valueOf(input);
        int[] ints = s.chars().map(c -> c - '0').toArray();
        int previous = ints[0];
        List<Integer> doubles = new ArrayList<>();
        for (int i = 1; i < ints.length; i++) {
            int num = ints[i];
            if (previous == num)
                doubles.add(i - 1);
            if (num < previous)
                return false;
            previous = num;
        }
        if (remove)
            removeInvalid(doubles, s);
        return !doubles.isEmpty();
    }

    private void removeInvalid(List<Integer> doubles, String input) {
        if (doubles.size() < 2)
            return;
        Set<Integer> remove = new HashSet<>();
        for (int index : doubles) {
            char c = input.charAt(index);
            //ensure that the indices are within range
            char before = index - 1 >= 0 ? input.charAt(index - 1) : 'a';
            char after = index + 2 < input.length() ? input.charAt(index + 2) : 'a';
            boolean b = before == c;
            boolean a = after == c;
            if (b || a) {
                //only remove those that matched
                remove.addAll(Set.of(b ? index - 1 : -1, index, index + 1, a ? index + 2 : -1));
            }
        }
        doubles.removeAll(remove);
    }

    @Override
    protected void parse() {
        String[] arr = lines.get(0).split("-");
        start = Integer.parseInt(arr[0]);
        end = Integer.parseInt(arr[1]);
    }
}
