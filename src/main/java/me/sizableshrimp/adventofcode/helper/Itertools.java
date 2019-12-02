/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//see Python Itertools
public class Itertools {

    public static <T> List<List<T>> product(List<T>... lists) {
        return product(1, lists);
    }

    @SafeVarargs
    public static <T> List<List<T>> product(int repeat, List<T>... lists) {
        List<List<T>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        List<List<T>> pools = new ArrayList<>(Arrays.asList(lists));
        pools = repeat(pools, repeat);
        for (List<T> pool : pools) {
            List<List<T>> newResult = new ArrayList<>();
            for (List<T> x : result) {
                for (T y : pool) {
                    List<T> list = new ArrayList<>();
                    list.addAll(x);
                    list.add(y);
                    newResult.add(list);
                }
            }
            result = newResult;
        }

        return result;
    }

    private static <T> List<List<T>> repeat(List<List<T>> pools, int repeat) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < repeat; i++) {
            for (List<T> pool : pools) {
                result.add(new ArrayList<>(pool));
            }
        }
        return result;
    }

    public static <T> List<List<T>> combinations(Collection<T> collection, int r) {
        List<List<T>> result = new ArrayList<>();
        List<T> pool = new ArrayList<>(collection);
        int n = pool.size();
        int[] indices = IntStream.range(0, r).toArray();
        result.add(yield(pool, indices));
        while (true) {
            boolean broke = false;
            int last = 0;
            for (int i : IntStream.range(0, r).mapToObj(i -> r - i - 1).collect(Collectors.toList())) {
                last = i;
                if (indices[i] != i + n - r) {
                    broke = true;
                    break;
                }
            }
            if (!broke)
                return result;

            indices[last]++;
            for (Integer j : IntStream.range(last + 1, r).boxed().collect(Collectors.toList())) {
                indices[j] = indices[j - 1] + 1;
            }
            result.add(yield(pool, indices));
        }
    }

    private static <T> List<T> yield(List<T> pool, int[] indices) {
        List<T> result = new ArrayList<>();
        for (int index : indices) {
            result.add(pool.get(index));
        }

        return result;
    }
}
