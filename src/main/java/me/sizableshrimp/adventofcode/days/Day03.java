/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.days;

import me.sizableshrimp.adventofcode.helper.LineConvert;
import me.sizableshrimp.adventofcode.templates.Coordinate;
import me.sizableshrimp.adventofcode.templates.Day;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day03 extends Day {

    Map<Character, Coordinate.Direction> directions = Coordinate.Direction.getDirections('U', 'R', 'D', 'L');
    Set<Coordinate> both = new HashSet<>();
    Set<Coordinate> intersection = new HashSet<>();
    boolean secondWire = false;

    @Override
    protected void evaluate() {
        List<Coordinate> wire1 = new ArrayList<>();
        List<Coordinate> wire2 = new ArrayList<>();
        wire(wire1, lines.get(0));
        wire(wire2, lines.get(1));
        setPart1(intersection.stream().mapToInt(c -> c.distance(0, 0)).min().getAsInt());
        setPart2(intersection.stream().mapToInt(c -> steps(c, wire1) + steps(c, wire2)).min().getAsInt());
    }

    private void wire(List<Coordinate> wire, String line) {
        Coordinate previous = new Coordinate(0, 0);
        for (String s : line.split(",")) {
            previous = map(wire, previous, s);
        }
    }

    private Coordinate map(List<Coordinate> wire, Coordinate start, String s) {
        Coordinate.Direction dir = directions.get(s.charAt(0));
        Coordinate current = start;
        int max = LineConvert.ints(s).get(0);
        for (int i = 0; i < max; i++) {
            current = current.resolve(dir);
            if (!both.add(current) && secondWire && !wire.contains(current))
                intersection.add(current);
            wire.add(current);
        }
        secondWire = true;
        return current;
    }

    private int steps(Coordinate c, List<Coordinate> wire) {
        for (int i = 0; i < wire.size(); i++) {
            Coordinate other = wire.get(i);
            if (other.equals(c))
                return i + 1;
        }

        return Integer.MAX_VALUE; //never reaches
    }
}
