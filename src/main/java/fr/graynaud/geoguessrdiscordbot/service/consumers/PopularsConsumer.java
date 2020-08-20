package fr.graynaud.geoguessrdiscordbot.service.consumers;

import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import fr.graynaud.geoguessrdiscordbot.common.Constants;
import fr.graynaud.geoguessrdiscordbot.common.utils.DiscordUtils;
import fr.graynaud.geoguessrdiscordbot.config.ApplicationProperties;
import fr.graynaud.geoguessrdiscordbot.service.MapsCache;
import org.springframework.stereotype.Component;

@Component
public class PopularsConsumer implements MessageConsumer {

    private final MapsCache mapsCache;

    public PopularsConsumer(MapsCache mapsCache) {
        this.mapsCache = mapsCache;
    }

    @Override
    public String getDescription() {
        return "Display the list of most popular maps of Geoguessr ! Which one will you play ?";
    }

    @Override
    public String getExample() {
        return getCommand();
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
        DiscordUtils.generalEmbedMessage(spec);
        spec.setTitle("Geoguessr popular maps");
        spec.setUrl(Constants.POPULAR_MAPS_URL);
        DiscordUtils.addGeoguessrMapsToEmbedDescription(spec, this.mapsCache.getPopularMaps());
    }
}
