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

import lombok.AllArgsConstructor;
import lombok.Value;
import me.sizableshrimp.adventofcode.helper.LineConvert;
import me.sizableshrimp.adventofcode.intcode.Intcode;
import me.sizableshrimp.adventofcode.templates.Coordinate;
import me.sizableshrimp.adventofcode.templates.Coordinate.Direction;
import me.sizableshrimp.adventofcode.templates.SeparatedDay;

import java.util.ArrayDeque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class Day15 extends SeparatedDay {
    Map<Coordinate, Tile> map = new HashMap<>();
    Map<Coordinate, Long> steps = new HashMap<>();
    Coordinate droid = Coordinate.ZERO;
    Direction currentDir = Direction.NORTH;
    long depth;
    Coordinate system;

    @Override
    protected Object part1() {
        Intcode ic = new Intcode(LineConvert.longs(lines.get(0)), this::runDroid, this::status);
        ic.runInstruction();
        system = map.entrySet().stream().filter(e -> e.getValue() == Tile.SYSTEM).findAny().get().getKey();
        Queue<Node> search = new ArrayDeque<>();
        Set<Coordinate> seen = new HashSet<>();
        search.add(new Node(Coordinate.ZERO, 0));
        while (!search.isEmpty()) {
            Node n = search.remove();
            if (n.coord.equals(system)) {
                return n.depth;
            }
            for (Direction dir : Direction.values()) {
                Coordinate coord = n.coord.resolve(dir);
                if (!seen.add(coord))
                    continue;
                if (map.get(coord) != Tile.WALL)
                    search.add(new Node(coord, n.depth + 1));
            }
        }
        return null;
    }

    @Override
    protected Object part2() {
        Queue<Node> search = new ArrayDeque<>();
        Set<Coordinate> seen = new HashSet<>();
        search.add(new Node(system, 0));
        int max = 0;
        while (!search.isEmpty()) {
            Node n = search.remove();
            seen.add(n.coord);
            max = Math.max(max, n.depth);
            for (Direction dir : Direction.values()) {
                Coordinate coord = n.coord.resolve(dir);
                if (!seen.add(coord))
                    continue;
                if (map.containsKey(coord) && map.get(coord) != Tile.WALL)
                    search.add(new Node(coord, n.depth + 1));
            }
        }
        return max;
    }

    Map<Direction, Integer> directions = Direction.getDirections('1', '4', '2', '3')
            .entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getValue, e -> Integer.parseInt(e.getKey().toString())));

    void status(long in) {
        Coordinate temp = droid.resolve(currentDir);
        Tile result;
        if (in == 0) {
            result = Tile.WALL;
        } else {
            droid = temp;
            result = in == 1 ? Tile.SPACE : Tile.SYSTEM;
        }
        map.put(temp, result);
        if (result != Tile.WALL) {
            depth++;
        }
        steps.put(droid, depth);
    }

    long runDroid() {
        if (checkPrev())
            return -1;
        Map<Direction, Long> adjacent = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            Coordinate c = droid.resolve(dir);
            Tile tile = map.get(c);
            if (tile == null) {
                adjacent.put(dir, Long.MAX_VALUE);
            } else {
                adjacent.put(dir, tile == Tile.WALL ? 0 : depth - steps.get(c));
            }
        }
        currentDir = adjacent.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        return directions.get(currentDir);
    }

    Map<Coordinate, Tile> prev = Map.of();
    int prevAmount;

    boolean checkPrev() {
        if (prevAmount == 50)
            return true;
        if (map.equals(prev)) {
            prevAmount++;
        } else {
            prevAmount = 0;
        }
        prev = Map.copyOf(map);
        return false;
    }

    @Value
    private static class Node {
        Coordinate coord;
        int depth;
    }

    @AllArgsConstructor
    private enum Tile {
        WALL, SPACE, SYSTEM
    }
}
