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

    @Override
    protected void evaluate() {
        List<Coordinate> wire1 = new ArrayList<>();
        List<Coordinate> wire2 = new ArrayList<>();
        wire(wire1, lines.get(0));
        wire(wire2, lines.get(1));
        setPart1(intersection.stream().mapToInt(Coordinate::distanceZero).min().getAsInt());
        setPart2(intersection.stream().mapToInt(c -> steps(c, wire1) + steps(c, wire2)).min().getAsInt());
    }

    private void wire(List<Coordinate> wire, String line) {
        Coordinate previous = Coordinate.ZERO;
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
            if (!both.add(current) && !wire.contains(current))
                intersection.add(current);
            wire.add(current);
        }
        return current;
    }

    private int steps(Coordinate c, List<Coordinate> wire) {
        return wire.indexOf(c) + 1;
    }
}
