/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.days;

import me.sizableshrimp.adventofcode.helper.LineConvert;
import me.sizableshrimp.adventofcode.templates.Day;
import me.sizableshrimp.adventofcode.use.Intcode;

import java.util.ArrayList;
import java.util.List;

public class Day05 extends Day {
    List<Integer> baseMemory;

    @Override
    protected void evaluate() {
        List<Integer> memory = new ArrayList<>(baseMemory);
        Intcode.runInstruction(memory, 1, 0);
        setPart1(Intcode.getLastCode());
        memory = new ArrayList<>(baseMemory);
        Intcode.runInstruction(memory, 5, 0);
        setPart2(Intcode.getLastCode());
    }

    @Override
    protected void parse() {
        baseMemory = LineConvert.ints(lines.get(0));
    }
}
