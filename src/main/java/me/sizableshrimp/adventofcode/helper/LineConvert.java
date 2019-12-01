package me.sizableshrimp.adventofcode.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LineConvert {

    public static List<String> testInput(String lines) {
        return Arrays.asList(lines.split("\n"));
    }

    /**
     * Find all integers in a line.
     *
     * @param line The string line.
     * @return The integers found in the line.
     */
    public static List<Integer> ints(String line) {
        Matcher m = Pattern.compile("-?\\d+").matcher(line);
        List<Integer> result = new ArrayList<>();
        while (m.find()) {
            result.add(Integer.parseInt(m.group(0)));
        }
        return result;
    }

    public static List<Character> chars(String s) {
        return s.chars()
                .mapToObj(i -> (char) i)
                .collect(Collectors.toList());
    }
}