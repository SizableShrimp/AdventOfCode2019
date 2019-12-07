/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
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
        int depth = -1;

        private int countIndirect() {
            if (depth == -1)
                depth = Math.max(parents("COM").size() - 1, 0);
            return depth;
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
