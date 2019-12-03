/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.days;

import me.sizableshrimp.adventofcode.helper.LineConvert;
import me.sizableshrimp.adventofcode.templates.SeparatedDay;

import java.util.ArrayList;
import java.util.List;

public class Day02 extends SeparatedDay {
    List<Integer> baseInstructions;

    @Override
    protected Object part1() {
        List<Integer> instructions = new ArrayList<>(baseInstructions);
        instructions.set(1, 12);
        instructions.set(2, 2);
        doInstruction(instructions, 0);
        return instructions.get(0);
    }

    @Override
    protected Object part2() {
        for (int i = 0; i <= 99; i++) {
            for (int j = 0; j <= 99; j++) {
                List<Integer> instructions = new ArrayList<>(baseInstructions);
                instructions.set(1, i);
                instructions.set(2, j);
                doInstruction(instructions, 0);
                if (instructions.get(0) == 19690720) {
                    return 100 * i + j;
                }
            }
        }
        return null;
    }

    @Override
    protected void parse() {
        baseInstructions = LineConvert.ints(lines.get(0));
    }

    void doInstruction(List<Integer> instructions, int position) {
        int a = instructions.get(position);
        int b = instructions.get(position + 1);
        int c = instructions.get(position + 2);
        int d = instructions.get(position + 3);
        int calculate = calculate(instructions, a, b, c, d);
        if (calculate == -1)
            return;
        doInstruction(instructions, calculate);
    }

    int calculate(List<Integer> input, int a, int b, int c, int d) {
        if (!inBounds(input, b, c, d) || a == 99)
            return -1;
        if (a == 1) {
            input.set(c, input.get(a) + input.get(b));
        } else {
            input.set(c, input.get(a) * input.get(b));
        }
        return a + 4;
    }

    boolean inBounds(List<Integer> input, int... positions) {
        for (int i : positions) {
            if (i < 0 || i >= input.size())
                return false;
        }
        return true;
    }
}
