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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Permutator {
    public static <T> Set<List<T>> permute(List<T> input) {
        return internalPermute(new ArrayList<>(input));
    }

    private static <T> Set<List<T>> internalPermute(List<T> base) {
        if (base.isEmpty()) {
            Set<List<T>> perms = new HashSet<>();
            perms.add(new ArrayList<>());
            return perms;
        }

        T first = base.remove(0);
        Set<List<T>> result = new HashSet<>();
        Set<List<T>> permutations = internalPermute(base);
        for (var list : permutations) {
            for (int i = 0; i <= list.size(); i++) {
                List<T> temp = new ArrayList<>(list);
                temp.add(i, first);
                result.add(temp);
            }
        }

        return result;
    }

    /**
     * Creates a list of combinations that are equal to the length of the base list and add up to the target.
     * If values are already present in the base list, these will be used as minimums in determining combinations.
     *
     * @param base The base integer list to use for building combinations.
     * @param target The target integer to have all combinations add up to.
     * @return A set of all possible combinations that add up to the target.
     */
    public static Set<List<Integer>> combinations(List<Integer> base, int target) {
        HashSet<List<Integer>> results = new HashSet<>();
        combinations(base, results, 0, target);
        return results;
    }

    //credit goes to https://github.com/nielsutrecht/adventofcode
    //slightly modified
    public static void combinations(List<Integer> base, Set<List<Integer>> results, int index, int target) {
        for (int i = 0; i <= target; i++) {
            if (i < base.get(index))
                continue;
            List<Integer> newList = new ArrayList<>(base);
            newList.set(index, i);

            if (index < base.size() - 1 && newList.subList(0, index).stream().mapToInt(v -> v).sum() <= target) {
                combinations(newList, results, index + 1, target);
            }
            if (index == base.size() - 1 && newList.stream().mapToInt(v -> v).sum() == target) {
                results.add(newList);
            }
        }
    }
}
