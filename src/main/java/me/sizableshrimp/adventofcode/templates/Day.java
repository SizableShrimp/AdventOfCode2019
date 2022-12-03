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

package me.sizableshrimp.adventofcode.templates;

import lombok.Getter;
import me.sizableshrimp.adventofcode.helper.DataReader;

import java.util.List;
import java.util.Objects;

/**
 * A single day which has two challenges to solve.
 * There are 25 days in <a href="http://adventofcode.com">Advent Of Code</a>.
 * Each day has two parts to it to solve the entire day.
 */
public abstract class Day {
    @Getter
    private String part1;
    @Getter
    private String part2;

    /**
     * The lines parsed from the input file for the challenge. For example, an input file with the data:
     * <pre>{@code 1
     * 2
     * 3
     * 4
     * 5
     * }</pre>
     * would be parsed as {"1", "2", "3", "4", "5"}.
     * <p>
     * <b>NOTE:</b> This variable is assigned using {@link DataReader#read}, which means it has the possibility to hit
     * the Advent Of Code servers to request the input data. See {@link DataReader#read} for more details.
     */
    protected List<String> lines = DataReader.read(Integer.parseInt(getClass().getSimpleName().substring(3)));

    /**
     * Execute a given day; outputting part 1, part 2, and the time taken.
     */
    public void run() {
        long before = System.nanoTime();
        parse();
        evaluate();
        long after = System.nanoTime();
        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
        System.out.printf("Completed in %.3fs%n%n", (after - before) / 1_000_000_000f);
    }

    /**
     * This internal method is what actually evaluates the result of part 1 and part 2.
     */
    protected abstract void evaluate();

    /**
     * This internal method can be overridden to parse the {@link #lines} of the day into something more useful for
     * the challenge.
     * <p>
     * This method will automatically be run before {@link #evaluate()}.
     */
    protected void parse() {}

    protected void setPart1(Object obj) {
        part1 = Objects.toString(obj);
    }

    protected void setPart2(Object obj) {
        part2 = Objects.toString(obj);
    }
}
