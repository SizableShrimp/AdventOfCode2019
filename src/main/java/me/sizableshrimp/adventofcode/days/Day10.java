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

import me.sizableshrimp.adventofcode.templates.Coordinate;
import me.sizableshrimp.adventofcode.templates.Day;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day10 extends Day {
    private static final double TWO_PI = 2 * Math.PI;
    List<Coordinate> asteroids = new ArrayList<>();
    Coordinate station;

    @Override
    protected void evaluate() {
        Map<Coordinate, Long> results = asteroids.stream().collect(Collectors.toMap(Function.identity(), this::count));
        var stationEntry = results.entrySet().stream().max(Map.Entry.comparingByValue()).get();
        station = stationEntry.getKey();
        asteroids.remove(station);
        setPart1(stationEntry.getValue());
        // System.out.println(station);
        Map<Double, List<Coordinate>> angles = new TreeMap<>(Comparator.comparingDouble(angle -> {
            // Prioritize positive angles for clockwise
            // This sort makes it so any negative angles are shifted by 1 period so that we rotate in continuous loop
            return angle < 0 ? TWO_PI + angle : angle;
        }));
        for (Coordinate asteroid : asteroids) {
            // Intentionally swap X and Y so a value of 0 radians equals up.
            // This configuration makes it so asteroids to the right of the station (higher X value) get a positive angle,
            // while asteroids to the left of the station (lower X value) get a negative angle.
            angles.computeIfAbsent(Math.atan2(asteroid.x - station.x, station.y - asteroid.y), c -> new ArrayList<>()).add(asteroid);
        }
        angles.forEach((d, l) -> l.sort(Comparator.comparingInt(c -> station.distance(c))));
        int count = 1;
        while (true) {
            for (var entry : angles.entrySet()) {
                if (entry.getValue().isEmpty())
                    continue;
                Coordinate remove = entry.getValue().remove(0);
                // System.out.println(count + " - " + remove);
                if (count++ == 200) {
                    setPart2(remove.x * 100 + remove.y);
                    return;
                }
            }
        }
    }

    long count(Coordinate station) {
        Set<Double> visible = new HashSet<>();

        for (Coordinate asteroid : this.asteroids) {
            if (asteroid == station)
                continue;
            visible.add(Math.atan2(station.y - asteroid.y, asteroid.x - station.x));
        }

        return visible.size();
    }

    @Override
    protected void parse() {
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            char[] chars = line.toCharArray();
            for (int x = 0; x < chars.length; x++) {
                if (chars[x] == '#') {
                    asteroids.add(new Coordinate(x, y));
                }
            }
        }
    }
}
