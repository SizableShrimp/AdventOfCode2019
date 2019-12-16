/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
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
