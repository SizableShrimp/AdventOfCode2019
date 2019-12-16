/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.days;

import me.sizableshrimp.adventofcode.helper.LineConvert;
import me.sizableshrimp.adventofcode.intcode.Intcode;
import me.sizableshrimp.adventofcode.templates.Coordinate;
import me.sizableshrimp.adventofcode.templates.Day;

import java.util.HashMap;
import java.util.Map;

public class Day13 extends Day {
    Map<Coordinate, Long> map = new HashMap<>();
    Coordinate ball;
    Coordinate paddle;
    long blockCount;
    long score;
    Coordinate current;

    @Override
    protected void evaluate() {
        Intcode ic = new Intcode(LineConvert.longs(lines.get(0)), this::readJoystick, this::parseTile);
        ic.setMemory(0, 2);
        ic.runInstruction();
        setPart1(blockCount);
        setPart2(score);
    }

    private int readJoystick() {
        if (blockCount == 0) {
            blockCount = map.values().stream().filter(id -> id == 2).count();
        }
        return Long.compare(ball.x, paddle.x);
    }

    private void parseTile(long input) {
        if (current == null) {
            current = new Coordinate((int) input, -1);
        } else if (current.y == -1) {
            current = new Coordinate(current.x, (int) input);
        } else if (current.x == -1 && current.y == 0) {
            score = input;
            current = null;
        } else {
            Coordinate coord = new Coordinate(current.x, current.y);
            map.put(coord, input);
            if (input == 3) {
                paddle = coord;
            } else if (input == 4) {
                ball = coord;
            }
            current = null;
        }
    }
}
