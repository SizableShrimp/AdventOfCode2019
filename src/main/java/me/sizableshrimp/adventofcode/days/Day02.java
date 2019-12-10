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
