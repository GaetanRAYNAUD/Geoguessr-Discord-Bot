package fr.graynaud.geoguessrdiscordbot.common.utils;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import fr.graynaud.geoguessrdiscordbot.common.Constants;
import fr.graynaud.geoguessrdiscordbot.service.objects.GeoguessrMap;
import org.apache.commons.lang3.StringUtils;

import java.text.Normalizer;
import java.time.Duration;
import java.time.Instant;

public final class DiscordUtils {

    private DiscordUtils() {}

    public static void geoMapToEmbedMessage(EmbedCreateSpec spec, GeoguessrMap geoguessrMap, String token, Integer duration) {
        if (spec == null || geoguessrMap == null || StringUtils.isBlank(token)) {
            return;
        }

        DiscordUtils.generalEmbedMessage(spec);

        String title = geoguessrMap.getName() + " (";
        title += duration == null ? "Unlimited time)" : TimeUtils.formatDuration(Duration.ofSeconds(duration)) + ")";

        spec.setTitle(title);
        spec.setUrl(Constants.CHALLENGE_URL + token);
        spec.setDescription(geoguessrMap.getDescription());
        spec.addField("Start game", Constants.CHALLENGE_URL + token, false);
        spec.addField("Map link", Constants.MAP_URL + geoguessrMap.getSlug(), false);

        if (geoguessrMap.getImages() != null && StringUtils.isNotBlank(geoguessrMap.getImages().getBackgroundLarge())) {
            spec.setThumbnail(Constants.IMAGE_144_URL + geoguessrMap.getImages().getBackgroundLarge());
        }

        if (geoguessrMap.getLikes() != null) {
            spec.addField(":heart: Likes", geoguessrMap.getLikes().toString(), true);
        }

        if (geoguessrMap.getDifficultyLevel() != null) {
            spec.addField(":chart_with_upwards_trend: Difficulty", geoguessrMap.getDifficulty(), true);
        }

        if (geoguessrMap.getAverageScore() != null) {
            spec.addField("\uD83D\uDCAF Average score", geoguessrMap.getAverageScore().toString(), true);
        }

        if (geoguessrMap.getNumFinishedGames() != null) {
            spec.addField("\uD83C\uDFC1 Finished games", geoguessrMap.getNumFinishedGames().toString(), true);
        }

        if (StringUtils.isNotBlank(geoguessrMap.getCoordinateCount()) && !"-".equals(geoguessrMap.getCoordinateCount())) {
            spec.addField("\uD83D\uDCCD Number of coordinates", geoguessrMap.getCoordinateCount(), true);
        }
    }

    public static void generalEmbedMessage(EmbedCreateSpec spec) {
        if (spec == null) {
            return;
        }

        spec.setColor(Color.RED);
        spec.setAuthor("Geoguessr", null, Constants.GEOGUESSR_ICON);
        spec.setThumbnail(Constants.GEOGUESSR_ICON);
        spec.setTimestamp(Instant.now());
    }
}
