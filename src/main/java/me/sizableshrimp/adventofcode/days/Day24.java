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

import lombok.Value;
import me.sizableshrimp.adventofcode.templates.Coordinate;
import me.sizableshrimp.adventofcode.templates.Coordinate.Direction;
import me.sizableshrimp.adventofcode.templates.SeparatedDay;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class Day24 extends SeparatedDay {
    private static final Coordinate CENTER = new Coordinate(2, 2);
    Set<Node> baseBugs;

    @Override
    protected Object part1() {
        Set<Set<Node>> seen = new HashSet<>();

        Set<Node> bugs = runSimulation(false, (minute, b) -> !seen.contains(b), seen::add);

        return biodiversity(bugs);
    }

    @Override
    protected Object part2() {
        Set<Node> bugs = runSimulation(true, (minute, b) -> minute <= 200, s -> {});

        return bugs.size();
    }

    private Set<Node> runSimulation(boolean recursive, BiPredicate<Integer, Set<Node>> pred, Consumer<Set<Node>> consumer) {
        Set<Node> bugs = new HashSet<>(baseBugs);
        // Current minute signifies the minute after updating the bugs
        for (int minute = 1; pred.test(minute, bugs); minute++) {
            Set<Node> nextBugs = new HashSet<>();
            Set<Node> leftToCheck = new HashSet<>();
            for (Node bug : bugs) {
                int neighbors = bug.countNeighbors(bugs, leftToCheck, recursive);
                if (neighbors == 1)
                    nextBugs.add(bug);
            }
            for (Node dead : leftToCheck) {
                int neighbors = dead.countNeighbors(bugs, null, recursive);
                if (neighbors >= 1 && neighbors <= 2)
                    nextBugs.add(dead);
            }
            consumer.accept(bugs); // Give consumer the previous bugs after calculating the next
            bugs = nextBugs;
        }

        return bugs;
    }

    int biodiversity(Set<Node> bugs) {
        int biodiversity = 0;

        for (Node bug : bugs) {
            biodiversity += Math.pow(2, bug.coord.x + (5 * bug.coord.y));
        }

        return biodiversity;
    }

    @Override
    protected void parse() {
        baseBugs = new HashSet<>();
        for (int y = 0; y < lines.size(); y++) {
            char[] chars = lines.get(y).toCharArray();
            for (int x = 0; x < chars.length; x++) {
                boolean isBug = chars[x] == '#';
                if (isBug)
                    baseBugs.add(new Node(new Coordinate(x, y), 0));
            }
        }
    }

    @Value
    private static class Node {
        Coordinate coord;
        int level;

        private Set<Node> getNeighbors(boolean recursive) {
            Set<Node> neighbors = new HashSet<>();

            for (Direction dir : Direction.values()) {
                populateDirection(dir, neighbors, recursive);
            }

            return neighbors;
        }

        private void populateDirection(Direction dir, Set<Node> set, boolean recursive) {
            Coordinate next = coord.resolve(dir);

            if (recursive && next.equals(CENTER)) {
                // Middle Edge
                Direction get = dir.opposite();
                if (get.x == 0) {
                    int y = get.y == -1 ? 0 : 4;
                    for (int x = 0; x < 5; x++) {
                        set.add(new Node(new Coordinate(x, y), level + 1));
                    }
                } else if (get.y == 0) {
                    int x = get.x == -1 ? 0 : 4;
                    for (int y = 0; y < 5; y++) {
                        set.add(new Node(new Coordinate(x, y), level + 1));
                    }
                }
            } else if (!next.isValid(5)) {
                // Outer Edge
                // Have to filter out invalid coordinates if we are not in a recursive setting
                if (recursive)
                    set.add(new Node(CENTER.resolve(dir), level - 1));
            } else {
                // Normal Neighbor
                set.add(new Node(next, level));
            }
        }

        private int countNeighbors(Set<Node> bugs, Set<Node> leftToCheck, boolean recursive) {
            Set<Node> neighbors = getNeighbors(recursive);

            int count = 0;
            for (Node neighbor : neighbors) {
                if (bugs.contains(neighbor)) {
                    count++;
                } else if (leftToCheck != null) {
                    leftToCheck.add(neighbor);
                }
            }

            return count;
        }
    }
}
