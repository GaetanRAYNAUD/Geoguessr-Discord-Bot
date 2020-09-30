package fr.graynaud.geoguessrdiscordbot.service.consumers;

import discord4j.core.object.entity.Message;
import fr.graynaud.geoguessrdiscordbot.common.Constants;
import fr.graynaud.geoguessrdiscordbot.common.utils.DiscordUtils;
import fr.graynaud.geoguessrdiscordbot.common.utils.GeoguessrUtils;
import fr.graynaud.geoguessrdiscordbot.config.ApplicationProperties;
import fr.graynaud.geoguessrdiscordbot.service.GeoguessrService;
import fr.graynaud.geoguessrdiscordbot.service.objects.GeoguessrMap;
import fr.graynaud.geoguessrdiscordbot.service.objects.SearchMapResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class GenerateLinkConsumer implements MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateLinkConsumer.class);

    private final GeoguessrService geoguessrService;

    public GenerateLinkConsumer(GeoguessrService geoguessrService) {
        this.geoguessrService = geoguessrService;
    }

    @Override
    public String getDescription() {
        return "Generate a game link of the provided map, duration and moving rule. How high will you score ?!";
    }

    @Override
    public String getExample() {
        return getCommand() + " a diverse world 300 false";
    }

    @Override
    public String getCommand() {
        return "play";
    }

    @Override
    public void consume(Message message, ApplicationProperties applicationProperties) {
        String content = message.getContent().substring(Constants.COMMAND_PREFIX.length() + getCommand().length()).trim();

        if (content.indexOf(' ') < 0) {
            message.getRestChannel().createMessage("The command require three parameters: map name, duration of the game and moving rule ! Ex: "
                                                   + Constants.COMMAND_PREFIX + getExample()).block();
            return;
        }

        int lastSpace = content.lastIndexOf(' ');
        boolean forbidMoving = Boolean.parseBoolean(content.substring(lastSpace + 1));
        int previousLastSpace = content.substring(0, lastSpace).lastIndexOf(' ');
        String map = content.substring(0, previousLastSpace);

        Integer duration;

        try {
            duration = Integer.parseInt(content.substring(previousLastSpace + 1, lastSpace));
        } catch (Exception e) {
            message.getRestChannel().createMessage("Could not parse the duration of the game ! Check that you entered a valid number !").block();
            return;
        }

        if (duration < 0) {
            message.getRestChannel().createMessage("The duration of the game needs to be positive !").block();
            return;
        }

        if (duration == 0) {
            duration = null; //Set to null to not send it to Geoguessr
        }

        GeoguessrMap geoguessrMap = this.geoguessrService.getMap(map);
        List<SearchMapResult> geoguessrMaps = null;

        if (geoguessrMap == null) {
            geoguessrMaps = this.geoguessrService.searchMaps(map);
            geoguessrMap = this.geoguessrService.getMap(map);
        }

        if (geoguessrMap != null) {
            try {
                String token = this.geoguessrService.getGameToken(geoguessrMap, duration, false, forbidMoving);

                Integer finalDuration = duration;
                GeoguessrMap finalGeoguessrMap = geoguessrMap;
                message.getChannel()
                       .block()
                       .createEmbed(spec -> DiscordUtils.geoMapToEmbedMessage(spec, finalGeoguessrMap, token, finalDuration, false, forbidMoving))
                       .block(Duration.of(1, ChronoUnit.SECONDS));
            } catch (Exception e) {
                LOGGER.error("An error occurred while creating game {}: {} !", map, e.getMessage(), e);
                message.getRestChannel().createMessage("Couldn't create the game ! Check your message again !").block(Duration.of(3, ChronoUnit.SECONDS));
            }
        } else {
            if (geoguessrMaps == null || geoguessrMaps.isEmpty()) {
                message.getRestChannel()
                       .createMessage("The map __**" + map + "**__ does not exist ! Check your message again !")
                       .block(Duration.of(3, ChronoUnit.SECONDS));
            } else {
                List<SearchMapResult> finalGeoguessrMaps = geoguessrMaps;
                message.getChannel().block().createEmbed(spec -> {
                    DiscordUtils.generalEmbedMessage(spec);
                    spec.setTitle("Geoguessr maps");
                    spec.setUrl(Constants.SEARCH_URL + "?query=" + GeoguessrUtils.cleanToUrl(map));
                    spec.setDescription("Could not find a map with name __**" + map + "**__.\n" +
                                        "Did you mean one of the following map ? (Add a reaction to generate a game !)");
                    DiscordUtils.addSearchMapToEmbedDescription(spec, finalGeoguessrMaps);
                }).block();
            }
        }
    }
}
