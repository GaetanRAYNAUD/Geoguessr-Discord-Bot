package fr.graynaud.geoguessrdiscordbot.common.utils;

import java.text.Normalizer;

public final class GeoguessrUtils {

    private GeoguessrUtils() {}

    public static String cleanName(String name) {
        name = Normalizer.normalize(name, Normalizer.Form.NFD);
        name = name.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        name = name.toLowerCase();
        return name.trim();
    }

    public static String cleanToUrl(String name) {
        return cleanName(name).replace(' ', '-');
    }
}
