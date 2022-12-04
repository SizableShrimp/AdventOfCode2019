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

import lombok.ToString;
import lombok.Value;
import lombok.experimental.Delegate;
import me.sizableshrimp.adventofcode.helper.LineConvert;
import me.sizableshrimp.adventofcode.intcode.Intcode;
import me.sizableshrimp.adventofcode.templates.Day;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day23 extends Day {
    List<Long> baseMemory = LineConvert.longs(lines.get(0));
    List<NIC> network;
    Packet nat;
    Packet part1;

    @Override
    protected void evaluate() {
        network = IntStream.range(0, 50).mapToObj(NIC::new).collect(Collectors.toList());
        NIC first = network.get(0);
        long prevNat;

        do {
            // Clear NAT
            prevNat = nat == null ? -1 : nat.y;
            nat = null;
            runNetworkTillIdle();
            // Stimulate network
            first.in.add(nat);
        } while (prevNat != nat.y);

        setPart1(part1.y);
        setPart2(nat.y);
    }

    void runNetworkTillIdle() {
        while (isNetworkActive()) {
            for (NIC nic : network) {
                nic.runInstruction();
            }
        }
    }

    boolean isNetworkActive() {
        for (NIC nic : network) {
            if (!nic.isIdle())
                return true;
        }

        return false;
    }

    @ToString
    private class NIC {
        int networkId;
        @Delegate
        final Intcode intcode;
        boolean sentNetworkId;
        int idleCount;
        Packet send;
        Queue<Packet> in = new ArrayDeque<>();

        NIC(int networkId) {
            this.networkId = networkId;
            intcode = new Intcode(new ArrayList<>(baseMemory), this::receive, this::send);
        }

        private void send(long in) {
            if (send == null) {
                send = new Packet((int) in, -1, -1);
            } else if (send.x == -1) {
                send = new Packet(send.destination, in, -1);
            } else if (send.y == -1) {
                send = new Packet(send.destination, send.x, in);
                if (send.destination == 255) {
                    // System.out.println("NAT: " + send);
                    if (part1 == null)
                        part1 = send;
                    nat = send;
                } else {
                    network.get(send.destination).in.add(send);
                }
                send = null;
            }
        }

        boolean sendingX = true;

        private long receive() {
            // Send the id of this NIC before it runs
            if (!sentNetworkId) {
                sentNetworkId = true;
                return networkId;
            }
            if (isIdle()) {
                this.exit();
                return -1;
            }
            if (in.isEmpty()) {
                idleCount++;
                return -1;
            }

            idleCount = 0;
            if (sendingX) {
                Packet current = in.peek();
                sendingX = false;
                return current.x;
            } else {
                Packet current = in.poll();
                sendingX = true;
                return current.y;
            }
        }

        boolean isIdle() {
            return in.isEmpty() && idleCount == 20;
        }
    }

    @Value
    private static class Packet {
        int destination;
        long x, y;
    }
}
