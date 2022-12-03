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

public class Day19 extends SeparatedDay {
    List<Long> baseMemory = LineConvert.longs(lines.get(0));
    int currentX;
    int currentY;

    @Override
    protected Object part1() {
        int total = 0;
        int minX = 0;
        for (int y = 0; y < 50; y++) {
            boolean seen = false;
            int newMin = Integer.MAX_VALUE;
            for (int x = minX; x < 50; x++) {
                if (isPulled(x, y)) {
                    seen = true;
                    newMin = Math.min(newMin, x);
                    total++;
                } else if (seen) {
                    break;
                }
            }
            minX = newMin == minX + 1 ? newMin : minX;
        }
        return total;
    }

    @Override
    protected Object part2() {
        int target = 100;
        int maxX = target;
        for (int y = target; ; y++) {
            for (int x = maxX; x <= maxX + 1; x++) {
                if (isPulled(x, y))
                    maxX = Math.max(maxX, x);
            }
            int value = isValidBox(maxX, y, target);
            if (value != -1) {
                return value;
            }
        }
    }

    int isValidBox(int maxX, int y, int target) {
        int farLeft = maxX - (target - 1);
        int bottom = y + (target - 1);
        if (farLeft < 0 || bottom < 0)
            return -1;
        if (isPulled(farLeft, bottom))
            return farLeft * 10_000 + y;
        return -1;
    }

    private boolean isPulled(int x, int y) {
        currentY = y;
        currentX = x;
        Intcode ic = new Intcode(new ArrayList<>(baseMemory), this::in, this::out);
        ic.runInstruction();
        return out;
    }

    boolean out;

    private void out(long in) {
        out = in == 1;
    }

    boolean askX = true;

    private long in() {
        if (askX) {
            askX = false;
            return currentX;
        } else {
            askX = true;
            return currentY;
        }
    }
}
