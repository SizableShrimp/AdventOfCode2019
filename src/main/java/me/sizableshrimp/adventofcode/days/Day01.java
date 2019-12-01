package me.sizableshrimp.adventofcode.days;

import me.sizableshrimp.adventofcode.helper.ListConvert;
import me.sizableshrimp.adventofcode.templates.SeparatedDay;

public class Day01 extends SeparatedDay {

    @Override
    protected Object part1() {
        return ListConvert.ints(lines).stream()
                .mapToInt(i -> i / 3 - 2)
                .sum();
    }

    @Override
    protected Object part2() {
        return ListConvert.ints(lines).stream()
                .mapToInt(i -> getFuel(i, 0))
                .sum();
    }

    int getFuel(int i, int total) {
        int result = (i / 3) - 2;
        if (result < 0)
            return total;
        return getFuel(result, total + result);
    }
}