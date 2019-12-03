/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.days;

import me.sizableshrimp.adventofcode.helper.LineConvert;
import me.sizableshrimp.adventofcode.templates.Coordinate;
import me.sizableshrimp.adventofcode.templates.Day;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day03 extends Day {
    private static class Wire {
        List<Coordinate> list = new ArrayList<>();
        Map<Coordinate, Integer> steps = new HashMap<>();
    }

    Set<Coordinate> both = new HashSet<>();
    Set<Coordinate> intersection = new HashSet<>();
    boolean secondWire = false;

    @Override
    protected void evaluate() {
        Wire wire1 = new Wire();
        Wire wire2 = new Wire();
        wire(wire1, lines.get(0));
        secondWire = true;
        wire(wire2, lines.get(1));
        setPart1(intersection.stream().mapToInt(c -> c.distance(0, 0)).min().getAsInt());
        setPart2(intersection.stream().mapToInt(c -> wire1.steps.get(c) + wire2.steps.get(c)).min().getAsInt());
    }

    private void wire(Wire wire, String line) {
        Coordinate previous = new Coordinate(0, 0);
        for (String s : line.split(",")) {
            previous = map(wire, previous, s);
        }
    }

    private Coordinate map(Wire wire, Coordinate start, String s) {
        Coordinate.Direction dir = getDirection(s.charAt(0));
        Coordinate current = start;
        int step = wire.steps.getOrDefault(current, 0);
        int max = LineConvert.ints(s).get(0);
        for (int i = 0; i < max; i++) {
            current = current.resolve(dir);
            if (!both.add(current) && secondWire && !wire.list.contains(current))
                intersection.add(current);
            wire.list.add(current);
            wire.steps.putIfAbsent(current, ++step);
        }
        return current;
    }

    private Coordinate.Direction getDirection(char c) {
        if (c == 'U')
            return Coordinate.Direction.NORTH;
        if (c == 'R')
            return Coordinate.Direction.EAST;
        if (c == 'L')
            return Coordinate.Direction.WEST;
        return Coordinate.Direction.SOUTH;
    }
}
