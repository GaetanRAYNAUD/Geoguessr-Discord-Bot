package fr.graynaud.geoguessrdiscordbot.service.consumers;

import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import fr.graynaud.geoguessrdiscordbot.common.Constants;
import fr.graynaud.geoguessrdiscordbot.config.ApplicationProperties;
import fr.graynaud.geoguessrdiscordbot.service.MapsCache;
import fr.graynaud.geoguessrdiscordbot.service.objects.GeoguessrMap;
import org.apache.commons.lang3.StringUtils;
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
        message.getChannel().block().createEmbed(this::generateMessage).block();
    }

    private void generateMessage(EmbedCreateSpec spec) {
        spec.setColor(Color.RED)
            .setTitle("Geoguessr popular maps")
            .setUrl(Constants.POPULAR_MAPS_URL)
            .setTimestamp(Instant.now());

        int i = 1;

        for (GeoguessrMap map : this.mapsCache.getPopularMaps()) {
            spec.addField(i + ". " + map.getName(),
                          StringUtils.defaultIfBlank(map.getDescription(), map.getName()) + "\n[Link](" + Constants.MAP_URL + map.getSlug() + ")",
                          false);
            i++;
        }
    }
}
