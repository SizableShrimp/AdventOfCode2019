/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.days;

import me.sizableshrimp.adventofcode.helper.LineConvert;
import me.sizableshrimp.adventofcode.helper.Permutator;
import me.sizableshrimp.adventofcode.templates.SeparatedDay;
import me.sizableshrimp.adventofcode.intcode.Intcode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Day07 extends SeparatedDay {
    List<Long> baseMemory = LineConvert.longs(lines.get(0));

    @Override
    protected Object part1() {
        return compileResults(List.of(0, 1, 2, 3, 4), false);
    }

    @Override
    protected Object part2() {
        return compileResults(List.of(5, 6, 7, 8, 9), true);
    }

    private long compileResults(List<Integer> basePhases, boolean loop) {
        return Permutator.permute(basePhases).parallelStream()
                .mapToLong(phases -> runAmplifiers(phases, loop))
                .max().getAsLong();
    }

    private long runAmplifiers(List<Integer> phases, boolean loop) {
        List<Amplifier> amps = getAmplifiers(phases, loop);
        Amplifier last = amps.get(amps.size() - 1);
        amps.forEach(Amplifier::start);
        try {
            last.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return last.intcode.getLastCode();
    }

    private List<Amplifier> getAmplifiers(List<Integer> phases, boolean loop) {
        List<Amplifier> amplifiers = new ArrayList<>();
        Amplifier previous = null;
        for (int phase : phases) {
            Amplifier amplifier = new Amplifier(new ArrayList<>(baseMemory), phase);
            amplifiers.add(amplifier);
            if (previous != null) {
                previous.setOutput(amplifier);
            } else {
                amplifier.in.add(0L); //Amplifier A
            }
            previous = amplifier;
        }
        if (loop)
            amplifiers.get(amplifiers.size() - 1).setOutput(amplifiers.get(0));
        return amplifiers;
    }

    private static class Amplifier extends Thread {
        final Intcode intcode;
        private final BlockingQueue<Long> in = new LinkedBlockingQueue<>();

        Amplifier(List<Long> memory, long phase) {
            intcode = new Intcode(memory, () -> {
                try {
                    return in.poll(1, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return 0;
                }
            });
            in.add(phase);
        }

        private void setOutput(Amplifier receiver) {
            intcode.setOut(receiver.in::add);
        }

        @Override
        public void run() {
            intcode.runInstruction();
        }
    }
}
