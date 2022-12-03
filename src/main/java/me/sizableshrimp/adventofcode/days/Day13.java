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
