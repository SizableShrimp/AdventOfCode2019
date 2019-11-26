package me.sizableshrimp.adventofcode;

import me.sizableshrimp.adventofcode.templates.Day;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    private static Map<String, Class<? extends Day>> days;

    public static void main(String[] args) {
        run();
    }

    private static void run() {
        LocalDateTime time = LocalDateTime.now(ZoneId.of("America/New_York"));
        int dayOfMonth = time.getDayOfMonth();

        compile();
        if (time.getMonth() == Month.DECEMBER && dayOfMonth <= 25) {
            Day day = getDay(dayOfMonth);
            if (day != null)
                day.run();
        } else {
            runAll();
        }
    }

    private static Day getDay(int dayOfMonth) {
        Class<? extends Day> clazz = days.get("Day" + dayOfMonth);
        if (clazz == null)
            return null;
        System.out.println("Day " + dayOfMonth + ":");
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void runAll() {
        for (int i = 1; i <= 25; i++) {
            Day day = getDay(i);
            if (day != null)
                day.run();
        }
    }

    private static void compile() {
        days = new Reflections(Main.class.getPackageName()).getSubTypesOf(Day.class).stream()
                .filter(c -> !Modifier.isAbstract(c.getModifiers()))
                .collect(Collectors.toMap(Class::getSimpleName, Function.identity()));
    }
}
