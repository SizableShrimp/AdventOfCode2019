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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public static <T> Set<List<T>> combinationsDistinct(Collection<T> collection, int r) {
        List<List<T>> combos = combinations(collection, r);
        Set<List<T>> results = new HashSet<>();
        for (List<T> combo : combos) {
            results.add(combo.stream().distinct().collect(Collectors.toList()));
        }
        return results;
    }

    private static <T> List<T> yield(List<T> pool, int[] indices) {
        List<T> result = new ArrayList<>();
        for (int index : indices) {
            result.add(pool.get(index));
        }

        return result;
    }
}
