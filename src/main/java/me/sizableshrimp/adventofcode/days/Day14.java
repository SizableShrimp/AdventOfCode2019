/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.days;

import lombok.Value;
import lombok.experimental.Delegate;
import me.sizableshrimp.adventofcode.templates.Day;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day14 extends Day {
    Map<String, Reaction> reactions;

    @Override
    protected void evaluate() {
        long ore = findOre(1);
        setPart1(ore);
        long target = 1_000_000_000_000L;
        long min = target / ore;
        long max = min * 2;
        while (min != max) {
            long mid = (min + max + 1) / 2;
            ore = findOre(mid);
            if (ore == target) {
                setPart2(ore);
                break;
            } else if (ore > target) {
                max = (min + max) / 2;
            } else {
                min = mid;
            }
        }
        setPart2(max);
    }

    private long findOre(long fuel) {
        Map<String, Long> amounts = new HashMap<>();
        Deque<Quantity> find = new ArrayDeque<>();
        long ore = 0;
        find.push(new Quantity("FUEL", fuel));
        while (!find.isEmpty()) {
            Quantity q = find.pop();
            String product = q.id;
            long quantity = q.amount;
            if (amounts.containsKey(product)) {
                long a = amounts.get(product);
                long consumed = Math.min(quantity, a);
                amounts.put(product, a - consumed);
                quantity -= consumed;
            }
            if (quantity > 0) {
                Reaction reaction = reactions.get(product);
                long times = (quantity + reaction.getAmount() - 1) / reaction.getAmount();
                for (Quantity reactant : reaction.reactants) {
                    if (reactant.id.equals("ORE")) {
                        ore += reactant.amount * times;
                    } else {
                        find.push(new Quantity(reactant.id, reactant.amount * times));
                    }
                }
                amounts.put(product, reaction.getAmount() * times - quantity);
            }
        }
        return ore;
    }

    @Override
    protected void parse() {
        reactions = lines.stream()
                .map(this::getReaction)
                .collect(Collectors.toMap(Reaction::getId, Function.identity()));
    }

    Reaction getReaction(String line) {
        String[] arr = line.split(" => ");
        Quantity result = parseQuantity(arr[1]);
        List<Quantity> reactants = Arrays.stream(arr[0].split(", "))
                .map(this::parseQuantity)
                .collect(Collectors.toList());
        return new Reaction(result, reactants);
    }

    Quantity parseQuantity(String s) {
        String[] arr = s.split(" ");
        return new Quantity(arr[1], Integer.parseInt(arr[0]));
    }

    @Value
    private static class Reaction {
        @Delegate
        Quantity result;
        List<Quantity> reactants;
    }

    @Value
    private static class Quantity {
        String id;
        long amount;
    }
}
