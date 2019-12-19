/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.days;

import me.sizableshrimp.adventofcode.helper.LineConvert;
import me.sizableshrimp.adventofcode.intcode.Intcode;
import me.sizableshrimp.adventofcode.templates.Coordinate;
import me.sizableshrimp.adventofcode.templates.Coordinate.Direction;
import me.sizableshrimp.adventofcode.templates.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day17 extends Day {
    List<Long> baseMemory = LineConvert.longs(lines.get(0));
    Set<Coordinate> coordinates = new HashSet<>();
    Coordinate parse = new Coordinate(0, 0);
    Coordinate robot;

    @Override
    protected void evaluate() {
        Intcode ic = new Intcode(new ArrayList<>(baseMemory), Intcode.DEFAULT_IN, this::run);
        ic.runInstruction();

        StringBuilder output = new StringBuilder();
        Set<Coordinate> visited = new HashSet<>();
        Set<Coordinate> intersections = new HashSet<>();
        Direction oldDir = Direction.NORTH;
        Direction dir = getAdjacent(robot, null).get(0);
        Coordinate prev = null;
        while (true) {
            Coordinate temp = robot.resolve(dir);
            int n = 0;
            while (coordinates.contains(temp)) {
                if (!visited.add(temp))
                    intersections.add(temp);
                prev = robot;
                robot = temp;
                temp = temp.resolve(dir);
                n++;
            }
            output.append(getASCIIDirection(dir, oldDir)).append(',').append(n).append(',');
            List<Direction> adjacent = getAdjacent(robot, prev);
            if (adjacent.isEmpty())
                break;
            oldDir = dir;
            dir = adjacent.get(0);
        }
        setPart1(intersections.stream().mapToInt(c -> c.x * c.y).sum());
        Matcher m = Pattern.compile("(.{1,21})\\1*(.{1,21})(?:\\1|\\2)*(.{1,21})(?:\\1|\\2|\\3)*").matcher(output);
        m.matches();
        List<String> functions = IntStream.rangeClosed(1, m.groupCount())
                .mapToObj(m::group)
                .map(s -> s.substring(0, s.length()-1))
                .collect(Collectors.toList());
        String routine = output.deleteCharAt(output.length()-1).toString().replace(functions.get(0), "A")
                .replace(functions.get(1), "B")
                .replace(functions.get(2), "C");
        functions.add(0, routine);
        functions.add("n\n");
        long[] input = String.join("\n", functions).chars().mapToLong(f -> f).toArray();
        ic = new Intcode(new ArrayList<>(baseMemory), input);
        ic.setOut(this::setPart2);
        ic.setMemory(0, 2);
        ic.runInstruction();
    }

    char getASCIIDirection(Direction dir, Direction oldDir) {
        Direction right = Direction.getDirection(oldDir.degrees + 90);
        if (right == dir) {
            return 'R';
        }
        return 'L';
    }

    List<Direction> getAdjacent(Coordinate c, Coordinate prev) {
        return Arrays.stream(Direction.values()).filter(d -> coordinates.contains(c.resolve(d)))
                .filter(d -> !c.resolve(d).equals(prev))
                .collect(Collectors.toList());
    }

    void run(long in) {
        if (in == '\n') {
            parse = new Coordinate(0, parse.y + 1);
            return;
        }
        Coordinate prev = parse;
        parse = new Coordinate(parse.x + 1, parse.y);
        if (in == '#') {
            coordinates.add(prev);
        }
        if (in == '^') {
            robot = prev;
        }
    }
}
