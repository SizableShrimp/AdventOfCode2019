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

package me.sizableshrimp.adventofcode.days;

import lombok.Data;
import me.sizableshrimp.adventofcode.templates.SeparatedDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day06 extends SeparatedDay {
    @Data
    private static class Node {
        final String planet;
        Node orbits;
        int count = -1;

        private int countIndirect() {
            if (count != -1)
                return count;
            if (planet.equals("COM"))
                return -1;
            count = orbits.countIndirect() + 1;
            return count;
        }

        private List<Node> parents(String target) {
            List<Node> parents = new ArrayList<>();
            Node node = orbits;
            while (node != null) {
                parents.add(node);
                if (node.planet.equals(target))
                    return parents;
                node = node.orbits;
            }
            return parents;
        }

        private int total() {
            if (planet.equals("COM"))
                return 0;
            return orbits != null ? 1 + countIndirect() : countIndirect();
        }
    }

    private Map<String, Node> map = new HashMap<>();

    @Override
    protected Object part1() {
        return map.values().stream().mapToInt(Node::total).sum();
    }

    @Override
    protected Object part2() {
        Node start = map.get("YOU").orbits;
        Node target = map.get("SAN").orbits;
        List<Node> parents = start.parents("COM");
        List<Node> targetParents = target.parents("COM");
        int i = parents.size();
        int j = targetParents.size();
        Node previous = null;
        while (true) {
            i--;
            j--;
            Node node = parents.get(i);
            Node other = targetParents.get(j);
            if (node != other) {
                return target.parents(previous.planet).size() + start.parents(previous.planet).size();
            }
            previous = node;
        }
    }

    @Override
    protected void parse() {
        lines.forEach(l -> {
            String[] arr = l.split("\\)");
            Node orbits = map.computeIfAbsent(arr[0], Node::new);
            Node base = map.computeIfAbsent(arr[1], Node::new);
            base.orbits = orbits;
        });
    }
}
