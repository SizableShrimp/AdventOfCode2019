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

package me.sizableshrimp.adventofcode;

import me.sizableshrimp.adventofcode.helper.DataReader;
import me.sizableshrimp.adventofcode.templates.Day;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

public class Main {
    /**
     * The hardcoded Advent Of Code year.
     * This year integer is used for retrieving data from the AOC servers with {@link DataReader#read}
     * and running a day during the competition.
     */
    public static final int YEAR = 2019;
    private static final String BASE_PACKAGE = Main.class.getPackageName() + ".days.";

    public static void main(String[] args) {
        run(10);
    }

    private static void run() {
        LocalDateTime time = LocalDateTime.now(ZoneId.of("America/New_York"));
        int dayOfMonth = time.getDayOfMonth();

        if (time.getYear() == YEAR && time.getMonth() == Month.DECEMBER && dayOfMonth <= 25) {
            run(dayOfMonth);
        } else {
            runAll();
        }
    }

    private static void run(int dayOfMonth) {
        try {
            Class<?> clazz = Class.forName(BASE_PACKAGE + "Day" + pad(dayOfMonth));
            System.out.println("Day " + dayOfMonth + ":");
            ((Day) clazz.getDeclaredConstructor().newInstance()).run();
        } catch (ClassNotFoundException ignored) {} catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runAll() {
        for (int i = 1; i <= 25; i++) {
            run(i);
        }
    }

    public static String pad(int i) {
        return String.format("%02d", i);
    }
}
