/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
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
