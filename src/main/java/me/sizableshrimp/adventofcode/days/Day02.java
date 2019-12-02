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
        if (a == 99)
            return;
        if (a == 1) {
            if (!one.calculate(instructions, b, c, d))
                return;
        } else {
            if (!two.calculate(instructions, b, c, d))
                return;
        }
        doInstruction(instructions, position + 4);
    }

    final OPCode one = (input, a, b, c) -> {
        if (!inBounds(input, a))
            return false;
        if (!inBounds(input, b))
            return false;
        if (!inBounds(input, c))
            return false;
        input.set(c, input.get(a) + input.get(b));
        return true;
    };
    final OPCode two = (input, a, b, c) -> {
        if (!inBounds(input, a))
            return false;
        if (!inBounds(input, b))
            return false;
        if (!inBounds(input, c))
            return false;
        input.set(c, input.get(a) * input.get(b));
        return true;
    };

    boolean inBounds(List<Integer> input, int position) {
        return position >= 0 && position < input.size();
    }

    @FunctionalInterface
    private interface OPCode {

        boolean calculate(List<Integer> input, int a, int b, int c);
    }
}
