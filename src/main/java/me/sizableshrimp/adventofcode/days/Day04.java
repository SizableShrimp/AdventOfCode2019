/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.days;

import me.sizableshrimp.adventofcode.templates.SeparatedDay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Day04 extends SeparatedDay {
    int start;
    int end;
    boolean remove = false;

    @Override
    protected Object part1() {
        return IntStream.rangeClosed(start, end).filter(this::passwordValid).count();
    }

    @Override
    protected Object part2() {
        remove = true;
        return IntStream.rangeClosed(start, end).filter(this::passwordValid).count();
    }

    private boolean passwordValid(int input) {
        String s = String.valueOf(input);
        char[] chars = s.toCharArray();
        char previous = chars[0];
        List<Integer> doubles = new ArrayList<>();
        for (int i = 1; i < chars.length; i++) {
            char c = chars[i];
            if (previous == c)
                doubles.add(i - 1);
            if (toInt(c) < toInt(previous))
                return false;
            previous = c;
        }
        if (remove)
            removeInvalid(doubles, s);
        return !doubles.isEmpty();
    }

    private void removeInvalid(List<Integer> doubles, String input) {
        if (doubles.size() < 2)
            return;
        Set<Integer> remove = new HashSet<>();
        for (int index : doubles) {
            char c = input.charAt(index);
            //ensure that the indices are within range
            char before = index - 1 >= 0 ? input.charAt(index - 1) : 'a';
            char after = index + 2 < input.length() ? input.charAt(index + 2) : 'a';
            boolean b = before == c;
            boolean a = after == c;
            if (b || a) {
                //only remove those that matched
                remove.addAll(Set.of(b ? index - 1 : -1, index, index + 1, a ? index + 2 : -1));
            }
        }
        doubles.removeAll(remove);
    }

    private int toInt(char c) {
        return Integer.parseInt(Character.toString(c));
    }

    @Override
    protected void parse() {
        String[] arr = lines.get(0).split("-");
        start = Integer.parseInt(arr[0]);
        end = Integer.parseInt(arr[1]);
    }
}