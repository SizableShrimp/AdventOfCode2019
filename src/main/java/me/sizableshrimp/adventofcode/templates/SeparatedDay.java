package me.sizableshrimp.adventofcode.templates;

public abstract class SeparatedDay extends Day {

    /**
     * @return The result of part 1
     */
    protected abstract Object part1();

    /**
     * @return The result of part 2
     */
    protected abstract Object part2();

    @Override
    protected void evaluate() {
        setPart1(part1());
        setPart2(part2());
    }
}
