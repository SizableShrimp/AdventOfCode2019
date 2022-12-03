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

import me.sizableshrimp.adventofcode.helper.GridHelper;
import me.sizableshrimp.adventofcode.templates.SeparatedDay;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day08 extends SeparatedDay {
    List<int[][]> layers = new ArrayList<>();
    int width = 25;
    int height = 6;

    @Override
    protected Object part1() {
        return layers.stream()
                .min(Comparator.comparing(g -> GridHelper.countOccurrences(g, 0)))
                .map(g -> GridHelper.countOccurrences(g, 1) * GridHelper.countOccurrences(g, 2)).get();
    }

    @Override
    protected Object part2() {
        StringBuilder sb = new StringBuilder("\n");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                List<Integer> values = getAtPosition(i, j);
                addPixel(sb, values);
            }
            sb.append('\n');
        }
        return sb;
    }

    @Override
    public void parse() {
        String line = lines.get(0);
        int size = width * height;
        for (int i = 0; i < line.length(); i += size) {
            int[][] grid = new int[height][width];
            String layer = line.substring(i, i + size);
            fillGrid(layer, grid);
            layers.add(grid);
        }
    }

    private void addPixel(StringBuilder sb, List<Integer> values) {
        int result = -1;
        for (int i = 1; i < values.size(); i += 2) {
            int front = values.get(i - 1);
            int behind = values.get(i);
            result = front == 2 ? behind : front;
            if (result != 2)
                break;
        }
        sb.append(result == 1 ? "██" : "  "); //in my opinion, it is much more readable when doubled up
    }

    private void fillGrid(String layer, int[][] grid) {
        for (int i = 0; i < layer.length(); i++) {
            int row = i / width;
            int column = i % width;
            grid[row][column] = Integer.parseInt(layer.substring(i, i + 1));
        }
    }

    private List<Integer> getAtPosition(int i, int j) {
        return layers.stream()
                .map(g -> g[i][j])
                .collect(Collectors.toList());
    }
}
