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

public class Day09 extends SeparatedDay {
    List<Long> baseMemory = LineConvert.longs(lines.get(0));

    @Override
    public Object part1() {
        return new Intcode(new ArrayList<>(baseMemory), 1).runInstruction();
    }

    @Override
    public Object part2() {
        return new Intcode(new ArrayList<>(baseMemory), 2).runInstruction();
    }
}
