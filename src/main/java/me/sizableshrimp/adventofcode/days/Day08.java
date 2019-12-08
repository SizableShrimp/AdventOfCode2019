/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
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
