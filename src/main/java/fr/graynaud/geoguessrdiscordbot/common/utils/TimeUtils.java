package fr.graynaud.geoguessrdiscordbot.common.utils;

import java.time.Duration;

public final class TimeUtils {

    private TimeUtils() {}

    public static String formatDuration(Duration duration) {
        String s = "";

        if (duration.toHoursPart() > 0) {
            s += duration.toHoursPart() + "h ";
        }

        if (duration.toMinutesPart() > 0) {
            s += duration.toMinutesPart() + "min ";
        }

        if (duration.toSecondsPart() > 0) {
            s += duration.toSecondsPart() + "s";
        }

        return s.trim();
    }
}
