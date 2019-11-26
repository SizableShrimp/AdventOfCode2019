package me.sizableshrimp.adventofcode.templates;

import lombok.Getter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

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
     */
    protected final List<String> lines = new ArrayList<>();

    public Day() {
        initialize("/days/"+getClass().getSimpleName().toLowerCase(Locale.ROOT) + ".txt");
    }

    public Day(String filename) {
        initialize(filename);
    }

    private void initialize(String filename) {
        InputStream stream = getClass().getResourceAsStream(filename);
        if (stream == null)
            return;
        try (Scanner scan = new Scanner(stream)) {
            while (scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
        }
    }

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
        System.out.printf("Completed in %.4fs%n%n", (after - before) / 1_000_000_000f);
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
