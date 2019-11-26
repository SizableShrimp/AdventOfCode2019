package me.sizableshrimp.adventofcode.helper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListConvert {
    public static List<Integer> ints(List<String> list) {
        return convert(list, Integer::valueOf);
    }

    public static List<Long> longs(List<String> list) {
        return convert(list, Long::valueOf);
    }

    public static List<Double> doubles(List<String> list) {
        return convert(list, Double::valueOf);
    }

    public static List<Boolean> booleans(List<String> list) {
        return convert(list, Boolean::valueOf);
    }

    private static <T> List<T> convert(List<String> list, Function<String, T> func) {
        return list.stream()
                .map(func)
                .collect(Collectors.toList());
    }
}
