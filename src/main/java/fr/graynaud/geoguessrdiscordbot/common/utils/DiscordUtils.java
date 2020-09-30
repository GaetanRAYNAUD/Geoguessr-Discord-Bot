package fr.graynaud.geoguessrdiscordbot.common.utils;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import fr.graynaud.geoguessrdiscordbot.common.Constants;
import fr.graynaud.geoguessrdiscordbot.service.consumers.MessageConsumer;
import fr.graynaud.geoguessrdiscordbot.service.objects.GeoguessrMap;
import fr.graynaud.geoguessrdiscordbot.service.objects.SearchMapResult;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;

public final class DiscordUtils {

    private DiscordUtils() {}

    public static String consumerToHelpDescription(MessageConsumer consumer) {
        String description = "__** " + StringUtils.capitalize(consumer.getCommand()) + "**__:\n";
        description += consumer.getDescription() + "\n";
        description += "\nExample: " + Constants.COMMAND_PREFIX + consumer.getExample() + "\n";
        return description;
    }

    public static void geoMapToEmbedMessage(EmbedCreateSpec spec, GeoguessrMap geoguessrMap, String token, Integer duration, boolean isCountryStreak,
                                            boolean forbidMoving) {
        if (spec == null || geoguessrMap == null || StringUtils.isBlank(token)) {
            return;
        }

        DiscordUtils.generalEmbedMessage(spec);

        String title = geoguessrMap.getName() + " (";
        title += (duration == null ? "Unlimited time" : TimeUtils.formatDuration(Duration.ofSeconds(duration))) + ","
                 + (forbidMoving ? " moving forbidden" : " moving allowed") + ")";

        spec.setTitle(title);
        spec.setUrl(Constants.CHALLENGE_URL + token);
        spec.addField("Start game", Constants.CHALLENGE_URL + token, false);

        if (!isCountryStreak) {
            spec.addField("Map link", Constants.MAP_URL + geoguessrMap.getSlug(), false);
        }

        if (geoguessrMap.getDescription() != null) {
            spec.setDescription(geoguessrMap.getDescription());
        }

        if (geoguessrMap.getImages() != null && StringUtils.isNotBlank(geoguessrMap.getImages().getBackgroundLarge())) {
            spec.setThumbnail(Constants.IMAGE_144_URL + geoguessrMap.getImages().getBackgroundLarge());
        }

        addMapMetaFields(spec, geoguessrMap);
    }

    public static void addGeoguessrMapsToEmbedDescription(EmbedCreateSpec spec, Collection<GeoguessrMap> maps) {
        int i = 1;
        for (GeoguessrMap map : maps) {
            spec.addField(i + ". " + map.getName(),
                          StringUtils.defaultIfBlank(map.getDescription(), map.getName()) + "\n[Map link](" + Constants.MAP_URL + map.getSlug() + ")",
                          false);
            i++;
        }
    }

    public static void addMapMetaFields(EmbedCreateSpec spec, GeoguessrMap map) {
        if (map.getCreator() != null && StringUtils.isNotBlank(map.getCreator().getNick())) {
            spec.addField("✍ Creator", map.getCreator().getNick(), true);
        }

        if (map.getLikes() != null) {
            spec.addField(":heart: Likes", map.getLikes().toString(), true);
        }

        if (map.getDifficultyLevel() != null) {
            spec.addField(":chart_with_upwards_trend: Difficulty", map.getDifficulty(), true);
        }

        if (map.getAverageScore() != null) {
            spec.addField("\uD83D\uDCAF Average score", map.getAverageScore().toString(), true);
        }

        if (map.getNumFinishedGames() != null) {
            spec.addField("\uD83C\uDFC1 Finished games", map.getNumFinishedGames().toString(), true);
        }

        if (StringUtils.isNotBlank(map.getCoordinateCount()) && !"-".equals(map.getCoordinateCount())) {
            spec.addField("\uD83D\uDCCD Number of coordinates", map.getCoordinateCount(), true);
        }
    }

    public static void addSearchMapToEmbedDescription(EmbedCreateSpec spec, Collection<SearchMapResult> maps) {
        int i = 1;
        for (SearchMapResult map : maps) {
            spec.addField(i + ". " + map.getName(),
                          "[Map link](" + Constants.GEOGUESSR_URL + map.getUrl() + ")",
                          false);

            if (map.getLikes() != null) {
                spec.addField(":heart: Likes", map.getLikes().toString(), true);
            }

            if (StringUtils.isNotBlank(map.getCreator())) {
                spec.addField("✍ Creator", map.getCreator(), true);
            }

            i++;
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
