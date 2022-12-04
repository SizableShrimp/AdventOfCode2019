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
import lombok.EqualsAndHashCode;
import lombok.Value;
import me.sizableshrimp.adventofcode.templates.Coordinate;
import me.sizableshrimp.adventofcode.templates.Day;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day18 extends Day {
    Tile[][] grid;
    Tile[][] part2;
    Map<Character, Coordinate> doors = new HashMap<>();
    Map<Character, Coordinate> keys = new HashMap<>();
    Coordinate base;
    List<Coordinate> robots;

    @Override
    protected void evaluate() {
        setPart1(simulate(List.of(this.base), this.grid));
        // print(robots, part2);
        setPart2(simulate(this.robots, this.part2));
    }

    private long simulate(List<Coordinate> robots, Tile[][] grid) {
        Deque<Unlocked> queue = new ArrayDeque<>();
        queue.push(new Unlocked(Set.of(), robots, 0));
        Map<Unlocked, Long> seen = new HashMap<>();
        long lowestSteps = Long.MAX_VALUE;
        while (!queue.isEmpty()) {
            Unlocked unlocked = queue.pop();
            if (seen.containsKey(unlocked) && seen.get(unlocked) < unlocked.steps)
                continue;
            seen.put(unlocked, unlocked.steps);
            Map<Character, Integer> available = getAvailableKeys(unlocked, grid);
            for (var entry : available.entrySet()) {
                Set<Character> set = new HashSet<>(unlocked.keys);
                char key = entry.getKey();
                set.add(key);
                List<Coordinate> newRobots = new ArrayList<>(unlocked.robots);
                Coordinate selectedRobot = newRobots.get(entry.getValue());
                Coordinate keyLoc = keys.get(key);
                long toKey = bfs(selectedRobot, keyLoc, unlocked.keys, grid);
                if (toKey == -1)
                    throw new IllegalStateException();
                newRobots.set(entry.getValue(), keyLoc);
                // long steps = bfs(start, end, set);
                // if (steps == -1)
                //     continue;
                Unlocked next = new Unlocked(set, newRobots, unlocked.steps + toKey);
                // print(next.robots, part2);
                if (set.size() == keys.size()) {
                    long prev = lowestSteps;
                    lowestSteps = Math.min(next.steps, lowestSteps);
                    if (prev != lowestSteps) {
                        int test = 1 + 1;
                    }
                } else {
                    if (!seen.containsKey(next) || seen.get(next) > next.steps)
                        queue.push(next);
                }
            }
        }
        return lowestSteps;
    }

    private void closeDeadEnds(Tile[][] grid, List<Coordinate> robots) {
        int changed;
        do {
            changed = 0;
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[0].length; x++) {
                    Tile current = grid[y][x];
                    if (current.type != Type.SPACE || current.containsKey || robots.contains(new Coordinate(x, y)))
                        continue;
                    int count = 0;
                    for (Coordinate adj : getAdjacents(new Coordinate(x, y), grid)) {
                        Tile tile = grid[adj.y][adj.x];
                        if (tile.type == Type.SPACE || tile.type == Type.DOOR) {
                            count++;
                        }
                    }
                    if (count == 1) {
                        grid[y][x] = new Tile(Type.WALL, false, '#');
                        changed++;
                    }
                }
            }
        } while (changed > 0);
    }

    private Map<Character, Integer> getAvailableKeys(Unlocked unlocked, Tile[][] grid) {
        Map<Character, Integer> available = new HashMap<>();
        for (int i = 0; i < unlocked.robots.size(); i++) {
            Coordinate coord = unlocked.robots.get(i);
            Deque<Node> queue = new ArrayDeque<>();
            queue.push(new Node(coord, 0, null));
            Set<Coordinate> seen = new HashSet<>();
            while (!queue.isEmpty()) {
                Node node = queue.pollLast();
                // System.out.println("node = " + node);
                List<Coordinate> list = getAdjacents(node.coord, grid);
                // System.out.println("list = " + list);
                for (Coordinate adjacent : list) {
                    if (!seen.add(adjacent))
                        continue;
                    Node n = new Node(adjacent, node.steps + 1, node);
                    Tile tile = grid[adjacent.y][adjacent.x];
                    if (tile.type == Type.DOOR && !unlocked.keys.contains(tile.id)) {
                        continue;
                    }
                    if (tile.containsKey) {
                        char c = tile.id;
                        if (!unlocked.keys.contains(c)) {
                            available.put(c, i);
                        } else {
                            queue.push(n);
                        }
                    } else {
                        queue.push(n);
                    }
                }
            }
        }
        return available;
    }

    private long bfs(Coordinate start, Coordinate end, Set<Character> available, Tile[][] grid) {
        Deque<Node> queue = new ArrayDeque<>();
        queue.push(new Node(start, 0, null));
        Set<Coordinate> seen = new HashSet<>();
        // Set<Character> needed = new HashSet<>();
        Node lowest = null;
        while (!queue.isEmpty()) {
            Node node = queue.pollLast();
            // System.out.println("node = " + node);
            List<Coordinate> list = getAdjacents(node.coord, grid);
            // System.out.println("list = " + list);
            for (Coordinate adjacent : list) {
                if (!seen.add(adjacent))
                    continue;
                Node n = new Node(adjacent, node.steps + 1, node);
                Tile tile = grid[adjacent.y][adjacent.x];
                if (tile.type == Type.DOOR && !available.contains(tile.id)) {
                    continue;
                }
                if (adjacent.equals(end)) {
                    if (lowest == null) {
                        lowest = n;
                    } else {
                        lowest = n.steps < lowest.steps ? n : lowest;
                    }
                }
                queue.push(n);
            }
        }
        return lowest == null ? -1 : lowest.steps;
    }

    private void print(List<Coordinate> robots, Tile[][] grid) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (robots.contains(new Coordinate(x, y))) {
                    System.out.print("@");
                    continue;
                }
                Tile tile = grid[y][x];
                if (tile.type == Type.WALL)
                    System.out.print('#');
                if (tile.type == Type.SPACE && tile.containsKey)
                    System.out.print(Character.toLowerCase(tile.id));
                if (tile.type == Type.SPACE && !tile.containsKey)
                    System.out.print('.');
                if (tile.type == Type.DOOR)
                    System.out.print(tile.id);
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

    private final Set<Type> allowed = EnumSet.of(Type.SPACE, Type.DOOR);

    List<Coordinate> getAdjacents(Coordinate coord, Tile[][] grid) {
        return Arrays.stream(Coordinate.Direction.values())
                .map(coord::resolve)
                .filter(c -> allowed.contains(grid[c.y][c.x].type))
                .collect(Collectors.toList());
    }

    @Override
    protected void parse() {
        base = null;
        grid = new Tile[lines.size()][lines.get(0).length()];
        robots = new ArrayList<>();
        for (int y = 0; y < grid.length; y++) {
            String line = lines.get(y);
            char[] chars = line.toCharArray();
            for (int x = 0; x < chars.length; x++) {
                parse(y, x, chars[x], grid, true);
            }
        }
        part2 = new Tile[lines.size()][lines.get(0).length()];
        for (int y = 0; y < part2.length; y++) {
            part2[y] = Arrays.copyOf(grid[y], grid[y].length);
        }
        closeDeadEnds(grid, List.of(base));
        if (robots.isEmpty()) {
            char[][] replace = {{'@', '#', '@'}, {'#', '#', '#'}, {'@', '#', '@'}};
            for (int y = -1; y <= 1; y++) {
                for (int x = -1; x <= 1; x++) {
                    char c = replace[y + 1][x + 1];
                    if (c == '@') {
                        robots.add(base.resolve(x, y));
                    }
                    parse(base.y + y, base.x + x, c, part2, false);
                }
            }
        }
        closeDeadEnds(part2, robots);
    }

    private void parse(int y, int x, char c, Tile[][] grid, boolean b) {
        if (c == '#') {
            grid[y][x] = new Tile(Type.WALL, false, c);
        } else if (c == '.') {
            grid[y][x] = new Tile(Type.SPACE, false, c);
        } else if (Character.isLetter(c)) {
            if (Character.isUpperCase(c)) {
                doors.put(c, new Coordinate(x, y));
                grid[y][x] = new Tile(Type.DOOR, false, c);
            } else {
                keys.put(Character.toUpperCase(c), new Coordinate(x, y));
                grid[y][x] = new Tile(Type.SPACE, true, Character.toUpperCase(c));
            }
        } else if (c == '@') {
            grid[y][x] = new Tile(Type.SPACE, false, c);
            if (b) {
                if (base == null) {
                    base = new Coordinate(x, y);
                } else {
                    if (robots.isEmpty())
                        robots.add(base);
                    robots.add(new Coordinate(x, y));
                }
            }
        }
    }

    @Value
    private static class Unlocked {
        Set<Character> keys;
        // @EqualsAndHashCode.Exclude
        List<Coordinate> robots;
        @EqualsAndHashCode.Exclude
        long steps;
    }

    @Value
    private static class Node {
        // Set<Character> keys = new HashSet<>();
        Coordinate coord;
        @EqualsAndHashCode.Exclude
        int steps;
        @EqualsAndHashCode.Exclude
        Node parent;

        @Override
        public String toString() {
            return "Node{" +
                    "coord=" + coord +
                    ", steps=" + steps +
                    '}';
        }
    }

    @Value
    @AllArgsConstructor
    private static class Tile {
        Type type;
        boolean containsKey;
        char id;
    }

    private enum Type {
        WALL, DOOR, SPACE;
    }
}
