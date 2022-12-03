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
import me.sizableshrimp.adventofcode.templates.Coordinate;
import me.sizableshrimp.adventofcode.templates.SeparatedDay;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 extends SeparatedDay {
    List<Long> baseMemory = LineConvert.longs(lines.get(0));
    Intcode ic;
    Map<Coordinate, Integer> map;
    Coordinate robot;
    Coordinate.Direction dir;

    @Override
    protected Object part1() {
        setup();
        ic.runInstruction();
        return map.size();
    }

    @Override
    protected Object part2() {
        setup();
        map.put(Coordinate.ZERO, 1);
        ic.runInstruction();
        StringBuilder sb = new StringBuilder("\n");
        Coordinate start = map.keySet().stream().min(Comparator.comparing(c -> c.x + c.y)).get();
        Coordinate end = map.keySet().stream().max(Comparator.comparing(c -> c.x + c.y)).get();
        for (int y = start.y; y <= end.y; y++) {
            for (int x = start.x; x <= end.x; x++) {
                sb.append(read(new Coordinate(x, y)) == 1 ? "██" : "  ");
            }
            sb.append('\n');
        }
        return sb;
    }

    boolean paintPanel = true;

    void run(long input) {
        if (paintPanel) {
            map.put(robot, (int) input);
        } else {
            int degrees = input == 0 ? -90 : 90;
            dir = Coordinate.Direction.getDirection(dir.degrees + degrees);
            robot = robot.resolve(dir);
        }
        paintPanel = !paintPanel;
    }

    void setup() {
        ic = new Intcode(new ArrayList<>(baseMemory), this::read, this::run);
        map = new HashMap<>();
        robot = Coordinate.ZERO;
        dir = Coordinate.Direction.NORTH;
    }

    long read() {
        return read(robot);
    }

    int read(Coordinate coord) {
        return map.getOrDefault(coord, 0);
    }
}
