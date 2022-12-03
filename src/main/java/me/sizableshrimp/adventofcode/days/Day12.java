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
import lombok.Data;
import lombok.Value;
import me.sizableshrimp.adventofcode.helper.Itertools;
import me.sizableshrimp.adventofcode.helper.LineConvert;
import me.sizableshrimp.adventofcode.templates.SeparatedDay;
import me.sizableshrimp.adventofcode.templates.ZCoordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day12 extends SeparatedDay {
    List<Moon> baseMoons = new ArrayList<>();
    List<Moon> moons;
    Set<List<Moon>> pairs;

    @Override
    protected Object part1() {
        setup();
        for (int i = 0; i < 1000; i++) {
            timeStep();
        }
        return moons.stream().mapToInt(Moon::total).sum();
    }

    @Override
    protected Object part2() {
        setup();
        var base = getAxes(); //looking for the initial, "base" moon positions
        int[] output = {1, 1, 1};
        boolean[] done = new boolean[3];
        while (containsFalse(done)) {
            timeStep();
            for (int i = 0; i < output.length; i++) {
                if (shouldIncrement(i, base, getAxes(), done))
                    output[i]++;
            }
        }
        return lcm(lcm(output[0], output[1]), output[2]);
    }

    boolean shouldIncrement(int i, List<Axis> base, List<Axis> states, boolean[] done) {
        if (done[i])
            return false;

        if (states.get(i).equals(base.get(i)))
            done[i] = true;
        return true;
    }

    void setup() {
        moons = baseMoons.stream().map(Moon::copy).collect(Collectors.toList());
        pairs = Itertools.combinationsDistinct(moons, 2);
    }

    void timeStep() {
        for (List<Moon> pair : pairs) {
            pair.get(0).addGravity(pair.get(1));
        }
        moons.forEach(Moon::addVelocity);
    }

    List<Axis> getAxes() {
        Axis x = Axis.state(moons, ZCoordinate::getX);
        Axis y = Axis.state(moons, ZCoordinate::getY);
        Axis z = Axis.state(moons, ZCoordinate::getZ);
        return List.of(x, y, z);
    }

    @Override
    protected void parse() {
        for (String line : lines) {
            List<Integer> ints = LineConvert.ints(line);
            baseMoons.add(new Moon(new ZCoordinate(ints.get(0), ints.get(1), ints.get(2))));
        }
    }

    boolean containsFalse(boolean[] done) {
        for (boolean b : done) {
            if (!b)
                return true;
        }
        return false;
    }

    long lcm(long a, long b) {
        return (a * b) / gcd(a, b);
    }

    long gcd(long a, long b) {
        return a == 0 ? b : gcd(b % a, a);
    }

    @Data
    @AllArgsConstructor
    private static class Moon {
        ZCoordinate position;
        ZCoordinate velocity;

        Moon(ZCoordinate position) {
            this.position = position;
            velocity = new ZCoordinate(0, 0, 0);
        }

        void addGravity(Moon other) {
            int xBig = position.x > other.position.x ? -1 : 1;
            if (position.x == other.position.x)
                xBig = 0;
            int yBig = position.y > other.position.y ? -1 : 1;
            if (position.y == other.position.y)
                yBig = 0;
            int zBig = position.z > other.position.z ? -1 : 1;
            if (position.z == other.position.z)
                zBig = 0;
            velocity = velocity.resolve(xBig, yBig, zBig);
            other.velocity = other.velocity.resolve(xBig * -1, yBig * -1, zBig * -1);
        }

        void addVelocity() {
            position = position.resolve(velocity);
        }

        int total() {
            return sum(velocity) * sum(position);
        }

        int sum(ZCoordinate coord) {
            return Math.abs(coord.x) + Math.abs(coord.y) + Math.abs(coord.z);
        }

        Moon copy() {
            return new Moon(position, velocity);
        }
    }

    @Value
    private static class Axis {
        List<Integer> values;

        static Axis state(List<Moon> moons, Function<ZCoordinate, Integer> func) {
            return new Axis(moons.stream().map(Moon::getPosition).map(func).collect(Collectors.toList()));
        }
    }
}
