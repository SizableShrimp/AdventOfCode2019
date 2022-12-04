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
import me.sizableshrimp.adventofcode.helper.Itertools;
import me.sizableshrimp.adventofcode.helper.LineConvert;
import me.sizableshrimp.adventofcode.intcode.Intcode;
import me.sizableshrimp.adventofcode.templates.Coordinate.Direction;
import me.sizableshrimp.adventofcode.templates.SeparatedDay;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day25 extends SeparatedDay {
    List<Long> baseMemory = LineConvert.longs(lines.get(0));
    Set<String> dontTake = new HashSet<>();
    StringBuilder feed;

    Map<String, Set<Direction>> traversed;
    List<String> inventory;
    Queue<String> queue;
    Area area;
    Set<String> visited;
    int alreadySeen;
    Direction prev;

    @Override
    protected Object part1() {
        dontTake.add("giant electromagnet");
        dontTake.add("infinite loop");
        while (true) {
            setup();
            Intcode ic = new Intcode(new ArrayList<>(baseMemory), this::out, this::in);
            ic.runInstruction();
            if (feed.toString().contains("You should be able to get in by typing")) {
                return LineConvert.ints(feed.toString()).get(0);
            }
            dontTake.add(current.substring(5).replace("\n", ""));
        }
    }

    void setup() {
        traversed = new HashMap<>();
        inventory = new ArrayList<>();
        queue = new ArrayDeque<>();
        feed = new StringBuilder();
        visited = new HashSet<>();
        alreadySeen = 0;
        prev = null;
    }

    @Override
    protected Object part2() {
        //there is no part 2 :)
        return null;
    }

    String current;
    int index = 0;

    long out() {
        if (current == null || index >= current.length()) {
            if (queue.isEmpty())
                determineActions();
            current = queue.poll();
            //System.out.println("COMMAND: " + current);
            index = 0;
        }
        return current.charAt(index++);
    }

    void determineActions() {
        if (alreadySeen > 1_000) {
            if (area.name.equals("Security Checkpoint")) {
                Direction dir = area.doors.stream().filter(d -> d != prev.opposite()).findAny().get();
                String move = Action.MOVE.doAction(dir);
                var permuted = IntStream.rangeClosed(4, 7)
                        .mapToObj(i -> Itertools.combinations(inventory, i))
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                for (List<String> list : permuted) {
                    inventory.stream().filter(item -> !list.contains(item))
                            .forEach(item -> queue.add(Action.DROP.doAction(item)));
                    list.stream().filter(item -> !inventory.contains(item))
                            .forEach(item -> queue.add(Action.TAKE.doAction(item)));
                    inventory = list;
                    queue.add(move);
                }
            } else {
                traverse(false);
            }
        } else {
            traverse(true);
        }
    }

    void traverse(boolean search) {
        feed.delete(0, feed.lastIndexOf("== "));
        area = new Area(feed.substring(feed.indexOf("== ") + 3, feed.indexOf(" ==")), getDoors());
        if (search) {
            alreadySeen = visited.contains(area.name) ? alreadySeen + 1 : 0;
            if (area.name.equals("Security Checkpoint")) {
                next(prev.opposite()); //don't want to visit the Pressure-Sensitive Floor yet
                return;
            }
            Set<String> items = getItems();
            items.removeIf(dontTake::contains);
            items.forEach(item -> queue.add(Action.TAKE.doAction(item)));
            inventory.addAll(items);
        }
        next(getDirection());
    }

    Direction getDirection() {
        Set<Direction> directions = traversed.computeIfAbsent(area.name, n -> new HashSet<>());
        if (directions.containsAll(area.doors)) {
            directions.clear();
        }
        Direction dir = area.doors.stream()
                //not already traversed and don't go back the way we came unless there is no other options
                .filter(d -> !directions.contains(d) && (prev == null || d != prev.opposite()))
                .findAny().orElse(area.doors.stream().findAny().get());
        directions.add(dir);
        return dir;
    }

    void next(Direction dir) {
        queue.add(Action.MOVE.doAction(dir));
        visited.add(area.name);
        prev = dir;
    }

    void in(long in) {
        char c = (char) in;
        //System.out.print(c);
        feed.append(c);
    }

    List<Direction> getDoors() {
        return get("Doors here lead:\n").stream()
                .map(String::toUpperCase)
                .map(Direction::valueOf)
                .collect(Collectors.toList());
    }

    Set<String> getItems() {
        return new HashSet<>(get("Items here:\n"));
    }

    List<String> get(String header) {
        int start = feed.indexOf(header);
        if (start == -1)
            return List.of();
        String sub = feed.substring(start + header.length());
        return Arrays.stream(sub.substring(0, sub.indexOf("\n\n")).split("\n"))
                .map(s -> s.substring(2))
                .collect(Collectors.toList());
    }

    @Value
    private static class Area {
        String name;
        List<Direction> doors;
    }

    @AllArgsConstructor
    private enum Action {
        MOVE(""), TAKE("take"), DROP("drop");

        String s;

        String doAction(Object in) {
            return s + (s.isEmpty() ? "" : " ") + in.toString().toLowerCase() + '\n';
        }
    }
}
