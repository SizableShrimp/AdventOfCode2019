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

package me.sizableshrimp.adventofcode.helper;

import me.sizableshrimp.adventofcode.templates.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static final Pattern pattern = Pattern.compile("(\\d+,\\d+)");

    /**
     * Attempts to find every coordinate in the provided line and returns them in a list, in order.
     *
     * @param line The provided line to search for coordinates.
     * @return A list of {@link Coordinate}s found in the provided line, in order.
     */
    public static List<Coordinate> parseCoordinates(String line) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            coordinates.add(Coordinate.parse(matcher.group(1)));
        }

        return coordinates;
    }

    /**
     * Parses each line from the input list using the provided pattern and {@link Matcher#matches()}.
     *
     * @param pattern The pattern to use for matching to each line.
     * @param lines The list of lines to parse.
     * @param consumer The consumer to use the matcher and line data returned for each line.
     */
    public static void parseLines(Pattern pattern, List<String> lines, BiConsumer<Matcher, String> consumer) {
        lines.forEach(line -> {
            Matcher m = pattern.matcher(line);
            m.matches();
            consumer.accept(m, line);
        });
    }

    /**
     * Parses each line from the input list using the provided pattern and {@link Matcher#find}.
     *
     * @param pattern The pattern to use for matching to each line.
     * @param lines The list of lines to parse.
     * @param consumer The consumer to use the matcher and line data returned for each line.
     */
    public static void parseLinesFind(Pattern pattern, List<String> lines, BiConsumer<Matcher, String> consumer) {
        lines.forEach(line -> {
            Matcher m = pattern.matcher(line);
            m.find();
            consumer.accept(m, line);
        });
    }
}
