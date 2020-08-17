package fr.graynaud.geoguessrdiscordbot.service.consumers;

import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import fr.graynaud.geoguessrdiscordbot.common.Constants;
import fr.graynaud.geoguessrdiscordbot.config.ApplicationProperties;
import fr.graynaud.geoguessrdiscordbot.service.MapsCache;
import fr.graynaud.geoguessrdiscordbot.service.objects.GeoguessrMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PopularsConsumer implements MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PopularsConsumer.class);

    private final MapsCache mapsCache;

    public PopularsConsumer(MapsCache mapsCache) {
        this.mapsCache = mapsCache;
    }

    @Override
    public String getCommand() {
        return "populars";
    }

    @Override
    public void consume(Message message, ApplicationProperties applicationProperties) {
        StringBuilder description = new StringBuilder();

        int i = 1;

        for (GeoguessrMap map : this.mapsCache.getPopularMaps()) {
            description.append("[")
                       .append(i)
                       .append(". ")
                       .append(map.getName())
                       .append(" (")
                       .append(map.getLikes())
                       .append(" ❤)")
                       .append("](")
                       .append(Constants.MAP_URL)
                       .append(map.getSlug())
                       .append(")")
                       .append("\n");
            i++;
        }

        message.getChannel().block().createEmbed(spec -> spec.setColor(Color.RED)
                                                             .setTitle("Geoguessr popular maps")
                                                             .setUrl(Constants.POPULAR_MAPS_URL)
                                                             .setDescription(description.toString())
                                                             .setTimestamp(Instant.now())
                                                ).block();
    }
}
