/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
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
     * This year integer is only used for retrieving data from the AOC servers with {@link DataReader#read}.
     * Otherwise, it is useless.
     */
    public static final int YEAR = 2019;
    private static final String BASE_PACKAGE = Main.class.getPackageName() + ".days.";

    public static void main(String[] args) {
        run();
    }

    private static void run() {
        LocalDateTime time = LocalDateTime.now(ZoneId.of("America/New_York"));
        int dayOfMonth = time.getDayOfMonth();

        if (time.getMonth() == Month.DECEMBER && dayOfMonth <= 25) {
            run(dayOfMonth);
        } else {
            runAll();
        }
    }

    private static void run(int dayOfMonth) {
        try {
            Class<?> clazz = Class.forName(BASE_PACKAGE + "Day" + pad(dayOfMonth));
            if (clazz == null)
                return;
            System.out.println("Day " + dayOfMonth + ":");
            ((Day) clazz.getDeclaredConstructor().newInstance()).run();
        } catch (Exception e) {
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
