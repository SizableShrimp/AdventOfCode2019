package me.sizableshrimp.adventofcode.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Permutator {

    public static <T> Set<List<T>> permute(List<T> base) {
        if (base.isEmpty()) {
            Set<List<T>> perms = new HashSet<>();
            perms.add(new ArrayList<>());
            return perms;
        }

        T first = base.remove(0);
        Set<List<T>> result = new HashSet<>();
        Set<List<T>> permutations = permute(base);
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
