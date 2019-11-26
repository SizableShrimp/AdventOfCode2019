package me.sizableshrimp.adventofcode.helper;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

public class DateGrabber {

    //TODO: won't work two years in the future; does it matter?
    public static int getAOCYear() {
        LocalDateTime time = estTime();
        if (time.getMonth() == Month.DECEMBER) {
            return time.getYear();
        } else {
            return time.getYear() - 1;
        }
    }

    public static LocalDateTime estTime() {
        return LocalDateTime.now(ZoneId.of("America/New_York"));
    }
}
