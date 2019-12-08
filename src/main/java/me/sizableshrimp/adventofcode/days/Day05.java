/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.days;

import me.sizableshrimp.adventofcode.helper.LineConvert;
import me.sizableshrimp.adventofcode.templates.SeparatedDay;
import me.sizableshrimp.adventofcode.use.Intcode;

import java.util.ArrayList;
import java.util.List;

public class Day05 extends SeparatedDay {
    List<Integer> baseMemory;

    @Override
    public Object part1() {
        return new Intcode(new ArrayList<>(baseMemory), 1).runInstruction();
    }

    @Override
    public Object part2() {
        return new Intcode(new ArrayList<>(baseMemory), 5).runInstruction();
    }

    @Override
    protected void parse() {
        baseMemory = LineConvert.ints(lines.get(0));
    }
}
